
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Parse the commandline arguments for the supplied program
 * Author: Grant Miller <gem1086.rit.edu>
 * File: ArgumentParser.java
 * Date: 25 February 2019
 * Version: 0.1
 */
public class ArgumentParser {

    // Instance variables
    private HashMap<String, Argument> argShortList;
    private HashMap<String, Argument> argLongList;


    /**
     * ArgumentParser constructor
     */
    ArgumentParser() {
        this.argShortList = new HashMap<>();
        this.argLongList = new HashMap<>();
    }

    /**
     * Adds a new argument to the argument parser
     * @param type Type of argument
     * @param argFullName The full name of the argument (output)
     * @param argShortName The short name of the argument (o)
     */
    public void addArgument(Argtype type, String argFullName, String argShortName) {

        // Create and add the new argument
        Argument arg = new Argument(type, argFullName, argShortName);
        this.argLongList.put(argFullName, arg);
        this.argShortList.put(argShortName, arg);
    }


    /**
     * Parse the command string based on the Argument objects
     * in this parsing object
     * @param argString The arguments string
     * @param retLongNameMap True if callee wants the long named map, false if short named map
     * @exception IllegalArgumentException thrown if an argument supplied is not found
     */
    public ParserResult parseArgs(String[] argString, boolean retLongNameMap) {

        Argument prevArg = null;
        ArrayList<String> extraArgs = new ArrayList<>();

        // Iterate over the strings in the command line
        for(String s : argString) {

            // Check if this is an argument
            if(s.charAt(0) == '-') {

                // Strip any leading -'s
                while(s.charAt(0) == '-')
                    s = s.substring(1);

                // try and get this by full name
                if(!this.argLongList.containsKey(s)) {

                    // check if this key exists as the short hand
                    if(!this.argShortList.containsKey(s)) {

                        // If the argument is not in the short list
                        // or long list throw an exception
                        throw new IllegalArgumentException();
                    } else {

                        // If this argument is a flag set it to true
                        if(this.argShortList.get(s).getType() == Argtype.FLAG) {
                            this.argShortList.get(s).setVal(true);
                            continue;
                        }

                        // Set the previous arg to the recent argument
                        prevArg = this.argShortList.get(s);
                    }
                } else {

                    // If this argument is a flag set it to true
                    if(this.argLongList.get(s).getType() == Argtype.FLAG) {
                        this.argLongList.get(s).setVal(true);
                        continue;
                    }

                    // Set the previous arg to the recent argument
                    prevArg = this.argLongList.get(s);
                }
            } else {

                // This is not a flag, handle it
                if(prevArg != null) {

                    // Set the value of the argument then reset prevArg back to null
                    if(prevArg.getType() == Argtype.INTEGER) {

                        try {
                            // parse the int
                            prevArg.setVal(Integer.parseInt(s));
                        } catch (NumberFormatException e) {
                            // Set the value to null if a number is not supplied
                            prevArg.setVal(null);
                        }
                    } else

                        // Just save the string
                        prevArg.setVal(s);

                    // Save the newly updated argument
                    this.argShortList.put(prevArg.getArgShortName(), prevArg);
                    this.argLongList.put(prevArg.getArgFullName(), prevArg);

                    // Set previous argument to null
                    prevArg = null;
                } else

                    // Otherwise add the unknown argument to extraArgs
                    extraArgs.add(s);
            }
        }


        if(retLongNameMap)

            // returns a new ParserResult with the long list
            return new ParserResult(extraArgs, this.argLongList);
        else

            // returns a new ParserResult with the short list
            return new ParserResult(extraArgs, this.argShortList);
    }
}
