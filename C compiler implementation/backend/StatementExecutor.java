package backend;

import frontend.CGrammarInitializer;

public class StatementExecutor extends BaseExecutor {
    private enum LoopType{
      FOR,WHILE,DO_WHILE
    };

    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        ICodeNode node;

        switch (production){
            case CGrammarInitializer.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                executeChild(root,0);
                while(isLoopContinue(root,LoopType.FOR)){
                    executeChild(root,3);
                    executeChild(root,2);
                }
                break;
            case CGrammarInitializer.Do_Statement_While_Test_To_Statement:
                do{
                    executeChild(root,0);
                }while(isLoopContinue(root,LoopType.DO_WHILE));

                break;
            case CGrammarInitializer.While_LP_Test_Rp_TO_Statement:
                while(isLoopContinue(root,LoopType.WHILE)){
                    executeChild(root,1);
                }

                break;
            case CGrammarInitializer.Return_Semi_TO_Statement:
                isContinueExecution(false);
                break;
            case CGrammarInitializer.Return_Expr_Semi_TO_Statement:
                node = executeChild(root,0);
                Object obj = node.getAttribute(ICodeKey.VALUE);
                setReturnObj(obj);
                isContinueExecution(false);
                break;
            default:
                executeChildren(root);
                break;

        }

        return root;
    }

    private boolean isLoopContinue(ICodeNode root,LoopType type){
        ICodeNode res = null;

        if(type == LoopType.FOR || type == LoopType.DO_WHILE){
            res = executeChild(root,1);
        }

        if (type==LoopType.WHILE){
            res = executeChild(root,0);
        }

        Integer result = (Integer) res.getAttribute(ICodeKey.VALUE);
        return res!=null&&result!=0;
    }
}
