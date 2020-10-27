import java.util.Stack;

public class LRParser {

    private Lexer lexer = null;
    int lexerInput = 0;
    LRStateMachine stateMachine = new LRStateMachine();

    Stack<Integer> parserStack = new Stack<Integer>();

    public LRParser(Lexer lexer){
        this.lexer = lexer;
        parserStack.push(0);
        lexer.advance();
        lexerInput = lexer.look_ahead;
    }

    public void parser(){
        LRStateMachine.STATE_MACHINE_ACTION action = stateMachine.getAction(parserStack.peek(),lexerInput);

        while(action != LRStateMachine.STATE_MACHINE_ACTION.accept){
            action = stateMachine.getAction(parserStack.peek(),lexerInput);

            if(action == LRStateMachine.STATE_MACHINE_ACTION.error){
                System.err.println("The Input Error");
                return;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.accept){
                System.out.println("The Input accept");
                return;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s1){
                parserStack.push(1);
                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s2){
                parserStack.push(2);
                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s3){
                parserStack.push(3);
                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.s4){
                parserStack.push(4);
                lexer.advance();
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r1){
                parserStack.pop();
                parserStack.pop();
                parserStack.pop();
                lexerInput=SymbolDefine.EXPR;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r2){
                parserStack.pop();
                lexerInput=SymbolDefine.EXPR;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.r3){
                parserStack.pop();
                lexerInput=SymbolDefine.TERM;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_2){
                parserStack.push(2);
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_3){
                parserStack.push(3);
                lexerInput=lexer.look_ahead;
            }

            if(action == LRStateMachine.STATE_MACHINE_ACTION.state_5){
                parserStack.push(5);
                lexerInput=lexer.look_ahead;
            }

        }

    }
}
