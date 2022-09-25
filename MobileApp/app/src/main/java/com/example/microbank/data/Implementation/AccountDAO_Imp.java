package com.example.microbank.data.Implementation;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.example.microbank.data.AccountDAO;
import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AccountDAO_Imp extends DBHandler implements AccountDAO {
    public AccountDAO_Imp(@Nullable Context context) {
        super(context);
    }

    @Override
    public void addAccount(Account acc) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("ACCOUNT_NO",acc.getAcc_No());
        cv.put("CUSTOMER_ID",acc.getCustomer_ID());
        cv.put("ACCOUNT_TYPE",acc.getAccount_type());
        cv.put("BALANCE",acc.getBalance());
        db.insert("ACCOUNTS",null,cv);
        db.close();
    }

    public List<Account> getAccountsList(String CustomerID) throws InvalidAccountException{
        List<Account> accountList = new ArrayList<>();
        String queryString = "SELECT * FROM ACCOUNTS NATURAL JOIN ACCOUNT_HOLDERS WHERE CUSTOMER_ID='"+CustomerID+"';";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if (cursor.moveToFirst()){
            do {
                String accNo = cursor.getString(0);
                String accountType = cursor.getString(1);
                Double balance = cursor.getDouble(2);
                Account account = new Account(accNo,CustomerID,accountType,balance);
                accountList.add(account);
            }while (cursor.moveToNext());
        }
        else{
            String msg = "No accounts found for "+CustomerID;
            throw new InvalidAccountException(msg);
        }
        cursor.close();
        db.close();
        return accountList;
    }
    public void updateBalance(String accNo,String type,Double trCharge,Double amount) throws InvalidAccountException {

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NO =?",new String[]{accNo});
        if (!cursor.moveToFirst()) {
            String msg = "Account " + accNo + " is invalid.";
            throw new InvalidAccountException(msg);
        } else {
            double balance = cursor.getDouble(0);
            switch (type) {
                case "DEPOSIT":
                    balance = balance + amount-trCharge;
                    break;
                case "WITHDRAW":
                    balance = balance - amount - trCharge;
                    break;
            }

            String setBalance_1 = "UPDATE ACCOUNTS SET BALANCE= " + balance + " WHERE ACCOUNT_NO = ?";
            SQLiteStatement statement = db.compileStatement(setBalance_1);
            statement.bindString(1,accNo);
            int effectedRows = statement.executeUpdateDelete();
            db.close();
        }
    }

    public boolean checkBalance(String accNo,double amount, String type,double charge) throws InvalidAccountException{
        boolean isValid;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT BALANCE FROM ACCOUNTS WHERE ACCOUNT_NO =?",new String[]{accNo});
        if (!cursor.moveToFirst()) {
            String msg = "Account " + accNo + " is invalid.";
            throw new InvalidAccountException(msg);
        } else {
            double balance = cursor.getDouble(0);
            switch (type) {
                case "DEPOSIT":
                    balance = balance + amount-charge;
                    break;
                case "WITHDRAW":
                    balance = balance - amount-charge;
                    break;
            }
            //Assume that the minimum balance is LKR250.00
            if (balance >= 250) {
                isValid = true;
            }else{
                isValid = false;
            }
        }
        cursor.close();
        db.close();
        return isValid;
    }

    // dummy data
    public void initAccTable(){
        Account acc1 = new Account("2039134","123432","ADULT",12500.0);
        Account acc2 = new Account("2031134","123432","TEEN",15500.0);
        addAccount(acc1);
        addAccount(acc2);
    }


    public void LoadAccountData(JSONArray accounts) {
        for (int i=0;i<accounts.length();i++){
            try {
                JSONObject c = new JSONObject(accounts.get(i).toString());
                String accountNumber = c.getString("accountNumber");
                String accountType = c.getString("accountType");
                Double balance = c.getDouble("accountBalance");
                String addAcc = "INSERT INTO ACCOUNTS VALUES (?,?,?)";
                SQLiteDatabase db = this.getWritableDatabase();
                SQLiteStatement insertAcc = db.compileStatement(addAcc);
                insertAcc.bindString(1,accountNumber);
                insertAcc.bindString(2,accountType.toUpperCase(Locale.ROOT));
                insertAcc.bindDouble(3,balance);
                insertAcc.executeInsert();
//                db.insert("ACCOUNTS",null,cv);
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearAccountsTable(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ACCOUNTS");
        db.close();
    }

    public Account getAccount(String accountNumber, String customerID){
        Account account = null;
        String queryString = "SELECT * FROM ACCOUNTS WHERE ACCOUNT_NO="+accountNumber+";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(queryString,null);
        if(cursor.moveToFirst()){
            String accNo = cursor.getString(0);
            String accountType = cursor.getString(1);
            Double balance = cursor.getDouble(2);
            account = new Account(accNo,customerID,accountType,balance);
        }
        return account;
    }
}
