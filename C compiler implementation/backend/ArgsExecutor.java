package backend;

import frontend.CGrammarInitializer;

import java.util.ArrayList;

public class ArgsExecutor extends BaseExecutor{
    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        ArrayList<Object> argList = new ArrayList<Object>();
        ICodeNode child;
        switch (production){
            case CGrammarInitializer.NoCommaExpr_TO_Args:
                child = executeChild(root,0);
                int val = (int) child.getAttribute(ICodeKey.VALUE);
                argList.add(val);
                break;
            case CGrammarInitializer.NoCommaExpr_Comma_Args_TO_Args:
                child = executeChild(root,0);
                val = (int) child.getAttribute(ICodeKey.VALUE);
                argList.add(val);

                child = executeChild(root,1);
                ArrayList<Object> list = (ArrayList<Object>)child.getAttribute(ICodeKey.VALUE);
                argList.addAll(list);
                break;
        }
        root.setAttribute(ICodeKey.VALUE,argList);
        return root;
    }
}
