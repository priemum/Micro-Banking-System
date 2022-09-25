package com.example.microbank.data.Implementation;

import com.example.microbank.data.Model.Account;

import java.util.List;

public class DataHolder {

    private List<Account> accountList;

    public void setAccountList(List<Account> accountList){
        this.accountList = accountList;
    }

    public List<Account> getAccList(){
        return accountList;
    }

    private static final DataHolder holder = new DataHolder();
    public static DataHolder getInstance() {return holder;}
}
