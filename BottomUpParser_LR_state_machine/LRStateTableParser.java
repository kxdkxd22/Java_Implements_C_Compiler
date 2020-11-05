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

        lrStateTable = GrammarStateManager.getGrammarManager().getLRStateTable();
        lexerInput = lexer.look_ahead;
        showCurrentStateInfo(0);
    }

    private void showCurrentStateInfo(int stateNum){
        System.out.println("current input is: "+SymbolDefine.getSymbolStr(lexerInput));

        System.out.println("current state is: ");
        GrammarState state = GrammarStateManager.getGrammarManager().getGrammarState(stateNum);
        state.print();
    }

    public void parser(){
        /*
        * 0:s->e
        * 1:e->e+t
        * 2:e->t
        * 3:t->t*f
        * 4:t->f
        * 5:f->(e)
        * 6:f->NUM
        */

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

                if(SymbolDefine.isSymbolTerminals(lexerInput)){
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

                switch(product.getProductionNum()){
                    case 1:
                    case 3:
                        String firstValue = (String)valueStack.get(valueStack.size()-1);
                        String secondValue = (String)valueStack.get(valueStack.size()-3);
                        if(product.getProductionNum()==1){
                            System.out.println(secondValue+"+="+firstValue);
                        }else{
                            System.out.println(secondValue+"*="+firstValue);
                        }

                        free_name(firstValue);
                        attributeForParentNode = secondValue;
                        break;

                    case 2:
                    case 4:
                        attributeForParentNode = valueStack.peek();
                        break;

                    case 5:
                        attributeForParentNode = valueStack.get(valueStack.size()-2);
                        break;

                    case 6:
                        String name = new_name();
                        System.out.println(name+"="+text);
                        attributeForParentNode = name;
                        break;
                    default:
                        break;
                }
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
