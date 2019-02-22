/*
    File: Worker.java
    Author: Grant Miller
    Description: Does the threaded work for the program
 */


/**
 * Worker class will do the actual work for the program,
 * opening file, reading it, hashing it.
 */
public class Worker implements Runnable {

    private String fileName;
    private boolean isDirectory;

    /**
     * Worker constructor
     * @param fileName The file that this Worker will hash
     */
    Worker(String fileName, boolean isDirectory) {
        this.fileName = fileName;
        this.isDirectory = isDirectory;
    }

    /**
     * Main thread function. May spawn new Workers if the
     * file this thread is directed to hash is a directory
     */
    @Override
    public void run() {


    }
}
