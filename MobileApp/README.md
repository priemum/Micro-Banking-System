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

Updating the central database is an essential function in this system. If the transactions table has 50 transactions or the time period has been 24 hours since the last update, then all these transactions are sent to the central database and the transactions table in it is updated. 

To check if the transactions table has 50 entries, we simply call an SQL query in the [checkForUpdate()](Micro-Banking-System/MobileApp/app/src/main/java/com/example/microbank/data/Implementation/TransactionDAO_Imp.java) method in TransactionDAO_Imp file.




