package backend;

import frontend.CGrammarInitializer;
import frontend.Symbol;
import frontend.TypeSystem;

import java.util.ArrayList;

public class ExtDefExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = new ArrayList<Object>();
    String funcName;
    ICodeNode root;
    @Override
    public Object Execute(ICodeNode root) {
        this.root = root;
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        switch (production){
            case CGrammarInitializer.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:

                ICodeNode child = root.getChildren().get(0);
                funcName = (String) child.getAttribute(ICodeKey.TEXT);
                root.setAttribute(ICodeKey.TEXT,funcName);
                saveArgs();
                executeChild(root,0);
                executeChild(root,1);
                Object resultVal = getReturnObj();
                clearReturnObj();
                //child = root.getChildren().get(1);
                //Object resultVal = child.getAttribute(ICodeKey.VALUE);
                if(resultVal!=null){
                    root.setAttribute(ICodeKey.VALUE,resultVal);
                }

                isContinueExecution(true);
                restoreArgs();
                break;
        }

        return root;
    }

    private void saveArgs(){
        System.out.println("Save arguments......");
        TypeSystem typeSystem = TypeSystem.getTypeSystem();
        ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
        int count = 0;
        while(count<args.size()){
            Symbol sym = args.get(count);
            Object val = sym.getValue();
            argsList.add(val);
            count++;
        }
    }

    private void restoreArgs()  {
        System.out.println("Restore arguments....");
        TypeSystem typeSystem = TypeSystem.getTypeSystem();
        ArrayList<Symbol> args = typeSystem.getSymbolsByScope(funcName);
        int count = 0;

        while(args!=null&&count<args.size()){
            IValueSetter setter = args.get(count);

            try {
                Object value = argsList.get(count);
                setter.setValue(value);
            } catch (Exception e) {
                e.printStackTrace();
            }
            count++;
        }
    }

}
