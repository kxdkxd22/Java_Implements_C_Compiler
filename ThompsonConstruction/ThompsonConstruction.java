import java.util.ArrayList;

/**
 * @author kxdstart
 * @date 2020/10/3 - 19:11
 */
public class ThompsonConstruction {

    private Input input = new Input();
    private MacroHandler macroHandler = null;

    public void runMacroHandler(){
        System.out.println("please enter macro definition");
        runNewInput();
        macroHandler = new MacroHandler(input);
        macroHandler.printMacro();
    }

    public void runRegularExprHandler() throws Exception {
        System.out.println("enter regular expression");
        runNewInput();
        RegularExpressionHandler regularExpressionHandler = new RegularExpressionHandler(input,macroHandler);
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

    public static void main(String[] args) throws Exception {
        ThompsonConstruction construction = new ThompsonConstruction();
        construction.runMacroHandler();
        construction.runRegularExprHandler();
    }
}
