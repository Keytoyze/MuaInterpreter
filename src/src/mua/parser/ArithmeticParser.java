package src.mua.parser;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

// TODO
public class ArithmeticParser implements IParser {

    @Override
    public Value parse(Context context, Statement current) {
        current.trim();
        CharSequence content = current.getContent();
        if (content.charAt(0) != '(')
            return null;
        int balance = 1, length = 1;
        while (balance != 0) {
            char c = content.charAt(length);
            if (c == '(') balance++;
            else if (c == ')') balance--;
            length++;
        }
        return calculate(current.consume(length).replaceAll("\\s", ""));
    }

    private static int getPriority(char op) {
        switch (op) {
            case '(':
                return 0;
            case '*':
            case '/':
            case '%':
                return 1;
            case '+':
            case '-':
                return 2;
        }
        throw new IllegalArgumentException("Unknown op: " + op);
    }

    private static double operate(char op, double v1, double v2) {
        switch (op) {
            case '*':
                return v1 * v2;
            case '/':
                return v1 / v2;
            case '%':
                return v1 % v2;
            case '+':
                return v1 + v2;
            case '-':
                return v1 - v2;
        }
        throw new IllegalArgumentException("Unknown op: " + op);
    }

    private static Value calculate(String content) {
        return null;
    }
}
