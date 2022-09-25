package com.example.microbank.data;

import com.example.microbank.data.Model.Transaction;

import java.io.IOException;

public interface TransactionDAO {
    public void addTransaction(String customerID, String accNo,String type,Double trCharge,Double amount,String reference);
    public void updateCentralDB(boolean alarm) throws IOException;
    public void specialRequest(String customerID, String accNo,String type,Double trCharge,Double amount,String reference);
}
