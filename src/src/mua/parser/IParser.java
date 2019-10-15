package src.mua.parser;

import src.mua.model.Context;
import src.mua.model.Statement;
import src.mua.model.Value;

public interface IParser {
    Value parse(Context context, Statement current);

    // For debug
    String getId();
}
