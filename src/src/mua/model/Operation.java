package src.mua.model;

public enum Operation {
    MAKE(2),
    THING(1),
    COLON(":", 1),
    QUOTATION("\"", 1),
    ERASE(1),
    ISNAME(1),
    PRINT(1),
    READ(0),
    ADD(2),
    SUB(2),
    MUL(2),
    DIV(2),
    MOD(2),
    EQ(2),
    GT(2),
    LT(2),
    AND(2),
    OR(2),
    NOT(1);

    public String word;
    public int argNum;

    Operation(int argNum) {
        this.word = name().toLowerCase();
        this.argNum = argNum;
    }

    Operation(String word, int argNum) {
        this.word = word;
        this.argNum = argNum;
    }

}