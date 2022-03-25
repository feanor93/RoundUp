package com.fra.interview.BankAccountOperations;

import com.fra.interview.Models.Account;
import com.fra.interview.Models.Amount;
import com.fra.interview.Models.SavingsGoal;
import com.fra.interview.Models.Transaction;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class RoundUpOperation {

    private final BankAccountInterface bankAccountInterface;
    public RoundUpOperation(BankAccountInterface bankAccountInterface){
        this.bankAccountInterface = bankAccountInterface;
    }

    /**
     *     this method returns all the transactions in the past week, it doesn't require custom Dates as those are generated dynamically in the main method
     */
    public List<Transaction> getAllTransactionGivenPeriod(Account account, String fromDate, String toDate) throws IOException {

        //get all transactions from account
        List<Transaction> allTransactions = bankAccountInterface.getTransactions(account.getAccountUid(), account.getDefaultCategory(), fromDate, toDate);
       for (Transaction allTransaction : allTransactions) {
            System.out.println(allTransaction.getAmount());
       }
        return allTransactions;
    }

    /**
     *     this method performs the effective roundUp operation by first checking the existente of at least one transaction
     *     then for every transaction (passed as a list) it loops and retrieves all the minorUnits by isolating the last 2 units.
     *     Once every minorUnits has been retrieved it substracts each one from 100 (the superior pound) and adds each one of that
     *     in a variable that is the totalRoundUp of the transactions.
     *     This final variable is then returned as the totalRoundUp from all the transactions
     * @param allTransactions
     * @return
     */

    public Amount doRoundUp(List<Transaction> allTransactions) {
        if(allTransactions.size() == 0){
            return new Amount("GBP", 0L);
        }
        //System.out.println(allTransactions);
        AtomicLong totalRoundUp = new AtomicLong(0L);

        allTransactions.forEach( t -> {
        long remains = t.getAmount().getMinorUnits() % 100L;
            totalRoundUp.addAndGet((100L - remains));
        });


        //System.out.println("totalRoundUp :" + totalRoundUp);
        return new Amount("GBP", totalRoundUp.get());
    }

    public List<SavingsGoal> getAllSavingsGoals(String accountUid, Amount amount) throws IOException {
        //first get all the savings goals for the given account
        List<SavingsGoal> savingsGoalList = bankAccountInterface.getSavingsGoals(accountUid);
        //then, for every account, return the percentage of it
        return savingsGoalList;
    }

    /**
     *     given an account, the total amount rounded up in the last week's transactions, and a specific savings goal (i.e. The trip to Norway)
     *     this performs a PUT adding up the money to the savings goal.
     *     if the "insert" is successful then it returns a true, otherwise it returns false
     * @param account
     * @param totalRoundUp
     * @param fromDate
     * @param toDate
     * @param savingsGoalUid
     * @return
     * @throws IOException
     */

    public boolean transferRoundUpToGoal(Account account, Amount totalRoundUp, String fromDate, String toDate, String savingsGoalUid) throws IOException {
        BankAccountInterface.GetTotalSavings totalSaving = new BankAccountInterface.GetTotalSavings(totalRoundUp);
        boolean isSent = bankAccountInterface.transferMoney(account.getAccountUid(), savingsGoalUid, UUID.randomUUID().toString(), totalSaving);
        if(isSent){
            return true;
        }
        return false;
    }

}
