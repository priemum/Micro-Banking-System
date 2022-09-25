package com.example.microbank.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microbank.Control.AppController;
import com.example.microbank.Control.AppController_ab;
import com.example.microbank.Control.SessionManagement;
import com.example.microbank.R;
import com.example.microbank.data.Exception.InvalidAccountException;
import com.example.microbank.data.Implementation.DataHolder;
import com.example.microbank.data.Model.Account;
import com.example.microbank.data.Model.Customer;

import java.util.List;

public class HomepageActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Button logout = findViewById(R.id.logoutBtn);
        ImageButton withdrawImgBtn = findViewById(R.id.withdrawalImgBtn);
        ImageButton depositImgBtn = findViewById(R.id.depositImgBtn);

        TextView nameTag = findViewById(R.id.name_tag);
        SessionManagement sessionManagement = new SessionManagement(HomepageActivity.this);
        String customer_id = sessionManagement.getSession();

        nameTag.setText("Hello "+sessionManagement.getFirstName());

        RecyclerView rv = findViewById(R.id.rv_accList);
        List<Account> accountList = null;
        accountList = DataHolder.getInstance().getAccList();

        AccDisplayAdapter arr_adp = new AccDisplayAdapter(this, accountList);

        // below line is for setting a layout manager for our recycler view.
        // here we are creating vertical list so we will provide orientation as vertical
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        // in below two lines we are setting layoutmanager and adapter to our recycler view.
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(arr_adp);

        withdrawImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openWithdrawalPage();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        depositImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDepositpage();
            }
        });
    }

    public void openLoginpage(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openWithdrawalPage(){

        Intent intent = new Intent(this, WithdrawalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void openDepositpage(){

        Intent intent = new Intent(this, DepositActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    public void logOut(){
        SessionManagement sessionManagement = new SessionManagement(HomepageActivity.this);
        sessionManagement.removeSession();
        openLoginpage();
    }

}
