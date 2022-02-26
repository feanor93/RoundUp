package com.fra.interview.Models;

public class SavingsGoal {


    private final String savingsGoalUid;
    private final String name;
    private final Amount totalSaved;
    private final Amount target;

    public SavingsGoal(String savingsGoalUid, String name, Amount totalSaved, Amount moneyAmount, Amount target) {
        this.savingsGoalUid = savingsGoalUid;
        this.name = name;
        this.totalSaved = totalSaved;
        this.target=target;
    }


    //getters
    public String getSavingsGoalUid() {
        return savingsGoalUid;
    }

    public String getName() {
        return name;
    }

    public Amount getTotalSaved() {
        return totalSaved;
    }

    public Amount getTarget() {
        return target;
    }
}
