package src.mua.parser;

import java.util.List;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.StopException;
import src.mua.model.Value;

public class FunctionParser implements IParser {

    public final static FunctionParser INSTANCE = new FunctionParser();

    private FunctionParser() {
    }

    @Override
    public Value parse(Context context, Statement current) {
        String functionName = current.consumeWord(s -> {
            Value firstWord = Value.of(s);
            return context.contains(firstWord) && context.get(firstWord).isFunction();
        });
        if (functionName == null) {
            return null;
        }
        List<Value> functionList = context.get(Value.of(functionName)).toList();
        List<Value> parameters = functionList.get(0).toList();
        Value function = functionList.get(1);
        Context localContext = new Context();
        for (Value parameter : parameters) {
            Value argument = Parser.INSTANCE.parse(context, current);
            localContext.set(parameter, argument);
        }
        try {
            function.run(localContext);
        } catch (StopException ignore) {
        }
        return localContext.getReturnValule();
    }

    @Override
    public String getId() {
        return "Function Parser";
    }
}
