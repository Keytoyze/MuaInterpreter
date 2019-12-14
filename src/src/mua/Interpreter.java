package src.mua;


import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;
import src.mua.parser.Parser;

public class Interpreter {

    public static final Context GLOBAL_CONTEXT = new Context(null);

    public static Value doInterprete(String sentence) {
        return doInterprete(sentence, GLOBAL_CONTEXT);
    }

    public static Value doInterprete(String sentence, Context context) {
        Statement statement = new Statement(sentence);
        while (statement.isEmpty()) {
            statement.append(context.inputLine());
        }
        Value value = null;
        while (!statement.isEmpty()) {
            value = Parser.INSTANCE.parse(context, statement);
        }
        if (value == null) {
            throw new IllegalArgumentException("Cannot interprete the input: " + sentence);
        }
        return value;
    }
}
