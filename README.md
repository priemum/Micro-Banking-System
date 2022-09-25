# Micro-Banking-System

## Overview
This is a mini-project implemented for CS3042 – Database Systems module in semester 5. 

Through this project, we developed a microbank system for a local bank. This system consists of a mobile device which is carried out to customers’ homes by bank agents, and a central server with a central database which is only accessible to administrators. The main goal of this system is to provide the customer the comfort of doing cash transactions from home instead of going to the usual bank branch.

Each bank customer is assigned a specific agent according to their residential area when creating their bank account. 

### The mobile device / The microbank app

Each agent has a mobile device that is taken to customers. This device has a lightweight database with a table of assigned customers’ details. In this project we implemented this device simply as an android app instead of developing the device itself since we were instructed to do so. Therefore, the device will be referred to as the ‘microbank app’ in the rest of this document.

This app performs the following functionalities for the customer:
  *	Cash deposits and withdrawals
  *	Multiple agent withdrawal facility (only for joint account holders)
  *	Special requests – a method to withdraw cash from a different agent than the assigned one. This costs an additional charge per each transaction.

After a certain number of transactions (in this prototype we have set this number to 10), or, after a certain period of time (here, it is 24 hours) the app will automatically update the central server with the transactions in its local database. 
Transactions done through multiple agent withdrawal facility or special request will be updated in the server immediately. They are not stored in the local database.

### The Central Server

The bank administrators are responsible for the following functionalities that are implemented in the central server.
*	Creating new customer accounts 
    -	Children
    -	Adult
    -	Joint
    -	Teen
    -	Senior
*	Creating Fixed Deposits (FD) – An FD can only be opened if the customer already has a savings account
*	Viewing the following reports
    -	Agent-wise total transaction report
    -	Account-wise total transaction report
    
There are other main functionalities that are automatically implemented in the server:
*	Calculating interest rates and updating the account balances accordingly (the rates differ for each account type)
*	Calculating the monthly interests for FD and updating the linked savings account

