package backend;
import backend.Compiler.Directive;
import backend.Compiler.ProgramGenerator;
import frontend.CGrammarInitializer;
import frontend.Declarator;
import frontend.Specifier;
import frontend.Symbol;

import java.util.ArrayList;
import java.util.Collections;


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
                symbol = (Symbol) root.getAttribute(ICodeKey.SYMBOL);
                generator.setCurrentFuncName(name);
                if(name!=null&&name.equals("main")!=true){
                    String declaration = name+emitArgs(symbol);
                    generator.emitDirective(Directive.METHOD_PUBLIC_STATIC,declaration);
                    generator.setNameAndDeclaration(name,declaration);
                }
                copy(root,root.getChildren().get(0));
                break;

            case CGrammarInitializer.NewName_LP_VarList_RP_TO_FunctDecl:
                n = root.getChildren().get(0);
                name = (String) n.getAttribute(ICodeKey.TEXT);
                symbol = (Symbol) root.getAttribute(ICodeKey.SYMBOL);
                generator.setCurrentFuncName(name);
                if(name!=null&&name.equals("main")!=true){
                    String declaration = name+emitArgs(symbol);
                    generator.emitDirective(Directive.METHOD_PUBLIC_STATIC,declaration);
                    generator.setNameAndDeclaration(name,declaration);
                }

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

        generator.initFuncArguments(true);
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
        generator.initFuncArguments(false);
    }

    private String emitArgs(Symbol funcSymbol){
        Symbol s = funcSymbol.getArgList();
        ArrayList<Symbol> params = new ArrayList<Symbol>();
        while(s!=null){
            params.add(s);
            s = s.getNextSymbol();
        }
        Collections.reverse(params);
        String args = "(";
        for(int i = 0; i < params.size(); i++){
            Symbol symbol = params.get(i);
            String arg = "";
            if(symbol.getDeclarator(Declarator.ARRAY)!=null){
                arg+="[";
            }

            if(symbol.hasType(Specifier.INT)){
                arg+= "I";
            }

            args+= arg;
        }

        if(funcSymbol.hasType(Specifier.INT)){
            args += ")I";
        }else{
            args += ")V";
        }

        return args;
    }

}
