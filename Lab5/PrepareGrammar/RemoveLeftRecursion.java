package Lab5.PrepareGrammar;

import Lab5.Grammar;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class RemoveLeftRecursion {
    public static void rlr(Grammar grammar){
        HashSet<String> haveLeftRecursion = new HashSet<>();

        for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
            for (String s: set.getValue()) {
                if (set.getKey().equals(Character.toString(s.charAt(0)))) {
                    haveLeftRecursion.add(set.getKey());
                    break;
                }
            }
        }

        for(String s : haveLeftRecursion) {
            remove(s, grammar);
        }
    }

    public static void remove(String k, Grammar grammar){
        String nt = grammar.getNextUnused();

        List<String> alfa = new ArrayList<>();
        List<String> beta = new ArrayList<>();

        for(String s : grammar.productionRules.get(k))
        {
            if (k.equals(Character.toString(s.charAt(0))))
            {
                alfa.add(s.substring(1));
            }
            else
            {
                beta.add(s);
            }
        }

        grammar.productionRules.remove(k);
        grammar.productionRules.put(k, new HashSet<>());

        for(String s: beta)
        {
            grammar.productionRules.get(k).add(s);
            grammar.productionRules.get(k).add(s + nt);
        }

        grammar.productionRules.put(nt, new HashSet<>());
        grammar.nonTerminalSet.add(nt);
        for(String s: alfa)
        {
            grammar.productionRules.get(nt).add(s);
            grammar.productionRules.get(nt).add(s + nt);
        }
    }
}