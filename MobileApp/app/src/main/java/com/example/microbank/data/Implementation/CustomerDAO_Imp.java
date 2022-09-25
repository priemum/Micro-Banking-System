package com.example.microbank.data.Implementation;

import static com.example.microbank.Constants.AGENT_ID;
import static com.example.microbank.Constants.HOST_IP;
//import org.apache.commons.codec.binary.Base64;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.microbank.Control.AppController;
import com.example.microbank.UI.LoginActivity;
import com.example.microbank.data.CustomerDAO;
import com.example.microbank.data.DBHandler;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.Model.Customer;
//import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.codec.binary.Base64;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import at.favre.lib.crypto.bcrypt.BCrypt;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class CustomerDAO_Imp extends DBHandler implements CustomerDAO {
    public CustomerDAO_Imp(@Nullable Context context) {
        super(context);
    }

    boolean isSpecial = false;
    String fname, lname, pw;
    List<Account> accList = new ArrayList<>();
    public static String accNo, type;
    public static Double bal;


    @Override
    public Customer checkUserNamePassword(String username, String password){
        SQLiteDatabase accountsTable = this.getWritableDatabase();
        Cursor cursor = accountsTable.rawQuery("SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID=?",new String[]{username});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String customer_id = cursor.getString(0);
            String first_name = cursor.getString(1);
            String last_name = cursor.getString(2);
            String password_data = cursor.getString(3);
            BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), password_data);
            if (result.verified){
                Customer customer= new Customer(customer_id,first_name,last_name);
                cursor.close();
                accountsTable.close();
                return customer;
            }
        }

        // If the customer is not in the local database, check if still valid
        Customer customer = checkValidCustomer(username, password);
        if (customer != null){
            return customer;
        }
        cursor.close();
        accountsTable.close();
        return null;
    }


    public Customer getUser(String customer_id){
        SQLiteDatabase accountsTable = this.getWritableDatabase();
        Cursor cursor = accountsTable.rawQuery("SELECT * FROM CUSTOMERS WHERE CUSTOMER_ID=?",new String[]{customer_id});
        if (cursor.getCount()>0){
            cursor.moveToFirst();
            String id = cursor.getString(0);
            String first_name = cursor.getString(1);
            String last_name = cursor.getString(2);
            Customer customer= new Customer(id,first_name,last_name);
            cursor.close();
            accountsTable.close();
            return customer;
        }
        cursor.close();
        accountsTable.close();
        return null;
    }

    public Customer checkValidCustomer(String customerID, String password){

        Customer customer = null;

        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("sync").addPathSegment("special-request").addQueryParameter("customerID", customerID).build();

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d("VALFAIL", "Response not received");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String dataString = response.body().string();
                try {
                    JSONObject details = new JSONObject(dataString);
                    if (details.getString("status").equals("Success")){
                        String str = details.getString("data");
                        JSONObject str1 = new JSONObject(str);
                        JSONObject str2 = new JSONObject(str1.getString("customer"));
                        fname = str2.getString("firstName");
                        lname = str2.getString("lastName");
                        pw = str2.getString("password");
                        BCrypt.Result result = BCrypt.verifyer().verify(password.toCharArray(), pw);
                        if (result.verified){
                            isSpecial = true;
                            String str3 = str1.getString("account");
                            accList = addToAccList(str3, customerID);
                        }
                        else{
                            Log.d("INCC", "Incorrect password");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        if (isSpecial){
            customer = new Customer(customerID, fname, lname);
        }
        return customer;
    }

    public void LoadCustomerData(JSONArray customers){
        for (int i=0;i<customers.length();i++){
            try {
                JSONObject c = new JSONObject(customers.get(i).toString());
                SQLiteDatabase db = this.getWritableDatabase();
                String insertCustomer = "INSERT INTO CUSTOMERS VALUES (?,?,?,?)";
                SQLiteStatement addCus = db.compileStatement(insertCustomer);
                addCus.bindString(1,c.getString("customerID"));
                addCus.bindString(2,c.getString("firstName"));
                addCus.bindString(3,c.getString("lastName"));
                addCus.bindString(4,c.getString("password"));
                addCus.executeInsert();
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    public boolean isSpecialCustomer(String customerID){
        return isSpecial;
    }

    public void clearCustomerTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM CUSTOMERS");
        db.close();
    }

    public List<Account> fetchAccounts(String customerID){
        return accList;
    }


    private List<Account> addToAccList(String str, String customerID){
        List<Account> accList = new ArrayList<>();
        JSONArray arr = null;
        try {
            arr = new JSONArray(str);
            JSONObject obj = null;
            for (int i = 0; i < arr.length(); i++){
                obj = (JSONObject) arr.get(i);
                accNo = obj.getString("accountNumber");
                type = obj.getString("accountType").toUpperCase();
                bal = obj.getDouble("accountBalance");
                Account account = new Account(accNo, customerID, type, bal);
                accList.add(account);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return accList;
    }

}

