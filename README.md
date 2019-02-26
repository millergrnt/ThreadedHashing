# ThreadedHashing
A simple recursive threaded file hashing program in Java


# Usage
java ThreadedHashingMain [-m/--hash_method method] file1 file2 ..

# Argument Parser
Included in the program is a commandline argument parser for java.

ParserResult.java
ArgumentParser.java
Argument.java

The parser sets unfound arguments to null or false if it is of Argtype.FLAG type.
Any values found in the argument string that are not associated with a flag are 
returned in the extraArgs string array in the ParseResult object.

Example:
val1 --flagName something val2
val1 and val2 will be returned in the extraArgs array in the ParseResult object.

# Argument Parser Usage
```
// Create argument parser
ArgumentParser parser = new ArgumentParser();

// Add argument to parser
// This will add the flag --flagName, and -f 
parser.addArgument(Argtype, "flagName", "f");

// parse arguments
// true - You want the long name hash map returned "flagName"
// false - You want the short name hash map returned "f"
ParseResult res = parser.parseArgs(args, true);

// Gets the value associated with flagName if the argument was supplied null otherwise
res.getArgMap().get("flagName").getVal();
```
