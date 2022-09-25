package com.example.microbank.Control;



import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.microbank.data.AccountDAO;
import com.example.microbank.data.AccountHoldersDAO;
import com.example.microbank.data.CustomerDAO;
import com.example.microbank.data.Implementation.AccountDAO_Imp;
import com.example.microbank.data.Implementation.AccountHoldersDAO_Imp;
import com.example.microbank.data.Implementation.CustomerDAO_Imp;
import com.example.microbank.data.Implementation.TransactionDAO_Imp;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.TransactionDAO;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class AppController extends AppController_ab {
    private Context context;

    public AppController(Context context) {
        this.context = context;
        setup();
    }

    public void setup(){
        TransactionDAO transactionDAO = new TransactionDAO_Imp(context);
        AccountDAO accountDAO = new AccountDAO_Imp(context);
        setAccountDAO(accountDAO);
        setTransactionsDAO(transactionDAO);
        CustomerDAO customerDAO = new CustomerDAO_Imp(context);
        setCustomerDAO(customerDAO);
        AccountHoldersDAO accountHoldersDAO  = new AccountHoldersDAO_Imp(context);
        setAccountHoldersDAO(accountHoldersDAO);

//        /*Run this below methods to load dummy data first time to the tables. Then comment out. Or else will run into unique constraint errors*/
//        customerDAO.initCustomerTable();
//        accountDAO.initAccTable();
    }


}

