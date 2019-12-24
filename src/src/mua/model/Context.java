package src.mua.model;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import src.mua.Interpreter;
import src.mua.Main;

@SuppressWarnings("WeakerAccess")
public class Context implements Serializable {

    private HashMap<Value, Value> variables = new HashMap<>();
    private Value returnValue = Value.VOID;
    private Value backupReturnValue = Value.VOID;
    private transient Context parent;
    private transient Random random = new Random();

    public Context(Context parent) {
        this.parent = parent;
        variables.put(Value.of("pi"), Value.of(3.14159));
        variables.put(Value.of("run"), Value.of("[[a] [repeat 1 :a]]"));
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

    public double random() {
        return random.nextDouble();
    }

    public void save(String name) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(name);

            for (Map.Entry<Value, Value> entry : variables.entrySet()) {
                fos.write(("make \"" + entry.getKey() + " " + entry.getValue() + "\n").getBytes());
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(fos);
        }
    }

    public void load(String name) {
        FileInputStream input = null;
        ByteArrayOutputStream output = null;
        try {
            input = new FileInputStream(name);
            output = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int n;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
            }
            Interpreter.doInterprete(new String(output.toByteArray()));

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            close(input);
            close(output);
        }
    }

    private static void close(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException ignore) {
        }
    }

    public void eraseAll() {
        variables.clear();
    }

    public void poAll() {
        System.out.println(variables.keySet());
    }
}