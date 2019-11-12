package src.mua.parser;

import java.util.Collection;

import src.mua.Interpreter;
import src.mua.model.Operation;
import src.mua.model.Value;
import src.mua.util.ParserTableBuilder;
import src.mua.util.StringUtils;

public interface OperationTable {

    Collection<IParser> TABLE = new ParserTableBuilder()
            // Buildin operation
            .addRegex(StringUtils.REGEX_WORD_LITERAL, (context, s) -> Value.of(s.substring(1)), true) // word
            .addRegex(StringUtils.REGEX_NAME_LITERAL, (context, s) -> context.get(Value.of(s.substring(1))), true) // name
            .addRegex(StringUtils.REGEX_DOUBLE_START, (context, s) -> Value.of(s, Value.ValueType.NUMBER), true)
            .addRegex(StringUtils.REGEX_BOOL_START, (context, s) -> Value.of(s, Value.ValueType.BOOL), true)
            .add(ListParser.INSTANCE)
            // Base operation
            .addVoidOperation(Operation.MAKE, (context, args) -> context.set(args[0], args[1]))
            .addOperation(Operation.THING, (context, args) -> context.get(args[0]))
            .addVoidOperation(Operation.ERASE, (context, args) -> context.remove(args[0]))
            .addOperation(Operation.ISNAME, (context, args) -> Value.of(context.contains(args[0])))
            .addVoidOperation(Operation.PRINT, (context, args) -> context.output(args[0]))
            .addOperation(Operation.READ, (context, args) -> context.input())
            // Math operation
            .addOperation(Operation.ADD, (context, args) -> Value.of(args[0].toNumber() + args[1].toNumber()))
            .addOperation(Operation.SUB, (context, args) -> Value.of(args[0].toNumber() - args[1].toNumber()))
            .addOperation(Operation.MUL, (context, args) -> Value.of(args[0].toNumber() * args[1].toNumber()))
            .addOperation(Operation.DIV, (context, args) -> Value.of(args[0].toNumber() / args[1].toNumber()))
            .addOperation(Operation.MOD, (context, args) -> Value.of(args[0].toNumber() % args[1].toNumber()))
            .addOperation(Operation.EQ, (context, args) -> Value.of(args[0].equals(args[1])))
            .addOperation(Operation.GT, (context, args) -> Value.of(args[0].compareTo(args[1]) > 0))
            .addOperation(Operation.LT, (context, args) -> Value.of(args[0].compareTo(args[1]) < 0))
            .addOperation(Operation.AND, (context, args) -> Value.of(args[0].toBool() & args[1].toBool()))
            .addOperation(Operation.OR, (context, args) -> Value.of(args[0].toBool() | args[1].toBool()))
            .addOperation(Operation.NOT, (context, args) -> Value.of(!args[0].toBool()))
            // List operation
            .addOperation(Operation.READLIST, (context, args) -> context.inputLineAsList())
            .addVoidOperation(Operation.REPEAT, (context, values) -> {
                int count = values[0].toNumber().intValue();
                String code = values[1].toUnpackListString();
                for (int i = 0; i < count; i++) Interpreter.doInterprete(code, context);
            })
            // Function operation
            .build();
}
