package com.fra.interview.Models;

import java.text.NumberFormat;
import java.util.Locale;

public class Amount {


    //use NumberFormat library to create a Locale instance of the money (changes depending on the country)
    private static final NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.UK);




    private final String currency;
    private final long minorUnits;

    public Amount(String currency, long minorUnits){
        this.currency = currency;
        this.minorUnits = minorUnits;
    }

    //method to format the output of the amounts, otherwise it would be an unformatted java object

    public String toString() {
        return nf.format(minorUnits * 0.01d);
    }

    public String getCurrency() {
        return currency;
    }

    public long getMinorUnits() {
        return minorUnits;
    }




}
