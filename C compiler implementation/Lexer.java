import java.util.HashMap;
import java.util.Scanner;

public class Lexer {
    public String current="";
    public String yytext="";
    public int yylineno=0;
    public int yyleng=0;
    public String input_buffer="";
    public int look_ahead = CTokenType.UNKNOWN_TOKEN.ordinal();

    private HashMap<String,Integer> keywordMap = new HashMap<String,Integer>();
    public Lexer(){
        initKeywordMap();
    }

    private void initKeywordMap(){
        keywordMap.put("auto",CTokenType.CLASS.ordinal());
        keywordMap.put("static",CTokenType.CLASS.ordinal());
        keywordMap.put("register",CTokenType.CLASS.ordinal());
        keywordMap.put("int",CTokenType.TYPE.ordinal());
        keywordMap.put("float",CTokenType.TYPE.ordinal());
        keywordMap.put("char",CTokenType.TYPE.ordinal());
        keywordMap.put("double",CTokenType.TYPE.ordinal());
        keywordMap.put("long",CTokenType.TYPE.ordinal());
        keywordMap.put("void",CTokenType.TYPE.ordinal());
        keywordMap.put("short",CTokenType.TYPE.ordinal());
        keywordMap.put("struct",CTokenType.STRUCT.ordinal());
        keywordMap.put("enum",CTokenType.ENUM.ordinal());
        keywordMap.put("return",CTokenType.RETURN.ordinal());

    }

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
                    return CTokenType.SEMI.ordinal();
                }

                current=input_buffer;
                ++yylineno;
                current.trim();

            }

            if(current.isEmpty()){
                return CTokenType.SEMI.ordinal();
            }

            boolean inString = false;

            for(int i = 0;i<current.length();i++){
                yytext=current.substring(i,i+1);
                yyleng=0;
                switch(yytext){
                    case ";":current=current.substring(1);return CTokenType.SEMI.ordinal();
                    case "+":current=current.substring(1);return CTokenType.PLUS.ordinal();
                    case "(":current=current.substring(1);return CTokenType.LP.ordinal();
                    case "*":current=current.substring(1);return CTokenType.STAR.ordinal();
                    case ")":current=current.substring(1);return CTokenType.RP.ordinal();
                    case ",":current=current.substring(1);return CTokenType.COMMA.ordinal();
                    case "{":current=current.substring(1);return CTokenType.LC.ordinal();
                    case "}":current=current.substring(1);return CTokenType.RC.ordinal();
                    case "=":current=current.substring(1);return CTokenType.EQUAL.ordinal();
                    case "?":current=current.substring(1);return CTokenType.QUEST.ordinal();
                    case ":":current=current.substring(1);return CTokenType.COLON.ordinal();
                    case "&":current=current.substring(1);return CTokenType.AND.ordinal();
                    case "|":current=current.substring(1);return CTokenType.OR.ordinal();

                    case "/":
                    case "%":
                        current=current.substring(1);return CTokenType.DIVOP.ordinal();
                    case ">":
                    case "<":
                        if(current.charAt(i+1)=='='){
                            current = current.substring(2);
                        }else if((current.charAt(i)=='<'&&current.charAt(i+1)=='<')||(current.charAt(i)=='>'&&current.charAt(i+1)=='>')){
                            current = current.substring(2);
                            return CTokenType.SHIFTOP.ordinal();
                        }else{
                            current = current.substring(1);
                        }
                        return CTokenType.RELOP.ordinal();

                    case " ":
                    case "\t":
                    case "\n":
                        current=current.substring(1);return CTokenType.WHITE_SPACE.ordinal();
                    default:
                        if(isAnum(current.charAt(i))==false){
                            return CTokenType.UNKNOWN_TOKEN.ordinal();
                        }else{
                            while(i<current.length()&&isAnum(current.charAt(i))){
                                i++;
                                yyleng++;
                            }


                            yytext=current.substring(0,i);
                            current=current.substring(i);
                            return id_keyword_or_number();
                        }
                }

            }

        }
    }

    private int id_keyword_or_number(){
        int type = CTokenType.UNKNOWN_TOKEN.ordinal();
        if(Character.isAlphabetic(yytext.charAt(0))){
            type = isKeyWord(yytext);
        }else{
            if(isNum()){
                type = CTokenType.NUMBER.ordinal();
            }
        }
        return type;
    }

    private boolean isNum(){
        int pos = 0;
        if(yytext.charAt(0)=='-'||yytext.charAt(0)=='+'){
           pos++;
        }

        for(;pos<yytext.length();pos++){
            if(Character.isDigit(yytext.charAt(pos))!=true){
                break;
            }
        }
        return pos==yytext.length();
    }

    private int isKeyWord(String str){
        if(keywordMap.containsKey(str)){
            return keywordMap.get(str);
        }

        return CTokenType.NAME.ordinal();
    }

    public boolean isAnum(char c){
        if(Character.isAlphabetic(c)||Character.isDigit(c))
            return true;
        return false;
    }

    public void advance(){
        look_ahead = lex();
        while(look_ahead==CTokenType.WHITE_SPACE.ordinal()){
            look_ahead = lex();
        }
    }

    public boolean match1(int i){
        if(look_ahead==-1){
            this.advance();
        }
        return i == look_ahead;
    }

}
