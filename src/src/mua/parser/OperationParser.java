package src.mua.parser;

import java.util.function.BiFunction;

import src.mua.model.Context;
import src.mua.model.Operation;
import src.mua.model.Statement;
import src.mua.model.Value;

public class OperationParser implements IParser {
    private final String name;
    private final int argNum;
    private final BiFunction<Context, Value[], Value> operation;

    public OperationParser(Operation op, BiFunction<Context, Value[], Value> operation) {
        this.name = op.word;
        this.argNum = op.argNum;
        this.operation = operation;
    }

    @Override
    public Value parse(Context context, Statement current) {
        Value result = new FirstWordParser(
                (context1, input) -> name.equals(input)
        ).parse(context, current);
        if (result == null) {
            return null;
        }
        Value[] args = new Value[argNum];
        for (int i = 0; i < argNum; i++) {
            Value v = Parser.INSTANCE.parse(context, current);
            if (v == null) {
                throw new IllegalArgumentException("Cannot parse argument " +
                        (i + 1) + " from " + current + " in operation: " + name);
            }
            args[i] = v;
        }
        return operation.apply(context, args);
    }

    @Override
    public String getId() {
        return "Operation Parser (" + name + ")";
    }
}
