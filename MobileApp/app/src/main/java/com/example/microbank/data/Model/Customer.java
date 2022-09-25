package com.example.microbank.data.Model;

public class Customer {
    private String customer_id;
    private String first_Name;
    private String last_Name;

    public Customer(String customer_id, String first_Name, String last_Name) {
        this.customer_id = customer_id;
        this.first_Name = first_Name;
        this.last_Name = last_Name;
    }

    public String getCustomer_id() {
        return customer_id;
    }

    public void setCustomer_id(String customer_id) {
        this.customer_id = customer_id;
    }

    public String getFirst_Name() {
        return first_Name;
    }

    public void setFirst_Name(String first_Name) {
        this.first_Name = first_Name;
    }

    public String getLast_Name() {
        return last_Name;
    }

    public void setLast_Name(String last_Name) {
        this.last_Name = last_Name;
    }
}
