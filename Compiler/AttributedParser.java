import java.util.Stack;

public class AttributedParser {

    private String name = null;
    private int nameIndex = 0;

    private Lexer lexer = null;
    private String[] names = null;

    private boolean isLegalStatements = true;

    private String getName(){
        return names[nameIndex++];
    }

    private void freeName(String name){
        nameIndex--;
        if(nameIndex >= 0){
            names[nameIndex] = name;
        }
    }

    public AttributedParser(Lexer lexer){
        this.lexer = lexer;
        names = new String[]{"t0","t1","t2","t3","t4","t5","t6"};
    }

    public void Statements(){
        String t = getName();
        expression(t);
        if(lexer.match(Lexer.SEMI)){
            lexer.advance();
        }
        else{
            isLegalStatements = false;
            System.out.println("line: "+lexer.yylineno+" Missing semicolon");
            return;
        }

        if(lexer.match(Lexer.EOI)!=true){
            Statements();
        }

        if(isLegalStatements){
            System.out.println("The statement is legal");
        }

    }

    private void expression(String t){
        term(t);
        expr_prime(t);
    }

    private void term(String t){
        factor(t);
        term_prime(t);
    }

    private void expr_prime(String t){
        if(lexer.match(Lexer.PLUS)){
            lexer.advance();
            String t2 = getName();
            term(t2);
            System.out.println(t+"+="+t2);
            freeName(t2);
            expr_prime(t);
        }
        else if(lexer.match(Lexer.UNKNOW_SYMBOL)){
            isLegalStatements = false;
            System.out.println(" unknow symbol: "+lexer.yytext);
            return;
        }
        else{
            return;
        }
    }

    private void term_prime(String t){
        if(lexer.match(Lexer.TIMES)){
            lexer.advance();
            String t2 = getName();
            factor(t2);
            System.out.println(t+"*="+t2);
            freeName(t2);
            term_prime(t);
        }else{
            return;
        }
    }

    private void factor(String t){
        if(lexer.match(Lexer.NUM_OR_ID)){
            System.out.println(t+"="+lexer.yytext);
            lexer.advance();
        }
        else if(lexer.match(Lexer.RP)){
            lexer.advance();
            expression(t);
            if(lexer.match(Lexer.RP)){
                lexer.advance();
                return;
            }else{
                System.out.println("line: "+lexer.yylineno+" Missing )");
                isLegalStatements = false;
                return;
            }
        }
        else{
            isLegalStatements = false;
            System.out.println("illegal statements");
            return;
        }

    }
}
