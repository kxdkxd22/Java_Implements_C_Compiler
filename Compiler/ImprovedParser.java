public class ImprovedParser {
    private Lexer lexer;

    public  ImprovedParser(Lexer lexer){
        this.lexer=lexer;
    }

    public void statements(){

        while(lexer.match(Lexer.EOI)!=true){
            expression();
            if(lexer.match(Lexer.SEMI)){
                lexer.advance();
            } else{
                System.out.println("Line Number "+lexer.yylineno+":Missing semicolon");
            }
        }

        if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
    }

    public void expression(){
        term();
        while(lexer.match(Lexer.PLUS)){
                lexer.advance();
                term();
        }
        if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
        lexer.yylineno++;
    }

    public void term(){
        factor();
        while(lexer.match(Lexer.TIMES)){
            lexer.advance();
            factor();
        }

        if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
        else{
            return;
        }
    }

    public void factor(){
        if (lexer.match(Lexer.NUM_OR_ID)){
            lexer.advance();
        }
        else if(lexer.match(Lexer.LP)){
            lexer.advance();
            expression();
            if (lexer.match(Lexer.RP)){
                lexer.advance();
                return;
            } else{
                System.out.println("Line Number "+lexer.yylineno+":Missing Left parenthesis");
            }
        }
        else if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
        else{
            System.out.println("illegal statements");
        }
    }
}
