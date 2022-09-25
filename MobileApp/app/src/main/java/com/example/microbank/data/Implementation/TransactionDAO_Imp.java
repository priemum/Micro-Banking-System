package com.example.microbank.data.Implementation;

import static com.example.microbank.Constants.AGENT_ID;
import static com.example.microbank.Constants.HOST_IP;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.example.microbank.data.DBHandler;
import com.example.microbank.data.TransactionDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class TransactionDAO_Imp extends DBHandler implements TransactionDAO {
    public TransactionDAO_Imp(@Nullable Context context) {
        super(context);
    }

    @Override
    public void addTransaction(String customerID, String accNo,String type,Double trCharge,Double amount,String reference) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String addTr = "INSERT INTO TRANSACTIONS VALUES (?,?,?,?,?,?,?,?)";
        SQLiteDatabase db = this.getWritableDatabase();
        SQLiteStatement statement = db.compileStatement(addTr);
        statement.bindString(2,customerID);
        statement.bindString(3,accNo);
        statement.bindString(4,currentDateandTime);
        statement.bindString(5,type);
        statement.bindDouble(6,trCharge);
        statement.bindDouble(7,amount);
        statement.bindString(8,reference);
        long rowId = statement.executeInsert();
        try {
            updateCentralDB(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateCentralDB(boolean alarm) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        String trs = "SELECT * FROM TRANSACTIONS";
        Cursor cursor = db.rawQuery(trs, null);

        if (checkForUpdate() || alarm){
            JSONArray arr = cur2Json(cursor);
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", arr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("transaction").addQueryParameter("id", AGENT_ID).build();
            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TRUPDFail", "onFailure: no response");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String dataString = response.body().string();
                    Log.d("TRUPDPass", "response received : " + dataString);
                }
            });
           clearTransactionsTable();
        }

    }

    private JSONArray cur2Json(Cursor cursor) {

        JSONArray resultSet = new JSONArray();
        String key = "";
        String newTRID = String.valueOf(AGENT_ID);
        String dt = "";
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {
            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();
            for (int i = 0; i < totalColumn; i++) {
                if (cursor.getColumnName(i) != null) {
                    key = cursor.getColumnName(i);
                    switch (cursor.getColumnName(i)){
                        case "TRANSACTION_ID":
                            key = "transactionID";
                            break;
                        case "ACCOUNT_NO":
                            key = "accountNumber";
                            break;
                        case "TIMESTAMP":
                            key = "date";
                            String arr1[] = cursor.getString(i).substring(2,10).split("-");
                            String arr2[] = cursor.getString(i).substring(11,16).split(":");
                            dt += TextUtils.join("", arr1);
                            dt += TextUtils.join("", arr2);
                            break;
                        case "TRANSACTION_TYPE":
                            key = "transactionType";
                            break;
                        case "AMOUNT":
                            key = "transactionAmount";
                            break;
                        case "TRANSACTION_CHARGE":
                            key = "transactionCharge";
                            break;
                        case "REFERENCE":
                            key = "reference";
                            break;
                        case "CUSTOMER_ID":
                            key = "customerID";
                            break;

                    }
                    try {
                        if (!key.equals("transactionID")){
                            if (key.equals("transactionAmount"))
                                rowObject.put(key,Double.parseDouble(cursor.getString(i)));
                            else
                                rowObject.put(key,cursor.getString(i));
                        }

                        else{
                            newTRID += cursor.getString(i);
                        }
                    } catch (Exception e) {
                        Log.d("trErr", e.getMessage());
                    }
                }
            }
            try {
                rowObject.put("agentID", AGENT_ID);
                newTRID += dt;
                rowObject.put("transactionID",newTRID);
                newTRID = String.valueOf(AGENT_ID);
                dt = "";
            } catch (JSONException e) {
                e.printStackTrace();
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }

        cursor.close();
        return resultSet;

    }

    private boolean checkForUpdate() {

        int noOfTransactions = 0;
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT COUNT(*) FROM TRANSACTIONS";
        Cursor c = db.rawQuery(query, null);
        if (c.moveToFirst())
            noOfTransactions = c.getInt(0);
        if (noOfTransactions >= 5)
            return true;
        return false;
    }

    public void specialRequest(String customerID, String accNo,String type,Double trCharge,Double amount,String reference){

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        String currentDateandTime = sdf.format(new Date());
        String trID = String.valueOf(AGENT_ID);
        String arr1[] = currentDateandTime.substring(2,10).split("-");
        String arr2[] = currentDateandTime.substring(11,16).split(":");
        trID += TextUtils.join("", arr1);
        trID += TextUtils.join("", arr2);

        JSONObject specialTR = new JSONObject();
        try {
            specialTR.put("transactionID",trID);
            specialTR.put("customerID", customerID);
            specialTR.put("accountNumber", accNo);
            specialTR.put("date",currentDateandTime);
            specialTR.put("transactionType", type);
            specialTR.put("transactionCharge", trCharge);
            specialTR.put("transactionAmount", amount);
            specialTR.put("reference", reference);
            specialTR.put("agentID", AGENT_ID);

            JSONArray sp_tr = new JSONArray().put(specialTR);
            JSONObject obj = new JSONObject();
            obj.put("data", sp_tr);

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("transaction").build();

            RequestBody body = RequestBody.create(JSON, obj.toString());

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("SPTRFAIL", "onFailure: response not received");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String datastring = response.body().string();
                    Log.d("SPTRPASS", "special request updated " + datastring);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void clearTransactionsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM TRANSACTIONS");
        db.close();
    }
}




