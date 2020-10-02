

import java.lang.reflect.AccessibleObject;

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

            if(yynstate!=FMS.STATE_FAILURE){

                System.out.println("from state: "+yystate+" to state: "+yynstate);
                input.ii_advance();
                if(fms.isAcceptState(yynstate)){
                    yyanchor=true;
                    yylastaccept=yynstate;
                    yyprev=yystate;
                    input.ii_mark_end();
                }
                yystate=yynstate;

            }else{
                if(yylastaccept==FMS.STATE_FAILURE){
                    if(yylook!='\n'){
                        System.out.println("bad input");
                    }
                    input.ii_advance();
                }else {
                    input.ii_to_mark();
                    System.out.println("Accept State: "+yylastaccept);
                    System.out.println("line: "+input.ii_lineno()+"accept text: "+input.ii_text());
                    switch(yylastaccept){
                        case 1:
                            System.out.println("it is a integer");
                            break;
                        case 2:
                        case 4:
                            System.out.println("it is a float");
                            break;
                        default:
                            System.out.println("interal error");
                    }
                    yylastaccept=FMS.STATE_FAILURE;
                    yystate=0;
                    input.ii_mark_start();

                }


            }


        }
    }

    public static void main(String[] args) {
        FinitStateMachine fsm = new FinitStateMachine();
        fsm.yylex();
    }

}
