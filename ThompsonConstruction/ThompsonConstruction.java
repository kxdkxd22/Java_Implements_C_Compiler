import java.util.ArrayList;

/**
 * @author kxdstart
 * @date 2020/10/3 - 19:11
 */
public class ThompsonConstruction {

    private Input input = new Input();
    private MacroHandler macroHandler = null;
    private ThompsonLexer lexer = null;
    RegularExpressionHandler regularExpressionHandler = null;
    NfaMachineConstructor nfaMachineConstructor = null;
    private NfaPrinter nfaPrinter = new NfaPrinter();


    public void runMacroHandler(){
        System.out.println("please enter macro definition");
        runNewInput();
        macroHandler = new MacroHandler(input);
        macroHandler.printMacro();
    }

    public void runRegularExprHandler() throws Exception {
        System.out.println("enter regular expression");
        runNewInput();
        regularExpressionHandler = new RegularExpressionHandler(input,macroHandler);
        System.out.println("regular expression after expand:");
        for(int i = 0; i<regularExpressionHandler.regularExpr.size();i++){
            System.out.println(regularExpressionHandler.regularExpr.get(i));
        }
    }

    public void runNewInput(){
        input.ii_newfile(null);
        input.ii_advance();
        input.ii_pushback(1);
    }

    private void runLexerExample(){
        lexer = new ThompsonLexer(regularExpressionHandler);
        int exprCount = 0;
        System.out.println("当前正在解析的正则表达式："+regularExpressionHandler.regularExpr.get(exprCount));
        lexer.advance();
        while(lexer.MatchToken(ThompsonLexer.Token.END_OF_INPUT)==false){
            if(lexer.MatchToken(ThompsonLexer.Token.EOS)==true){
                System.out.println("解析下一个正则表达式");
                exprCount++;
                System.out.println("当前正在解析的正则表达式："+regularExpressionHandler.getRegularExpression(exprCount));
                lexer.advance();
            }else{
                printLexResult();
            }
        }

    }

    private void printLexResult(){
        while(lexer.MatchToken(ThompsonLexer.Token.EOS)==false){
            System.out.println("当前识别的字符是：" + (char)lexer.getLexeme());
            if(lexer.MatchToken(ThompsonLexer.Token.L)!=true){
                System.out.println("当前字符具有特殊含义");
                printMetaCharMeaning(lexer);
            }else{
                System.out.println("当前字符是普通字符常量");
            }
            lexer.advance();

        }

    }

    private void printMetaCharMeaning(ThompsonLexer lexer){
        String s = "";
        if(lexer.MatchToken(ThompsonLexer.Token.ANY)){
            s = "当前字符是点通配符";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.AT_BOL)){
            s = "当前字符是开头匹配符";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.AT_EOL)){
            s = "当前字符是末尾匹配符";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.CCL_END)){
            s = "当前字符是字符集类结尾括号";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.CCL_START)){
            s = "当前字符是字符集类的开始括号";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.CLOSE_CURLY)){
            s = "当前字符是结尾大括号";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.CLOSE_PAREN)){
            s = "当前字符是结尾圆括号";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.DASH)){
            s = "当前字符是横杠";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.OPEN_CURLY)){
            s = "当前字符是起始大括号";
        }
        if(lexer.MatchToken(ThompsonLexer.Token.OPEN_PAREN)){
            s = "当前字符是起始圆括号";
        }
        if (lexer.MatchToken(ThompsonLexer.Token.OPTIONAL)) {
            s = "当前字符是单字符匹配符?";
        }

        if (lexer.MatchToken(ThompsonLexer.Token.OR)) {
            s = "当前字符是或操作符";
        }

        if (lexer.MatchToken(ThompsonLexer.Token.PLUS_CLOSE)) {
            s = "当前字符是正闭包操作符";
        }

        if (lexer.MatchToken(ThompsonLexer.Token.CLOSURE)) {
            s = "当前字符是闭包操作符";
        }

        System.out.println(s);
    }

    private void runNfaMachineConstructor() throws Exception {
        lexer = new ThompsonLexer(regularExpressionHandler);
        nfaMachineConstructor = new NfaMachineConstructor(lexer);
        NfaPair nfaPair = new NfaPair();
        //nfaMachineConstructor.constructNfaForSingleCharacter(nfaPair);
        //nfaMachineConstructor.constructNfaForCharacterSet(nfaPair);
        //nfaMachineConstructor.constructNfaForDot(nfaPair);
        //nfaMachineConstructor.constructStarClosure(nfaPair);
        //nfaMachineConstructor.constructPlusClosure(nfaPair);
        //nfaMachineConstructor.constructOptionsClosure(nfaPair);
       // nfaMachineConstructor.cat_expr(nfaPair);
        nfaMachineConstructor.expr(nfaPair);
        nfaPrinter.printNfa(nfaPair.startNode);

    }

    public void runNfaIntepretor(){


    }

    public static void main(String[] args) throws Exception {
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroHandler();
        construction.runRegularExprHandler();
        construction.runLexerExample();

        construction.runNfaMachineConstructor();

        construction.runNfaIntepretor();
    }
}
