package Lab5.Parsing;

import Lab5.Grammar;
import java.util.Stack;

public class CheckWord {
    public static boolean finished = false;

    //verifica regula din parse table
    public static void parse(String word, Grammar grammar)
    {
        Stack<Character> stack = new Stack<>();
        stack.push('$');
        stack.push(grammar.startingSymbol.charAt(0));

        StringBuilder w = new StringBuilder(word + "$");

        while (stack.size() > 0) {
            String d = grammar.parsingTable.get(Character.toString(stack.peek())).get(Character.toString(w.charAt(0)));

            printTrace(stack, w, d);

            stack.pop();
            for(int i=d.length()-1; i>=0; i--){
                stack.push(d.charAt(i));
            }

            while(popInput(stack, w))
                skipAction(stack, w);

            if(endOfInput(stack, w)!=0)
                break;

            while(skipAction(stack, w))
                popInput(stack, w);

            if(endOfInput(stack, w)!=0)
                break;
        }
    }

    //Print stack, last used rule
    public static void printTrace(Stack<Character> stack, StringBuilder w, String d){
        System.out.print(stack + "\t\t");
        System.out.print(w + "\t\t");
        System.out.println(d);
    }

    //last letter from input is deleted
    public static boolean popInput(Stack<Character> stack, StringBuilder w){
        boolean action = false;
        while(stack.peek()!= '$' && Character.toString(w.charAt(0)).equals(Character.toString(stack.peek()))){
            printTrace(stack, w, Character.toString(stack.peek()));
            w.deleteCharAt(0);
            stack.pop();
            action = true;
        }
        return action;
    }

    //skip empty symbol
    public static boolean skipAction(Stack<Character> stack, StringBuilder w) {
        boolean action = false;
        while (stack.peek()!= '$' && stack.peek().equals('ε')) {
            printTrace(stack, w, Character.toString(stack.peek()));
            stack.pop();
            action = true;
        }
        return action;
    }

    //if at end dollar sign
    public static int endOfInput(Stack<Character> stack, StringBuilder w){
        if(w.toString().equals("$")) {
            if(Character.toString(stack.pop()).equals("$")) {
                System.out.println("WORD ACCEPTED");
                finished = true;
                w.deleteCharAt(0);
                return 1;
            }
            System.out.println("WORD NOT ACCEPTED");
            finished = true;
            w.deleteCharAt(0);
            return -1;
        }
        return 0;
    }
}
