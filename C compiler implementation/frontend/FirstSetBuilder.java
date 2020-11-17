package frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FirstSetBuilder {

    private HashMap<Integer, Symbols> symbolMap = new HashMap<Integer, Symbols>();
    private ArrayList<Symbols> symbolArray = new ArrayList<Symbols>();
    private boolean runFirstSetPass = true;

    int productionCount = 0;

    public FirstSetBuilder(){
        initProductions();
    }

    public boolean isSymbolNullable(int sym){
        Symbols symbol = symbolMap.get(sym);
        if(symbol==null){
            return false;
        }
        return symbol.isNullable?true:false;
    }

    private void initProductions(){
        symbolArray = CGrammarInitializer.getInstance().getSymbolArray();
        symbolMap = CGrammarInitializer.getInstance().getSymbolMap();

    }

    public void runFirstSets(){
        while(runFirstSetPass){
            runFirstSetPass = false;

            Iterator it = symbolArray.iterator();
            while(it.hasNext()){
                Symbols symbol = (Symbols) it.next();
                addSymbolFirstSet(symbol);
            }

        }

        printAllFirstSet();
        System.out.println("===============");
    }

    private void addSymbolFirstSet(Symbols symbol){

        if(isSymbolTerminals(symbol.value)==true){
            return;
        }

        for(int i = 0; i < symbol.productions.size(); i++){
            int[] rightSize = symbol.productions.get(i);
            if(rightSize.length==0){
                continue;
            }

            if(isSymbolTerminals(rightSize[0])&&symbol.firstSet.contains(rightSize[0])==false){
                runFirstSetPass = true;
                symbol.firstSet.add(rightSize[0]);
            }
            else if(isSymbolTerminals(rightSize[0])==false)
            {
                int pos = 0;
                Symbols curSymbols = null;
                do{
                    curSymbols = symbolMap.get(rightSize[pos]);
                    if(symbol.firstSet.containsAll(curSymbols.firstSet)==false){
                        runFirstSetPass = true;
                        for(int j = 0; j < curSymbols.firstSet.size(); j++){
                            if(symbol.firstSet.contains(curSymbols.firstSet.get(j))==false){
                                symbol.firstSet.add(curSymbols.firstSet.get(j));
                            }
                        }

                    }
                    pos++;
                }while(pos<rightSize.length&&curSymbols.isNullable);

            }
        }

    }

    private boolean isSymbolTerminals(int symbol){return CTokenType.isTerminal(symbol);}

    public void printAllFirstSet(){
        Iterator<Symbols> it = symbolArray.iterator();
        while(it.hasNext()){
            Symbols sym = it.next();
            printFirstSet(sym);
        }

    }

    private void printFirstSet(Symbols symbol){

        String s = CTokenType.getSymbolStr(symbol.value);
        s += "{";

        for(int i = 0; i < symbol.firstSet.size(); i++){
            s+= CTokenType.getSymbolStr(symbol.firstSet.get(i))+" ";
        }
        s += "}";
        System.out.println(s);

    }

    public ArrayList<Integer> getFirstSet(int symValue){
        Iterator it = symbolArray.iterator();

        while(it.hasNext()){
            Symbols symbol = (Symbols) it.next();
            if(symbol.value==symValue){
                return symbol.firstSet;
            }
        }

        return null;
    }

}
