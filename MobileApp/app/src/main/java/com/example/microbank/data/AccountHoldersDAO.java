package com.example.microbank.data;

import org.json.JSONArray;

public interface AccountHoldersDAO {
    public void LoadAccHolderData(JSONArray accHolders);
    public void clearAccHolderts();
}
