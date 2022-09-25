package com.example.microbank.data;

import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Model.Account;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

public interface AccountDAO {
    public void addAccount(Account acc);
    public List<Account> getAccountsList(String CustomerID) throws InvalidAccountException;
    public void updateBalance(String accNo,String type,Double trCharge,Double amount) throws InvalidAccountException;
    public boolean checkBalance(String accNo,double amount, String type,double charge) throws InvalidAccountException;
    public void initAccTable();
    public void LoadAccountData(JSONArray accounts);
    public void clearAccountsTable();
    public Account getAccount(String accountNumber, String customerID);
}
