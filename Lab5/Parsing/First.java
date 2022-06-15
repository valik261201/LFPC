package Lab5.Parsing;

import Lab5.Grammar;

import java.util.HashSet;
import java.util.Map;

public class First {
    public static void initializeFirst(Grammar grammar) {
        for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
            String key = set.getKey();
            grammar.first.put(key, new HashSet<>());
            getFirst(key, set.getValue(), grammar, 0);
        }
    }

    //find first char from rule if character use indirect traverse if empty symbol verify next symbol
    public static void getFirst(String key, HashSet<String> set, Grammar grammar, int index) {
        for (String s : set) {
            if (index < s.length()) {
                String firstSymbol = Character.toString(s.charAt(index));
                if (!grammar.isNonTerminal(firstSymbol)) {
                    grammar.first.get(key).add(firstSymbol);
                } else {
                    if (indirectTraverse(firstSymbol, key, grammar, 0)) {
                        getFirst(key, set, grammar, index + 1);
                    }
                }
            } else grammar.first.get(key).add("ε");
        }
    }

    public static boolean indirectTraverse(String key, String parent, Grammar grammar, int index) {
        boolean res = false;
        for (String s : grammar.productionRules.get(key)) {
            if (index < s.length()) {
                String firstSymbol = Character.toString(s.charAt(index));
                if (firstSymbol.equals("ε")) {
                    res = true;
                } else if (!grammar.isNonTerminal(firstSymbol)) {
                    grammar.first.get(parent).add(firstSymbol);
                } else {
                    if (indirectTraverse(firstSymbol, parent, grammar, 0))
                        indirectTraverse(key, parent, grammar, index + 1);
                }
            }
        }
        return res;
    }
}
