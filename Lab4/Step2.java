package Lab4;

// STEP 2 - Eliminate any renaming / unit production (S -> A)

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class Step2 extends Step1 {

    public Step2(File grammarInput) throws FileNotFoundException {
        super(grammarInput);
    }

    protected void removeUnitProductions() {
        for (String left : this.grammar.getLeft()) {
            List<String> newRules = new ArrayList<>();
            for (String rule : this.grammar.rules(left)) {
                // check for any unit production of type A -> B
                if (rule.length() < 2 && rule.toUpperCase().equals(rule)) {
                    List<String> rules = this.grammar.rules(rule);
                    for (String production : rules) {
                        // case when a non-terminal derives into a single character/symbol:
                        if (production.length() == 1 && production.toLowerCase().equals(production)) {
                            newRules.add(production);
                        }
                        /*
                        case when a non-terminal derives into more than one character:
                        S -> A
                        A -> BD | bDAB | bAB  =>  S -> BD | bDAB | bAB
                         */
                        if ((production.length() > 1)) {
                            newRules.add(production);
                        }

                    }
                } else {
                    newRules.add(rule);
                }
            }
            this.grammar.changeRules(left, newRules);
        }
        System.out.println("\n\033[1mSTEP 2: Removing Unit Productions\033[0m");
        this.grammar.print();
    }
}