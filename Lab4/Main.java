package Lab4;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static void main(String[] args) {
        String pathname = "C:\\Users\\Odiseu\\IdeaProjects\\lfpc_labs_java\\src\\main\\java\\Lab4\\input.txt";
        try {
            Step5 step5 = new Step5(new File(pathname));
            step5.printChomskyNormalForm();
        }
        catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
