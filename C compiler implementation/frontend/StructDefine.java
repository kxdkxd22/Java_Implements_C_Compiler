package frontend;

public class StructDefine {
    private String tag;
    private int level;
    private Symbol fields;

    public StructDefine(String tag, int level, Symbol fields){
        this.tag = tag;
        this.level = level;
        this.fields = fields;
    }

    public String getTag() {
        return tag;
    }

    public int getLevel() {
        return level;
    }

    public Symbol getFields() {
        return fields;
    }

    public void setFields(Symbol field){
        this.fields = field;
    }
}
