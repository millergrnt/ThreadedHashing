/*
    File: ThreadedHashingMain.java
    Author: Grant Miller <gem1086@rit.edu>
    Description: Main function of the program. Parses command line args
                    and then spawns threads
 */

import java.io.File;
import java.util.ArrayList;

/**
 * Main function of the program. Parses command line args and then spawns the threads
 * Author: Grant Miller <gem1086.rit.edu>
 * File: ThreadedHashingMain.java
 * Date: 25 February 2019
 * Version: 0.1
 */
public class ThreadedHashingMain {


    /**
     * Main function of the program
     * @param args Commandline arguments
     */
    public static void main(String[] args) {

        // Make sure the start location is supplied
        if(args.length < 1) {
            System.err.println("Usage: java ThreadedHashingMain startLocation");
            System.exit(1);
        }

        // Create argument parser
        ArgumentParser parser = new ArgumentParser();

        // Add hash method argument to the parsers
        parser.addArgument(Argtype.STRING, "hash_method", "m");

        // Parse the arguments and get results
        ParserResult res = parser.parseArgs(args, true);

        // Get the input files
        ArrayList<String> inFiles = res.getExtraArgs();
        ArrayList<Worker> mainWorkers = new ArrayList<>();

        // Save the start location and attempt to create the file object
        for(String extraFile : inFiles) {

            File file = null;
            try {

                // Create the file object
                file = new File(extraFile);
            } catch (NullPointerException e) {

                System.err.println("Error getting supplied start file");
                System.exit(1);
            }

            // Get the argument from the hash method if it was supplied
            Worker mainWorker;
            if(res.getArgMap().get("hash_method").getVal() != null) {
                mainWorker = new Worker(file, (String)res.getArgMap().get("hash_method").getVal());
            } else {
                mainWorker = new Worker(file, "MD5");
            }

            // Start the main worker thread
            mainWorkers.add(mainWorker);
            mainWorker.start();
        }

        // Join the main worker thread
        for(Worker mainWorker : mainWorkers) {
            try {
                mainWorker.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        // Print output for each worker
        for(Worker mainWorker : mainWorkers) {
            // Print results
            System.out.println(String.format("%s %s digest: %s",
                    mainWorker.getFile().getAbsolutePath(), mainWorker.getHashMethod(),
                    mainWorker.getHashResult()));
        }
    }
}
