public class FinitStateMachine {

    private int yystate=0;
    private int yynstate=FMS.STATE_FAILURE;
    private int yyprev=FMS.STATE_FAILURE;
    private int yylastaccept=FMS.STATE_FAILURE;
    private boolean yyanchor=false;
    private byte yylook=Input.EOF;
    private Input input = new Input();
    private FMSTable fms = new FMSTable();
    private boolean endOfReads = false;

    public FinitStateMachine(){
        input.ii_newfile(null);
        input.ii_advance();
        input.ii_pushback(1);
        input.ii_mark_start();
    }

    public void yylex(){

        while(true){

            while(true){
                if((yylook = input.ii_lookahead(1))!=input.EOF){
                    yynstate = fms.yy_next(yystate,yylook);
                    break;
                }else{
                    endOfReads = true;
                    if(yylastaccept!=FMS.STATE_FAILURE){
                        yynstate = FMS.STATE_FAILURE;
                        break;
                    }else{
                        return;
                    }
                }
            }

            if(fms.isAcceptState(yynstate)){
                yyanchor=true;
                yylastaccept=yynstate;
                yyprev=yystate;
            }
            yystate=yynstate;




        }
    }

    public static void main(String[] args) {
        FinitStateMachine fsm = new FinitStateMachine();
        fsm.yylex();
    }

}
