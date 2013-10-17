package com.personal.stocks.simulator;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import org.apache.hadoop.conf.Configuration;


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

        /* Going forward we will have numerous types of Traders
         * Currently we only have one SmartTrader. But the trader we
         * use to perform our simulation should come from a configuration
         * file.
         */
        String className = Conf.get(Trader.TRADER_CLASS_PROPERTY);

        /* Initialize the simulator */
        Simulator sim = new Simulator();

        Class <? extends Object> traderClass = null;

        if (className != null)
        {
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            traderClass = cl.loadClass(className);
            System.out.println("trader class: " + traderClass);
        }

        /* For each stock symbol in this directory
         * run the simulation assuming you can buy
         * only one stock
         */
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

            System.out.println("TraderClassName: " + traderClass.getName());

            Trader trader1 = (Trader) traderClass.newInstance();

            trader1.money = money;

            //SmartTrader trader = new SmartTrader(money);
            trader1.setPortfolio(portfolio);

            sim.startSimulation(inputDirectory, trader1);

            System.out.println("-------------------------------------");
        }
    }
}
