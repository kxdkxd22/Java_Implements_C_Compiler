package frontend;

import backend.CodeTreeBuilder;

import java.util.HashMap;
import java.util.Stack;

public class LRStateTableParser {
    private Lexer lexer = null;
    int lexerInput = 0;
    int nestingLevel = 0;
    int enumVal = 0;
    private String text = "";
    private String[] names = new String[]{"t0","t1","t2","t3","t4","t5","t6","t7"};
    private int curName = 0;
    private TypeSystem typeSystem = TypeSystem.getTypeSystem();
    public static final String GLOBAL_SCOPE = "global";
    public String symbolScope = GLOBAL_SCOPE;

    public String new_name(){
        if(curName >= names.length){
            System.out.println("The expression is too complicated");
            System.exit(1);
        }
        return names[curName++];
    }

    public int getCurrentLevel(){return nestingLevel;}

    public TypeSystem getTypeSystem(){return typeSystem;}

    public void free_name(String name){
        names[--curName] = name;
    }

    private Object attributeForParentNode = null;

    private Stack<Integer> statusStack = new Stack<Integer>();

    private Stack<Integer> parserStack = new Stack<Integer>();
    private Stack<Object> valueStack = new Stack<Object>();
    HashMap<Integer,HashMap<Integer,Integer>> lrStateTable = null;

    CodeTreeBuilder treeBuilder = CodeTreeBuilder.getCodeTreeBuilder();

    public LRStateTableParser(Lexer lexer){
        this.lexer = lexer;
        statusStack.push(0);
        valueStack.push(null);
        lexer.advance();
        lexerInput = CTokenType.EXT_DEF_LIST.ordinal();

        lrStateTable = GrammarStateManager.getGrammarManager().getLRStateTable();

        treeBuilder.setParser(this);
       // showCurrentStateInfo(0);
    }

    private String relOperationText;
    public String getRelOperationText(){return relOperationText;}

    private void showCurrentStateInfo(int stateNum){
        System.out.println("current input is: "+ CTokenType.getSymbolStr(lexerInput));

        System.out.println("current state is: ");
        GrammarState state = GrammarStateManager.getGrammarManager().getGrammarState(stateNum);
        state.print();
    }

    public void parser(){

        while(true){
            Integer action = getAction(statusStack.peek(),lexerInput);

            if(action == null){
                System.err.println("The input is denied");
                return;
            }

            if(action>0){

                statusStack.push(action);
                parserStack.push(lexerInput);

                text = lexer.yytext;
                if(lexerInput==CTokenType.RELOP.ordinal()){
                    relOperationText = text;
                }

                if(CTokenType.isTerminal(lexerInput)){
                    System.out.println("Shift for input: "+ CTokenType.values()[lexerInput].toString());
                    takeActionForShift(lexerInput);
                    lexer.advance();
                    lexerInput=lexer.look_ahead;
                    valueStack.push(null);
                }else{
                    lexerInput = lexer.look_ahead;
                }

            }
            else{
                if(action==0){
                    System.out.println("The input can be accepted");
                    //System.out.println(parserStack.size());
                    return;
                }

                int reduceProduction = -action;
                Production product = ProductionManager.getProductionManager().getProductionByIndex(reduceProduction);
                System.out.println("reduce by product: ");
                product.print();

                takeActionForReduce(reduceProduction);

                int rightSize = product.getRight().size();
                while(rightSize>0){
                    parserStack.pop();
                    valueStack.pop();
                    statusStack.pop();
                    rightSize--;
                }
                lexerInput = product.getLeft();
                parserStack.push(lexerInput);
                valueStack.push(attributeForParentNode);

            }

        }
    }

    private void takeActionForShift(int token){
        if(token == CTokenType.LP.ordinal()||token == CTokenType.LC.ordinal()){
            nestingLevel++;
        }

        if(token == CTokenType.RP.ordinal()||token == CTokenType.RC.ordinal()){
            nestingLevel--;
        }
    }

