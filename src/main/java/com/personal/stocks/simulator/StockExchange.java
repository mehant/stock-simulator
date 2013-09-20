package com.personal.stocks.simulator;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.LinkedHashMap;
import org.joda.time.DateTimeComparator;

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

    static final int STOCK_ERR_CODE = Integer.MIN_VALUE;

    Date startDate;
    Date currentDate;

    LinkedHashMap<String, LinkedList<DayStock>> stockData;


    public void initStockExchange(File inputDirectory, Date startDate) throws IOException, ParseException {

        this.startDate = startDate;
        this.currentDate = startDate;

        if (stockData != null)
            return; /* nothing to do; we've already initialized the Stock exchange */

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

    public LinkedList<DayStock> getStockList(String symbol)
    {
        /* Check if we have a stock with symbol in our map */
        LinkedList<DayStock> stockList = stockList = stockData.get(symbol);

        if (stockList == null)
            System.out.println("ERROR: Symbol does not exist" + symbol);

        return stockList;
    }

    public boolean validateDate(String symbol)
    {
        Date startDate = getFirstTradeDate(symbol);
        Date endDate   = getLastTradeDate(symbol);

        /* Check if our current date is within the
         * start and end dates of the stock being traded
         */
        if ((currentDate.after(startDate) || currentDate.equals(startDate)) && (currentDate.before(endDate) || currentDate.equals(endDate)))
            return true;

        //System.out.println("ERROR: Current date: " + currentDate + "is invalid for stock symbol: " + symbol + "Start: " + startDate + " enddate: " + endDate);
        return false;
    }

    public void buy(String symbol, int buyQty, Money money) throws StockExchangeException
    {

        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        float stockPrice = getHighPrice(symbol);
        float totalCost  = stockPrice * buyQty;

        if (totalCost > money.getAvailableFunds())
        {
            System.out.println("Not enough funds available: cost: " +  stockPrice * buyQty + "availableFunds: " + money.getAvailableFunds());
        }

        money.invest(totalCost);

        System.out.println("Bought stocks: " + symbol + " Date:" + currentDate + " cost/stock: " + stockPrice + " Qty: " + buyQty + " remaining funds: " + money.getAvailableFunds());
    }

    public void sell(String symbol, int sellQty, Money money) throws StockExchangeException
    {

        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        Float costPrice = getHighPrice(symbol);

        money.returns(costPrice * sellQty);

        System.out.println("Sold stocks: " + symbol + " Date: " + currentDate + " cost/stock: " + costPrice + " Qty: " + sellQty + " remaining funds: " + money.getAvailableFunds());
    }

    public float getHighPrice(String symbol) throws StockExchangeException
    {
        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
        if ((validateDate(symbol)) == false)
        {
            /* Raising exceptions is too darn slow, returning error codes */
            //throw new StockExchangeException("Current date is invalid: " + currentDate + " for symbol: " + symbol);
            return STOCK_ERR_CODE;
        }

        for(DayStock ds: stockDayPrices)
        {
            if ((DateTimeComparator.getDateOnlyInstance().compare(currentDate, ds.getDate())) == 0)
            {
                return ds.getHighPrice();
            }
        }

        //throw new StockExchangeException("Current date is invalid1: " + currentDate + " for symbol: " + symbol);
        return STOCK_ERR_CODE;
    }

    public float getLowPrice(String symbol) throws StockExchangeException
    {

        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
        if ((validateDate(symbol)) == false)
        {
            //throw new StockExchangeException("Current date is invalid: " + currentDate + " for symbol: " + symbol);
            return STOCK_ERR_CODE;

        }

        for(DayStock ds: stockDayPrices)
        {
            if ((DateTimeComparator.getDateOnlyInstance().compare(currentDate, ds.getDate())) == 0)
            {
                return ds.getLowPrice();
            }
        }

        //throw new StockExchangeException("Current date is invalid: " + currentDate + " for symbol: " + symbol);
        return STOCK_ERR_CODE;
    }

    public float getYearLowPrice(String symbol) throws StockExchangeException
    {

        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        /* make sure the current date is within the dates
         * where the stock was traded publicly
         */
         if ((validateDate(symbol) == false))
        {
            //throw new StockExchangeException("Current date is invalid: " + currentDate + " for symbol: " + symbol);
            return STOCK_ERR_CODE;
        }

        for(DayStock ds: stockDayPrices)
        {
            if ((DateTimeComparator.getDateOnlyInstance().compare(currentDate, ds.getDate())) == 0)
            {
                return ds.getYearLow();
            }
        }

        //throw new StockExchangeException("Current date is invalid: " + currentDate + " for symbol: " + symbol);
        return STOCK_ERR_CODE;

    }

    public Date getFirstTradeDate(String symbol)
    {
        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        DayStock beginDay = stockDayPrices.getFirst();

        return beginDay.getDate();
    }

    public Date getLastTradeDate(String symbol)
    {
        LinkedList<DayStock> stockDayPrices = getStockList(symbol);

        DayStock endDay = stockDayPrices.getLast();

        return endDay.getDate();
    }

    /* This function is called when
     * simulator is finished performing
     * all actions for a day and is
     * moving to the next day
     */
    public void setDate(Date date) {
        this.currentDate = date;
    }

    public float getLastPrice(String symbol) throws  StockExchangeException{
        /* returns the low price for the current date
         * if the price for today does not exist it will
         * return the low price for the last available date
         */
        float lastPrice = getLowPrice(symbol);

        if (lastPrice == STOCK_ERR_CODE)
        {
            LinkedList<DayStock> stockDayPrices = getStockList(symbol);
            DayStock endDay = stockDayPrices.getLast();

            lastPrice =  endDay.getLowPrice();

        }

        return lastPrice;

    }
}
