package src.mua.parser;

import java.util.LinkedList;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class ArithmeticParser implements IParser {

    private static IParser operatorParser = (context, current) -> {
        current.trim();
        if (isOperator(Value.of(current.toString().substring(0, 1)))) {
            return Value.of(current.consume(1));
        }
        return null;
    };

    @Override
    public Value parse(Context context, Statement current) {
        current.trim();
        CharSequence content = current.getContent();
        if (content.charAt(0) != '(')
            return null;
        current.consume(1);
        current.trim();

        LinkedList<Value> opStack = new LinkedList<>();
        LinkedList<Value> numberStack = new LinkedList<>();

        while (current.getContent().charAt(0) != ')') {
            // parse a number
            Value v = Parser.INSTANCE.parse(context, current);
            if (v == null) {
                numberStack.addLast(Value.of(0.0));
            } else {
                numberStack.addLast(v);
            }
            // parse op if possible
            v = operatorParser.parse(context, current);
            if (v == null) {
                break;
            }
            // calculate
            while (!opStack.isEmpty()) {
                int priority = getPriority(v);
                Value top = opStack.getLast();
                if (getPriority(top) <= priority) {
                    processStack(numberStack, top);
                    opStack.removeLast();
                } else {
                    break;
                }
            }
            opStack.addLast(v);
            current.trim();
//            System.out.println(opStack.toString());
//            System.out.println(numberStack.toString());
//            System.out.println(current);
        }
        current.consume(1); // )
        while (!opStack.isEmpty()) {
            processStack(numberStack, opStack.removeLast());

        }
        if (numberStack.size() != 1) {
            throw new IllegalStateException("Lack of operator! Number stack size is " + numberStack.size());
        }
        return numberStack.pop();
    }

    private static void processStack(LinkedList<Value> numberStack, Value op) {
        Value v2 = numberStack.removeLast();
        Value v1 = numberStack.pollLast();
        if (v1 == null) {
            v1 = Value.of(0.0);
        }
        numberStack.addLast(operate(op, v1, v2));
    }

    private static int getPriority(Value value) {
        char op = value.toString().charAt(0);
        switch (op) {
            case '*':
            case '/':
            case '%':
                return 1;
            case '+':
            case '-':
                return 2;
        }
        return -1;
    }

    private static Value operate(Value op, Value v1, Value v2) {
        double a1 = v1.toNumber(), a2 = v2.toNumber();
        double result;
        switch (op.toString().charAt(0)) {
            case '*':
                result = a1 * a2;
                break;
            case '/':
                result = a1 / a2;
                break;
            case '%':
                result = a1 % a2;
                break;
            case '+':
                result = a1 + a2;
                break;
            case '-':
                result = a1 - a2;
                break;
            default:
                throw new IllegalArgumentException("Unknown op: " + op);
        }
        return Value.of(result);
    }

    private static boolean isOperator(Value v) {
        return v.toString().length() == 1 && getPriority(v) >= 0;
    }

}
