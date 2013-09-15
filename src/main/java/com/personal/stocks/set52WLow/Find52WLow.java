package com.personal.stocks.set52WLow;

import com.personal.stocks.set52WLow.DayStock;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.PriorityQueue;
/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/14/13
 * Time: 6:55 PM
 * To change this template use File | Settings | File Templates.
 */

/* Size of the heap = 365
 * We want to compute the
 * 52 week low
 */


public class Find52WLow {

    LinkedList<DayStock> stockList;

    /* Since we want to calculate the 365 day low (or 52Week low)
     * our heap size is 365
     */
    int heapSize = 365;

    public Find52WLow(LinkedList<DayStock> stockList)
    {
        this.stockList = stockList;
    }


    public void compute() {

        PriorityQueue<DayStock> pq = new PriorityQueue<DayStock>(365, new Comparator<DayStock>() {
            @Override
            public int compare(DayStock dayStock, DayStock dayStock2) {

                if (dayStock.low > dayStock2.low)
                    return 1;
                else if (dayStock.low < dayStock2.low)
                    return -1;
                else
                    return 0;  //To change body of implemented methods use File | Settings | File Templates.
            }
        });


        if (stockList.size() <= heapSize)
            System.err.println("ERROR: Size of stock list is lesser than heap size cannot compute 52W low");
        /* Initialize the priority queue with first 365 entries
         */
        initPriorityQueue(pq);

        /* Now we have  a priority queue with size 365
         * For each new element in our list, we remove
         * the oldest element in the priority queue and
         * add the new element to it to obtain the
         * 52 week low price
         */
        int windowEndIndex = heapSize;
        int windowBeginIndex = 0;

        while (windowEndIndex < stockList.size())
        {
            /* Remove the stock at the beginning of the window */
            pq.remove(stockList.get(windowBeginIndex));

            /* Add the stock at the end of the window */
            DayStock ds = stockList.get(windowEndIndex);
            pq.add(ds);

            DayStock yearLow = pq.peek();

            ds.yearLow = yearLow.low;
            ds.yearLowDay = yearLow.day;

            windowBeginIndex++;
            windowEndIndex++;
        }
    }

    private void initPriorityQueue(PriorityQueue<DayStock> pq) {

        int count = 0;
        for (DayStock ds: stockList)
        {
            pq.add(ds);
            count++;

            DayStock yearLowStock = pq.peek();
            ds.yearLow = yearLowStock.low;
            ds.yearLowDay = yearLowStock.day;

            /* Check if our queue is complete */
            if (count >= 365)
            {
                System.out.println("Index: " + stockList.indexOf(ds));
                break;
            }
        }

    }
}
