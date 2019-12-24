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

    private final String raw;
    private final ValueType type;
    private final List<Value> list = new ArrayList<>(); // valid only when type == LIST

    private Value(String raw, ValueType type) {

        this.type = type;
        this.raw = raw.trim();

        if (type == ValueType.LIST) {
            Statement s = new Statement(raw.substring(1, raw.length() - 1));
            IParser firstWordParser = new FirstWordParser(), listParser = ListParser.INSTANCE;
            while (true) {
                Value value = listParser.parse(null, s);
                if (value == null) {
                    // Parse first word
                    value = firstWordParser.parse(null, s);
                    if (value == null) {
                        // end
                        break;
                    }
                }
                list.add(value);
            }
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
        return Value.of(StringUtils.listToString(list), ValueType.LIST);
    }

    public static Value of(String value, ValueType type) {
        return new Value(value, type);
    }

    @Override
    public String toString() {
        if (type == ValueType.LIST) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; i < list.size(); i++) {
                stringBuilder.append(list.get(i).raw);
                if (i != list.size() - 1) {
                    stringBuilder.append(" ");
                }
            }
            return stringBuilder.toString();
        } else {
            return raw;
        }
    }

    public String toRawString() {
        return raw;
    }

    public Double toNumber() {
        return Double.valueOf(raw);
    }

    public boolean isNumber() {
        return type == ValueType.NUMBER;
    }

    public boolean isWord() {
        return type == ValueType.WORD;
    }

    public List<Value> toList() {
        return new ArrayList<>(list);
    }

    public boolean isList() {
        return type == ValueType.LIST;
    }

    public boolean isFunction() {
        return isList();
    }

    public Boolean toBool() {
        if (isBool()) {
            return Boolean.valueOf(raw);
        }
        throw new IllegalStateException(raw + " is not a bool!");
    }

    public boolean isBool() {
        return type == ValueType.BOOL;
    }

    public boolean isEmpty() {
        if (isList()) {
            return this.list.size() == 0;
        } else {
            return raw.isEmpty();
        }
    }

    public Value run(Context context) {
        return Interpreter.doInterprete(toString(), context);
    }

    public void flatIntoList(List<Value> list) {
        if (isList()) {
            list.addAll(this.list);
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
