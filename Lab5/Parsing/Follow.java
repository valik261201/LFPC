package Lab5.Parsing;

import Lab5.Grammar;
import org.apache.commons.lang3.SerializationUtils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Follow {
    public static void createFollow(Grammar grammar) {
        //find last letter
        HashSet<String> temp = new HashSet<>();
        temp.add("$");
        grammar.follow.put(grammar.startingSymbol, temp);

        for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
            if (!set.getKey().equals(grammar.startingSymbol))
                grammar.follow.put(set.getKey(), new HashSet<>());
        }

        //verify if changes were done
        HashMap<String, HashSet<String>> deepCopy;
        do {
            deepCopy = SerializationUtils.clone(new HashMap<>(grammar.follow));

            for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
                completeFollow(set.getKey(), grammar);
            }

        } while (!deepCopy.equals(grammar.follow));

    }

    //fill in table
    public static void completeFollow(String currentSymbol, Grammar grammar) {
        for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
            for (String s : set.getValue()) {
                if (s.contains(currentSymbol)) {
                    verifyRules(s, currentSymbol, currentSymbol, set.getKey(), grammar);
                }
            }
        }
    }

    public static void verifyRules(String s, String currentSymbol, String addTo, String key, Grammar grammar) {
        int nextSymbIndx = s.indexOf(currentSymbol) + 1;

        if (nextSymbIndx < s.length()) {
            String nextSymbol = Character.toString(s.charAt(nextSymbIndx));

            if (!grammar.isNonTerminal(nextSymbol)) {
                grammar.follow.get(addTo).add(nextSymbol);
            } else {
                if (!grammar.first.get(nextSymbol).contains("ε")) {
                    grammar.follow.get(addTo).addAll(grammar.first.get(nextSymbol));
                } else {
                    for (String string : grammar.first.get(nextSymbol)) {
                        if (!string.equals("ε"))
                            grammar.follow.get(addTo).add(string);
                    }
                    verifyRules(s, nextSymbol, currentSymbol, key, grammar);
                }
            }
        } else
            grammar.follow.get(addTo).addAll(grammar.follow.get(key));
    }
}
