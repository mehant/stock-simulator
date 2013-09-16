package com.personal.stocks.simulator;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
public class SmartTrader {

    public class BuyDetails
    {
        int    quantity;
        float  buyPrice;

        /* TODO: Need to add a field buy date
         * This would be useful when we buy the
         * same stock in multiple chunks
         */

        public BuyDetails(int quantity, float buyPrice)
        {
            this.quantity = quantity;
            this.buyPrice = buyPrice;
        }

        public float getBuyPrice()
        {
            return this.buyPrice;
        }

        public int getQuantity()
        {
            return this.quantity;
        }

    }

    public Money money;

    /* Indicates the margin upon which
     * the sell will be triggered
     * Its X times the buy price
     */
    public float profitMargin = (float) 2;

    LinkedHashMap<String, BuyDetails> portfolio;

    /* years after which to start investing in stock
     * after it went IPO
     */
    int years = 5;


    public SmartTrader(Money money)
    {
        this.money = money;
    }

    public void setPortfolio(String[] symbols)
    {
        portfolio = new LinkedHashMap<String, BuyDetails>();

        for (String str: symbols)
        {

            portfolio.put(str, new BuyDetails(0, 0));
        }
    }

    public void performAction(StockExchange mse, Date currentDate) {

        Iterator it = portfolio.entrySet().iterator();

        for (Map.Entry<String, BuyDetails> entry: portfolio.entrySet())
        {
            String symbol  = entry.getKey();
            int    sellQty = entry.getValue().getQuantity();
            int    buyQty  = 0;

            if ((buyQty = checkToBuy(symbol, mse, currentDate)) > 0)
            {
                mse.buy(symbol, buyQty, money);
            }

            if (sellQty > 0 && (checkToSell(symbol, mse)) == true)
            {
                mse.sell(symbol, sellQty, money);
                portfolio.put(symbol, new BuyDetails(0, 0));
            }

        }
    }

    private int checkToBuy(String symbol, StockExchange mse, Date currentDate) {

        float availableFunds = money.getAvailableFunds();
        float stockPrice     = mse.getHighPrice(symbol);

        /* Naive approach; don't buy the stock if we already have it */
        if (portfolio.get(symbol).getQuantity() > 0)
            return 0;

        if (availableFunds < stockPrice)
            return 0;

        /* we have enough funds to buy the stock (atleast one)
         * Check if we want to buy the stocks
         */
        if (mse.getLowPrice(symbol) == mse.getYearLowPrice(symbol))
        {

            /* We want to buy after stock has been trading in the market
             * for atleast 'years' years
             */
            Date stableDate = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(mse.getFirstTradeDate(symbol));
            cal.add(Calendar.YEAR, years);
            stableDate = cal.getTime();

            if (currentDate.after(stableDate))
            {

                //System.out.println("Stable Date" + stableDate + " Current Date: " + currentDate + "First Trade date: " + mse.getFirstTradeDate(symbol));

                /* looks like we are near the 52 week low,
                 * Go ahead and buy
                 */
                int quantity = (int) Math.floor(availableFunds/stockPrice);

                /* TODO: This should not be here
                 * move it
                 */
                portfolio.put(symbol, new BuyDetails(quantity, stockPrice));

                return quantity;
            }
        }

        return 0;

    }

    private boolean checkToSell(String symbol, StockExchange mse) {

        float buyPrice  = portfolio.get(symbol).getBuyPrice();
        float sellPrice = mse.getHighPrice(symbol);

        if (((sellPrice/buyPrice)) >= profitMargin)
        {
            //System.out.println("Sellprice: " + sellPrice + "buyPrice: " + buyPrice + "margin" + ((sellPrice/buyPrice)));
            return true;
        }

        return false;

    }
}
