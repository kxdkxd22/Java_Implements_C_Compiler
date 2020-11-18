package frontend;

public class Symbol {
    String name;
    String rname;

    int level;
    boolean implicit;
    boolean duplicate;

    Symbol args;

    private Symbol next;

    TypeLink typeLinkBegin;
    TypeLink typeLinkEnd;

    private Object value = null;

    public Symbol(String name,int level){
        this.name = name;
        this.level = level;
    }

    public void addDeclarator(TypeLink link){
        if(typeLinkBegin==null){
            typeLinkBegin = link;
            typeLinkEnd = link;
        }else{
            link.setNextLink(typeLinkBegin);
            typeLinkBegin = link;
        }
    }

    public void addSpecifier(TypeLink link){
        if(typeLinkBegin==null){
            typeLinkBegin = link;
            typeLinkEnd = link;
        }else{
            typeLinkEnd.setNextLink(link);
            typeLinkEnd= link;
        }
    }

    public String getName(){return name;}

    public void setValue(Object obj){this.value = obj;}

    public Object getValue(){return value;}

    public void setNextSymbol(Symbol symbol){
        this.next = symbol;
    }

    public Symbol getNextSymbol() {
        return next;
    }

    public TypeLink getTypeLinkBegin(){
        return typeLinkBegin;
    }

    public int getLevel(){return level;}
}
