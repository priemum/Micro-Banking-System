package com.example.microbank.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

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

import java.util.List;

public class LogoutActivity extends AppCompatActivity {

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        Button logout = findViewById(R.id.logoutBtn);

        AppController_ab appController = new AppController(LogoutActivity.this);
        SessionManagement sessionManagement = new SessionManagement(LogoutActivity.this);
        String customer_id = sessionManagement.getSession();

        RecyclerView rv = findViewById(R.id.rv_accList);
        List<Account> accountList;

        accountList = DataHolder.getInstance().getAccList();

        AccDisplayAdapter arr_adp = new AccDisplayAdapter(this, accountList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(linearLayoutManager);
        rv.setAdapter(arr_adp);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logOut();
            }
        });
    }

    public void logOut(){
        SessionManagement sessionManagement = new SessionManagement(LogoutActivity.this);
        sessionManagement.removeSession();
        openLoginpage();
    }

    public void openLoginpage(){
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
