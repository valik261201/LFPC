package Lab4;

// STEP 1 - Eliminate ε-productions (D -> ε)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class Step1 {


    protected final ContextFreeGrammar grammar;

    public Step1(File grammarInput) throws FileNotFoundException {
        this.grammar = new ContextFreeGrammar(grammarInput);
    }

    protected void removeEpsilonProductions() {
        if (!this.grammar.containsEpsilon) {
            System.out.println("There are no ε-productions");
            return;
        }
        List<String> epsilon = this.grammar.epsilonNonTerminals();
        System.out.println("\n\033[1mSTEP 1: Removing ε-productions\033[0m");
        System.out.print("ε non-terminals = { ");

        for (String s : epsilon) {
            System.out.print(s + " ");
        }
        System.out.println("}\n");
        // traverse the left side of the production
        for (int i = 0; i < this.grammar.getLeft().size(); i++) {
            // traverse the right side of the production
            for (int j = 0; j < this.grammar.getRight().get(i).size(); j++) {
                // set 'word' to be the right-side production
                String word = this.grammar.getRight().get(i).get(j);
                // traverse the 'word'
                for (char c : word.toCharArray()) {
                    // if epsilon contains a character from that word (ε non-terminal)
                    if (epsilon.contains(String.valueOf(c))) {
                        // and its length == 1
                        if (word.length() == 1)
                            // we add that this is an ε-production
                            this.grammar.getRight().get(i).add("ε");
                        else
                            /* otherwise, if the length of the word > 1, we replace that character
                            (ε non-terminal) with empty string and get all possible combinations */
                            this.grammar.getRight().get(i).add(word.replaceFirst(String.valueOf(c), ""));
                        break;
                    }
                }
            }
        }
        List<String> removals = new ArrayList<>();
        for (String left : this.grammar.getLeft()) {
            if (left.equals("S"))
                continue;
            this.grammar.rules(left).remove("ε");
            if (this.grammar.rules(left).size() == 0)
                removals.add(left);
        }
        for (String left : removals) {
            // remove that non-terminal because it results in an ε-production
            this.grammar.getNonTerminals().remove(left);
            // remove that non-terminal from the right side of the production
            this.grammar.getRight().remove(this.grammar.getLeft().indexOf(left));
            // remove it also from the left side
            this.grammar.getLeft().remove(left);
        }
        this.grammar.print();
    }
}
