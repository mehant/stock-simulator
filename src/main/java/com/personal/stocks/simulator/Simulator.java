package com.personal.stocks.simulator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import java.io.File;
import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Simulator {


    StockExchange mse;

    /* This value gives the sum of total assets
    * for each simulation
    */
    float sumOfAllSimulations = 0;


    public void startSimulation(File inputDirectory, Trader trader) throws Exception {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01");

        Calendar cal = Calendar.getInstance();
        cal.clear(Calendar.HOUR);
        cal.clear(Calendar.MINUTE);
        cal.clear(Calendar.SECOND);
        cal.clear(Calendar.MILLISECOND);

        Date endDate = cal.getTime();

        startSimulation(inputDirectory, trader, date, endDate);

    }

    private void startSimulation(File inputDirectory, Trader trader, Date startDate, Date endDate) throws Exception
    {

        float asset = 0;

        /* Start the stock market simulation beginning at date */

        /* Initialize the stock exchange
         * For the current date and the
         * available data under our inputDirectory
         *
         * Ofcouse if the world were my java program then
         * I would name the stock exchange after myself
         *
         */
        if (mse == null)
          mse = new StockExchange();

        /* Now the StockExchange has all the stock
         * data in the form of a hashmap
         * Key: Stock Symbol
         * Value: Linked list of daily prices of the stock
         */
        mse.initStockExchange(inputDirectory, startDate);

        System.out.println("Before starting ");
        trader.money.print();

        while(startDate.before(endDate))
        {
            trader.performAction(mse, startDate);

            startDate.setTime(startDate.getTime() + 1 * 24 * 60 * 60 * 1000);
            mse.setDate(startDate);

        }

        System.out.println("Finally left with on: " + endDate);
        trader.money.print();

        /* Find the total assets */
        asset = trader.getTotalAssetValue(mse);

        System.out.println("\nTotal Assets: " + asset);
        sumOfAllSimulations += asset;

        System.out.println("********************************************************");
        System.out.println("SUM: " + sumOfAllSimulations);
        System.out.println("********************************************************");
    }
}
