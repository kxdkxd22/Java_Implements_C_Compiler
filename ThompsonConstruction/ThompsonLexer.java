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
        CLOSURE,//*
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
    private boolean inQuoted = false;
    private boolean sawEsc = false;

    public ThompsonLexer(RegularExpressionHandler regularExpressionHandler){
        this.regularExpressionHandler = regularExpressionHandler;
        initTokenMap();
    }

    public ThompsonLexer.Token getCurrentToken(){return currentToken;}

    public int getLexeme(){return lexeme;}

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
        tokenMap['*'] = Token.CLOSURE;
        tokenMap['-'] = Token.DASH;
        tokenMap['{'] = Token.OPEN_CURLY;
        tokenMap['('] = Token.OPEN_PAREN;
        tokenMap['?'] = Token.OPTIONAL;
        tokenMap['|'] = Token.OR;
        tokenMap['+'] = Token.PLUS_CLOSE;

    }

    public boolean MatchToken(Token t){return currentToken == t;}

    public Token advance(){

        if(currentToken == Token.EOS){
            if(exprCount >= regularExpressionHandler.regularExpr.size()){
                currentToken = Token.END_OF_INPUT;
                return currentToken;
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
            charIndex++;
        }

        sawEsc = (curExpr.charAt(charIndex)=='\\');
        if(sawEsc&&curExpr.charAt(charIndex+1)!='"'&&inQuoted==false){
            lexeme = handleEsc();
        }else{
            if(sawEsc&&curExpr.charAt(charIndex+1)=='"'){
                lexeme = '"';
                charIndex+=2;
            }else{
                lexeme = curExpr.charAt(charIndex);
                charIndex++;
            }
        }

        currentToken = (inQuoted || sawEsc)?Token.L:tokenMap[lexeme];
        //currentToken = tokenMap[lexeme];
        return currentToken;

    }

    private int handleEsc(){

        int rval = 0;
        String exprToUpper = curExpr.toUpperCase();
        charIndex++;

        switch (exprToUpper.charAt(charIndex)){
            case '\0':
                rval = '\\';
                break;
            case 'B':
                rval = '\b';
                break;
            case 'F':
                rval = '\f';
                break;
            case 'N':
                rval = '\n';
                break;
            case 'R':
                rval = '\r';
                break;
            case 'S':
                rval = ' ';
                break;
            case 'T':
                rval = '\t';
                break;
            case 'E':
                rval = '\033';
                break;
            case '^':
                charIndex++;
                rval = (char)(curExpr.charAt(charIndex)-'@');
                break;
            case 'X':
                charIndex++;
                if(isHexDigit(curExpr.charAt(charIndex))){
                    rval = hex2Bin(curExpr.charAt(charIndex));
                    charIndex++;
                }
                if(isHexDigit(curExpr.charAt(charIndex))){
                    rval <<= 4;
                    rval |= hex2Bin(curExpr.charAt(charIndex));
                    charIndex++;
                }
                if(isHexDigit(curExpr.charAt(charIndex))){
                    rval <<= 4;
                    rval |= hex2Bin(curExpr.charAt(charIndex));
                    charIndex++;
                }
                charIndex--;
                break;
            default:
                if(isOctDigit(curExpr.charAt(charIndex))==false){
                    rval = curExpr.charAt(charIndex);
                }else{
                    rval = oct2Bin(curExpr.charAt(charIndex));
                    charIndex++;
                    if(isOctDigit(curExpr.charAt(charIndex))){
                        rval <<= 3;
                        rval |= oct2Bin(curExpr.charAt(charIndex));
                        charIndex++;
                    }
                    if (isOctDigit(curExpr.charAt(charIndex))){
                        rval <<= 3;
                        rval |= oct2Bin(curExpr.charAt(charIndex));
                        charIndex++;
                    }
                    charIndex--;
                }

        }

        charIndex++;
        return rval;
    }

    private boolean isHexDigit(char c){
        return Character.isDigit(c)||(c>='a'&& c<='z')||(c >= 'A'&&c <= 'Z');
    }

    private int hex2Bin(char c){
        return (Character.isDigit(c)?c-'0':(Character.toUpperCase(c)-'A'+10))&0xf;
    }

    private int oct2Bin(char c){
        return (c-'0')&0x7;
    }

    private boolean isOctDigit(char c){
        return (c>='0'&&c<='7');
    }
}
