package backend;

import frontend.CGrammarInitializer;

public class StatementExecutor extends BaseExecutor {
    private enum LoopType{
      FOR,WHILE,DO_WHILE
    };

    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);

        switch (production){
            case CGrammarInitializer.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                executeChild(root,0);
                while(isLoopContinue(root,LoopType.FOR)){
                    executeChild(root,3);
                    executeChild(root,2);
                }
                break;
            default:
                executeChildren(root);
                break;

        }

        return root;
    }

    private boolean isLoopContinue(ICodeNode root,LoopType type){
        ICodeNode res = null;

        if(type == LoopType.FOR){
            res = executeChild(root,1);
            Integer val = (Integer) res.getAttribute(ICodeKey.VALUE);
            return val!=0;
        }

        return false;
    }
}
