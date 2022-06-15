package Lab5;

import java.util.*;

public class Grammar {
    public String startingSymbol;
    public HashSet<String> nonTerminalSet;
    public HashSet<String> terminalSet;
    public HashMap<String, HashSet<String>> productionRules;

    public char unusedNonTerminal = 'A';

    public HashMap<String, HashSet<String>> first = new HashMap<>();
    public HashMap<String, HashSet<String>> follow = new HashMap<>();
    public HashMap<String, HashMap<String, String>> parsingTable;

    public Grammar(String startingSymbol, HashSet<String> nonTerminalSet, HashSet<String> terminalSet, HashMap<String, HashSet<String>> productionRules) {
        this.startingSymbol = startingSymbol;
        this.nonTerminalSet = nonTerminalSet;
        this.terminalSet = terminalSet;
        this.productionRules = productionRules;
    }

    public boolean isNonTerminal(String checkSymbol){
        return nonTerminalSet.contains(checkSymbol);
    }

    public String getNextUnused(){
        while(nonTerminalSet.contains(Character.toString(unusedNonTerminal))){
            unusedNonTerminal++;
        }
        return Character.toString(unusedNonTerminal);
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "\nstartingSymbol = '" + startingSymbol + '\'' +
                ",\nnonTerminalSet = " + nonTerminalSet +
                ",\nterminalSet = " + terminalSet +
                ",\nproductionRules = " + productionRules +
                "\n}";
    }
}