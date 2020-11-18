package backend;

import frontend.Symbol;
import frontend.CGrammarInitializer;

public class NoCommaExprExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);

        int production = (int)root.getAttribute(ICodeKey.PRODUCTION);
        Symbol symbol;
        Object value;
        ICodeNode child;
        switch (production){
            case CGrammarInitializer.Binary_TO_NoCommaExpr:
                child = root.getChildren().get(0);
                copy(root,child);
                break;
            case CGrammarInitializer.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr:
                child = root.getChildren().get(0);
                symbol = (Symbol) child.getAttribute(ICodeKey.SYMBOL);
                child = root.getChildren().get(1);
                value = child.getAttribute(ICodeKey.VALUE);
                symbol.setValue(value);
                child = root.getChildren().get(0);
                child.setAttribute(ICodeKey.VALUE,value);
                copy(root,root.getChildren().get(0));
                System.out.println(" Variable "+(String)root.getAttribute(ICodeKey.TEXT)+" is assigned to value of "+value.toString());
                break;
        }


        return root;
    }

}
