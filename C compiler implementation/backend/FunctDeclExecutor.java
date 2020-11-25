package backend;
import frontend.CGrammarInitializer;
import frontend.Symbol;

import java.util.ArrayList;
import java.util.Collections;

public class FunctDeclExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = null;
    private ICodeNode currentNode;
    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        Symbol symbol = null;
        currentNode = root;
        switch (production){
            case CGrammarInitializer.NewName_LP_RP_TO_FunctDecl:
                root.reverseChildren();
                copy(root,root.getChildren().get(0));
                break;

            case CGrammarInitializer.NewName_LP_VarList_RP_TO_FunctDecl:
                symbol = (Symbol) root.getAttribute(ICodeKey.SYMBOL);

                Symbol args = symbol.getArgList();
                initArgumentList(args);

                if(args==null||argsList==null||argsList.isEmpty()){
                    System.err.println("Execute function with arg list but arg list is null");
                    System.exit(1);
                }

                break;
        }
        return root;
    }

    private void initArgumentList(Symbol args){
        if(args==null){
            return;
        }

        argsList = FunctionArgumentList.getFunctionArgumentList().getFuncArgList(true);

        Symbol eachSym =args;
        int count = 0;
        while(eachSym!=null){
            IValueSetter setter = (IValueSetter)eachSym;

            try {
                setter.setValue(argsList.get(count));
                count++;
            } catch (Exception e) {
                e.printStackTrace();
            }
            eachSym=eachSym.getNextSymbol();

        }

    }
}
