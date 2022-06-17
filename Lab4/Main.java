package Lab4;

import java.io.IOException;
//import static Lab4New.Grammar.instatiateGrammar;

public class Main {
    public static void main(String[] args) throws IOException {
        StepFive stepFive = new StepFive("src/main/java/Lab4New/input.txt");
        stepFive.printChomskyNormalForm();
    }
}
