package com.example.microbank.UI;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.microbank.R;

import java.util.List;

import com.example.microbank.data.Model.Account;

public class AccDisplayAdapter extends RecyclerView.Adapter<AccDisplayAdapter.Viewholder> {

    private Context context;
    private List<Account> accList;

    public AccDisplayAdapter(Context context, List<Account> accList){
        this.context = context;
        this.accList = accList;
    }

    @NonNull
    @Override
    public AccDisplayAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_list_item, parent, false);
        return new Viewholder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull AccDisplayAdapter.Viewholder holder, int position) {
        // to set data to textview and imageview of each card layout
        Account acc = accList.get(position);
        holder.accID.setText(acc.getAcc_No());
        holder.bal.setText(acc.getBalance().toString());

        String acc_type = acc.getAccount_type();
        switch (acc_type){
            case "CHILDREN":
                holder.type.setImageResource(R.drawable.child);
                break;
            case "TEEN":
                holder.type.setImageResource(R.drawable.teen);
                break;
            case "ADULT":
                holder.type.setImageResource(R.drawable.adult);
                break;
            case "SENIOR":
                holder.type.setImageResource(R.drawable.senior);
                break;
            case "JOINT":
                holder.type.setImageResource(R.drawable.joint);
                break;
        }
    }

    @Override
    public int getItemCount() {
        // this method is used for showing number
        // of card items in recycler view.
        return accList.size();
    }

    // View holder class for initializing of
    // your views such as TextView and Imageview.
    public class Viewholder extends RecyclerView.ViewHolder{

        private TextView accID;
        private TextView bal;
        private ImageView type;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            accID = itemView.findViewById(R.id.list_item1);
            bal = itemView.findViewById(R.id.list_item2);
            type = itemView.findViewById(R.id.accType);
        }
    }
}
