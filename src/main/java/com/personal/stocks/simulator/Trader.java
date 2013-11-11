package com.personal.stocks.simulator;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 10/4/13
 * Time: 12:08 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class Trader {

    public Money money;

    public Trader(Money money)
    {
        this.money = money;
    }

    public Trader()
    {
    }

    public static final String TRADER_CLASS_PROPERTY = "simulator.trader.class";

    public abstract void setPortfolio(String[] symbols);

    public abstract void performAction(StockExchange mse, Date currentDate);

    public abstract int checkToBuy(String symbol, StockExchange mse, Date currentDate);

    public abstract boolean checkToSell(String symbol, StockExchange mse);

    public abstract float getTotalAssetValue(StockExchange mse);
}
