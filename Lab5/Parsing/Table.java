package Lab5.Parsing;

import Lab5.Grammar;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class Table {
    public static void createParsingTable(Grammar grammar)
    {
        //
        HashMap<String, HashMap<String, String>> table = new HashMap<>();

        for(String n : grammar.nonTerminalSet)
        {
            table.put(n, new HashMap<>());
            for(String t : grammar.terminalSet)
                table.get(n).put(t, "");
            table.get(n).remove("ε");
            table.get(n).put("$", "");
        }

        for(Map.Entry<String, HashSet<String>> set : grammar.first.entrySet()){
            for(String s : set.getValue()){
                if(!s.equals("ε")){
                    String result = "";
                    for(String string : grammar.productionRules.get(set.getKey())){
                        if(!string.equals("ε") && string.contains(s))
                            result += string +" ";
                    }
                    if(result.equals("")){
                        for(String string : grammar.productionRules.get(set.getKey())){
                            if(!string.equals("ε"))
                                result += string +" ";
                        }
                    }
                    table.get(set.getKey()).replace(s, table.get(set.getKey()).get(s).concat(result));
                }
                else {
                    for(String string : grammar.follow.get(set.getKey())) {
                        table.get(set.getKey()).replace(string, table.get(set.getKey()).get(string).concat("ε"));
                    }
                }
            }
        }

        table.forEach((key, value) -> value.forEach((k, v) -> value.replace(k, v.trim())));
        grammar.parsingTable = table;
    }
}
