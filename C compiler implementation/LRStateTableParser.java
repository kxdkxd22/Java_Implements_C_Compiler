import java.util.HashMap;
import java.util.Stack;

public class LRStateTableParser {
    private Lexer lexer = null;
    int lexerInput = 0;
    private String text = "";
    private String[] names = new String[]{"t0","t1","t2","t3","t4","t5","t6","t7"};
    private int curName = 0;

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
        lexer.advance();
        lexerInput = CTokenType.EXT_DEF_LIST.ordinal();

        lrStateTable = GrammarStateManager.getGrammarManager().getLRStateTable();

        showCurrentStateInfo(0);
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
                 //   valueStack.push(null);
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

                int rightSize = product.getRight().size();
                while(rightSize>0){
                    parserStack.pop();
                 //   valueStack.pop();
                    statusStack.pop();
                    rightSize--;
                }
                lexerInput = product.getLeft();
                parserStack.push(lexerInput);
               // valueStack.push(attributeForParentNode);

            }

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