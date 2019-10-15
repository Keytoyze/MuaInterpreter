package src.mua.model;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import src.mua.util.StringUtils;

public class Value implements Comparable<Value> {

    public static final Value VOID = new Value("");
    public static final BiFunction<Context, String, Value> CONTEXT_STRING_MAPPER = (context, s) -> Value.of(s);

    private String raw;

    private Value(String raw) {
        this.raw = raw.trim();
    }

    public static Value of(Boolean value) {
        return new Value(value.toString());
    }

    public static Value of(Double value) {
        return new Value(value.toString());
    }

    public static Value of(String value) {
        return new Value(value);
    }

    @Override
    public String toString() {
        return raw;
    }

    public Double toNumber() {
        return Double.valueOf(raw);
    }

    public boolean isNumber() {
        return StringUtils.test(StringUtils.REGEX_DOUBLE_START, raw);
    }

    public List<Value> toList() {
        if (isList()) {
            return Stream.of(raw.substring(1, raw.length() - 1)
                    .split("\\s+"))
                    .map(Value::of)
                    .collect(Collectors.toList());
        }
        throw new IllegalStateException(raw + " is not a list!");
    }

    public boolean isList() {
        return StringUtils.test(StringUtils.REGEX_LIST_START, raw);
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

    @Override
    public int hashCode() {
        return raw.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Value)) return false;

        Value other = (Value)obj;
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
