# Microbank Mobile-App
 This is the mobile app for the CS3042 project.
 
 ## Using the app 

1.	Log into the app using the customer ID and password
<p align="center">
 <img src="/MobileApp/Screenshots/Screenshot1.png" width = 15%>
 </p>
2.	Select the desired function:
      a.	Cash deposit  
      b.	Cash withdrawal    
      (if the customer is not an assigned customer of the agent, only the withdrawal functionality will be available. This transaction will execute as a special request)
    <p align="center">
 <img src="/MobileApp/Screenshots/Screenshot2.png" width = 15%>
 </p>
3.	Enter bank account number, transaction amount and reference 
<p align="center">
 <img src="/MobileApp/Screenshots/Screenshot3.png" width = 15%>
 </p>
4.	Confirm the transaction and proceed to homepage<br>
5.	Log out or perform another transaction

## Methodology

There are 4 tables in the microbank app database –

*	Accounts : This table holds the vital account details which are the account number, account type and balance.
*	AccountHolders : This table maps the account numbers with the relevant account holders. Only the accounts of the assigned customers are stored here. A customer can have more than one account.
*	Customers : Data of the customers who are assigned to the specific agent will be stored in this table. Customer ID, name and password are stored as only these fields are necessary for the operations of the mobile app. 
*	Transactions : This table stores all the transactions that happen using the mobile app except for special requests and joint account transactions. 

### Important functions 

#### 1. Loading data from central database

It is important to establish proper communication with the central database, as most of the details in the local databases need to be updated regularly. Each time the app is loaded it will send an API call to the server and load the necessary details into these tables. The execution of this function is essential before any transaction could take place as it needs the updated account balances on each account. 

```java

public void getDataforAgent(){
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("sync").addQueryParameter("id",AGENT_ID).build();
        Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()){
                    String dataString = response.body().string();
                    try {
                        JSONArray accountsJson  = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("accounts");
                        JSONArray customerJson = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("users");
                        JSONArray AccHolderJson = new JSONObject(new JSONObject(dataString).getString("data")).getJSONArray("accountholders");
                        customerDAO.clearCustomerTable();
                        accountDAO.clearAccountsTable();
                        accountHoldersDAO.clearAccHolderts();
                        customerDAO.LoadCustomerData(customerJson);
                        accountDAO.LoadAccountData(accountsJson);
                        accountHoldersDAO.LoadAccHolderData(AccHolderJson);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
       }
 ```

The OkHttp3 library was used to send http requests to the server. The LoadCustomerData(), LoadAccountData() and LoadAccHolderData() functions are used for the load the above mentioned data which are in JSONArray format. Therefore the account balances are always up-to-date and there are no disparities between the actual amount and the amount in the local database.

#### 2. Updating Central Database

Updating the central database is an essential function in this system. If the transactions table has 50 transactions or the time period has been 24 hours since the last update, then all these transactions are sent to the central database and the transactions table in it is updated. Given below are some snippets of the [method](https://github.com/Wenuka19/Micro-Banking-System/blob/0d13583335a5e9fc1bbb8cbd778029304438bf2f/MobileApp/app/src/main/java/com/example/microbank/data/Implementation/TransactionDAO_Imp.java#L63) for updating the central database in the TransactionDAO_Imp class.

First all transactions in the Transactions table are selected. 
```java

public void updateCentralDB(boolean alarm) throws IOException {
        SQLiteDatabase db = this.getWritableDatabase();
        String trs = "SELECT * FROM TRANSACTIONS";
        Cursor cursor = db.rawQuery(trs, null);
```
Next, we check if the one of the 2 above mentioned conditions are satisfied. checkForUpdate() method checks if 50 transactions are completed, and 'alarm' is the boolean value that is passed if the alarm trigger is received.
If at least one condition is true, then all transactions are converted into a JSONArray object so they can be sent via an API call to the central database. 

```java

        if (checkForUpdate() || alarm){
            JSONArray arr = cur2Json(cursor);
            JSONObject obj = new JSONObject();
            try {
                obj.put("data", arr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
```
The OkHttpClient class is used to send the post request with the transactions JSONArray. After this API call is sent, the Transaction table in the local database is fully cleared by calling the clearTransactionsTable() method.

```java

            MediaType JSON = MediaType.parse("application/json; charset=utf-8");
            OkHttpClient client = new OkHttpClient();
            HttpUrl url = new HttpUrl.Builder().scheme("http").host(HOST_IP).port(3000).addPathSegment("api").addPathSegment("v1").addPathSegment("transaction").addQueryParameter("id", AGENT_ID).build();
            RequestBody body = RequestBody.create(JSON, obj.toString());
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    Log.d("TRUPDFail", "onFailure: no response");
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String dataString = response.body().string();
                    Log.d("TRUPDPass", "response received : " + dataString);
                }
            });
           clearTransactionsTable();
        }

    }
```


To check if the transactions table has 50 entries, we simply call an SQL query in the [checkForUpdate()](https://github.com/Wenuka19/Micro-Banking-System/blob/main/MobileApp/app/src/main/java/com/example/microbank/data/Implementation/TransactionDAO_Imp.java) method in TransactionDAO_Imp file. 

To update the central database at every 24 hours, we used a recurring alarm. The [SyncService](https://github.com/Wenuka19/Micro-Banking-System/blob/main/MobileApp/app/src/main/java/com/example/microbank/Control/SyncService.java) class, which is extended from BroadcastReceiver class, is used to trigger the update of central database once the alarm is received. 

Given below is the function to set the alarm in the [UpdateTrigger](https://github.com/Wenuka19/Micro-Banking-System/blob/main/MobileApp/app/src/main/java/com/example/microbank/Control/UpdateTrigger.java) class.

##### Alarm function

```java

 public void setAlarm(Class _class){
        Intent intent = new Intent(context, _class);
        PendingIntent pIntent = PendingIntent.getBroadcast(context, 2, intent, 0);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        if (alarmManager != null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 02);
            calendar.set(Calendar.MINUTE, 00);
            calendar.set(Calendar.SECOND, 00);
            long firstMillis = calendar.getTimeInMillis();
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, firstMillis,AlarmManager.INTERVAL_DAY, pIntent);
            Log.d("TRIGGER","Trigger is Set");
        }
    }
  ```    
Here, the AlarmManager class and Calendar classes were used to set the alarm at 02:00, and it is set to repeat at the interval of 1 day (AlarmManager.INTERVAL_DAY).  




