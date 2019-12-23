package src.mua.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import src.mua.Interpreter;
import src.mua.parser.ArithmeticParser;
import src.mua.parser.FunctionParser;
import src.mua.parser.IParser;
import src.mua.parser.ListParser;
import src.mua.util.ParserTableBuilder;
import src.mua.util.StringUtils;

public interface OperationTable {

    Collection<IParser> TABLE = new ParserTableBuilder()
            // Buildin operation
            .addRegex(StringUtils.REGEX_WORD_LITERAL, (context, s) -> Value.of(s.substring(1)), true) // word
            .addRegex(StringUtils.REGEX_NAME_LITERAL, (context, s) -> context.get(Value.of(s.substring(1))), false) // name
            .addRegex(StringUtils.REGEX_DOUBLE_START, (context, s) -> Value.of(s, Value.ValueType.NUMBER), false)
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
            .addVoidOperation(Operation.REPEAT, (context, args) -> {
                int count = args[0].toNumber().intValue();
                for (int i = 0; i < count; i++) args[1].run(context);
            })
            // Function operation
            .add(FunctionParser.INSTANCE)
            .addVoidOperation(Operation.OUTPUT, (context, args) -> context.setReturnValule(args[0], true))
            .addVoidOperation(Operation.STOP, (context, args) -> {
                throw new StopException();
            })
            .addVoidOperation(Operation.EXPORT, (context, args) -> Interpreter.GLOBAL_CONTEXT.set(args[0], context.get(args[0])))
            // Bool
            .addOperation(Operation.IF, (context, args) -> {
                if (args[0].toBool()) {
                    return args[1].run(context);
                } else {
                    return args[2].run(context);
                }
            })
            .addOperation(Operation.ISNUMBER, (context, args) -> Value.of(args[0].isNumber()))
            .addOperation(Operation.ISWORD, (context, args) -> Value.of(args[0].isWord()))
            .addOperation(Operation.ISLIST, (context, args) -> Value.of(args[0].isList()))
            .addOperation(Operation.ISBOOL, (context, args) -> Value.of(args[0].isBool()))
            .addOperation(Operation.ISEMPTY, (context, args) -> Value.of(args[0].isEmpty()))
            // arithmetic
            .add(new ArithmeticParser())
            .addOperation(Operation.WORD, (context, args) -> Value.of(args[0].toString() + args[1].toString()))
            .addOperation(Operation.SENTENCE, (context, args) -> {
                List<Value> list = new ArrayList<>();
                args[0].flatIntoList(list);
                args[1].flatIntoList(list);
                return Value.of(list);
            })
            .addOperation(Operation.LIST, (context, args) -> Value.of(Arrays.asList(args[0], args[1])))
            .addOperation(Operation.JOIN, (context, args) -> {
                List<Value> list = args[0].toList();
                list.add(args[1]);
                return Value.of(list);
            })
            .addOperation(Operation.FIRST, (context, args) -> {
                if (args[0].isList()) return args[0].toList().get(0);
                else return Value.of(args[0].toString().substring(0, 1));
            })
            .addOperation(Operation.LAST, (context, args) -> {
                if (args[0].isList()) {
                    List<Value> list = args[0].toList();
                    return list.get(list.size() - 1);
                } else {
                    String raw = args[0].toString();
                    return Value.of(raw.substring(raw.length() - 1));
                }
            })
            .addOperation(Operation.BUTFIRST, (context, args) -> {
                if (args[0].isList()) {
                    List<Value> list = args[0].toList();
                    list.remove(0);
                    return Value.of(list);
                } else {
                    String raw = args[0].toString();
                    return Value.of(raw.substring(1));
                }
            })
            .addOperation(Operation.BUTLAST, (context, args) -> {
                if (args[0].isList()) {
                    List<Value> list = args[0].toList();
                    list.remove(list.size() - 1);
                    return Value.of(list);
                } else {
                    String raw = args[0].toString();
                    return Value.of(raw.substring(0, raw.length() - 1));
                }
            })
            // calculation
            .addOperation(Operation.RANDOM, ((context, args) -> Value.of(context.random() * args[0].toNumber())))
            .addOperation(Operation.FLOOR, (context, args) -> Value.of(Math.floor(args[0].toNumber())))
            .addOperation(Operation.SQRT, (context, args) -> Value.of(Math.sqrt(args[0].toNumber())))
            // other operations
            .addVoidOperation(Operation.WAIT, (context, args) -> context.sleep(args[0].toNumber().longValue()))
            .addVoidOperation(Operation.SAVE, (context, args) -> context.save(args[0].toString()))
            .addVoidOperation(Operation.LOAD, (context, args) -> context.load(args[0].toString()))
            .addVoidOperation(Operation.ERALL, (context, args) -> context.eraseAll())
            .addVoidOperation(Operation.POALL, (context, args) -> context.poAll())
            .build();
}
