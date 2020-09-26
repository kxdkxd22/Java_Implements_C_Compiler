public class Parser {
    private Lexer lexer;

    String[] names = {"t0","t1","t2","t3","t4","t5","t6"};
    public int nameP=0;

    public String newName(){
        if(nameP>=names.length){
            System.out.println("no reg distribution");
            System.exit(1);
        }
        String reg = names[nameP];
        ++nameP;
        return reg;
    }

    public void freeName(String tempReg){
        if(nameP<0){
            System.out.println("all reg free");
            System.exit(1);
        }
        names[nameP]=tempReg;
        nameP--;
    }

    public Parser(Lexer lexer){
        this.lexer=lexer;
    }

    public void statements(){
        String tempReg = newName();

        while(lexer.match(Lexer.EOI)!=true){
            expression(tempReg);
            freeName(tempReg);
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

    public void expression(String tempReg){
        String tempReg1;
        term(tempReg);
        while(lexer.match(Lexer.PLUS)){
            lexer.advance();
            tempReg1=newName();
            term(tempReg1);
            System.out.println(tempReg+"+="+tempReg1);
            freeName(tempReg1);
        }
        if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
        lexer.yylineno++;
    }

    public void term(String tempReg){
        String tempReg2;
        factor(tempReg);
        while(lexer.match(Lexer.TIMES)){
            lexer.advance();
            tempReg2=newName();
            factor(tempReg2);
            System.out.println(tempReg+"*="+tempReg2);
            freeName(tempReg2);
        }

        if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            System.out.println("Unknow Symbol:"+lexer.yytext);
        }
        else{
            return;
        }
    }

    public void factor(String tempReg){
        if (lexer.match(Lexer.NUM_OR_ID)){
            System.out.println(tempReg+"="+lexer.yytext);
            lexer.advance();
        }
        else if(lexer.match(Lexer.LP)){
            lexer.advance();
            expression(tempReg);
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