    private void takeActionForReduce(int productNum){

        switch (productNum){
            case CGrammarInitializer.TYPE_TO_TYPE_SPECIFIER:
                attributeForParentNode = typeSystem.newType(text);
                break;
           /* case CGrammarInitializer.CLASS_TO_TypeOrClass:
                attributeForParentNode = typeSystem.newClass(text);
                break;*/
            case CGrammarInitializer.SPECIFIERS_TypeOrClass_TO_SPECIFIERS:
                attributeForParentNode = valueStack.peek();
                Specifier last = (Specifier) ((TypeLink)valueStack.get(valueStack.size()-2)).getTypeObject();
                Specifier dst = (Specifier)((TypeLink)attributeForParentNode).getTypeObject();
                typeSystem.specifierCpy(dst,last);
                break;
            case CGrammarInitializer.Name_TO_NameNT:
            case CGrammarInitializer.NAME_TO_NewName:
                attributeForParentNode = typeSystem.newSymbol(text,nestingLevel);
                break;

            case CGrammarInitializer.START_VarDecl_TO_VarDecl:
            case CGrammarInitializer.Start_VarDecl_TO_VarDecl:
                typeSystem.addDeclarator((Symbol) attributeForParentNode, Declarator.POINTER);
                break;

            case CGrammarInitializer.ExtDeclList_COMMA_ExtDecl_TO_EXTDecllist:
            case CGrammarInitializer.VarList_COMMA_ParamDeclaration_TO_VarList:
            case CGrammarInitializer.DeclList_Comma_Decl_TO_DeclList:
                Symbol currentSym = (Symbol) attributeForParentNode;
                Symbol lastSym = (Symbol) valueStack.get(valueStack.size()-3);
                currentSym.setNextSymbol(lastSym);
                break;
            case CGrammarInitializer.OptSpecifier_ExtDeclList_Semi_TO_ExtDef:
            case CGrammarInitializer.TypeNT_VarDecl_TO_ParamDeclaration:
            case CGrammarInitializer.Specifiers_DeclList_Semi_TO_Def:
                Symbol symbol = (Symbol) attributeForParentNode;
                TypeLink link = (TypeLink) valueStack.get(valueStack.size()-3);
                typeSystem.addSpecifierToDeclarator(link,symbol);
                typeSystem.addSymbolsToTable(symbol,symbolScope);
                break;

            case CGrammarInitializer.VarDecl_Equal_Intializer_TO_Decl:
                attributeForParentNode = (Symbol) valueStack.get(valueStack.size()-2);
                break;
            case CGrammarInitializer.NewName_LP_VarList_RP_TO_FunctDecl:
                setFunctionSymbol(true);
                Symbol argList = (Symbol) valueStack.get(valueStack.size()-2);
                ((Symbol)attributeForParentNode).args = argList;
                typeSystem.addSymbolsToTable((Symbol) attributeForParentNode,symbolScope);

                symbolScope = ((Symbol)attributeForParentNode).getName();
                Symbol sym = argList;
                while(sym!=null){
                    sym.addScope(symbolScope);
                    sym=sym.getNextSymbol();
                }
                break;
            case CGrammarInitializer.NewName_LP_RP_TO_FunctDecl:
                setFunctionSymbol(false);
                typeSystem.addSymbolsToTable((Symbol) attributeForParentNode,symbolScope);
                symbolScope = ((Symbol)attributeForParentNode).getName();
                break;

            case CGrammarInitializer.Name_TO_Tag:
                attributeForParentNode = typeSystem.getStructObjFromTable(text);
                if(attributeForParentNode == null){
                    attributeForParentNode = new StructDefine(text,nestingLevel,null);
                    typeSystem.addStructToTable((StructDefine) attributeForParentNode);
                }

                break;

            case CGrammarInitializer.Struct_OptTag_LC_DefList_RC_TO_StructSpecifier:
                Symbol defList = (Symbol) valueStack.get(valueStack.size()-2);
                StructDefine structObj = (StructDefine)valueStack.get(valueStack.size()-4);
                structObj.setFields(defList);
                attributeForParentNode = structObj;
                break;
            case CGrammarInitializer.DefList_Def_TO_DefList:
                Symbol currentSym1 = (Symbol) attributeForParentNode;
                Symbol lastSym1 = (Symbol) valueStack.get(valueStack.size()-2);
                currentSym1.setNextSymbol(lastSym1);
                break;
            case CGrammarInitializer.StructSpecifier_TO_TypeSpecifier:
                attributeForParentNode = typeSystem.newType(text);
                TypeLink typeLink = (TypeLink) attributeForParentNode;
                Specifier specifier = (Specifier) typeLink.getTypeObject();
                specifier.setType(Specifier.STRUCTURE);
                StructDefine structDefine = (StructDefine) valueStack.get(valueStack.size()-1);
                specifier.setStructObj(structDefine);
                break;
            case CGrammarInitializer.Enum_TO_EnumNT:
                enumVal = 0;
                break;
            case CGrammarInitializer.EnumSpecifier_TO_TypeSpecifier:
                attributeForParentNode = typeSystem.newType("int");
                break;
            case CGrammarInitializer.Name_Equal_ConstExpr_TO_Enumerator:
                enumVal = (Integer) valueStack.get(valueStack.size()-1);
                attributeForParentNode = (Symbol)valueStack.get(valueStack.size()-3);
                doEnum();
                break;
            case CGrammarInitializer.NameNT_TO_Enumerator:
                doEnum();
                break;
            case CGrammarInitializer.Number_TO_ConstExpr:
            case CGrammarInitializer.Number_TO_Unary:
                attributeForParentNode = Integer.valueOf(text);
                break;

            case CGrammarInitializer.VarDecl_LB_ConstExpr_RB_TO_VarDecl:
                Declarator declarator = typeSystem.addDeclarator((Symbol)valueStack.get(valueStack.size()-4),Declarator.ARRAY);
                int arrayNum = (Integer) attributeForParentNode;
                declarator.setElementNum(arrayNum);
                attributeForParentNode = valueStack.get(valueStack.size()-4);
                break;
            case CGrammarInitializer.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                symbol = (Symbol) valueStack.get(valueStack.size()-2);
                TypeLink link2 = (TypeLink) valueStack.get(valueStack.size()-3);
                typeSystem.addSpecifierToDeclarator(link2,symbol);
                symbolScope = GLOBAL_SCOPE;
                break;
            case CGrammarInitializer.Name_TO_Unary:
                attributeForParentNode = typeSystem.getSymbolByText(text,nestingLevel);
                break;
        }

        treeBuilder.buildCodeTree(productNum,text);

    }

