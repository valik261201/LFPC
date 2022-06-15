package Lab4;

// STEP 5 - Obtain the Chomsky Normal Form

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Step5 extends Step4 {

    public Step5(File grammarInput) throws FileNotFoundException {
        super(grammarInput);
    }

    protected void printChomskyNormalForm() {
        System.out.println("\033[1mSTEP 0: Context Free Grammar\033[0m");
        this.grammar.print();
        this.removeEpsilonProductions();
        this.removeUnitProductions();
        this.removeUnproductiveSymbols();
        this.removeInaccessibleSymbols();
        this.removeMoreThanTwo();
        this.addTerminalRules();
    }

    // remove productions with length > 2 and make them of length 1 or 2
    private void removeMoreThanTwo() {
        List<String> r = new ArrayList<>(this.grammar.getLeft());
        for (String left : r) {
            List<String> newRules = new ArrayList<>();
            for (String rule : this.grammar.rules(left)) {
                if (rule.length() > 2) {
                    // while the rule's length is > 2
                    while (rule.length() > 2) {
                        // take the first 2 characters and substitute them with a one-character symbol
                        String w = rule.substring(0, 2);
                        rule = rule.substring(2);
                        // create a list containing the symbols to be substituted
                        List<String> l = new ArrayList<>();
                        // add first 2 characters to the list
                        l.add(w);
                        String substituteSymbol = this.grammar.substituteSymbols();
                        // add to the non-terminals list the substituted symbol
                        this.grammar.getNonTerminals().add(substituteSymbol);
                        // add to the grammar new rules with the substitution symbols
                        this.grammar.addRule(substituteSymbol, l);
                        rule = substituteSymbol + rule;
                    }
                }
                newRules.add(rule);
            }
            this.grammar.changeRules(left, newRules);
        }
        System.out.println("\n\033[1mSTEP 5: Chomsky Normal Form\n5.1 Removing productions with length > 2:\033[0m");
        this.grammar.print();
    }

    // add terminal rules and replace those having 1 terminal and 1 non-terminal on
    // the right side (S -> aB)
    private void addTerminalRules() {
        for (String left : this.grammar.getLeft()) {
            List<String> l = new ArrayList<>();
            for (String rule : this.grammar.rules(left)) {
                StringBuilder newWord = new StringBuilder();
                if (rule.length() > 1) {
                    for (char c : rule.toCharArray()) {
                        if (!String.valueOf(c).toUpperCase().equals(String.valueOf(c))) {
                            newWord.append(String.valueOf(c).toUpperCase()).append("1");
                        } else {
                            newWord.append(c);
                        }
                    }
                    l.add(String.valueOf(newWord));
                } else
                    l.add(rule);
            }
            this.grammar.changeRules(left, l);
        }
        for (String terminal : this.grammar.getTerminals()) {
            String nonTerminal = terminal.toUpperCase() + "1";
            if (!this.grammar.getNonTerminals().contains(nonTerminal)) {
                this.grammar.getNonTerminals().add(nonTerminal);
            }
            List<String> l = new ArrayList<>();
            l.add(terminal);
            this.grammar.addRule(nonTerminal, l);
            this.grammar.getNonTerminals().add(nonTerminal);
        }
        System.out.println("\n\033[1m5.2 Adding new rules for terminals:\033[0m");
        this.grammar.print();
    }
}