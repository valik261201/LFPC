package Lab4;

// STEP 3 - Eliminate Unproductive Symbols

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Step3 extends Step2 {


    public Step3(File grammarInput) throws FileNotFoundException {
        super(grammarInput);
    }

    protected void removeUnproductiveSymbols() {
        List<String> unproductive = new ArrayList<>();
        for (String nonTerminal : this.grammar.getNonTerminals()) {
            if (!this.grammar.getLeft().contains(nonTerminal)) {
                // first, add all non-terminals to the list with unproductive symbols
                unproductive.add(nonTerminal);
            }
        }
        int i = 0;
        // analyse all right-side productions
        for (List<String> words : this.grammar.getRight()) {
            i += 1;
            List<String> removals = new ArrayList<>();
            // analyse each right-side production separately
            for (String word : words) {
                // analyse each character from every right-side production
                for (char c : word.toCharArray()) {
                    // if the list with unproductive symbols contains that character
                    if (unproductive.contains(String.valueOf(c))) {
                        // add the whole word/production to the list with removals
                        removals.add(word);
                        break;
                    }
                }
            }
            this.grammar.getRight().get(i - 1).removeAll(removals);
        }
        List<Integer> removals = new ArrayList<>();
        int k = 0;
        // analyse each non-terminal symbol from left-side
        for (String left : this.grammar.getLeft()) {
            // if the grammar rules don't contain that non-terminal symbol (== 0)
            if (this.grammar.rules(left).size() == 0) {
                // remove it
                removals.add(k);
            }
            k += 1;
        }
        k = 0;
        for (int removal : removals) {
            this.grammar.getNonTerminals().remove(this.grammar.getLeft().get(removal - k));
            this.grammar.getRight().remove(removal - k);
            this.grammar.getLeft().remove(removal - k);
            k += 1;
        }
        if (unproductive.size() != 0) {
            System.out.print("\n\033[1mSTEP 3: Removing Unproductive Symbols\033[0m\nUnproductive symbols = { ");
            this.grammar.getNonTerminals().removeAll(unproductive);
            for (String s : unproductive) {
                System.out.print(s + " ");
            }
            System.out.println("}\n");
            this.grammar.print();
        } else {
            System.out.println("\n\033[1mSTEP 3: Removing Unproductive Symbols\033[0m\nNo unproductive symbols.");
        }
    }
}