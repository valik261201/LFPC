package Lab4;

//Remove unproductive symbols

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepThree extends StepTwo {
    public StepThree(String filePath) throws IOException {
        super(filePath);
    }

    public void removeUnproductiveSymbols() {
        List<String> unproductive = new ArrayList<>();
        for (String nonTerminal :
                grammar.nonTerminals) {
            if (grammar.left.contains(nonTerminal)) {
                unproductive.add(nonTerminal);
            }
        }
        int i = 0;
        //verify all right side productions
        for (List<String> words :
                grammar.right) {
            i++;
            List<String> removals = new ArrayList<>();
            //verify right side prod separately
            for (String word :
                    words) {
                //verify each character from every right-side prod
                for (char c :
                        word.toCharArray()) {
                    if (unproductive.contains(String.valueOf(c))) {
                        removals.add(word);
                        break;
                    }
                }
            }
            grammar.right.get(i - 1).removeAll(removals);
        }
        List<Integer> removals = new ArrayList<>();
        i = 0;
        //analyse the non terminal symbols from left side
        for (String left :
                grammar.left) {
            //if grammar rules doesn't contain the non-terminal symbol delete it
            if (grammar.rules(left).size() == 0) {
                removals.add(i);
            }
            i++;
        }
        i = 0;
        for (int removal :
                removals) {
            grammar.nonTerminals.remove(grammar.left.get(removal - i));
            grammar.right.remove(removal - i);
            grammar.left.remove(removal - i);
            i++;
        }
        if (unproductive.size() != 0) {
            System.out.println("Step 3: Removing unproductive smbols\n\nUnproductive symbols = {");
            grammar.nonTerminals.removeAll(unproductive);
            for (String s :
                    unproductive) {
                System.out.print(s + " ");
            }
            System.out.println("}\n");
            grammar.paste();
        } else {
            System.out.println("Step 3: removing unproductive symbols.\n\nThere are no unproductive symbols :)");
        }
    }
}