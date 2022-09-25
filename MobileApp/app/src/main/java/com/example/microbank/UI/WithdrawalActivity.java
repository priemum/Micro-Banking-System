package com.example.microbank.UI;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.microbank.Control.AppController;
import com.example.microbank.Control.SessionManagement;
import com.example.microbank.R;
import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Implementation.DataHolder;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.Model.Transaction;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class WithdrawalActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);

        Button withdrawalBtn = findViewById(R.id.withdrawalBtn);
        Spinner accSel = findViewById(R.id.spinnerWithdrawal);
        TextView amount = findViewById(R.id.withdraw_amount);
        TextView reference = findViewById(R.id.withdraw_reference);

        SessionManagement session = new SessionManagement(WithdrawalActivity.this);
        String customerID = session.getSession();
        boolean specialReq = session.isSpecialRequest();

        List<Account> accList = DataHolder.getInstance().getAccList();

        List<String> accountList = getSpinnerList(accSel, accList);
        ArrayAdapter acc_arr = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accountList);
        acc_arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        accSel.setAdapter(acc_arr);

        List<Account> finalAccList = accList;
        withdrawalBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String accountNo = accSel.getSelectedItem().toString();
                String withdraw_amount = amount.getText().toString();
                String withdraw_reference = reference.getText().toString();
                String type = "WITHDRAW";
                double charge = 30.00;
                if (specialReq)
                    charge = 60.00;

                if (checkAmountValid(withdraw_amount)){
                    double amount = Double.parseDouble(withdraw_amount);
                    if (isWithdrawable(amount, accountNo, finalAccList)){
                        Transaction tr = new Transaction(customerID, accountNo, type, charge, amount, withdraw_reference);
                        openConfrimWithdrawalPage(tr);
                    }
                }else{
                    Toast.makeText(WithdrawalActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public void openConfrimWithdrawalPage(Transaction tr){
        Intent intent = new Intent(this, ConfirmWithdrawalActivity.class);
        intent.putExtra("Transaction",tr);
        startActivity(intent);
    }

    public boolean checkAmountValid(String amount){
        return amount.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    public boolean isWithdrawable(double amount, String accNo, List<Account> accountList){
        double balance = 0;
        for (int i=0; i<accountList.size(); i++){
            Account acc = accountList.get(i);
            if (acc.getAcc_No().equals(accNo)){
                balance = acc.getBalance();
                break;
            }
        }
        return (balance > amount + 123.0);
    }

    public List<String> getSpinnerList(Spinner spin, List<Account> accList){

        List<String> strAcc_no = new ArrayList<>();
        spin.setOnItemSelectedListener(this);
        for (int i=0; i<accList.size(); i++){
            Account a = accList.get(i);
            strAcc_no.add(a.getAcc_No());
        }
        return strAcc_no;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}