package Lab5.PrepareGrammar;

import Lab5.Grammar;

import java.util.*;

public class RemoveLeftFactoring {
    public static void rlf(Grammar grammar) {
        //Define hashmap
        HashMap<String, String> haveLeftFactoring = new HashMap<>();

        while (true) {
            for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
                if (set.getValue().size() < 2)
                    continue;

                //find the biggest prefix and create a new rule
                String prefix = biggestCommonPrefix(set.getValue(), grammar);

                if (!prefix.equals("")) {
                    haveLeftFactoring.put(set.getKey(), prefix);
                }
            }

            if (haveLeftFactoring.size() == 0)
                return;

            for (Map.Entry<String, String> set : haveLeftFactoring.entrySet()) {
                remove(set.getKey(), set.getValue(), grammar);
            }

            haveLeftFactoring.clear();
        }
    }

    //remove element from grammar
    public static void remove(String key, String prefix, Grammar grammar) {
        String nt = grammar.getNextUnused();
        List<String> symbols = new ArrayList<>();

        for (String s : grammar.productionRules.get(key)) {
            if (s.startsWith(prefix)) {
                symbols.add(s);
            }
        }

        HashSet<String> temp = new HashSet<>();
        for (String s : symbols) {
            String suffix = s.substring(prefix.length());
            if (!suffix.equals(""))
                temp.add(suffix);
            else {
                grammar.terminalSet.add("ε");
                temp.add("ε");
            }
        }

        boolean needsToBeAdded = true;
        for (Map.Entry<String, HashSet<String>> set : grammar.productionRules.entrySet()) {
            if (set.getValue().equals(temp)) {
                needsToBeAdded = false;
                grammar.productionRules.get(key).add(prefix + set.getKey());
                for (String s : symbols) {
                    grammar.productionRules.get(key).remove(s);
                }
                break;
            }
        }

        if (needsToBeAdded) {
            grammar.productionRules.get(key).add(prefix + nt);
            grammar.nonTerminalSet.add(nt);
            grammar.productionRules.put(nt, new HashSet<>());

            for (String s : symbols) {
                grammar.productionRules.get(key).remove(s);

                String suffix = s.substring(prefix.length());
                if (!suffix.equals(""))
                    grammar.productionRules.get(nt).add(suffix);
                else {
                    grammar.terminalSet.add("ε");
                    grammar.productionRules.get(nt).add("ε");
                }
            }
        }
    }

    //find biggest prefix
    public static String biggestCommonPrefix(HashSet<String> set, Grammar grammar) {
        String res = "";
        String tempRes;

        for (String s : set) {
            tempRes = findCommonPart(s, set);
            if (tempRes.length() > res.length()) {
                res = tempRes;
            }
        }

        return res;
    }

    //find common prefix
    public static String findCommonPart(String word, HashSet<String> set) {
        String result = word;
        int tempLength = 0, resultLength = 0;

        for (String s : set) {
            if (!s.equals(word)) {
                int len = Math.min(s.length(), result.length());

                for (int i = 0; i < len; i++) {
                    if (s.charAt(i) == result.charAt(i))
                        tempLength++;
                }
                if (tempLength < result.length() && tempLength > 0) {
                    result = s.substring(0, len);
                    resultLength = result.length();
                }
                tempLength = 0;
            }
        }
        if (resultLength == 0)
            return "";
        return result;
    }
}