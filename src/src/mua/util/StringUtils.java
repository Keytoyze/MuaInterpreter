package src.mua.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import src.mua.model.Operation;
import src.mua.model.Value;

public class StringUtils {

    public static String REGEX_DOUBLE_START = "^-?[0-9]+(\\.[0-9]+)?";
    public static String REGEX_LIST_START = "^\\[[\\s\\S]+\\]";
    public static String REGEX_FUNCTION = "\\[\\s*\\[(.+)\\]\\s*\\[(.+)\\]\\s*\\]";
    public static String REGEX_BOOL_START = "^(true)|(false)+";
    public static String REGEX_WORD_LITERAL = "\\" + Operation.QUOTATION.word + ".+";
    public static String REGEX_NAME_LITERAL = "\\" + Operation.COLON.word + ".+";

    private StringUtils() {
    }

    public static boolean test(String pattern, String text) {
        return Pattern.compile(pattern).matcher(text).matches();
    }

    public static CharSequence find(String pattern, CharSequence text) {
        Matcher matcher = Pattern.compile(pattern).matcher(text);
        if (!matcher.find()) {
            return null;
        }
        return text.subSequence(matcher.start(), matcher.end());
    }

    public static String listToString(List<Value> valueList) {
        StringBuilder sb = new StringBuilder("[");
        for (Value v : valueList) {
            sb.append(v).append(" ");
        }
        return sb.replace(sb.length() - 1, sb.length(), "]").toString();

    }
}
