package com.fra.interview.APIConnection;

import com.fra.interview.BankAccountOperations.BankAccountInterface;
import com.fra.interview.Models.Account;
import com.fra.interview.Models.Balance;
import com.fra.interview.Models.SavingsGoal;
import com.fra.interview.Models.Transaction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

public interface HttpConnectInterface {

    List<Account> getAccounts() throws IOException;

    Balance getAccountBalance(String accountUid) throws MalformedURLException, IOException;

    List<Transaction> getTransactionFeeds(String accountUid, String categoryUid, String fromDate, String ToDate) throws IOException;
    List<SavingsGoal> getAllSavingsGoals(String accountUid) throws MalformedURLException, IOException;
    SavingsGoal getSavingsGoal(String accountUid, String savingsGoalUid) throws IOException;
    String createSavingsGoal(String accountUid, BankAccountInterface.SavingsGoalReq savingsGoalReq) throws IOException;
    boolean transferMoneyToSavingGoal(String accountUid, String savingsGoalUid, String transferUid, BankAccountInterface.GetTotalSavings getTotalSavings) throws IOException;

}
