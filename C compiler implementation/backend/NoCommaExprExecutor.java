package backend;

import frontend.CGrammarInitializer;
import frontend.Symbol;

public class NoCommaExprExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);

        int production = (int)root.getAttribute(ICodeKey.PRODUCTION);

        Object value;
        ICodeNode child;
        switch (production){
            case CGrammarInitializer.Binary_TO_NoCommaExpr:
                child = root.getChildren().get(0);
                copy(root,child);
                break;
            case CGrammarInitializer.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr:
                child = root.getChildren().get(0);
                IValueSetter setter;
                setter = (IValueSetter) child.getAttribute(ICodeKey.SYMBOL);

                child = root.getChildren().get(1);
                value = child.getAttribute(ICodeKey.SYMBOL);
                if(value == null){
                    value = child.getAttribute(ICodeKey.VALUE);
                }

                try {
                    setter.setValue(value);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Runtime Error: Assign value Error");
                }

                child = root.getChildren().get(0);
                child.setAttribute(ICodeKey.VALUE,value);
                copy(root,root.getChildren().get(0));

                break;
        }


        return root;
    }

}
