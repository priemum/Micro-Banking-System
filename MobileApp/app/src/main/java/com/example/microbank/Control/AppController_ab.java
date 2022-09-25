package com.example.microbank.Control;


import androidx.annotation.NonNull;

import com.example.microbank.Control.Exception.AppControllerException;
import com.example.microbank.data.AccountDAO;
import com.example.microbank.data.AccountHoldersDAO;
import com.example.microbank.data.CustomerDAO;
import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.TransactionDAO;
import static com.example.microbank.Constants.AGENT_ID;
import static com.example.microbank.Constants.TR_CHARGE;
import static com.example.microbank.Constants.HOST_IP;

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

public abstract class AppController_ab implements Serializable  {
    private AccountDAO accountDAO;
    private TransactionDAO transactionDAO;
    private CustomerDAO customerDAO;
    private AccountHoldersDAO accountHoldersDAO;

    public List<Account> getAccounts(String customerID) throws InvalidAccountException {
        return accountDAO.getAccountsList(customerID);
    }

    public void addTransaction(String cusID, String accNo,String type,Double amount,String reference) throws InvalidAccountException {
        if (!(amount==0)){
            accountDAO.updateBalance(accNo,type,TR_CHARGE,amount);
            transactionDAO.addTransaction(cusID, accNo,type,TR_CHARGE,amount,reference);
        }
    }

    public void addAccount(String accNo,String customerID,String type,Double balance){
        Account account = new Account(accNo,customerID,type,balance);
        accountDAO.addAccount(account);
    }

    public abstract void setup() throws AppControllerException;

    public void setTransactionsDAO(TransactionDAO transactionDAO) {
        this.transactionDAO = transactionDAO;
    }
    public TransactionDAO getTransactionDAO() {
        return transactionDAO;
    }
    public void setAccountDAO(AccountDAO accountDAO) {
        this.accountDAO = accountDAO;
    }
    public AccountDAO getAccountDAO() {
        return accountDAO;
    }
    public CustomerDAO getCustomerDAO() {
        return customerDAO;
    }
    public void setCustomerDAO(CustomerDAO customerDAO) {
        this.customerDAO = customerDAO;
    }
    public Double getTrCharge() {
        return TR_CHARGE;
    }

    public AccountHoldersDAO getAccountHoldersDAO() { return accountHoldersDAO;}

    public void setAccountHoldersDAO(AccountHoldersDAO accountHoldersDAO) {this.accountHoldersDAO = accountHoldersDAO;}

    public void getDataforAgent(){
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("sync").addQueryParameter("id",AGENT_ID).build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String dataString = response.body().string();
                    try {
                        JSONArray accountsJson  = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("accounts");
                        JSONArray customerJson = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("users");
                        JSONArray AccHolderJson = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("accountholders");
                        customerDAO.clearCustomerTable();
                        accountDAO.clearAccountsTable();
                        accountHoldersDAO.clearAccHolderts();
                        customerDAO.LoadCustomerData(customerJson);
                        accountDAO.LoadAccountData(accountsJson);
                        accountHoldersDAO.LoadAccHolderData(AccHolderJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }


}
