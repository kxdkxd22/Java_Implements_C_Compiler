import java.util.ArrayList;

public class Symbols {

    public int value;
    public ArrayList<int[]> productions;
    public ArrayList<Integer> firstSet = new ArrayList<Integer>();
    public ArrayList<Integer> followSet = new ArrayList<Integer>();
    public boolean isNullable;

    public Symbols(int symVal,boolean nullable,ArrayList<int[]> productions){
        this.productions = productions;
        isNullable = nullable;
        value = symVal;
        if(value < 256){
            firstSet.add(value);
        }

    }



}
