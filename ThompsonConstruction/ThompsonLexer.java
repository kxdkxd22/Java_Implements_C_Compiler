import com.sun.org.apache.xerces.internal.impl.xpath.regex.RegularExpression;

public class ThompsonLexer {

    public enum Token{
        EOS,//正则表达式末尾
        ANY,// . 通配符
        AT_BOL,//^开头匹配符
        AT_EOL,//$末尾匹配符
        CCL_END,//字符集类结尾括号 ]
        CCL_START,//字符集类开始括号 [
        CLOSE_CURLY,// }
        CLOSE_PAREN,// )
        CLOSEURE,//*
        DASH,//-
        END_OF_INPUT,//输入流结束
        L,//字符常量
        OPEN_CURLY,//{
        OPEN_PAREN,//(
        OPTIONAL,//?
        OR,//|
        PLUS_CLOSE//+
    };

    private final int ASCII_COUNT = 128;
    private Token[] tokenMap = new Token[ASCII_COUNT];
    RegularExpressionHandler regularExpressionHandler = null;
    private int charIndex = 0;
    private int exprCount = 0;
    private int lexeme;
    private Token currentToken = Token.EOS;
    private String curExpr = "";
    private boolean inQuoted;
    private boolean sawEsc;

    public ThompsonLexer(RegularExpressionHandler regularExpressionHandler){
        this.regularExpressionHandler = regularExpressionHandler;
        initTokenMap();
    }

    public void initTokenMap(){
        for(int i= 0; i < ASCII_COUNT; i++){
            tokenMap[i] = Token.L;
        }

        tokenMap['.'] = Token.ANY;
        tokenMap['^'] = Token.AT_BOL;
        tokenMap['$'] = Token.AT_EOL;
        tokenMap[']'] = Token.CCL_END;
        tokenMap['['] = Token.CCL_START;
        tokenMap['}'] = Token.CLOSE_CURLY;
        tokenMap[')'] = Token.CLOSE_PAREN;
        tokenMap['*'] = Token.CLOSEURE;
        tokenMap['-'] = Token.DASH;
        tokenMap['{'] = Token.OPEN_CURLY;
        tokenMap['('] = Token.OPEN_PAREN;
        tokenMap['?'] = Token.OPTIONAL;
        tokenMap['|'] = Token.OR;
        tokenMap['+'] = Token.PLUS_CLOSE;

    }

    public Token advance(){

        if(currentToken == Token.EOS){
            if(exprCount >= regularExpressionHandler.regularExpr.size()){

                return Token.END_OF_INPUT;
            }else{

                curExpr = regularExpressionHandler.regularExpr.get(exprCount);
                exprCount++;
            }

        }

        if(charIndex >= curExpr.length()){
            currentToken = Token.EOS;
            charIndex = 0;
            return currentToken;
        }

        if(curExpr.charAt(charIndex)=='"'){
            inQuoted=!inQuoted;
        }

        sawEsc = (curExpr.charAt(charIndex)=='"');



        return Token.L;
    }

    public int handleEsc(){


        return 0;
    }


}
