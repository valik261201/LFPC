package Lab4;

/*
   Context Free Grammar (CFG) -> Chomsky Normal Form (CNF)
   STEP 1 - Eliminate ε-productions (D -> ε)
   STEP 2 - Eliminate any renaming / unit production (S -> A)
   STEP 3 - Eliminate Unproductive Symbols
   STEP 4 - Eliminate Inaccessible Symbols
   STEP 5 - Obtain the Chomsky Normal Form
*/

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ContextFreeGrammar {
    private final Scanner input;
    private final List<String> left;
    private final List<List<String>> right;
    private final List<String> terminals;
    private final List<String> nonTerminals;
    boolean containsEpsilon = false;

    public ContextFreeGrammar(File input) throws FileNotFoundException {
        this.input = new Scanner(input);
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
        this.terminals = new ArrayList<>();
        this.nonTerminals = new ArrayList<>();
        this.parseInput();
        this.merge();
    }

    protected List<String> epsilonNonTerminals() {
        // initialize a list where all epsilon productions will be stored
        List<String> epsilon = new ArrayList<>();
        // traverse the right side of the production
        for (int i = 0; i < this.right.size(); i++) {
            // if the right side contains ε
            if (this.right.get(i).contains("ε"))
                // we add the corresponding non-terminal from the left side to the list
                epsilon.add(this.left.get(i));
        }
        // initialize new variable for checking the state of epsilon non-terminals
        boolean addedNew = true;
        while (addedNew) {
            addedNew = false;
            for (int i = 0; i < this.right.size(); i++) {
                // traverse the right side of the production
                for (String word : this.right.get(i)) {
                    // set the default value to true for all right productions to be ε-productions
                    boolean allEpsilon = true;
                    // traverse each character from the right side
                    for (char c : word.toCharArray()) {
                        // if this character is not in the list
                        if (!epsilon.contains(String.valueOf(c))) {
                            // set allEpsilon to false
                            allEpsilon = false;
                            break;
                        }
                    }
                    /* if allEpsilon is true and the list with epsilon productions
                    doesn't contain the corresponding non-terminal from left side */
                    if (allEpsilon && !epsilon.contains(this.left.get(i))) {
                        // set addedNew to true
                        addedNew = true;
                        // and add that non-terminal to the list
                        epsilon.add(this.left.get(i));
                        break;
                    }
                }
            }
        }
        return epsilon;
    }

    private void parseInput() {
        while (this.input.hasNext()) {
            List<String> line = Arrays.asList(input.nextLine().split(" "));
            this.left.add(line.get(0));
            this.right.add(new ArrayList<>());
            this.right.get(this.right.size() - 1).addAll(line.subList(1, line.size()));

            // adding terminals and non-terminals
            for (String s : line) {
                for (char c : s.toCharArray()) {
                    if (c == 'ε') {
                        this.containsEpsilon = true;
                    }
                    // if the letter is uppercase
                    else if (String.valueOf(c).toUpperCase().equals(String.valueOf(c))) {
                        String nonTerminal = String.valueOf(c);
                        // and if it's not in the list with non-terminals
                        if (!this.nonTerminals.contains(nonTerminal))
                            // add it to the list
                            this.nonTerminals.add(nonTerminal);
                    } else {
                        String terminal = String.valueOf(c);
                        // else if the letter is lowercase and not in the terminals list
                        if (!this.terminals.contains(terminal)) {
                            // add it to the terminals list
                            this.terminals.add(String.valueOf(c));
                        }
                    }
                }
            }
        }
    }

    private void merge() {
        List<Integer> remove = new ArrayList<>();
        for (int i = 0; i < this.left.size(); i++) {
            for (int j = i + 1; j < this.left.size(); j++) {
                if (this.left.get(i).equals(this.left.get(j))) {
                    this.right.get(i).addAll(this.right.get(j));
                    remove.add(j);
                }
            }
        }
        int k = 0;
        for (int i : remove) {
            this.left.remove(i - k);
            this.right.remove(i - k);
            k += 1;
        }
    }

    protected void print() {
        for (int i = 0; i < this.right.size(); i++) {
            System.out.print(this.left.get(i) + " -> ");
            for (String word : this.right.get(i)) {
                System.out.print(word + " ");
            }
            System.out.println();
        }
    }

    // special for step 5 when making substitutions
    protected String substituteSymbols() {
        for (char c = 'T'; c < 'Z'; c++) {
            if (!this.nonTerminals.contains(String.valueOf(c))) {
                return String.valueOf(c);
            }
        }
        return "A";
    }

    protected void addRule(String left, List<String> right) {
        this.left.add(left);
        this.right.add(right);
    }

    protected void changeRules(String left, List<String> right) {
        this.right.get(this.left.indexOf(left)).clear();
        this.right.get(this.left.indexOf(left)).addAll(right);
    }

    protected List<String> rules(String string) {
        return this.right.get(this.left.indexOf(string));
    }

    protected List<String> getTerminals() {
        return terminals;
    }

    protected List<String> getNonTerminals() {
        return nonTerminals;
    }

    protected List<String> getLeft() {
        return left;
    }

    protected List<List<String>> getRight() {
        return right;
    }
}
