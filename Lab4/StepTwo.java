package Lab4;

//Removing unit production

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class StepTwo extends StepOne {

    public StepTwo(String filePath) throws IOException {
        super(filePath);
    }

    public void removeUnitProduction() {
        for (String left : grammar.left) {
            List<String> newRules = new ArrayList<>();
            for (String rule :
                    grammar.rules(left)) {
                if (rule.length() < 2 && rule.toUpperCase().equals(rule)) {
                    List<String> rules = grammar.rules(rule);
                    for (String production : rules) {
                        if (production.length() == 1 && production.toLowerCase().equals(production)) {
                            newRules.add(production);
                        }
                        if (production.length() > 1) {
                            newRules.add(production);
                        }
                    }
                } else {
                    newRules.add(rule);
                }
            }
            grammar.changeRules(left, newRules);
        }
        System.out.println("Step 2: Removing unit productions:");
        grammar.paste();
    }
}