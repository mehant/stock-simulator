package com.personal.stocks.simulator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.LinkedHashMap;

import com.personal.stocks.set52WLow.Reader;
import com.personal.stocks.set52WLow.DayStock;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 3:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class StockExchange {

    Date startDate;
    Date currentDate;

    LinkedHashMap<String, LinkedList<DayStock>> stockData;


    public void initStockExchange(File inputDirectory, Date startDate) throws IOException, ParseException {

        this.startDate = startDate;
        this.currentDate = startDate;

        for (File inputFile: inputDirectory.listFiles())
        {
            Reader read = new Reader(inputFile.getAbsolutePath(), Reader.YEAR_LOW_INFO);
            String symbol = inputFile.getName();
            LinkedList<DayStock> list = read.createListFromFile();

            System.out.println("Displaying list: ");

            /*
            for (DayStock ds: list)
            {
                System.out.println(ds.toString() + " 52 Week Low: " + ds.getYearLow() + " 52 Week low date: " + ds.getYearLowDate());
            } */

            if (stockData == null)
            {
                /* Initialize the hash map */
                stockData = new LinkedHashMap<String, LinkedList<DayStock>>();
            }

            stockData.put(symbol, list);


        }

    }

    public void buy(String symbol, int buyQty, Money money) {

        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
        }

        float stockPrice = getHighPrice(symbol);
        float totalCost  = stockPrice * buyQty;

        if (totalCost > money.getAvailableFunds())
        {
            System.out.println("Not enough funds available: cost: " +  stockPrice * buyQty + "availableFunds: " + money.getAvailableFunds());
        }

        money.invest(totalCost);

        System.out.println("Bought stocks: " + symbol + " Date:" + currentDate + " cost/stock: " + stockPrice + " Qty: " + buyQty + " remaining funds: " + money.getAvailableFunds());
    }

    public void sell(String symbol, int sellQty, Money money) {

        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
        }

        Float costPrice = getHighPrice(symbol);

        money.returns(costPrice * sellQty);

        System.out.println("Sold stocks: " + symbol + " Date: " + currentDate + " cost/stock: " + costPrice + " Qty: " + sellQty + " remaining funds: " + money.getAvailableFunds());
    }

    public float getHighPrice(String symbol)
    {
        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
            return -1;
        }

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
        DayStock beginDay = stockDayPrices.getFirst();
        DayStock endDay   = stockDayPrices.getLast();

        if (!(currentDate.after(beginDay.getDate()) && currentDate.before(endDay.getDate())))
        {
            return -1;
        }

        for(DayStock ds: stockDayPrices)
        {
            if (ds.getDate().equals(currentDate))
            {
                return ds.getHighPrice();
            }
        }

        return -1;


    }

    public float getLowPrice(String symbol) {
        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
            return -1;
        }

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
        DayStock beginDay = stockDayPrices.getFirst();
        DayStock endDay   = stockDayPrices.getLast();

        if (!(currentDate.after(beginDay.getDate()) && currentDate.before(endDay.getDate())))
        {
            return -1;

        }

        for(DayStock ds: stockDayPrices)
        {
            if (ds.getDate().equals(currentDate))
            {
                return ds.getLowPrice();
            }
        }

        return -1;

    }

    public float getYearLowPrice(String symbol) {

        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
            return -1;
        }

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
        DayStock beginDay = stockDayPrices.getFirst();
        DayStock endDay   = stockDayPrices.getLast();

        if (!(currentDate.after(beginDay.getDate()) && currentDate.before(endDay.getDate())))
        {
            return -1;
        }

        for(DayStock ds: stockDayPrices)
        {
            if (ds.getDate().equals(currentDate))
            {
                return ds.getYearLow();
            }
        }

        return -1;
    }

    public Date getFirstTradeDate(String symbol)
    {
        LinkedList<DayStock> stockDayPrices = stockData.get(symbol);

        if (stockDayPrices == null)
        {
            System.out.println("ERROR: Symbol does not exist" + symbol);
        }

        DayStock beginDay = stockDayPrices.getFirst();

        return beginDay.getDate();
    }

    public void setDate(Date date) {
        this.currentDate = date;
    }
}
