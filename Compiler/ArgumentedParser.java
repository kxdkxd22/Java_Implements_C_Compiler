import java.util.Stack;

public class ArgumentedParser {
    private Stack<String> temporaryStack = new Stack<String>();
    private String name =null;
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

    public ArgumentedParser(Lexer lexer){
        this.lexer = lexer;
        names = new String[]{"t0","t1","t2","t3","t4","t5","t6"};
    }

    private void op(String what){
        String left,right;
        right = temporaryStack.pop();
        left = temporaryStack.peek();
        System.out.println(left+what+"="+right);
        freeName(right);
    }

    private void rvalue(String str){
        String name = getName();
        System.out.println(name+"="+str);
        temporaryStack.push(name);
    }

    public void Statements(){

        expression();
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

    private void expression(){
        term();
        expr_prime();
    }

    private void term(){
        factor();
        term_prime();
    }

    private void expr_prime(){
        if(lexer.match(Lexer.PLUS)){
            lexer.advance();
            term();
            op("+");
            expr_prime();
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

    private void term_prime(){
        if(lexer.match(Lexer.TIMES)){
            lexer.advance();
            factor();
            op("*");
            term_prime();
        }else{
            return;
        }
    }

    private void factor(){
        if(lexer.match(Lexer.NUM_OR_ID)){
            rvalue(lexer.yytext);
            lexer.advance();
        }
        else if(lexer.match(Lexer.RP)){
            lexer.advance();
            expression();
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
