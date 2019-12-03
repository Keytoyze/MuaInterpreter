package src.mua.parser;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class ListParser implements IParser {

    public final static ListParser INSTANCE = new ListParser();

    private ListParser() {
    }

    @Override
    public Value parse(Context context, Statement current) {
        current.trim();
        if (current.getContent().length() == 0 || current.getContent().charAt(0) != '[') return null;
        int balance = 0;
        int length = 0;
        String line = current.toString();
        while (true) {
            for (char c : line.toCharArray()) {
                length++;
                if (c == '[') {
                    balance++;
                } else if (c == ']') {
                    balance--;
                    if (balance == 0) {
                        return Value.of(current.consume(length), Value.ValueType.LIST);
                    }
                }
            }
            // read the next line.
            line = " " + context.inputLine();
            current.append(line);
        }
    }

}
