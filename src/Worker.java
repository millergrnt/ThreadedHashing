/*
    File: Worker.java
    Author: Grant Miller
    Description: Does the threaded work for the program
 */


import java.io.*;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
            this.hashMethod = new String(sb);
        }
    }
}
