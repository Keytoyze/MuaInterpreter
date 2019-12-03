package src.mua.parser;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class FirstWordParser implements IParser {

    private final BiPredicate<Context, String> predicate;
    private final BiFunction<Context, String, Value> valueMapper;

    public FirstWordParser() {
        this((context, s) -> !s.isEmpty());
    }

    public FirstWordParser(BiPredicate<Context, String> predicate) {
        this(predicate, (context, s) -> Value.of(s));
    }

    public FirstWordParser(
            BiPredicate<Context, String> predicate,
            BiFunction<Context, String, Value> valueMapper
    ) {
        this.predicate = predicate;
        this.valueMapper = valueMapper;
    }

    @Override
    public Value parse(Context context, Statement current) {
        current.trim();
        final String result = current.consumeWord(s -> predicate.test(context, s));
        if (result == null) {
            return null;
        }
        return valueMapper.apply(context, result);
    }
}
