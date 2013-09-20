package com.personal.stocks.simulator;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/18/13
 * Time: 12:55 AM
 * To change this template use File | Settings | File Templates.
 */
public class StockExchangeException extends Exception {

    /* this class is used to encapsulate exceptions that can
     * be thrown by StockExchange class
     */

    public StockExchangeException(String msg)
    {
        super(msg);
    }
}
