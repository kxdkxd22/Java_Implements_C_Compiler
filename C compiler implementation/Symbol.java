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

    public void setNextSymbol(Symbol symbol){
        this.next = symbol;
    }

    public Symbol getNextSymbol() {
        return next;
    }
}
