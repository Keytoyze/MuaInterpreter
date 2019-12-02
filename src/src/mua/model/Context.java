package src.mua.model;

import java.util.HashMap;
import java.util.Map;

import src.mua.Main;

public class Context {

    private final HashMap<Value, Value> variables = new HashMap<>();
    private Value returnValue = Value.VOID;
    private Value backupReturnValue = Value.VOID;
    private Context parent;

    public Context(Context parent) {
        this.parent = parent;
    }

    public Value get(Value key) {
        Value v = variables.get(key);
        if (v != null) {
            return v;
        }
        if (parent == null) {
            throw new IllegalArgumentException(key + " is not a variable!");
        }
        return parent.get(key);
    }

    public void set(Value key, Value value) {
        variables.put(key, value);
    }

    public boolean contains(Value key) {
        return variables.containsKey(key) || (parent != null && parent.contains(key));
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

    public void setReturnValule(Value value, boolean force) {
        if (force) {
            this.returnValue = value;
        } else {
            this.backupReturnValue = value;
        }
    }

    public Value getReturnValue() {
        return returnValue != Value.VOID ? returnValue : backupReturnValue;
    }

    public void poAll() {
        System.out.println(variables.keySet());
    }

    public void mergeWith(Context another) {
        for (Map.Entry<Value, Value> entry : another.variables.entrySet()) {
            variables.put(entry.getKey(), entry.getValue());
        }
    }
}