package com.personal.stocks.set52WLow;

import au.com.bytecode.opencsv.CSVReader;
import com.csvreader.CsvReader;


import java.io.FileInputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.LinkedList;
import java.io.IOException;
import com.personal.stocks.set52WLow.DayStock;
import java.io.File;
import java.util.ListIterator;
import org.joda.time.DateTime;
import org.joda.time.Days;




/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/14/13
 * Time: 5:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class Reader {

    public static final String BASIC_INFO = "BASIC";
    public static final String YEAR_LOW_INFO = "YEARLOW";

    String fileName;

    String infoType;

    public Reader (String fileName, String infoType)
    {
        this.fileName = fileName;
        this.infoType = infoType;
    }
    Reader(String fileName)
    {
        this(fileName, Reader.BASIC_INFO);
    }

    public LinkedList<DayStock> createListFromFile() throws IOException, ParseException {
        CSVReader read = new CSVReader(new FileReader(fileName));

        System.out.println("Initializing data for stock: " + new File(fileName).getName() + "\n");


        LinkedList<DayStock> list = new LinkedList<DayStock>();


        /* Read the header */
        String[] nextLine = read.readNext();

        Date nextDate = null;

        while ((nextLine = read.readNext()) != null)
        {

            DayStock stock = null;

            if (this.infoType.equalsIgnoreCase(Reader.BASIC_INFO))
            {
                Date date = new SimpleDateFormat("yyyy-MM-dd").parse(nextLine[0]);

                stock = new DayStock((new File(fileName).getName()), Float.parseFloat(nextLine[1]),
                                      Float.parseFloat(nextLine[2]),
                                      Float.parseFloat(nextLine[3]),
                                      date);
            }
            else if (this.infoType.equalsIgnoreCase(Reader.YEAR_LOW_INFO))
            {
                Date date = new SimpleDateFormat("MM-dd-yyyy").parse(nextLine[0]);

                Date yearLowDate = new SimpleDateFormat("MM-dd-yyyy").parse(nextLine[5]);

                stock  = new DayStock((new File(fileName).getName()), Float.parseFloat(nextLine[1]),
                        Float.parseFloat(nextLine[2]),
                        Float.parseFloat(nextLine[3]),
                        date, Float.parseFloat(nextLine[4]), yearLowDate);
            }


            /**
             * To compute the 52 Week low we have a heap with size 365;
             * This would actually compute the 365 day low, but this time
             * frame would span more than a year as this means 365 days where
             * trading took place. To not screw around with the sliding window and
             * have complicated logic there I've added entries for holidays as well;
             * They have the same price as the previous day but have a separate entry for
             * each holiday. This way we can keep our heap size constant and get the 365 day
             * low easily; Only drawback is the below piece of hacky logic.
             */
            /* This IF block inserts stocks for holidays */
            if (list.size() > 0)
            {
                Date currentDate = new Date();
                currentDate.setTime(stock.day.getTime());

                int  i = 0;
                do {
                    if (i > 0)
                    {
                        Date dupDate = new Date();
                        dupDate.setTime(nextDate.getTime());

                        if (this.infoType == Reader.YEAR_LOW_INFO)
                            System.err.println("ERROR: Missing dates while reading year low data: " + dupDate);

                        list.addFirst(new DayStock(stock.symbol, stock.open, stock.high, stock.low, dupDate));
                    }
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(nextDate);
                    cal.add(Calendar.DATE, -1);
                    //nextDate.setTime(nextDate.getTime() - 1 * 24 * 60 * 60 * 1000);
                    nextDate = cal.getTime();
                    i++;
                }   while (currentDate.before(nextDate));
            }

            if (this.infoType == Reader.BASIC_INFO)
                list.addFirst(stock);
            else if (this.infoType == Reader.YEAR_LOW_INFO)
                list.addLast(stock);

            nextDate = new Date();
            nextDate.setTime(stock.day.getTime());
        }

        /* Print the linked list */

        /*
        System.out.println("PRINTING THE LIST ");
        for (DayStock e: list)
        {
            System.out.println(e.toString());
        } */

        return list;
    }


}
