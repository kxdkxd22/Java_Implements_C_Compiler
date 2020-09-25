import java.util.Scanner;

public class Lexer {
    public static final int EOI=0;
    public static final int SEMI=1;
    public static final int PLUS=2;
    public static final int TIMES=3;
    public static final int LP=4;
    public static final int RP=5;
    public static final int NUM_OR_ID=6;
    public static final int UNKNOW_SYMBOL=7;

    public String current="";
    public String yytext="";
    public int yylineno=0;
    public int yyleng=0;
    public String input_buffer="";
    public int look_ahead = -1;

    public int lex(){
        while(true){

            while(current==""){
                Scanner s = new Scanner(System.in);
                while(true){
                    String line = s.nextLine();
                    if(line.equals("end")){
                        break;
                    }
                    input_buffer+=line;
                }
                s.close();

                if(input_buffer.isEmpty()){
                    current="";
                    return EOI;
                }

                current+=input_buffer;
                yylineno++;
                current.trim();
            }

            for(int i = 0;i<current.length();i++){
                yytext=current.substring(0,1);
                yyleng=0;
                switch(yytext){
                    case ";":current=current.substring(1);return SEMI;
                    case "+":current=current.substring(1);return PLUS;
                    case "(":current=current.substring(1);return LP;
                    case "*":current=current.substring(1);return TIMES;
                    case ")":current=current.substring(1);return RP;
                    case " ":
                    case "\n":
                    case "\t":
                        current=current.substring(1);break;
                    default:
                        while(isAnum(current.charAt(i))){
                            i++;
                            yyleng++;
                        }
                        yytext=current.substring(0,i);
                        current=current.substring(i);
                        return NUM_OR_ID;
                }

            }

        }
    }

    public boolean isAnum(char c){
        if(Character.isAlphabetic(c)||Character.isDigit(c))
            return true;
        return false;
    }

    public void advance(){
        look_ahead=this.lex();
    }

    public boolean match(int i){
        if(look_ahead==-1){
            this.advance();
        }
        return i == look_ahead;
    }

    public void runlexer(){
        while(!match(EOI)){

            System.out.println("Token: " + token() + ",Symbol:"+yytext);
            advance();
        }
    }

    public String token(){
        String token;
        switch (look_ahead){
            case LP:
                token="LP";
                return token;
            case RP:
                token="RP";
                return token;
            case PLUS:
                token="PLUS";
                return token;
            case TIMES:
                token="TIMES";
                return token;
            case NUM_OR_ID:
                token="NUM_OR_ID";
                return token;
            case EOI:
                token="EOI";
                return token;
            case SEMI:
                token="SEMI";
                return token;
            default:
                return "UNKNOW_SYMBOL";
        }
    }

}
