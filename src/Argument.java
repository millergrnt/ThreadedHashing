/**
 * Represents an argument on the command line
 */

/**
 * Simple enum to help interpret arguments
 */
enum Argtype{ FLAG,STRING,INTEGER }

/**
 * Represents an argument type
 * Author: Grant Miller <gem1086@rit.edu>
 * File: Argument.java
 * Date: 25 February 2019
 * Version
 */
public class Argument {

    // Instance variables
    private Argtype type;
    private Object val;
    private String argFullName;
    private String argShortName;

    /**
     * Argument constructor
     * @param type Type of the argument
     * @param argFullName Full name of the argument i.e. 'output'
     * @param argShortName Short name of the argument i.e 'o'
     */
    Argument(Argtype type, String argFullName, String argShortName) {
        this.type = type;
        this.argFullName = argFullName;
        this.argShortName = argShortName;
    }


    /**
     * Full name getter
     * @return The full name of this argument
     */
    public String getArgFullName() {
        return argFullName;
    }


    /**
     * Short name getter
     * @return The short name of this argument
     */
    public String getArgShortName() {
        return argShortName;
    }

    /**
     * Val setter
     * @param val The value derived from the command line
     */
    public void setVal(Object val) {
        this.val = val;
    }


    /**
     * Gets the type of this argument
     * @return type of the argument
     */
    public Argtype getType() {
        return type;
    }
}
