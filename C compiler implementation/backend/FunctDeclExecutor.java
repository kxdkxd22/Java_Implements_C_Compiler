package backend;
import backend.Compiler.Directive;
import backend.Compiler.ProgramGenerator;
import frontend.CGrammarInitializer;
import frontend.Declarator;
import frontend.Specifier;
import frontend.Symbol;

import java.util.ArrayList;


public class FunctDeclExecutor extends BaseExecutor {
    private ArrayList<Object> argsList = null;
    private ICodeNode currentNode;
    ProgramGenerator generator = ProgramGenerator.getInstance();

    @Override
    public Object Execute(ICodeNode root) {

        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        Symbol symbol = null;
        currentNode = root;

        switch (production){
            case CGrammarInitializer.NewName_LP_RP_TO_FunctDecl:
                root.reverseChildren();
                ICodeNode n = root.getChildren().get(0);
                String name = (String) n.getAttribute(ICodeKey.TEXT);
                if(name!=null&&name.equals("main")!=true){
                    String declaration = name+"()V";
                    generator.emitDirective(Directive.METHOD_PUBLIC_STATIC,declaration);
                    generator.setNameAndDeclaration(name,declaration);
                }
                copy(root,root.getChildren().get(0));
                break;

            case CGrammarInitializer.NewName_LP_VarList_RP_TO_FunctDecl:
                n = root.getChildren().get(0);
                name = (String) n.getAttribute(ICodeKey.TEXT);
                if(name!=null&&name.equals("main")!=true){
                    String declaration = name+emitArgs();
                    generator.emitDirective(Directive.METHOD_PUBLIC_STATIC,declaration);
                    generator.setNameAndDeclaration(name,declaration);
                }

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

    private String emitArgs(){
        argsList = FunctionArgumentList.getFunctionArgumentList().getFuncArgList(true);
        String args = "(";
        for(int i = 0; i < argsList.size(); i++){
            Symbol symbol = (Symbol) argsList.get(i);
            String arg = "";
            if(symbol.getDeclarator(Declarator.ARRAY)!=null){
                arg+="[";
            }

            if(symbol.hasType(Specifier.INT)){
                arg+= "I";
            }

            args+= arg;
        }

        args += ")V";

        generator.emitString(args);

        return args;
    }

}
