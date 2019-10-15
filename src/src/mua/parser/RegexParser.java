package src.mua.parser;

import java.util.function.BiFunction;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.util.StringUtils;
import src.mua.model.Value;

public class RegexParser implements IParser {

    private final String pattern;
    private final BiFunction<Context, String, Value> valueMapper;
    private final boolean firstWord;
    private final IParser parser;

    public RegexParser(String pattern, BiFunction<Context, String, Value> valueMapper, boolean firstWord) {
        this.pattern = pattern;
        this.valueMapper = valueMapper;
        this.firstWord = firstWord;
        this.parser = new FirstWordParser(
                (context, s) -> StringUtils.test(RegexParser.this.pattern, s),
                valueMapper);
    }

    @Override
    public Value parse(Context context, Statement current) {
        if (firstWord) {
            return parser.parse(context, current);
        } else {
            current.trim();
            CharSequence result = StringUtils.find(pattern, current.getContent());
            if (result == null) {
                return null;
            }
            current.consume(result.length());
            return valueMapper.apply(context, result.toString());
        }
    }

    @Override
    public String getId() {
        return "Regex parser (" + pattern + ")";
    }
}
