package com.personal.stocks.simulator;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: mbaid
 * Date: 9/16/13
 * Time: 12:30 AM
 * To change this template use File | Settings | File Templates.
 */
public class SimpleMain {
    public static void main(String args[]) throws Exception
    {

        /* Verify if input directory exists */
        String inputDirectoryName = args[0];

        /*  portfolio */
        String[] portfolio = {"GE"};

        /* Get the total investment amount and
         * create a Money object
         */

        System.out.println("Investing: " + args[1]);

        Money money = new Money(Integer.parseInt(args[1]));
        SmartTrader trader = new SmartTrader(money);
        trader.setPortfolio(portfolio);

        File inputDirectory = new File(inputDirectoryName);

        if (!inputDirectory.exists())
        {
            System.out.println("ERROR: Input directory does not exist");
            System.exit(0);
        }

        /* Start the simulator */
        Simulator sim = new Simulator();

        sim.startSimulation(inputDirectory, trader);
    }
}

