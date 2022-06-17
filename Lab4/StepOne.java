package Lab4;

// Eliminate ε-productions (D -> ε)

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepOne {
    Grammar grammar;

    public StepOne(String filePath) throws IOException {
        grammar = new Grammar(filePath);
    }

    public void removeEmptyProd() {
        if (!grammar.isEpsilon) {
            System.out.println("ε were not found");
            return;
        }
        List<String> epsilon = grammar.epsilon();
        System.out.println("Step1: Remove ε-productions:\n");
        System.out.print("ε non-terminals = {");

        for (String s : epsilon) {
            System.out.println(s + " ");
        }
        System.out.println("}\n");
        for (int i = 0; i < grammar.left.size(); i++) {
            for (int j = 0; j < grammar.right.get(i).size(); j++) {
                String word = grammar.right.get(i).get(j);
                for (char c : word.toCharArray()) {
                    if (epsilon.contains(String.valueOf(c))) {
                        if (word.length() == 1) {
                            grammar.right.get(i).add("ε");
                        } else grammar.right.get(i).add(word.replaceFirst(String.valueOf(c), ""));
                        break;
                    }
                }
            }
        }
        List<String> remove = new ArrayList<>();
        for (String left : grammar.left) {
            if (left.equals("S"))
                continue;
            grammar.rules(left).remove("ε");
            if (grammar.rules(left).size() == 0)
                remove.add(left);
        }
        for (String left : remove) {
            grammar.nonTerminals.remove(left);
            grammar.right.remove(grammar.left.indexOf(left));
            grammar.left.remove(left);
        }
        grammar.paste();
    }

}
