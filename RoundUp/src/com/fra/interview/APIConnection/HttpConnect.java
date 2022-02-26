package com.fra.interview.APIConnection;

import com.fra.interview.BankAccountOperations.BankAccountInterface;
import com.fra.interview.Constants;
import com.fra.interview.Models.*;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import okhttp3.OkHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class HttpConnect implements HttpConnectInterface {

    //private static final baseUrl;
    private static String url = Constants.BaseUrl + Constants.ApiVersion;
    private static String apiVersion = Constants.ApiVersion;
    private static String accessToken;
    private static OkHttpClient client;

    private final Gson gson;

    public HttpConnect(String baseUrl, String accessToken){
        this.url = baseUrl;
        this.accessToken = accessToken;
        this.gson = new Gson();
    }



    @Override
    public List<Account> getAccounts() throws IOException {
        URL devUrl = new URL(url+ Constants.Accounts);
        String output = PerformGetQuery(devUrl);

        JsonObject accountsJsonObject = gson.fromJson(output, JsonObject.class);
        JsonArray accountsJsonArr = accountsJsonObject.getAsJsonArray("accounts");

        //it's possible to have multiple account, this function returns a list of every account (if it's a single one then it just returns a 1-d list)
        List<Account> listOfAccounts = new ArrayList<>();

        accountsJsonArr.forEach( jsonElement -> listOfAccounts.add(gson.fromJson(jsonElement, Account.class)));
        return listOfAccounts;
    }

    @Override
    public Balance getAccountBalance(String accountUid) throws IOException {
        URL devUrlBalance = new URL(url+Constants.Accounts+"/"+accountUid+Constants.Balance);
        String output = PerformGetQuery(devUrlBalance);
        JsonObject accountBalanceObject = gson.fromJson(output, JsonObject.class);
        JsonElement totalBalance = accountBalanceObject.get("amount");
        //parses the output to retrieve the object containing the account balance

        return new Balance(gson.fromJson(totalBalance, Amount.class));
    }

    @Override
    public List<Transaction> getTransactionFeeds(String accountUid, String categoryUid, String fromDate, String toDate) throws IOException {
        URL devUrlTransactionFeeds = new URL(url+"/feed"+Constants.Account+"/"+accountUid+"/category/"+categoryUid+Constants.TransactionsBetween+ Constants.MinTransactionTimestamp+fromDate+"&"+Constants.MaxTransactionTimestamp+toDate);
        String output = PerformGetQuery(devUrlTransactionFeeds);
        JsonObject transactionFeedObj = gson.fromJson(output, JsonObject.class);
        JsonArray transactionFeedArr = transactionFeedObj.getAsJsonArray("feedItems");
        //Returns a list of every transaction occurred in the last 7 days (dynamic)
        //using the starlingAPIs with Transaction-between
        List<Transaction> listOfTransactions = new ArrayList<>();
        transactionFeedArr.forEach(jsonElement -> listOfTransactions.add(gson.fromJson(jsonElement, Transaction.class)));
        return listOfTransactions;
    }

    @Override
    public List<SavingsGoal> getAllSavingsGoals(String accountUid) throws IOException {
        URL devUrlSavingsGoals = new URL(url+Constants.Account+"/"+accountUid+Constants.SavingsGoals);
        String output = PerformGetQuery(devUrlSavingsGoals);
        JsonObject savingsGoalsJsonObj = gson.fromJson(output, JsonObject.class);
        JsonArray savingsGoalsJsonArr = savingsGoalsJsonObj.getAsJsonArray("savingsGoalList");
        //returns a list of every saving goal created
        //note: for testing purposes I created many goals, see Main class for detailed info
        List<SavingsGoal> savingsGoalList = new ArrayList<>();
        savingsGoalsJsonArr.forEach(jsonElement -> savingsGoalList.add(gson.fromJson(jsonElement, SavingsGoal.class)));
        return savingsGoalList;
    }


    @Override
    public SavingsGoal getSavingsGoal(String accountUid, String savingsGoalUid) throws IOException {
        URL devUrlSavingsGoal = new URL(url+Constants.Account+accountUid+Constants.SavingsGoals+savingsGoalUid);
        //Retrieve a specific Savings Goal (i.e. Trip to Norway)
        String output = PerformGetQuery(devUrlSavingsGoal);
        return gson.fromJson(output, SavingsGoal.class);
    }

    @Override
    public String createSavingsGoal(String accountUid, BankAccountInterface.SavingsGoalReq savingsGoalReq) throws IOException {

        //this method is called only if there isn't an existing Savings Goal and returns the savingsGoalUid after parsing the API response
        //
        //Note: I created many savingsGoals for testing purposes, this method should be called from Main only if we want to create a new Goal
        //i.e. Saving for a new Car

        //the method reeives in input the json (created in the main method) to pass in the body containing
        //{
        //  "name": "Trip to Paris",
        //  "currency": "GBP",
        //  "target": {
        //    "currency": "GBP",
        //    "minorUnits": 123456
        //  },
        //  "base64EncodedPhoto": "string"
        //}

        String savingsGoalReqJson = gson.toJson(savingsGoalReq);

        URL devSavingsGoal = new URL(url+Constants.Account+"/"+accountUid+Constants.SavingsGoals);
        String output = PerformPutRequestToCreateAGoal(savingsGoalReqJson, devSavingsGoal);
        JsonObject savingsGoalUidObj = gson.fromJson(output, JsonObject.class);
        //returns the savingsGoalUid generated from the server
        //this value is used to access a specific SavingsGoal in other methods
        return savingsGoalUidObj.get("savingsGoalUid").toString();
    }



    @Override
    public boolean transferMoneyToSavingGoal(String accountUid, String savingsGoalUid, String transferUid, BankAccountInterface.GetTotalSavings getTotalSavings) throws IOException {


        //this method receives an element containing the money to send to a specific account, so:
        //totalSavings obtained by the roundUp method
        //{
        //  "amount": {
        //    "currency": "GBP",
        //    "minorUnits": 123456
        //  }
        //}

        //also required the savingsGoalUid to identify a specific goal
        //and a transferUid, that, according to the official API, is a random generated UUID
        String moneyToTransferJson = gson.toJson(getTotalSavings);
        URL sendMoneyToGoal = new URL(url+Constants.Account+"/"+accountUid+Constants.SavingsGoals+"/"+savingsGoalUid+Constants.AddMoney+"/"+transferUid);
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) sendMoneyToGoal.openConnection();

        //System.out.println(sendMoneyToGoal);
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+Constants.AuthToken);
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
        //System.out.println(moneyToTransferJson);
        osw.write(String.valueOf(moneyToTransferJson));
        osw.flush();
        osw.close();
        int responseCode = conn.getResponseCode();
        //System.out.println(responseCode);
        String output;
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        )){
            while((output = reader.readLine()) != null){
                buffer.append(output);
                buffer.append("\n");
            }
        }catch (Exception e){
            System.out.println("error");
        }
        output = buffer.toString();
        //System.out.println(output);
        JsonObject moneyToSentObj = gson.fromJson(output, JsonObject.class);

        //this, like methods that deal with database operations, returns a boolean indicating the outcomes of the operation
        //if returns true then it means the operation has been completely successfully.
        //in this case the server returns a 200 success in the body, so I just parsed the output to check for that word

        if(moneyToSentObj.has("success")){
            return true;
        }
        return false;


    }


    //method that performs a GET query to the StarlingAPIs
    private String PerformGetQuery(URL devUrl) throws IOException {
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) devUrl.openConnection();

        //System.out.println(devUrl);

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+Constants.AuthToken);
        int responseCode = conn.getResponseCode();
        String output;
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        )){
            while((output = reader.readLine()) != null){
                buffer.append(output);
                buffer.append("\n");
            }
        }catch (Exception e){
            System.out.println("error");
        }
        output = buffer.toString();
        return output;
    }

    private String PerformPutRequestToCreateAGoal(String savingsGoalReqJson, URL devSavingsGoal) throws IOException {
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) devSavingsGoal.openConnection();

        System.out.println(devSavingsGoal);
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+Constants.AuthToken);
        //obj.put()
        //output = buffer.toString();
        OutputStreamWriter osw = new OutputStreamWriter(conn.getOutputStream());
        osw.write(String.valueOf(savingsGoalReqJson));
        osw.flush();
        osw.close();
        int responseCode = conn.getResponseCode();
        String output;
        try(BufferedReader reader = new BufferedReader(
                new InputStreamReader(conn.getInputStream())
        )){
            while((output = reader.readLine()) != null){
                buffer.append(output);
                buffer.append("\n");
            }
        }catch (Exception e){
            System.out.println("error");
        }
        output = buffer.toString();
        return output;
    }
}
