package backend;

import frontend.CGrammarInitializer;

public class ExtDefExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        switch (production){
            case CGrammarInitializer.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                executeChild(root,0);
                ICodeNode child = root.getChildren().get(0);
                String funcName = (String) child.getAttribute(ICodeKey.TEXT);
                root.setAttribute(ICodeKey.TEXT,funcName);

                executeChild(root,1);
                Object resultVal = getReturnObj();
                clearReturnObj();
                //child = root.getChildren().get(1);
                //Object resultVal = child.getAttribute(ICodeKey.VALUE);
                if(resultVal!=null){
                    root.setAttribute(ICodeKey.VALUE,resultVal);
                }

                isContinueExecution(true);
                break;
        }

        return root;
    }
}
