package com.fra.interview.Models;

import java.util.Date;

public class Account {


    private final String accountUid;
    private final String currency;
    private final Date createdDate;
    private final String defaultCategory;

    public Account(String accountUid, String currency, Date createdDate, String defaultCategory){
        this.accountUid = accountUid;
        this.currency = currency;
        this.defaultCategory = defaultCategory;
        this.createdDate = createdDate;
    }

    //getters
    public String getAccountUid() {
        return accountUid;
    }

    public String getCurrency() {
        return currency;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public String getDefaultCategory() {
        return defaultCategory;
    }
}
