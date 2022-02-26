package com.fra.interview.Models;

import java.util.Date;

public class Transaction {


    private final String feedItemUid;
    private final String categoryUid;
    private final Amount amount;
    private final Date date;
    private final String source;
    private final String status;
    private final String direction;

    public Transaction(String feedItemUid, String categoryUid, Amount amount, Date date, String source, String status, String direction){
        this.feedItemUid = feedItemUid;
        this.categoryUid = categoryUid;
        this.amount = amount;
        this.date = date;
        this.source = source;
        this.status = status;
        this.direction = direction;
    }

    //getters
    public String getFeedItemUid() {
        return feedItemUid;
    }

    public String getCategoryUid() {
        return categoryUid;
    }

    public Amount getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }

    public String getDirection() {
        return direction;
    }
}
