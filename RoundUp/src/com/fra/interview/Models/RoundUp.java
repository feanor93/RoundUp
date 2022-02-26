package com.fra.interview.Models;

import java.util.Date;

public class RoundUp {


    private final String accountUid;
    private final String savingsGoalUid;
    private final Amount amount;
    private final String fromDate;
    private final String toDate;

    public RoundUp(String accountUid, String savingsGoalUid, Amount amount, String fromDate, String toDate) {
        this.accountUid = accountUid;
        this.savingsGoalUid = savingsGoalUid;
        this.amount = amount;
        this.fromDate = fromDate;
        this.toDate = toDate;
    }

    //getters
    public String getAccountUid() {
        return accountUid;
    }

    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public Amount getAmount() {
        return amount;
    }

    public String getFromDate() {
        return fromDate;
    }

    public String getToDate() {
        return toDate;
    }
}
