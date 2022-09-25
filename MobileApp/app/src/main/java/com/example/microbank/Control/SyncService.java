package com.example.microbank.Control;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;

public class SyncService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Sync function
        AppController appController = new AppController(context);
        try {
            appController.getTransactionDAO().updateCentralDB(true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(context, "Update Central Server!!!!!!!!!", Toast.LENGTH_SHORT).show();
        Log.d("TRIGTRIGTRIG", "TriggerReceived");
    }
}
