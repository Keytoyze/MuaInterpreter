package src.mua.model;

import java.util.function.Predicate;

public class Statement {

    private final StringBuilder content;

    public Statement(String statement) {
        this.content = new StringBuilder(statement);
        trim();
        // remove annotation
        int annotationIndex = content.indexOf("//");
        if (annotationIndex >= 0) {
            content.delete(annotationIndex, content.length());
        }
    }

    public CharSequence getContent() {
        return content;
    }

    public void append(String s) {
        content.append(s);
    }

    // StringBuilder has no method to do self-trim(). So we make one.
    public void trim() {
        int start = 0, end = content.length() - 1;
        if (isEmpty()) {
            return;
        }
        while (Character.isWhitespace(content.charAt(start))) {
            start++;
        }

        while (Character.isWhitespace(content.charAt(end))) {
            end--;
        }

        content.delete(end + 1, content.length());
        content.delete(0, start);
    }

    public int firstSpaceIndex(int defaultIndex) {
        for (int i = 0; i < content.length(); i++) {
            if (Character.isWhitespace(content.charAt(i))) {
                return i;
            }
        }
        return defaultIndex;
    }

    public String consumeWord(Predicate<String> wordConsumer) {
        int index = firstSpaceIndex(content.length());
        String result = content.substring(0, index);
        if (wordConsumer.test(result)) {
            content.delete(0, result.length());
            return result;
        }
        return null;
    }

    public String consume(int length) {
        String s = content.substring(0, length);
        content.delete(0, length);
        return s;
    }

    public boolean isEmpty() {
        return content.toString().trim().length() == 0;
    }

    @Override
    public String toString() {
        return content.toString();
    }
}
