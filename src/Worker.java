/*
    File: Worker.java
    Author: Grant Miller
    Description: Does the threaded work for the program
 */


import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Worker class will do the actual work for the program,
 * opening file, reading it, hashing it.
 */
public class Worker extends Thread {

    private File file;
    private String hashResult;
    private String hashMethod;

    /**
     * Worker constructor
     * @param file The file that this Worker will hash
     */
    Worker(File file, String hashMethod) {
        this.file = file;
        this.hashMethod = hashMethod;
    }


    /**
     * Hash result getter
     * @return Hash result of this thread
     */
    public String getHashResult() {
        return hashResult;
    }


    /**
     * HashMethod getter
     * @return hash method that this thread used
     */
    public String getHashMethod() {
        return this.hashMethod;
    }

    /**
     * Does the digesting for the worker
     * @param content Content to digest
     */
    private void digest(byte[] content) {

        // Create digest object
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(this.hashMethod);

        } catch (NoSuchAlgorithmException e) {

            // Something went horribly wrong if this is called
            synchronized (System.err) {

                // Alert that the file wanted was not found
                System.err.println("Digest method not found");
                this.hashResult = null;
                return;
            }
        }

        // Digest the file
        byte[] digestRes = digest.digest(content);


        // Convert the bytes into their correct hex format and return it
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < digestRes.length; ++i) {
            sb.append(Integer.toHexString((digestRes[i] & 0xFF) | 0x100).substring(1,3));
        }

        // Save this hashes result
        this.hashResult = new String(sb);
    }

    /**
     * Main thread function. May spawn new Workers if the
     * file this thread is directed to hash is a directory
     */
    @Override
    public void run() {

        // Tests if this is an actual file
        if(this.file.isFile()) {

            byte[] content;
            try {
                content = Files.readAllBytes(this.file.toPath());
            } catch (IOException e) {

                synchronized (System.err) {
                    System.err.println("Error occurred while reading from the file.");
                    return;
                }
            }

            // Digest the content of the file
            digest(content);

        } else if (this.file.isDirectory()) {

            // The file handed is a directory so spawn new workers and wait for them to finish
            // before hashing their hashes and returning

            // List the files in this dir
            File[] dirChildren = this.file.listFiles();

            // Create the array list of workers which will be used to join
            ArrayList<Worker> dirWorkers = new ArrayList<>();
            for(File child : dirChildren) {

                // For every file in the dir start a new worker with that directory
                Worker w = new Worker(child, this.hashMethod);
                dirWorkers.add(w);
                w.start();
            }

            // Join the workers
            for(Worker w : dirWorkers) {
                try {
                    w.join();
                } catch (InterruptedException e) {

                    // Shouldn't happen
                    e.printStackTrace();
                    return;
                }
            }

            // Get the results from every worker into one string for this worker to digest
            String totalDigest = "";
            for(Worker w : dirWorkers)
                totalDigest += w.getHashResult();

            digest(totalDigest.getBytes());

        } else {

            // Unknown file type
            System.out.println(String.format("File: %s was of unknown type and not digested"));
        }
    }
}
