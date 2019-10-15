package src.mua.parser;

import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class FirstWordParser implements IParser {

    private final BiPredicate<Context, String> predicate;
    private final BiFunction<Context, String, Value> valueMapper;

    public FirstWordParser(BiPredicate<Context, String> predicate) {
        this(predicate, Value.CONTEXT_STRING_MAPPER);
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

    @Override
    public String getId() {
        return "First word parser";
    }
}
