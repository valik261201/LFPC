package Lab4;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Grammar {
    List<String> left;
    List<List<String>> right;
    List<String> terminals;
    List<String> nonTerminals;
    boolean isEpsilon = false;

    public Grammar(String filePath) throws IOException {
        List<String> left = new ArrayList<>();
        List<List<String>> right = new ArrayList<>();
        List<String> terminals = new ArrayList<>();
        List<String> nonTerminals = new ArrayList<>();
        instatiateGrammar(filePath);
    }

    public void instatiateGrammar(String filePath) throws IOException {
        System.out.println("instantiate grammar:\n");
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(filePath));
        String[] newString = new String(bufferedInputStream.readAllBytes()).split("\\r?\\n");
        for (String e : newString) {
            System.out.println(e);
            List<String> line = Arrays.asList(e.split(" "));
            System.out.println(line);
            //addLine(line);
            this.left.add(line.get(0));
            this.right.add(new ArrayList<>());
            this.right.get(this.right.size() - 1).addAll(line.subList(1, line.size()));
        }
        bufferedInputStream.close();
    }

    public void addLine(List<String> line) {
        left.add(line.get(0));
        right.add(new ArrayList<>());
        right.get(this.right.size() - 1).addAll(line.subList(1, line.size()));
    }

    public void addTerminalAndNonTerminal(List<String> line) {
        for (String s : line) {
            for (char c : s.toCharArray()) {
                if (c == 'ε') {
                    isEpsilon = true;
                }
                //if character is upper case then is non-terminal
                if (String.valueOf(c).toUpperCase().equals(String.valueOf(c))) {
                    String test = String.valueOf(c);
                    if (!nonTerminals.contains(test)) {
                        nonTerminals.add(test);
                    }
                } else {
                    //else is terminal
                    String test = String.valueOf(c);
                    if (!terminals.contains(test)) {
                        terminals.add(String.valueOf(c));
                    }
                }
            }
        }
    }

    public List<String> epsilon() {
        //initialize a list to store ε-productions
        List<String> epsilon = new ArrayList<>();
        //traverse the right side of the production
        for (int i = 0; i < right.size(); i++) {
            //if the right side contains ε
            if (right.get(i).contains("ε"))
                //add the non-terminal from the left to the list
                epsilon.add(left.get(i));
        }
        //
        boolean addedNew = true;
        while (addedNew) {
            addedNew = false;
            for (int i = 0; i < right.size(); i++) {
                for (String word :
                        right.get(i)) {
                    boolean allEpsilon = true;
                    for (char c : word.toCharArray()) {
                        if (!epsilon.contains(String.valueOf(c))) {
                            allEpsilon = false;
                            break;
                        }
                    }
                    //if allEpsilon is true and the list with epsilon productions doesn't contain the corresponding
                    //non-terminal from the left
                    if (allEpsilon && !epsilon.contains(left.get(i))) {
                        //set addedNew to true
                        addedNew = true;
                        //and add non-terminal to list
                        epsilon.add(left.get(i));
                        break;
                    }
                }
            }
        }
        return epsilon;
    }

    public List<String> rules(String string) {
        return right.get(left.indexOf(string));
    }

    public void paste() {
        for (int i = 0; i < right.size(); i++) {
            System.out.print(left.get(i) + " -> ");
            for (String word :
                    right.get(i)) {
                System.out.print(word + " ");
            }
            System.out.println();
        }
    }

    public void changeRules(String left, List<String>right){
        this.right.get(this.left.indexOf(left)).clear();
        this.right.get(this.left.indexOf(left)).addAll(right);
    }

    public void addRule(String left, List<String> right){
        this.left.add(left);
        this.right.add(right);
    }

    public String replaceSymbol() {
        for (char c = 'M'; c < 'Z'; c++) {
            if(!nonTerminals.contains(String.valueOf(c))){
                return String.valueOf(c);
            }
        }
        return "A";
    }
}