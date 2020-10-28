import java.util.Stack;

public class LRParser {

    private Lexer lexer = null;
    int lexerInput = 0;
    LRStateMachine stateMachine = new LRStateMachine();
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

    public LRParser(Lexer lexer){
        this.lexer = lexer;
        statusStack.push(0);
        lexer.advance();
        lexerInput = lexer.look_ahead;
    }

    public void parser(){
        LRStateMachine.STATE_MACHINE_ACTION action = stateMachine.getAction(statusStack.peek(),lexerInput);

        while(action != LRStateMachine.STATE_MACHINE_ACTION.accept){
            action = stateMachine.getAction(statusStack.peek(),lexerInput);

            if(action == LRStateMachine.STATE_MACHINE_ACTION.error){
                System.err.println("The Input Error");
                return;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.accept){
                System.out.println("The Input accept");
                return;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s1){
                statusStack.push(1);
                parserStack.push(lexerInput);
                valueStack.push(null);
                text = lexer.yytext;

                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s2){
                statusStack.push(2);
                parserStack.push(lexerInput);
                valueStack.push(null);
                text = lexer.yytext;

                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s3){
                statusStack.push(3);
                parserStack.push(lexerInput);
                valueStack.push(null);
                text = lexer.yytext;

                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s4){
                statusStack.push(4);
                parserStack.push(lexerInput);
                valueStack.push(null);
                text = lexer.yytext;

                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r1){
                String firstValue = (String)valueStack.get(valueStack.size()-1);
                String secondValue = (String)valueStack.get(valueStack.size()-3);
                System.out.println(secondValue+"+="+firstValue);
                free_name(firstValue);

                statusStack.pop();
                statusStack.pop();
                statusStack.pop();

                parserStack.pop();
                valueStack.pop();
                parserStack.pop();
                valueStack.pop();
                parserStack.pop();
                valueStack.pop();

                lexerInput=SymbolDefine.EXPR;
                attributeForParentNode = secondValue;
                parserStack.push(lexerInput);
                valueStack.push(attributeForParentNode);

            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r2){
                statusStack.pop();

                parserStack.pop();
                attributeForParentNode = (String)valueStack.pop();
                lexerInput=SymbolDefine.EXPR;

                parserStack.push(lexerInput);
                valueStack.push(attributeForParentNode);
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r3){
                statusStack.pop();

                String name = new_name();
                System.out.println(name+"="+text);

                parserStack.pop();
                valueStack.pop();

                lexerInput=SymbolDefine.TERM;

                parserStack.push(lexerInput);
                valueStack.push(name);
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_2){
                statusStack.push(2);
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_3){
                statusStack.push(3);
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_5){
                statusStack.push(5);
                lexerInput=lexer.look_ahead;
            }

        }

    }
}
