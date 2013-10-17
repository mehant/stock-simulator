package com.personal.stocks.simulator;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 5:15 PM
 * To change this template use File | Settings | File Templates.
 */
public class Money {

    public static final String INITIAL_INVESTMENT_PROPERTY = "initial.investment";

    float totalAmount;
    float investedAmount;

    public Money(int totalAmount)
    {
        this.totalAmount = totalAmount;
        this.investedAmount = 0;
    }

    public Money()
    {
        totalAmount = Float.parseFloat(Conf.get(INITIAL_INVESTMENT_PROPERTY));
    }

    public float getAvailableFunds() {
        return this.totalAmount - this.investedAmount;  //To change body of created methods use File | Settings | File Templates.
    }

    public void invest(float totalCost) {
        this.investedAmount += totalCost;
    }

    public void returns(float cash) {

        if (cash > investedAmount)
        {
            totalAmount += cash - investedAmount; /* add profits */
            investedAmount = 0;

        }
        else
            this.investedAmount -= cash;
        //To change body of created methods use File | Settings | File Templates.
    }

    public void print()
    {
        System.out.printf("Total Funds: %f Available Funds: %f Invested Funds: %f \n", this.totalAmount, getAvailableFunds(), this.investedAmount);
    }
}
