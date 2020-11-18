package backend;

import frontend.Symbol;
import frontend.CGrammarInitializer;

public class UnaryNodeExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);

        int production = (Integer) root.getAttribute(ICodeKey.PRODUCTION);
        String text;
        Symbol symbol;
        Object value;

        switch (production){
            case CGrammarInitializer.Number_TO_Unary:
                text = (String) root.getAttribute(ICodeKey.TEXT);
                boolean isFloat = text.indexOf('.')!=-1;
                if(isFloat){
                    value = Float.valueOf(text);
                    root.setAttribute(ICodeKey.VALUE,value);
                }else{
                    value = Integer.valueOf(text);
                    root.setAttribute(ICodeKey.VALUE,value);
                }
                break;
            case CGrammarInitializer.Name_TO_Unary:
                symbol = (Symbol)root.getAttribute(ICodeKey.SYMBOL);
                root.setAttribute(ICodeKey.VALUE,symbol.getValue());
                root.setAttribute(ICodeKey.TEXT,symbol.getName());
                break;

            case CGrammarInitializer.String_TO_Unary:
                text = (String) root.getAttribute(ICodeKey.TEXT);
                symbol = (Symbol)root.getAttribute(ICodeKey.SYMBOL);
                root.setAttribute(ICodeKey.VALUE,text);
                root.setAttribute(ICodeKey.TEXT,symbol.getName());
                break;
        }
        return root;
    }


}
