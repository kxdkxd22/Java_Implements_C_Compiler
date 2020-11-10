import java.util.ArrayList;
import java.util.HashMap;

public class CGrammarInitializer {
    private static CGrammarInitializer instance = null;
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<Integer, ArrayList<Production>>();
    private HashMap<Integer,Symbols> symbolMap = new HashMap<Integer, Symbols>();
    private ArrayList<Symbols> symbolArray = new ArrayList<Symbols>();

    public static CGrammarInitializer getInstance(){
        if(instance==null){
            instance = new CGrammarInitializer();
        }
        return instance;
    }

    private CGrammarInitializer(){

    }

    public HashMap<Integer, ArrayList<Production>> getProductionMap(){return productionMap;}

    public HashMap<Integer,Symbols> getSymbolMap(){return symbolMap;}

    public ArrayList<Symbols> getSymbolArray(){return symbolArray;}

    public void initVariableDecalationProduction(){

        productionMap.clear();

        /*LB:{RB:}
        *
        * C variable declaration grammar
        *  PROGRAM -> EXT_DEF_LIST
        *
        * EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
        *
        * EXT_DEF->OPT_SPECIFIERS EXT_DECL_LIST SEMI
        *          | OPT_SPECIFIERS SEMI
        *
        * EXT_DECL_LIST->EXT_DECL
        *               | EXT_DECL_LIST COMMA EXT_DECL
        *
        * EXT_DECL -> VAR_DECL
        *
        * OPT_SPECIFIERS->CLASS TTYPE
        *                  | TTYPE
        *                  | SPECIFIERS
        *                  | EMPTY?
        *
        * SPECIFIERS -> TYPE_OR_CLASS
        *               | SPECIFIERS TYPE_OR_CLASS
        *
        * TYPE_OR_CLASS-> TYPE_SPECIFIER
        *               | CLASS
        *
        * TYPE_SPECIFIER -> TYPE
        *
        * NEW_NAME ->NAME
        *
        * NAME_NT -> NAME
        *
        * VAR_DECL ->| NEW_NAME
        *            | START VAR_DECL
        *
         */

        //PROGRAM -> EXT_DEF_LIST
        int productionNum = 0;
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.EXT_DEF_LIST.ordinal()});
        Production production = new Production(productionNum,CTokenType.PROGRAM.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
        right = getProductionRight(new int[]{CTokenType.EXT_DEF_LIST.ordinal(),CTokenType.EXT_DEF.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DEF_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //EXT_DEF->OPT_SPECIFIERS EXT_DECL_LIST SEMI
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(),CTokenType.EXT_DECL_LIST.ordinal(),CTokenType.SEMI.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DEF->OPT_SPECIFIERS SEMI
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(),CTokenType.SEMI.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL_LIST->EXT_DECL
        right = getProductionRight(new int[]{CTokenType.EXT_DECL.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL_LIST->EXT_DECL_LIST COMMA EXT_DECL
        right = getProductionRight(new int[]{CTokenType.EXT_DECL_LIST.ordinal(),CTokenType.COMMA.ordinal(),CTokenType.EXT_DECL.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL -> VAR_DECL
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum,CTokenType.EXT_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_SPECIFIERS->CLASS TTYPE
        /*right = getProductionRight(new int[]{CTokenType.CLASS.ordinal(),CTokenType.TTYPE.ordinal()});
        production = new Production(productionNum,CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //OPT_SPECIFIERS->TTYPE
        right = getProductionRight(new int[]{CTokenType.TTYPE.ordinal()});
        production = new Production(productionNum,CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);
        */
        //OPT_SPECIFIERS->SPECIFIERS
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal()});
        production = new Production(productionNum,CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //SPECIFIERS -> TYPE_OR_CLASS
        right = getProductionRight(new int[]{CTokenType.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum,CTokenType.SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //SPECIFIERS -> SPECIFIERS TYPE_OR_CLASS
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal(),CTokenType.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum,CTokenType.SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_OR_CLASS-> TYPE_SPECIFIER
        right = getProductionRight(new int[]{CTokenType.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum,CTokenType.TYPE_OR_CLASS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_OR_CLASS-> CLASS
        /*right = getProductionRight(new int[]{CTokenType.CLASS.ordinal()});
        production = new Production(productionNum,CTokenType.TYPE_OR_CLASS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);*/

        //TYPE_SPECIFIER -> TYPE
        right = getProductionRight(new int[]{CTokenType.TYPE.ordinal()});
        production = new Production(productionNum,CTokenType.TYPE_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NEW_NAME -> NAME
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum,CTokenType.NEW_NAME.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NAME_NT -> NAME
        /*right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum,CTokenType.NAME_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);*/

        //VAR_DECL ->| NEW_NAME
        right = getProductionRight(new int[]{CTokenType.NEW_NAME.ordinal()});
        production = new Production(productionNum,CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL ->| START VAR_DECL
        right = getProductionRight(new int[]{CTokenType.STAR.ordinal(),CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum,CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        addTerminalToSymbolMapArray();

    }

    private void addProduction(Production production,boolean nullable){
        ArrayList<Production> productionList = productionMap.get(production.getLeft());
        if(productionList==null){
            productionList = new ArrayList<Production>();
            productionMap.put(production.getLeft(),productionList);
        }

        if(productionList.contains(production)==false){
            productionList.add(production);
        }

        addSymbolMapAndArray(production,nullable);
    }

    private void addSymbolMapAndArray(Production production,boolean nullable){
        int[] right = new int[production.getRight().size()];
        for(int i = 0; i < production.getRight().size(); i++){
            right[i] = production.getRight().get(i);
        }

        if(symbolMap.containsKey(production.getLeft())){
            symbolMap.get(production.getLeft()).addProduction(right);
        }else{
            ArrayList<int[]> productions = new ArrayList<int[]>();
            productions.add(right);
            Symbols symbol = new Symbols(production.getLeft(),nullable,productions);

            symbolMap.put(production.getLeft(),symbol);
            symbolArray.add(symbol);
        }

    }

    private void addTerminalToSymbolMapArray(){
        for(int i = CTokenType.FIRST_TERMINAL_INDEX; i < CTokenType.LAST_TERMINAL_INDEX; i++){
            Symbols symbol = new Symbols(i,false,null);
            symbolMap.put(i,symbol);
            symbolArray.add(symbol);
        }
    }

    private ArrayList<Integer> getProductionRight(int[] arr){
        ArrayList<Integer> right = new ArrayList<Integer>();
        for(int i = 0; i < arr.length; i++){
            right.add(arr[i]);
        }
        return right;
    }

}