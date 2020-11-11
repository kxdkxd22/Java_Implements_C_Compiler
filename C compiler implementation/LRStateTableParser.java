import java.util.HashMap;
import java.util.Stack;

public class LRStateTableParser {
    private Lexer lexer = null;
    int lexerInput = 0;
    int nestingLevel = 0;
    private String text = "";
    private String[] names = new String[]{"t0","t1","t2","t3","t4","t5","t6","t7"};
    private int curName = 0;
    private TypeSystem typeSystem = new TypeSystem();

    public String new_name(){
        if(curName >= names.length){
            System.out.println("The expression is too complicated");
            System.exit(1);
        }
        return names[curName++];
    }

    public void free_name(String name){
        names[--curName] = name;
    }

    private Object attributeForParentNode = null;

    Stack<Integer> statusStack = new Stack<Integer>();

    Stack<Integer> parserStack = new Stack<Integer>();
    Stack<Object> valueStack = new Stack<Object>();
    HashMap<Integer,HashMap<Integer,Integer>> lrStateTable = null;

    public LRStateTableParser(Lexer lexer){
        this.lexer = lexer;
        statusStack.push(0);
        valueStack.push(null);
        lexer.advance();
        lexerInput = CTokenType.EXT_DEF_LIST.ordinal();

        lrStateTable = GrammarStateManager.getGrammarManager().getLRStateTable();

       // showCurrentStateInfo(0);
    }

    private void showCurrentStateInfo(int stateNum){
        System.out.println("current input is: "+CTokenType.getSymbolStr(lexerInput));

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

                if(CTokenType.isTerminal(lexerInput)){
                    System.out.println("Shift for input: "+CTokenType.values()[lexerInput].toString());
                    lexer.advance();
                    lexerInput=lexer.look_ahead;
                    valueStack.push(null);
                }else{
                    lexerInput = lexer.look_ahead;
                   // lexerInput = parserStack.pop();
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

    private void takeActionForReduce(int productNum){

        switch (productNum){
            case CGrammarInitializer.TYPE_TO_TYPE_SPECIFIER:
                attributeForParentNode = typeSystem.newType(text);
                break;
            case CGrammarInitializer.CLASS_TO_TypeOrClass:
                attributeForParentNode = typeSystem.newClass(text);
                break;
            case CGrammarInitializer.SPECIFIERS_TypeOrClass_TO_SPECIFIERS:
                attributeForParentNode = valueStack.peek();
                Specifier last = (Specifier) ((TypeLink)valueStack.get(valueStack.size()-2)).getTypeObject();
                Specifier dst = (Specifier)((TypeLink)attributeForParentNode).getTypeObject();
                typeSystem.specifierCpy(dst,last);
                break;
            case CGrammarInitializer.NAME_TO_NewName:
                attributeForParentNode = typeSystem.newSymbol(text,nestingLevel);
                break;
            case CGrammarInitializer.START_VarDecl_TO_VarDecl:
                typeSystem.addDeclarator((Symbol) attributeForParentNode,Declarator.POINTER);
                break;
            case CGrammarInitializer.ExtDeclList_COMMA_ExtDecl_TO_EXTDecllist:
                Symbol currentSym = (Symbol) attributeForParentNode;
                Symbol lastSym = (Symbol) valueStack.get(valueStack.size()-3);
                currentSym.setNextSymbol(lastSym);
                break;
            case CGrammarInitializer.OptSpecifier_ExtDeclList_Semi_TO_ExtDef:
                Symbol symbol = (Symbol) attributeForParentNode;
                TypeLink link = (TypeLink) valueStack.get(valueStack.size()-3);
                typeSystem.addSpecifierToDeclarator(link,symbol);
                typeSystem.addSymbolsToTable(symbol);
                break;

        }

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
