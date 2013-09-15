package com.personal.stocks.set52WLow;

import java.io.IOException;
import java.io.*;
import java.text.ParseException;
import java.util.LinkedList;

import com.personal.stocks.set52WLow.DayStock;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/14/13
 * Time: 2:49 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static File createOutputFile(File inputFile) throws IOException
    {
        String input = inputFile.getAbsolutePath();
        String path  = input.substring(0, input.lastIndexOf('/'));
        String outputPath = path.concat("/outputDir");

        File outputDirectory = new File(outputPath);

        if (!outputDirectory.exists())
            outputDirectory.mkdir();

        String outputName = outputPath.concat("/" + inputFile.getName());

        File outputFile = new File(outputName);

        if (!outputFile.exists())
            outputFile.createNewFile();

        return outputFile;

    }
    public static void main(String args[])  throws  IOException, ParseException
    {
        System.out.println("hello world");

        String inputDir = args[0];

        File inputDirectory = new File(inputDir);

        /* check if input directory exists */
        if (!inputDirectory.exists())
        {
            System.out.println("ERROR:Input directory does not exist ");
            System.exit(0);
        }

        for (File inputFile : inputDirectory.listFiles())
        {

            if (!inputFile.exists())
            {
                System.err.println("Error: Input directory does not exist ");
                System.exit(0);
            }

            System.out.println("Processing file: " + inputFile.getAbsolutePath());


            Reader obj = new Reader(inputFile.getAbsolutePath());

            LinkedList<DayStock> list = obj.createListFromFile();

            /*
            for (DayStock ds: list)
            {
                System.out.println(ds.toString());
            } */

            Find52WLow yearLow = new Find52WLow(list);

            yearLow.compute();

            /*
            for (DayStock ds: list)
            {
                System.out.println(ds.toString() + " 52W LOW: " + ds.yearLow + " Date: " + ds.yearLowDay);
            } */


            File outputFile = createOutputFile(inputFile);

            System.out.println("OutputFile: " + outputFile.getName() + "Entire path: " + outputFile.getAbsolutePath());

            Writer write = new Writer(list);

            write.writeListToFile(outputFile);
        }
    }
}
