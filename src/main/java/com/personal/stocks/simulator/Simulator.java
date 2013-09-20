package com.personal.stocks.simulator;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 2:50 PM
 * To change this template use File | Settings | File Templates.
 */
public class Simulator {

    StockExchange mse;

    public void startSimulation(File inputDirectory, SmartTrader trader) throws Exception {

        Date date = new SimpleDateFormat("yyyy-MM-dd").parse("1980-01-01");
        startSimulation(inputDirectory, trader, date);

    }

    private void startSimulation(File inputDirectory, SmartTrader trader, Date date) throws Exception{

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
        mse.initStockExchange(inputDirectory, date);

        Date endDate = new SimpleDateFormat("yyyy-MM-dd").parse("2013-09-13");

        System.out.println("Before starting ");
        trader.money.print();

        while(date.before(endDate))
        {
            trader.performAction(mse, date);

            date.setTime(date.getTime() + 1 * 24 * 60 * 60 * 1000);
            mse.setDate(date);

        }

        System.out.println("Finally left with: ");
        trader.money.print();
        System.out.println("\nTotal Assets: " + trader.getTotalAssetValue(mse));
    }
}
