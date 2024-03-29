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
    NOT(1),
    READLIST(0),
    REPEAT(2),
    OUTPUT(1),
    STOP(0),
    EXPORT(1),
    IF(3),
    ISNUMBER(1),
    ISWORD(1),
    ISLIST(1),
    ISBOOL(1),
    ISEMPTY(1),
    WORD(2),
    SENTENCE(2),
    LIST(2),
    JOIN(2),
    FIRST(1),
    LAST(1),
    BUTFIRST(1),
    BUTLAST(1),
    RANDOM(1),
    FLOOR(1),
    SQRT(1),
    WAIT(1),
    SAVE(1),
    LOAD(1),
    ERALL(0),
    POALL(0);

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