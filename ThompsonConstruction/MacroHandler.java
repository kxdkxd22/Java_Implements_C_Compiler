import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author kxdstart
 * @date 2020/10/3 - 19:11
 */
public class MacroHandler {
    private HashMap<String,String> macroMap = new HashMap<String,String>();
    private Input input;

    public MacroHandler(Input input){
        this.input = input;
        while(input.ii_lookahead(1)!=Input.EOF){
            setMacroMap();
        }
    }

    public void setMacroMap(){
        String macroKey = "";
        String macroContent = "";
        char c;

        while(Character.isSpaceChar(input.ii_lookahead(1))||input.ii_lookahead(1)=='\n'){
           input.ii_advance();
        }

        c = (char)input.ii_lookahead(1);
        while(Character.isSpaceChar(input.ii_lookahead(1))==false){
            macroKey += c;
            input.ii_advance();
            c = (char)input.ii_lookahead(1);
        }

        while(Character.isSpaceChar(input.ii_lookahead(1))){
            input.ii_advance();
        }

        c = (char)input.ii_lookahead(1);
        while(Character.isSpaceChar(c)==false&&c!='\n'){
            macroContent+=c;
            input.ii_advance();
            c = (char) input.ii_lookahead(1);
        }

        input.ii_advance();
        macroMap.put(macroKey,macroContent);

    }

    public String getMacroValue(String macrokey) throws Exception{

        if(macroMap.containsKey(macrokey)){
            return "("+macroMap.get(macrokey)+")";
        }else{
            ErrorHandler.parseErr(ErrorHandler.Error.E_NOMAC);
        }

        return "ERROR";
    }

    public void printMacro(){
        if(macroMap.isEmpty()){
            System.out.println("There are no macros");
        }else{
            Iterator iter = macroMap.entrySet().iterator();
            while(iter.hasNext()){
                Map.Entry<String,String> entry = (Map.Entry<String,String>)iter.next();
                System.out.println("Macro name: "+entry.getKey()+" Macro value: "+entry.getValue());
            }
        }
    }

}
