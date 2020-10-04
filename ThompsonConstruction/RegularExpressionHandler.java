import java.util.ArrayList;

/**
 * @author kxdstart
 * @date 2020/10/3 - 19:11
 */
public class RegularExpressionHandler {
    private Input input=null;
    private MacroHandler macroHandler=null;
    ArrayList<String> regularExpr = new ArrayList<String>();
    boolean inquoted = false;

    public RegularExpressionHandler(Input input,MacroHandler macroHandler) throws Exception {
        this.input = input;
        this.macroHandler = macroHandler;
        processExprHandler();
    }


    public void processExprHandler() throws Exception {

        while(input.ii_lookahead(1)!=input.EOF){
            preExprHandler();
        }
    }

    public void preExprHandler() throws Exception {

        while(Character.isSpaceChar(input.ii_lookahead(1))||input.ii_lookahead(1)=='\n'){
            input.ii_advance();
        }

        String macroRegularExpr ="";
        char c = (char)input.ii_advance();
        while(Character.isSpaceChar(c) == false && c != '\n'){
            if(c == '"'){
                inquoted=!inquoted;
            }

            if(inquoted!=true&&c == '{'){
                String name = ExtractMacroName();
                macroRegularExpr+=ExpandMacro(name);
            }else{
                macroRegularExpr += c;
            }

             c= (char)input.ii_advance();
        }

        regularExpr.add(macroRegularExpr);

    }

    public String ExtractMacroName() throws Exception {
        String name="";
        char c = (char)input.ii_advance();
        while(c != '}' && c != '\n'){
            name+=c;
            c = (char)input.ii_advance();
        }
        if(c == '}'){
            return name;
        }else{
            ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
            return null;
        }

    }

    public String ExpandMacro(String name) throws Exception {
        String macroContent = macroHandler.getMacroValue(name);
        int begin = macroContent.indexOf('{');
        while(begin!=-1){
            int end = macroContent.indexOf('}');
            if(end==-1){
                ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
                return null;
            }
            inquoted = checkInquoted(macroContent,begin,end);
            if(!inquoted){
                name = macroContent.substring(begin+1,end);
                String content = macroContent.substring(0,begin);
                content += macroHandler.getMacroValue(name);
                content += macroContent.substring(end+1,macroContent.length());
                macroContent = content;
                begin = macroContent.indexOf('{');
            }else{
                begin = macroContent.indexOf('{',end);
            }

        }
        return macroContent;
    }

    public boolean checkInquoted(String macroContent,int begin,int end) throws Exception {

        int inquotedBegin = macroContent.indexOf('"');
        int inquotedEnd = -1;
        while(inquotedBegin!=-1){
            inquotedEnd = macroContent.indexOf('"',inquotedBegin+1);
            if(inquotedEnd==-1){
                ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
            }

            if(inquotedBegin < begin && inquotedEnd > end){
                return true;
            }

            if(inquotedBegin < begin && inquotedEnd < end){
                ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
            }

            if(inquotedBegin > begin && inquotedEnd > end){
                ErrorHandler.parseErr(ErrorHandler.Error.E_BADMAC);
            }

            if(inquotedBegin > begin && inquotedEnd < end){
                return false;
            }

            inquotedEnd = macroContent.indexOf('"',inquotedEnd+1);
        }

        return false;
    }

}
