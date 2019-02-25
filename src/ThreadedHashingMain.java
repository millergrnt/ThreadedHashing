/*
    File: ThreadedHashingMain.java
    Author: Grant Miller <gem1086@rit.edu>
    Description: Main function of the program. Parses command line args
                    and then spawns threads
 */

import java.io.File;

/**
 * Main function of the program. Parses command line args and then spawns the threads
 */
public class ThreadedHashingMain {


    /**
     * Main function of the program
     * @param args Commandline arguments
     */
    public static void main(String[] args) {

        if(args.length < 1) {
            System.err.println("Usage: java ThreadedHashingMain startLocation");
            System.exit(1);
        }

        String startLocation = args[0];

        File file = null;

        try {

            // Create the file object
            file = new File(startLocation);
        } catch (NullPointerException e) {

            System.err.println("Error getting supplied start file");
            System.exit(1);
        }

        Worker mainWorker = new Worker(file, "MD5");

        mainWorker.run();

        try {
            mainWorker.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.exit(1);
        }

        System.out.println(String.format("%s %s digest: %s",
                file.getAbsolutePath(), mainWorker.getHashMethod(), mainWorker.getHashResult()));
    }
}