    private void doEnum(){
        Symbol symbol = (Symbol)attributeForParentNode;
        if(convSymToIntConst(symbol,enumVal)){
            typeSystem.addSymbolsToTable(symbol,symbolScope);
            enumVal++;
        }else{
            System.err.println("enum symbol redefinition: "+symbol.name);
        }
    }

    private boolean convSymToIntConst(Symbol symbol, int val){
        if(symbol.getTypeLinkBegin()!=null){
            return false;
        }

        TypeLink typeLink = typeSystem.newType("int");
        Specifier specifier = (Specifier) typeLink.getTypeObject();
        specifier.setType(Specifier.CONSTANT);
        specifier.setConstantValue(val);
        symbol.addSpecifier(typeLink);

        return true;
    }

    private void setFunctionSymbol(boolean hasArgs){
        Symbol funcSymbol = null;
        if(hasArgs){
            funcSymbol = (Symbol) valueStack.get(valueStack.size()-4);
        }else{
            funcSymbol = (Symbol) valueStack.get(valueStack.size()-3);
        }

        typeSystem.addDeclarator(funcSymbol, Declarator.FUNCTION);
        attributeForParentNode = funcSymbol;
    }

    private Integer getAction(Integer currentState,Integer currentInput){
        HashMap<Integer,Integer> jump = lrStateTable.get(currentState);
        if(jump!=null){
            Integer next = jump.get(currentInput);
            if(next!=null){
                return next;
            }
        }

        return null;
    }

}
