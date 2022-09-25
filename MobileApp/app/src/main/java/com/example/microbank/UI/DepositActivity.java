package com.example.microbank.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.microbank.Control.AppController;
import com.example.microbank.Control.SessionManagement;
import com.example.microbank.R;

import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Implementation.DataHolder;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.Model.Transaction;

import java.util.ArrayList;
import java.util.List;

public class DepositActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deposit);

        Button depositBtn = findViewById(R.id.btn_proceedDeposit);
        Spinner accSel = findViewById(R.id.spinnerDeposit);
        TextView amount = findViewById(R.id.deposit_amount);
        TextView reference = findViewById(R.id.deposit_reference);

        SessionManagement session = new SessionManagement(DepositActivity.this);
        String customerID = session.getSession();

        List<String> accountList = getSpinnerList(accSel, customerID);

        //Creating the ArrayAdapter instance having the accounts list
        ArrayAdapter acc_arr = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, accountList);
        acc_arr.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        accSel.setAdapter(acc_arr);

        depositBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String accountNo = accSel.getSelectedItem().toString();
                String deposit_amount = amount.getText().toString();
                String deposit_reference = reference.getText().toString();
                String type = "DEPOSIT";
                double charge = 30.00;

                if (checkAmountValid(deposit_amount)){
                    double amount = Double.parseDouble(deposit_amount);
                    Transaction tr = new Transaction(customerID, accountNo,type,charge,amount,deposit_reference);
                    openConfirmDepositView(tr);
                }
                else{
                    Toast.makeText(DepositActivity.this, "Please enter a valid amount", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    public void openConfirmDepositView(Transaction tr){
        Intent intent = new Intent(this, ConfirmDepositActivity.class);
        intent.putExtra("Transaction",tr);
        startActivity(intent);
    }

    public boolean checkAmountValid(String amount){
        return amount.matches("[-+]?[0-9]*\\.?[0-9]+");
    }

    // get the account numbers list for spinner
    public List<String> getSpinnerList(Spinner spin, String customerID){

        List<String> strAcc_no = new ArrayList<>();
        List<Account> accList = null;
        accList = DataHolder.getInstance().getAccList();
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
