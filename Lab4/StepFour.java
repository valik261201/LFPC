package Lab4;

//Remove inaccessible symbols

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepFour extends StepThree{
    public StepFour(String filePath) throws IOException {
        super(filePath);
    }

    public void removeInaccessibleSymbols(){
        List<String> inaccessibleSymbols = new ArrayList<>();
        for (String nonTerminal: this.grammar.left) {
            if (nonTerminal.equals("S")) continue;
            boolean found = false;
            for (List<String> words: this.grammar.right) {
                for (String word: words) {
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
            System.out.println("STEP 4: Removing Inaccessible Symbols\n");
            this.grammar.nonTerminals.removeAll(inaccessibleSymbols);
            System.out.println("Inaccessible non-terminals = { ");
            for (String s: inaccessibleSymbols) {
                System.out.print(s + " ");
            }
            System.out.println("}\n");
            int i = 0, removed = 0;
            for (String left: this.grammar.left) {
                if (inaccessibleSymbols.contains(left)) {
                    this.grammar.right.remove(i - removed);
                    removed++;
                }
                i++;
            }
            this.grammar.left.removeAll(inaccessibleSymbols);
            this.grammar.paste();
        }
        else {
            System.out.println("STEP 4: Removing Inaccessible Symbols.\nThere were no inaccessible symbols :)");
        }
    }
}
