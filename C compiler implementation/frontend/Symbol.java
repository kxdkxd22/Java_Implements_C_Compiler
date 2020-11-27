package frontend;

import backend.IValueSetter;

public class Symbol implements IValueSetter {
    String name;
    String rname;

    int level;
    boolean implicit;
    boolean duplicate;

    Symbol args;

    private Symbol next = null;

    TypeLink typeLinkBegin = null;
    TypeLink typeLinkEnd = null;

    private Object value = null;

    private String symbolScope = LRStateTableParser.GLOBAL_SCOPE;

    public void addScope(String scope){
        this.symbolScope = scope;
    }

    public String getScope(){return symbolScope;}

    public Symbol getArgList(){return args;}

    public boolean equals(Symbol symbol){
        if(this.getLevel()==symbol.getLevel()&&this.symbolScope.equals(symbol.symbolScope)&&this.name.equals(symbol.name)){
            return true;
        }

        return false;
    }

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

    public void setValue(Object obj){
        if(obj!=null){
            System.out.println("Assign value of "+obj.toString()+" to variable " +name);
        }

        this.value = obj;
    }

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

    public Declarator getDeclarator(int type){
        TypeLink begin = typeLinkBegin;
        while(begin!=null&&begin.getTypeObject()!=null){
            if(begin.isDeclarator==true){
                Declarator declarator = (Declarator) begin.getTypeObject();
                if(declarator.getDeclareType()==type){
                    return declarator;
                }
            }
            begin = begin.toNext();
        }

        return null;
    }

    public int getByteSize(){
        int size = 0;
        TypeLink head = typeLinkBegin;
        while(head!=null){
            if(head.isDeclarator!=true){
                Specifier sp = (Specifier) head.typeObject;
                if(sp.getLong()==true||sp.getType()==Specifier.INT){
                    size = 4;
                    break;
                }else{
                    size = 1;
                    break;
                }
            }
            head = head.toNext();
        }
        return size;
    }

    public int getLevel(){return level;}
}
