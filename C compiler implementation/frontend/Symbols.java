package frontend;

import java.util.ArrayList;

public class Symbols {

    public int value;
    public ArrayList<int[]> productions;
    public ArrayList<Integer> firstSet = new ArrayList<Integer>();
    public ArrayList<Integer> followSet = new ArrayList<Integer>();
    public ArrayList<ArrayList<Integer>> selectionSet = new ArrayList<ArrayList<Integer>>();
    public boolean isNullable;

    public Symbols(int symVal,boolean nullable,ArrayList<int[]> productions){
        this.productions = productions;
        isNullable = nullable;
        value = symVal;
        if(CTokenType.isTerminal(symVal)){
            firstSet.add(symVal);
        }

    }

    public void addProduction(int[] production){
        if(this.productions.contains(production)==false){
            this.productions.add(production);
        }
    }

}
