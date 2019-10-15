package src.mua.parser;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public class Parser implements IParser {

    public static Parser INSTANCE = new Parser();

    private Parser() {
    }

    @Override
    public Value parse(Context context, Statement current) {
        for (IParser IParser : OperationTable.TABLE) {
            try {
                Value result = IParser.parse(context, current);

                if (result != null) {
                    return result;
                }
            } catch (Exception e) {
                throw new RuntimeException("Exception in " + IParser.getId(), e);
            }
        }
        return null;
    }

    @Override
    public String getId() {
        return "Global parser";
    }
}
