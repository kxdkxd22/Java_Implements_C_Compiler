package backend;

import frontend.Declarator;
import frontend.Symbol;

public class ArrayValueSetter implements IValueSetter{
    private Symbol symbol;
    private int index = 0;


    @Override
    public void setValue(Object object) {
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);

        try {
            declarator.addElements(index,object);
            System.out.println(" Set Value of "+object.toString()+" to Array of name "+symbol.getName()+" with index of "+index);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

    }

    public  ArrayValueSetter(Symbol symbol,int index){
        this.symbol = symbol;
        this.index = index;
    }

}