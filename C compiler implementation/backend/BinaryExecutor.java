package backend;
import frontend.CGrammarInitializer;

import java.util.Collections;

public class BinaryExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);
        ICodeNode child;
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        switch(production){
            case CGrammarInitializer.Unary_TO_binary:
                child = root.getChildren().get(0);
                copy(root,child);
                break;
            case CGrammarInitializer.Binary_Plus_Binary_TO_Binary:
                Collections.reverse(root.getChildren());
                int val1 = (Integer)root.getChildren().get(0).getAttribute(ICodeKey.VALUE);
                int val2 = (Integer)root.getChildren().get(1).getAttribute(ICodeKey.VALUE);
                root.setAttribute(ICodeKey.VALUE,val1+val2);
                System.out.println("Assign sum of "+root.getChildren().get(0).getAttribute(ICodeKey.TEXT)+" and "+
                        root.getChildren().get(1).getAttribute(ICodeKey.TEXT)+" to variable "+root.getAttribute(ICodeKey.VALUE));
                break;
        }

        return root;
    }

}