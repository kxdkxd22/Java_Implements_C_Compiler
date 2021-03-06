package frontend;

import backend.ArrayValueSetter;
import backend.BaseExecutor;
import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;
import backend.IValueSetter;

public class Symbol implements IValueSetter {
    String name;
    String rname;

    int level;
    boolean implicit;
    boolean duplicate;

    Symbol args=null;

    private Symbol next = null;

    TypeLink typeLinkBegin = null;
    TypeLink typeLinkEnd = null;

    private Object value = null;

    private String symbolScope = LRStateTableParser.GLOBAL_SCOPE;
    private Object valueSetter = null;

    private boolean isMember = false;
    private Symbol structParent = null;

    public void addValueSetter(Object valueSetter){this.valueSetter = valueSetter;}

    public Object getValueSetter(){return this.valueSetter;}

    public void addScope(String scope){
        this.symbolScope = scope;
    }

    public String getScope(){return symbolScope;}

    public Symbol getArgList(){return args;}

    public void setArgList(Symbol args){this.args = args;}

    public Symbol getStructSymbol(){return this.structParent;}

    public boolean isStructMember(){return isMember;}

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

    public Symbol copy(){
        Symbol symbol = new Symbol(this.name,this.level);
        symbol.args = this.args;
        symbol.next = this.next;
        symbol.value = this.value;
        symbol.typeLinkBegin = this.typeLinkBegin;
        symbol.typeLinkEnd = this.typeLinkEnd;
        symbol.symbolScope = this.symbolScope;

        return symbol;
    }

    public void setStructParent(Symbol parent){
        this.isMember = true;
        this.structParent = parent;
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
        }else{
            return;
        }

        ProgramGenerator generator = ProgramGenerator.getInstance();
        if(BaseExecutor.resultOnStack == true){
            this.value = obj;
            BaseExecutor.resultOnStack = false;
        }else if(obj instanceof ArrayValueSetter){
            ArrayValueSetter setter = (ArrayValueSetter)obj;
            Symbol symbol = setter.getSymbol();
            Object index = setter.getIndex();
            if(symbol.getSpecifierByType(Specifier.STRUCTURE)==null){
                if(index instanceof Symbol){
                    ProgramGenerator.getInstance().readArrayElement(symbol,index);
                    if(((Symbol)index).getValue()!=null){
                        int  i = (int)((Symbol)index).getValue();
                        try {
                            this.value = symbol.getDeclarator(Declarator.ARRAY).getElement(i);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }else{
                    int  i = (int) index;
                    try {
                        this.value = symbol.getDeclarator(Declarator.ARRAY).getElement(i);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    ProgramGenerator.getInstance().readArrayElement(symbol,i);
                }


            }
        }else if(obj instanceof Symbol){
            Symbol symbol = (Symbol) obj;
            this.value = symbol.value;
            int i = generator.getLocalVariableIndex(symbol);
            generator.emit(Instruction.ILOAD,""+i);
        }else if(obj instanceof Integer){
            Integer val = (Integer)obj;
            generator.emit(Instruction.SIPUSH,""+val);
            this.value = obj;
        }

        if(this.value != null||BaseExecutor.isCompileMode==true){

            if(this.isStructMember()==false){
                int idx = generator.getLocalVariableIndex(this);
                if(generator.isPassingArguments() == false){
                    generator.emit(Instruction.ISTORE,""+idx);
                }
            }else{
                if(this.getStructSymbol().getValueSetter()!=null){
                    generator.assignValueToStructMemberFromArray(this.getStructSymbol().getValueSetter(),this,this.value);
                }else{
                    generator.assignValueToStructMember(this.getStructSymbol(),this,this.value);
                }
            }

        }
    }

    @Override
    public Symbol getSymbol() {
        return this;
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
                if(sp.getLong()==true||sp.getType()==Specifier.INT||getDeclarator(Declarator.POINTER)!=null){
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

    public boolean hasType(int type){
        TypeLink head = typeLinkBegin;
        while(head != null){
            if(head.isDeclarator!=true){
                Specifier sp = (Specifier) head.typeObject;
                if(sp.getType()==type){
                    return true;
                }
            }

            head = head.toNext();
        }

        return false;
    }

    public Specifier getSpecifierByType(int type){
        TypeLink head = typeLinkBegin;
        while(head!=null){
            if(head.isDeclarator!=true){
                Specifier sp = (Specifier) head.typeObject;
                if(sp.getType() == type){
                    return sp;
                }
            }
            head = head.toNext();
        }
        return null;
    }

    public int getLevel(){return level;}
}
