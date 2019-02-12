package com.example.aluno.tabexperiment.beans;

public class Currency {
    private String currencyFrom;
    private String currencyTo;
    private String rateDate;
    private String amount;

    public Currency() {
    }

    public Currency(String currencyFrom, String currencyTo, String rateDate, String amount) {
        this.currencyFrom = currencyFrom;
        this.currencyTo = currencyTo;
        this.rateDate = rateDate;
        this.amount = amount;
    }

    public String getCurrencyFrom() {
        return currencyFrom;
    }

    public void setCurrencyFrom(String currencyFrom) {
        this.currencyFrom = currencyFrom;
    }

    public String getCurrencyTo() {
        return currencyTo;
    }

    public void setCurrencyTo(String currencyTo) {
        this.currencyTo = currencyTo;
    }

    public String getRateDate() {
        return rateDate;
    }

    public void setRateDate(String rateDate) {
        this.rateDate = rateDate;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}
