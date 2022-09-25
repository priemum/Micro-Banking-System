package com.example.microbank.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.microbank.data.Implementation.AccountDAO_Imp;
import com.example.microbank.data.Model.Account;

import java.io.Console;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class DBHandler extends SQLiteOpenHelper {
    private static final int VERSION=2;
    private static final String DB_NAME = "microDB";
    Context context;


    public DBHandler(@Nullable Context context) {
        super(context, DB_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String dropTableAccounts = "DROP TABLE IF EXISTS ACCOUNTS";
        String dropTableCustomers = "DROP TABLE IF EXISTS CUSTOMERS";
        String dropTableTransactions = "DROP TABLE IF EXISTS TRANSACTIONS";
        String dropTableAccHolders = "DROP TABLE IF EXISTS ACCOUNT_HOLDERS";

        String createTableAccounts = "CREATE TABLE ACCOUNTS (" +
                "ACCOUNT_NO VARCHAR(20) NOT NULL," +
                "ACCOUNT_TYPE CHECK (ACCOUNT_TYPE IN ('CHILDREN','TEEN','ADULT','SENIOR','JOINT'))," +
                "BALANCE DOUBLE," +
                "PRIMARY KEY(ACCOUNT_NO))";

        String createTableAccHolders = "CREATE TABLE ACCOUNT_HOLDERS (" +
                "ACCOUNT_NO VARCHAR(20) NOT NULL," +
                "CUSTOMER_ID VARCHAR(10) NOT NULL)";

        String createTableTransactions = "CREATE TABLE TRANSACTIONS (" +
                "TRANSACTION_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "CUSTOMER_ID VARCHAR(10) NOT NULL," +
                "ACCOUNT_NO VARCHAR(20)," +
                "TIMESTAMP TEXT,"+
                "TRANSACTION_TYPE TEXT," +
                "TRANSACTION_CHARGE DOUBLE," +
                "AMOUNT DOUBLE,"+
                "REFERENCE TEXT,"+
                "FOREIGN KEY(ACCOUNT_NO) REFERENCES ACCOUNTS(ACCOUNT_NO))";

        String createTableCustomers = "CREATE TABLE CUSTOMERS (" +
                "CUSTOMER_ID VARCHAR(10) NOT NULL," +
                "FIRST_NAME TEXT,"+
                "LAST_NAME TEXT,"+
                "PASSWORD VARCHAR(50) NOT NULL," +
                "PRIMARY KEY(CUSTOMER_ID))";
        db.execSQL(dropTableAccounts);
        db.execSQL(dropTableTransactions);
        db.execSQL(dropTableCustomers);
        db.execSQL(dropTableAccHolders);
        db.execSQL(createTableCustomers);
        db.execSQL(createTableAccounts);
        db.execSQL(createTableTransactions);
        db.execSQL(createTableAccHolders);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2){
            db.execSQL("DROP TABLE IF EXISTS ACCOUNTS");
            db.execSQL("DROP TABLE IF EXISTS CUSTOMERS");
            db.execSQL("DROP TABLE IF EXISTS TRANSACTIONS");
            onCreate(db);
        }
    }


}

