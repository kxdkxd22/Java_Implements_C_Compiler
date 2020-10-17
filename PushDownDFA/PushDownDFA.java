import java.util.Stack;

public class PushDownDFA {
    private enum StateAction{
        PUSH_1,
        POP,
        ERROR,
        ACCEPT
    }

    private Stack<Integer> pushDownDFA = new Stack<Integer>();
    private StateAction[][] dfaTransFormTable = null;
    private boolean lastAccept = false;
    private static final int STATE_0 = 0;
    private static final int STATE_1 = 1;

    private Input input = null;

    public PushDownDFA(){
        this.input = new Input();
        initTransformTable();
        inputFormConsole();

        pushDownDFA.push(STATE_0);
    }

    private void initTransformTable(){
        dfaTransFormTable = new StateAction[2][4];

        dfaTransFormTable[0][1] = StateAction.PUSH_1;
        dfaTransFormTable[0][2] = StateAction.ERROR;
        dfaTransFormTable[0][3] = StateAction.ACCEPT;
        dfaTransFormTable[1][1] = StateAction.PUSH_1;
        dfaTransFormTable[1][2] = StateAction.POP;
        dfaTransFormTable[1][3] = StateAction.ERROR;
    }

    private void inputFormConsole(){
        input.ii_newfile(null);
        input.ii_advance();
        input.ii_pushback(1);

        while(Character.isSpaceChar(input.ii_lookahead(1))||input.ii_lookahead(1)=='\n'){
            input.ii_advance();
        }
    }

    private boolean recognizeParenthese(){
        char c = (char) input.ii_advance();

        while(true){
            if(c=='\n'){
                c = (char) input.ii_advance();
                continue;
            }

            String receive = "";
            receive+=c;
            if(c==Input.EOF){
                receive = "EOF";
            }

            System.out.println("receive char: "+receive);
            int column = getColumnForInputChar(c);
            int topStackElement = pushDownDFA.peek();
            StateAction action = dfaTransFormTable[topStackElement][column];
            takeAction(action);

            c = (char) input.ii_advance();
            if(c == Input.EOF){
                break;
            }

        }
        return lastAccept;
    }

    private int getColumnForInputChar(char c){

        switch (c){
            case '(':
                return 1;
            case ')':
                return 2;
            case Input.EOF:
                return 3;
            default:
                System.exit(1);
                return -1;
        }
    }

    private void takeAction(StateAction action){

        switch (action){
            case PUSH_1:
                pushDownDFA.push(STATE_1);
                System.out.println("take action by push state 1");
                break;
            case POP:
                pushDownDFA.pop();
                System.out.println("take action by pop state from stack");
                break;
            case ERROR:
                System.out.println("Error! The input string can not be accepted");
                System.exit(1);
                break;
            case ACCEPT:
                System.out.println("DFA go into accept state");
                lastAccept = true;
                break;

        }
    }

    public static void main(String[] args) {
        PushDownDFA pushDownDFA = new PushDownDFA();
        pushDownDFA.recognizeParenthese();
    }

}
