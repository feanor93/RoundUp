package com.fra.interview.BankAccountOperations;

import com.fra.interview.APIConnection.HttpConnect;
import com.fra.interview.Models.Account;
import com.fra.interview.Models.Balance;
import com.fra.interview.Models.SavingsGoal;
import com.fra.interview.Models.Transaction;

import java.io.IOException;
import java.util.List;

public class BankAccount implements BankAccountInterface {
    private final HttpConnect httpConnect;

    public BankAccount(HttpConnect httpConnect){
        this.httpConnect = httpConnect;
    }

    @Override
    public List<Account> getAccounts() throws IOException {
        return httpConnect.getAccounts();
    }

    @Override
    public Balance getBalance(String accountUid) throws IOException {
        return httpConnect.getAccountBalance(accountUid);
    }

    @Override
    public List<Transaction> getTransactions(String accountUid, String categoryUid, String fromDate, String toDate) throws IOException {
        return httpConnect.getTransactionFeeds(accountUid, categoryUid, fromDate, toDate);
    }

    @Override
    public List<SavingsGoal> getSavingsGoals(String accountUid) throws IOException {
        return httpConnect.getAllSavingsGoals(accountUid);
    }


    @Override
    public SavingsGoal getSavingGoal(String accountUid, String savingsGoalUid) throws IOException{
        return httpConnect.getSavingsGoal(accountUid, savingsGoalUid);
    }

    @Override
    public String createSavingsGoal(String accountUid, SavingsGoalReq savingsGoalReq) throws IOException {
        return httpConnect.createSavingsGoal(accountUid, savingsGoalReq);
    }

    @Override
    public boolean transferMoney(String accountUid, String savingsGoalUid, String transferUid, GetTotalSavings totalSavings) throws IOException {
        return httpConnect.transferMoneyToSavingGoal(accountUid, savingsGoalUid, transferUid, totalSavings);
    }
}
