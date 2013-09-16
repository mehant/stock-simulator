package com.personal.stocks.simulator;

import java.io.File;


/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/15/13
 * Time: 1:54 PM
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    public static void main(String args[]) throws Exception
    {

        /* Verify if input directory exists */
        String inputDirectoryName = args[0];

        File inputDirectory = new File(inputDirectoryName);

        if (!inputDirectory.exists())
        {
            System.out.println("ERROR: Input directory does not exist");
            System.exit(0);
        }

        /* Initialize the simulator */
        Simulator sim = new Simulator();

        for (File inputFile: inputDirectory.listFiles())
        {
            /*  portfolio */
            //String[] portfolio = {"GE"};

            String[] portfolio = new String[1];

            portfolio[0] = inputFile.getName();

            System.out.println("Trading stock: " + portfolio[0] + "\n \n \n");

            /* Get the total investment amount and
             * create a Money object
             */

            System.out.println("Investing: " + args[1]);

            Money money = new Money(Integer.parseInt(args[1]));
            SmartTrader trader = new SmartTrader(money);
            trader.setPortfolio(portfolio);

            sim.startSimulation(inputDirectory, trader);

            System.out.println("-------------------------------------");
        }
    }
}
