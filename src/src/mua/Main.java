package src.mua;

import java.util.*;

public class Main {

    public static final Scanner SCANNER = new Scanner(System.in);

    public static void main(String[] args) {

        while (SCANNER.hasNextLine()) {
            String sentence = SCANNER.nextLine();
            try {
                Interpreter.doInterprete(sentence);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

}