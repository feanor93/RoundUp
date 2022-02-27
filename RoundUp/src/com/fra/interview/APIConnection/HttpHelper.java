package com.fra.interview.APIConnection;

import com.fra.interview.Constants;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHelper {

    //method that performs a GET query to the StarlingAPIs
    public static String PerformGetQuery(URL devUrl) throws IOException {
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) devUrl.openConnection();


        conn.setRequestMethod("GET");
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+ Constants.AuthToken);
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

    public static String PerformPutRequestToCreateAGoal(String savingsGoalReqJson, URL devSavingsGoal) throws IOException {
        StringBuilder buffer = new StringBuilder();
        HttpURLConnection conn = (HttpURLConnection) devSavingsGoal.openConnection();

        //System.out.println(devSavingsGoal);
        conn.setRequestMethod("PUT");
        conn.setDoOutput(true);
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Authorization", "Bearer "+Constants.AuthToken);

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
