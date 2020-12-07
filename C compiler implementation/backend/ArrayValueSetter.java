package backend;

import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;
import frontend.Declarator;
import frontend.Symbol;

public class ArrayValueSetter implements IValueSetter{
    private Symbol symbol;
    private int index = 0;
    private Object indexObj = null;

    @Override
    public void setValue(Object object) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);

        try {
            declarator.addElements(index,object);
            if(indexObj == null){
                ProgramGenerator.getInstance().writeArrayElement(symbol,index,object);
            }else{
                ProgramGenerator.getInstance().writeArrayElement(symbol,indexObj,object);
            }

            System.out.println(" Set Value of "+object.toString()+" to Array of name "+symbol.getName()+" with index of "+index);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    @Override
    public Symbol getSymbol() {
        return symbol;
    }

    public Object getIndex(){
        if(indexObj != null){
            return indexObj;
        }
        return index;
    }

    public  ArrayValueSetter(Symbol symbol,Object index){
        this.symbol = symbol;
        if(index instanceof Integer){
            this.index = (int) index;
        }else{
            this.indexObj = index;
        }
    }

}
