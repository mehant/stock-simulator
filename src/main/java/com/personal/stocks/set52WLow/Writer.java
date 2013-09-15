package com.personal.stocks.set52WLow;

import java.util.LinkedList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import au.com.bytecode.opencsv.CSVWriter;


/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 9:15 AM
 * To change this template use File | Settings | File Templates.
 */
public class Writer {

    LinkedList<DayStock> stockList;

    public Writer(LinkedList<DayStock> stockList)
    {
        this.stockList = stockList;
    }

    public void writeListToFile(File outputFile) throws IOException
    {
        CSVWriter writer = new CSVWriter(new FileWriter(outputFile));

        String[] description = {"Date", "Open", "High", "Low", "52 Week Low", "52 Week Low Date"};

        writer.writeNext(description);

        for (DayStock ds: stockList)
        {
            String[] nextDay = ds.getStringWithoutSymbol();

            writer.writeNext(nextDay);
        }

        writer.close();
    }
}
