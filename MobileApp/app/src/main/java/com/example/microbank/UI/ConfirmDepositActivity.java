package com.example.microbank.UI;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microbank.Control.AppController;
import com.example.microbank.Control.AppController_ab;
import com.example.microbank.Control.SessionManagement;
import com.example.microbank.R;


import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Implementation.TransactionDAO_Imp;
import com.example.microbank.data.Model.Transaction;
//Shared Preferences(customerID)=>
public class ConfirmDepositActivity extends AppCompatActivity {
    private AppController_ab appController = new AppController(ConfirmDepositActivity.this);
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_confirm_deposit);
        Button confirm = findViewById(R.id.conDepositBtn);
        TextView depositAccount = findViewById(R.id.depositToAcc);
        TextView depositAmount = findViewById(R.id.depositAmount);
        TextView depositRef = findViewById(R.id.depositReference);

        Transaction tr = getIntent().getParcelableExtra("Transaction");

        String cusId = tr.getCustomerID();
        String accNo = tr.getAccNo();
        Double amount = tr.getAmount();
        String reference = tr.getReference();
        Double charge = appController.getTrCharge();
        String type = "DEPOSIT";

        depositAccount.setText(accNo);
        depositAmount.setText(String.valueOf(amount));
        depositRef.setText(reference);
        Log.d("DEPOSITAMT", String.valueOf(amount));
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (appController.getAccountDAO().checkBalance(accNo,amount,type,charge)){
                        appController.addTransaction(cusId, accNo,type,amount,reference);
                        Toast.makeText(ConfirmDepositActivity.this, "Transaction added successfully", Toast.LENGTH_LONG).show();
                        openHomePage();
                    }
                } catch (InvalidAccountException e) {
                    e.printStackTrace();
                }
                ;
            }
        });
    }

    public void openHomePage(){
        Intent intent = new Intent(this, HomepageActivity.class);
        startActivity(intent);
    }
}
