package src.mua.model;

import java.lang.IllegalArgumentException;
import java.util.*;

import src.mua.Main;

public class Context {

    private final HashMap<Value, Value> variables = new HashMap<>();

    public Value get(Value key) {
        Value v = variables.get(key);
        if (v == null) {
            throw new IllegalArgumentException(v + " is not a variable!");
        }
        return v;
    }

    public void set(Value key, Value value) {
        variables.put(key, value);
    }

    public boolean contains(Value key) {
        return variables.containsKey(key);
    }

    public void remove(Value key) {
        variables.remove(key);
    }

    public void output(Value value) {
        System.out.println(value.toString());
    }

    public Value input() {
        return Value.of(Main.SCANNER.next());
    }

    public Value inputLineAsList() {
        return Value.of("[" + Main.SCANNER.nextLine() + "]", Value.ValueType.LIST);
    }

    public String inputLine() {
        return Main.SCANNER.nextLine();
    }

}