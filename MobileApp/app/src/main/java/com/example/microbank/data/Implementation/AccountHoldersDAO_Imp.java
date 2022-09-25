package com.example.microbank.data.Implementation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.microbank.data.AccountHoldersDAO;
import com.example.microbank.data.DBHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AccountHoldersDAO_Imp extends DBHandler implements AccountHoldersDAO {

    public AccountHoldersDAO_Imp(@Nullable Context context) {
        super(context);
    }

    @Override
    public void LoadAccHolderData(JSONArray accHolders) {
        for (int i=0;i<accHolders.length();i++){
            try {
                JSONObject c = new JSONObject(accHolders.get(i).toString());
                SQLiteDatabase db = this.getWritableDatabase();
                String insertAccHoldr = "INSERT INTO ACCOUNT_HOLDERS VALUES (?,?)";

                SQLiteStatement addHolder = db.compileStatement(insertAccHoldr);
                addHolder.bindString(1,c.getString("accountNumber"));
                addHolder.bindString(2,c.getString("customerID"));
                addHolder.executeInsert();
                db.close();
            } catch (JSONException e) {
                e.printStackTrace();
            }
    }
}

    @Override
    public void clearAccHolderts() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM ACCOUNT_HOLDERS");
        db.close();
    }
}
