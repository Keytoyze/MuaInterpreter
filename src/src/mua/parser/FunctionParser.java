package src.mua.parser;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class FunctionParser implements IParser {

    @Override
    public Value parse(Context context, Statement current) {
        String functionName = current.consumeWord(s -> {
            Value firstWord = Value.of(s);
            return context.contains(firstWord) && context.get(firstWord).isList();
        });
        return null;
    }

    @Override
    public String getId() {
        return null;
    }
}
