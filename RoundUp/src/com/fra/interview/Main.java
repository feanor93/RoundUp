package com.fra.interview;

import com.fra.interview.APIConnection.HttpConnect;
import com.fra.interview.BankAccountOperations.BankAccount;
import com.fra.interview.BankAccountOperations.BankAccountInterface;
import com.fra.interview.BankAccountOperations.RoundUpOperation;
import com.fra.interview.Models.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Main {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public static void main(String[] args) throws IOException {

        String accessToken = Constants.AccessToken;

        HttpConnect httpClient = new HttpConnect(Constants.BaseUrl+Constants.ApiVersion, accessToken);
        BankAccountInterface bankAccountInterface = new BankAccount(httpClient);
        RoundUpOperation roundUpOperation = new RoundUpOperation(bankAccountInterface);

        //Get list of accounts
        List<Account> accounts = bankAccountInterface.getAccounts();
        if(accounts.isEmpty()){
            System.out.println("No accounts found");

        }

        Account account = accounts.get(0);
        String accountUid = account.getAccountUid();
        //System.out.println("accountUid: " + accountUid);

        //get account balance
        Balance balance = bankAccountInterface.getBalance(accountUid);

        /**
         * it's possible to retrieve the currency and the minorUnits for the balance
         */
        //System.out.println("balance in total is: " + balance.getAmount().getCurrency());
        //System.out.println("balance minorUnits: " + balance.getAmount().getMinorUnits());


        //get last week's transactions, where fromDate is one week from today
        Date date = new Date();
        String toDate = sdf.format(date);
        Date oneWeekAgo = new Date(System.currentTimeMillis() - 7L*24*3600*1000);
        String fromDate =sdf.format(oneWeekAgo);
//        System.out.println(toDate);
//        System.out.println(fromDate);
        List<Transaction> allTransactions = roundUpOperation.getAllTransactionGivenPeriod(account, fromDate, toDate);
        //
        //cover empty transations
        //System.out.println("transaction total: " + allTransactions.size());
        if(allTransactions.size()==0){
            System.out.println("no transactions for last week");
            System.exit(0);
        }

        /**
         * do the roundUp for every transaction in the passed list
         */
        Amount roundUpMoney = roundUpOperation.doRoundUp(allTransactions);

        //this is how much the tool rounded up
        //System.out.println("rounded up: " + roundUpMoney.toString());

        //check if roundUp is empty, if so end
        if(roundUpMoney.getMinorUnits() == 0){
            System.out.println("nothing to send");
            System.exit(0);
        }

        /**
         * the following lines can be executed once or many times:
         * @param savingsGoalsUid is used to create a savingsGoal in the case there aren't.
         * so a good approach would be to first check the existence of a saving goal
         *                        using the following code, then, if none, a new one can be created.
         *
         */
        List<SavingsGoal> savingsGoalList = roundUpOperation.getAllSavingsGoals(accountUid, roundUpMoney);
        //System.out.println("list of how many savingsGoals for this account: " + savingsGoalList.size());
        //for every goal print the total amount of money saved
        //savingsGoalList.forEach(s -> System.out.println(s.getTotalSaved().toString()));

        //for every goal print the target amount required
        //savingsGoalList.forEach(s -> System.out.println(s.getTarget().toString()));



//        Amount savingTarget = new Amount("GBP", 15000L);
//        String savingsGoalsUid = httpClient.createSavingsGoal(accountUid, new BankAccountInterface.SavingsGoalReq("Trip to Norway", "GBP", savingTarget));
//        System.out.println(savingsGoalsUid);
//        savingsGoalsUid = savingsGoalsUid.replaceAll("\"", "");
//
        /**
         * it's possible to pass a savingsGoalUid to the following method, instead of hardcoding a savingsGoalUid in the params
         */
        Boolean sendMoneyToSavingsGoal = roundUpOperation.transferRoundUpToGoal(account, roundUpMoney, fromDate, toDate, "e7ca3b72-9001-43d4-b1a3-cb8bbe8f59bf");
        if(!sendMoneyToSavingsGoal){
            System.out.println("Error in sending money");
            System.exit(0);
        }








    }
}
