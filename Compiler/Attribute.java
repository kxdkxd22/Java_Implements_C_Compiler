public class Attribute {
    Object left;
    Object right;

    private String parderGrammar = "";
    private Attribute(){

    }

    public static Attribute getAttribute(Object attrVal){
        Attribute obj = new Attribute();
        obj.left = attrVal;
        obj.right = attrVal;

        return obj;
    }

    public void setGrammar(String s){
        parderGrammar = s;
    }

    public String getGrammar(){
        return parderGrammar;
    }

}
