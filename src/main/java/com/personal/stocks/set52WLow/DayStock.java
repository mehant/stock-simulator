package com.personal.stocks.set52WLow;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/14/13
 * Time: 5:59 PM
 * To change this template use File | Settings | File Templates.
 */

/* Class describes the stock data for a day */
public class DayStock {

    String symbol;
    float  open;
    float  high;
    float  low;
    float  yearLow;
    Date   day;
    Date   yearLowDay;

    public DayStock(String symbol, float open, float high, float low, Date day)
    {
        this.symbol = symbol;
        this.open   = open;
        this.high   = high;
        this.low    = low;
        this.day    = day;
    }

    public DayStock() {
    }

    public DayStock clone()
    {
        return new DayStock(this.symbol, this.open, this.high, this.low, this.day);
    }


    @Override
    public String toString()
    {
        String stock = new String();
        stock = stock.concat("Stock: ");
        stock = stock.concat(this.symbol + " ");
        stock = stock.concat(this.day.toString() + " ");
        stock = stock.concat(Float.toString(this.high) + " ");
        stock = stock.concat(Float.toString(this.low));

        return stock;
    }

    public String[] getStringWithoutSymbol() {

        String[] day = new String[6];

        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd-yyyy");

        day[0] = formatter.format(this.day);
        day[1] = Float.toString(this.open);
        day[2] = Float.toString(this.high);
        day[3] = Float.toString(this.low);
        day[4] = Float.toString(this.yearLow);
        day[5] = formatter.format(this.yearLowDay);

        return day;
    }
}
