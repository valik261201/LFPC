package Lab4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepFive extends StepFour {
    public StepFive(String filePath) throws IOException {
        super(filePath);
    }

    public void printChomskyNormalForm() {
        System.out.println("Context free grammar");
        grammar.paste();
        removeEmptyProd();
        removeUnitProduction();
        removeUnproductiveSymbols();
        removeInaccessibleSymbols();
        removeBiggerThanTwo();
    }

    //delete productions with length > 2
    public void removeBiggerThanTwo() {
        List<String> r = new ArrayList<>(this.grammar.left);
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
                        String substituteSymbol = this.grammar.replaceSymbol();
                        // add to the non-terminals list the substituted symbol
                        this.grammar.nonTerminals.add(substituteSymbol);
                        // add to the grammar new rules with the substitution symbols
                        grammar.addRule(substituteSymbol, l);
                        rule = substituteSymbol + rule;
                    }
                }
                newRules.add(rule);
            }
            this.grammar.changeRules(left, newRules);
        }
        System.out.println("STEP 5: Chomsky Normal Form\n5.1 Removing productions with length > 2:");
        this.grammar.paste();
    }

}
