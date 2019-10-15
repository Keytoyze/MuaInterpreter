package src.mua.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import src.mua.model.Context;
import src.mua.model.Operation;
import src.mua.model.Value;
import src.mua.parser.FirstWordParser;
import src.mua.parser.OperationParser;
import src.mua.parser.IParser;
import src.mua.parser.RegexParser;

public class ParserTableBuilder {

    private Collection<IParser> set;

    public ParserTableBuilder() {
        set = new ArrayList<>();
    }

    public ParserTableBuilder add(IParser IParser) {
        this.set.add(IParser);
        return this;
    }

    public ParserTableBuilder addOperation(Operation keyword,
                                           BiFunction<Context, Value[], Value> operation) {
        return add(new OperationParser(keyword, operation));
    }

    public ParserTableBuilder addVoidOperation(Operation keyword,
                                           BiConsumer<Context, Value[]> consumer) {
        return addOperation(keyword, (context, values) -> {
            consumer.accept(context, values);
            return Value.VOID;
        });
    }

    public ParserTableBuilder addFirstWord(BiPredicate<Context, String> predicate,
                                           BiFunction<Context, String, Value> valueMapper) {
        return add(new FirstWordParser(predicate, valueMapper));
    }

    public ParserTableBuilder addRegex(String pattern,
                                       BiFunction<Context, String, Value> valueMapper,
                                       boolean firstWord) {
        return add(new RegexParser(pattern, valueMapper, firstWord));
    }

    public Collection<IParser> build() {
        return this.set;
    }
}
