
public class BasicParser {
    private Lexer lexer;

    public  BasicParser(Lexer lexer){
        this.lexer=lexer;
    }

    public void statements(){

        expression();
        if(lexer.match(Lexer.SEMI)){
            lexer.advance();
            if(!lexer.match(Lexer.EOI)){
                statements();
            }
        }
        else if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("unknow symbol:"+lexer.yytext);
        }
        else{
            System.out.println("Line Number "+lexer.yylineno+":Missing semicolon");
        }

    }

    public void expression(){
        term();
        expr_prime();
        lexer.yylineno++;
    }

    public void expr_prime(){
        if (lexer.match(Lexer.PLUS)){
            lexer.advance();
            term();
            expr_prime();
        }else if(lexer.match(Lexer.UNKNOW_SYMBOL)){
                System.out.println("unknow symbol:"+lexer.yytext);
        }
        else{
            return;
        }
    }

    public void term(){
        factor();
        expr_term();
    }

    public void expr_term(){
        if (lexer.match(Lexer.TIMES)){
            lexer.advance();
            factor();
            expr_term();
        }
        else if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("unknow symbol:"+lexer.yytext);
        }
        else{
            return;
        }
    }

    public void factor(){
        if (lexer.match(Lexer.NUM_OR_ID)){
            lexer.advance();
        }else if(lexer.match(Lexer.LP)){
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
            System.out.println("unknow symbol:"+lexer.yytext);
        }
        else{
            System.out.println("illegal statements");
        }
    }

}
