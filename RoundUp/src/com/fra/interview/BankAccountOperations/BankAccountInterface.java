package com.fra.interview.BankAccountOperations;

import com.fra.interview.Models.*;

import java.io.IOException;
import java.util.List;

public interface BankAccountInterface {

    List<Account> getAccounts() throws IOException;

    Balance getBalance(String accountUid) throws IOException;

    List<Transaction> getTransactions(String accountUid, String categoryUid, String fromDate, String doDate) throws IOException;
    List<SavingsGoal> getSavingsGoals(String accountUid) throws IOException;
    SavingsGoal getSavingGoal(String accountUid, String savingsGoalUid) throws IOException;
    String createSavingsGoal(String accountUid, SavingsGoalReq savingsGoalReq) throws IOException;
    boolean transferMoney(String accountUid, String savingsGoalUid, String transferUid, GetTotalSavings totalSavings) throws IOException;



    public class SavingsGoalReq {

        private final String name;
        private final String currency;
        //I don't like target, change it
        private final Amount target;
        public SavingsGoalReq(String name, String currency, Amount target){
            this.name = name;
            this.currency = currency;
            this.target = target;
        }
        public String getName() {
            return name;
        }

        public String getCurrency() {
            return currency;
        }

        public Amount getTarget() {
            return target;
        }

    }

    public class GetTotalSavings {


        private final Amount amount;

        public GetTotalSavings(Amount amount){
            this.amount = amount;
        }
        public Amount getTotal() {
            return amount;
        }
    }
}
