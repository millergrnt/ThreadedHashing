
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
     * Args getter
     * @return the list of args
     */
    public HashMap<String, Argument> getArgShortList() {
        return argShortList;
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
                        prevArg = this.argLongList.get(s);
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
                    prevArg.setVal(s);
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

    public static void main(String[] args) {

        //TODO handle cases where argument is appended to end of name of flag
        String[] myargs = {"-o", "file.txt", "-i", "in.txt", "--setTrue"};

        //, "--number7", "-pSomething", "--bob=2"};

        ArgumentParser parser = new ArgumentParser();
        parser.addArgument(Argtype.STRING, "output", "o");
        parser.addArgument(Argtype.STRING, "input", "i");
        parser.addArgument(Argtype.FLAG, "setTrue", "t");
        parser.addArgument(Argtype.INTEGER, "number", "n");
        parser.addArgument(Argtype.STRING, "philly", "p");
        parser.addArgument(Argtype.INTEGER, "bob", "b");

        ParserResult res = parser.parseArgs(myargs, true);

        System.out.println("Hi");
    }
}
