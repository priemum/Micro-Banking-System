package com.example.microbank.data.Model;

import java.nio.DoubleBuffer;

public class Account{
    private String acc_No;
    private String customer_ID;
    private String account_type;
    private Double balance;


    public Account(String accountNo, String customer_ID,String account_type,Double balance) {
        this.acc_No = accountNo;
        this.customer_ID=customer_ID;
        this.account_type=account_type;
        this.balance=balance;
    }

    public String getAcc_No(){
        return acc_No;
    }
    public String getCustomer_ID(){
        return customer_ID;
    }
    public String getAccount_type(){
        return account_type;
    }
    public Double getBalance() {
        return balance;
    }

    //Small security issue here. Try to think of a wayaround to not have a getter for pwd -> DONE

    public void setBalance(double new_balance){
        balance=new_balance;
    }

}
