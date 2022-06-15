package Lab4;

// STEP 4 - Remove Inaccessible Symbols

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Step4 extends Step3 {

    public Step4(File grammarInput) throws FileNotFoundException {
        super(grammarInput);
    }

    protected void removeInaccessibleSymbols() {
        List<String> inaccessibleSymbols = new ArrayList<>();
        for (String nonTerminal : this.grammar.getLeft()) {
            if (nonTerminal.equals("S")) continue;
            boolean found = false;
            for (List<String> words : this.grammar.getRight()) {
                for (String word : words) {
                    if (word.contains(nonTerminal)) {
                        found = true;
                        break;
                    }
                }
                if (found) break;
            }
            if (!found && !inaccessibleSymbols.contains(nonTerminal)) {
                inaccessibleSymbols.add(nonTerminal);
            }
        }
        if (inaccessibleSymbols.size() != 0) {
            System.out.print("\n\033[1mSTEP 4: Removing Inaccessible Symbols\033[0m");
            this.grammar.getNonTerminals().removeAll(inaccessibleSymbols);
            System.out.print("\nInaccessible non-terminals = { ");
            for (String s : inaccessibleSymbols) {
                System.out.print(s + " ");
            }
            System.out.println("}\n");
            int i = 0, removed = 0;
            for (String left : this.grammar.getLeft()) {
                if (inaccessibleSymbols.contains(left)) {
                    this.grammar.getRight().remove(i - removed);
                    removed += 1;
                }
                i += 1;
            }
            this.grammar.getLeft().removeAll(inaccessibleSymbols);
            this.grammar.print();
        } else {
            System.out.println("\n\033[1mSTEP 4: Removing Inaccessible Symbols\033[0m\nNo inaccessible symbols :-)");
        }
    }
}