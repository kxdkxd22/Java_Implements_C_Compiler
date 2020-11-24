import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

public class FirstSetBuilder {

    private HashMap<Integer,Symbols> symbolMap = new HashMap<Integer, Symbols>();
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
        ArrayList<int[]> productions = new ArrayList<int[]>();
        productions.add(new int[]{SymbolDefine.EXPR});
        Symbols stmt = new Symbols(SymbolDefine.STMT,false,productions);
        symbolMap.put(SymbolDefine.STMT,stmt);
        symbolArray.add(stmt);

        productions = new ArrayList<int[]>();
        productions.add(new int[]{SymbolDefine.EXPR,SymbolDefine.PLUS,SymbolDefine.TERM});
        productions.add(new int[]{SymbolDefine.TERM});
        Symbols expr = new Symbols(SymbolDefine.EXPR,false,productions);
        symbolMap.put(SymbolDefine.EXPR,expr);
        symbolArray.add(expr);

        productions = new ArrayList<int[]>();
        productions.add(new int[]{SymbolDefine.TERM,SymbolDefine.TIMES,SymbolDefine.FACTOR});
        productions.add(new int[]{SymbolDefine.FACTOR});
        Symbols term = new Symbols(SymbolDefine.TERM,false,productions);
        symbolMap.put(SymbolDefine.TERM,term);
        symbolArray.add(term);

        productions = new ArrayList<int[]>();
        productions.add(new int[]{SymbolDefine.LP,SymbolDefine.EXPR,SymbolDefine.RP});
        productions.add(new int[]{SymbolDefine.NUM_OR_ID});
        Symbols factor = new Symbols(SymbolDefine.FACTOR,false,productions);
        symbolMap.put(SymbolDefine.FACTOR,factor);
        symbolArray.add(factor);

        Symbols eoi = new Symbols(SymbolDefine.EOI,false,null);
        symbolMap.put(SymbolDefine.EOI,eoi);
        symbolArray.add(eoi);

        Symbols lp = new Symbols(SymbolDefine.LP,false,null);
        symbolMap.put(SymbolDefine.LP,lp);
        symbolArray.add(lp);

        Symbols num_or_id = new Symbols(SymbolDefine.NUM_OR_ID,false,null);
        symbolMap.put(SymbolDefine.NUM_OR_ID,num_or_id);
        symbolArray.add(num_or_id);

        Symbols plus = new Symbols(SymbolDefine.PLUS,false,null);
        symbolMap.put(SymbolDefine.PLUS,plus);
        symbolArray.add(plus);

        Symbols rp = new Symbols(SymbolDefine.RP,false,null);
        symbolMap.put(SymbolDefine.RP,rp);
        symbolArray.add(rp);

        Symbols times = new Symbols(SymbolDefine.TIMES,false,null);
        symbolMap.put(SymbolDefine.TIMES,times);
        symbolArray.add(times);

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

    private boolean isSymbolTerminals(int symbol){return symbol<256;}

    public void printAllFirstSet(){
        Iterator<Symbols> it = symbolArray.iterator();
        while(it.hasNext()){
            Symbols sym = it.next();
            printFirstSet(sym);
        }

    }

    private void printFirstSet(Symbols symbol){

        String s = SymbolDefine.getSymbolStr(symbol.value);
        s += "{";

        for(int i = 0; i < symbol.firstSet.size(); i++){
            s+=SymbolDefine.getSymbolStr(symbol.firstSet.get(i))+" ";
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