package src.mua.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

import src.mua.Interpreter;
import src.mua.parser.FirstWordParser;
import src.mua.parser.IParser;
import src.mua.parser.ListParser;
import src.mua.util.StringUtils;

public class Value implements Comparable<Value>, Serializable {

    public enum ValueType {WORD, NUMBER, BOOL, LIST}

    public static final Value VOID = new Value("", ValueType.WORD);
    public static final BiFunction<Context, String, Value> CONTEXT_STRING_MAPPER = (context, s) -> Value.of(s);

    private String raw;
    private ValueType type;

    private Value(String raw, ValueType type) {
        this.raw = raw.trim();
        this.type = type;
        if (isList()) {
            this.raw = StringUtils.listToString(toList());
        }
    }

    public static Value of(Boolean value) {
        return new Value(value.toString(), ValueType.BOOL);
    }

    public static Value of(Double value) {
        return new Value(value.toString(), ValueType.NUMBER);
    }

    public static Value of(String value) {
        return new Value(value, ValueType.WORD);
    }

    public static Value of(List<Value> list) {
        StringBuilder sb = new StringBuilder("[");
        list.forEach(value -> sb.append(value.toString()).append(" "));
        return Value.of(sb.replace(sb.length() - 1, sb.length(), "]").toString(), ValueType.LIST);
    }

    public static Value of(String value, ValueType type) {
        return new Value(value, type);
    }

    @Override
    public String toString() {
        return raw;
    }

    public Double toNumber() {
        return Double.valueOf(raw);
    }

    public String toUnpackListString() {
        if (isList()) {
            return raw.substring(1, raw.length() - 1);
        }
        throw new IllegalStateException(raw + " is not a list!");
    }

    public boolean isNumber() {
        return StringUtils.test(StringUtils.REGEX_DOUBLE_START, raw);
    }

    public boolean isWord() {
        return !isList();
    }

    public List<Value> toList() {
        Statement s = new Statement(toUnpackListString());
        List<Value> result = new ArrayList<>();
        IParser firstWordParser = new FirstWordParser(), listParser = ListParser.INSTANCE;
        while (true) {
            Value value = listParser.parse(null, s);
            if (value == null) {
                // Parse first word
                value = firstWordParser.parse(null, s);
                if (value == null) {
                    // end
                    return result;
                }
            }
            result.add(value);
        }
    }

    public boolean isList() {
        return StringUtils.test(StringUtils.REGEX_LIST_START, raw);
    }

    public boolean isFunction() {
        return StringUtils.test(StringUtils.REGEX_FUNCTION, raw);
    }

    public Boolean toBool() {
        if (isBool()) {
            return Boolean.valueOf(raw);
        }
        throw new IllegalStateException(raw + " is not a bool!");
    }

    public boolean isBool() {
        return StringUtils.test(StringUtils.REGEX_BOOL_START, raw);
    }

    public boolean isEmpty() {
        if (isList()) {
            return toList().size() == 0;
        } else {
            return raw.isEmpty();
        }
    }

    public Value run(Context context) {
        return Interpreter.doInterprete(toUnpackListString(), context);
    }

    public void flatIntoList(List<Value> list) {
        if (isList()) {
            list.addAll(toList());
        } else {
            list.add(this);
        }
    }

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Value)) return false;

        Value other = (Value) obj;
        if (isNumber() && other.isNumber()) {
            return other.toNumber().equals(toNumber());
        }
        return raw.equals(other.raw);
    }

    @Override
    public int compareTo(Value o) {
        if (isNumber() && o.isNumber()) {
            return toNumber().compareTo(o.toNumber());
        }
        return toString().compareTo(o.toString());
    }
}
