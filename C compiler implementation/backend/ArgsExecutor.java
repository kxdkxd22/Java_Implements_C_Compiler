package backend;

import frontend.CGrammarInitializer;

import java.util.ArrayList;

public class ArgsExecutor extends BaseExecutor{
    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        ArrayList<Object> argList = new ArrayList<Object>();
        ArrayList<Object> symList = new ArrayList<Object>();
        ICodeNode child;
        switch (production){
            case CGrammarInitializer.NoCommaExpr_TO_Args:
                child = executeChild(root,0);
                Object val = child.getAttribute(ICodeKey.VALUE);
                argList.add(val);
                Object sym = child.getAttribute(ICodeKey.SYMBOL);
                symList.add(sym);
                break;
            case CGrammarInitializer.NoCommaExpr_Comma_Args_TO_Args:
                child = executeChild(root,0);
                Object objVal = child.getAttribute(ICodeKey.VALUE);
                argList.add(objVal);
                objVal = child.getAttribute(ICodeKey.SYMBOL);
                symList.add(objVal);

                child = executeChild(root,1);
                ArrayList<Object> list = (ArrayList<Object>)child.getAttribute(ICodeKey.VALUE);
                argList.addAll(list);
                list = (ArrayList<Object>)child.getAttribute(ICodeKey.SYMBOL);
                symList.addAll(list);
                break;
        }
        root.setAttribute(ICodeKey.VALUE,argList);
        root.setAttribute(ICodeKey.SYMBOL,symList);
        return root;
    }
}
