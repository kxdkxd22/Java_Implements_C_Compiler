package backend.Compiler;

import frontend.Symbol;
import frontend.TypeSystem;

import java.util.ArrayList;
import java.util.Stack;

public class ProgramGenerator extends CodeGenerator{
    private static  ProgramGenerator instance = null;
    private Stack<String> nameStack = new Stack<String>();
    private boolean isInitArguments = false;

    public void initFuncArguments(boolean b){isInitArguments = b;}

    public boolean isPassingArguments(){return isInitArguments;}

    public void setCurrentFuncName(String name){
        nameStack.push(name);
    }

    public String getCurrentFuncName(){
        return nameStack.peek();
    }

    public void popFuncName(){nameStack.pop();}

    public int getLocalVariableIndex(Symbol symbol){
        TypeSystem typeSystem = TypeSystem.getTypeSystem();
        String funcName = nameStack.peek();
        Symbol funcSym = typeSystem.getSymbolByText(funcName,0,"main");
        ArrayList<Symbol> localVariables = new ArrayList<Symbol>();
        Symbol s = funcSym.getArgList();
        while(s!=null){
            localVariables.add(s);
            s = s.getNextSymbol();
        }

        ArrayList<Symbol> list = typeSystem.getSymbolsByScope(symbol.getScope());
        for(int i = 0; i < list.size(); i++){
            if(localVariables.contains(list.get(i))==false){
                localVariables.add(list.get(i));
            }
        }

        for(int i = 0; i < localVariables.size(); i++){
            if(localVariables.get(i)==symbol){
                return i;
            }
        }

        return -1;
    }

    public static ProgramGenerator getInstance(){
        if(instance == null){
            instance = new ProgramGenerator();
        }
        return instance;
    }

    private ProgramGenerator(){}

    public String getProgramName(){return programName;}

    public void generate(){
        emitDirective(Directive.CLASS_PUBLIC,programName);
        emitDirective(Directive.SUPER,"java/lang/Object");
        generateMainMethod();
    }

    private void generateMainMethod(){
        emitBlankLine();
        emitDirective(Directive.METHOD_PUBLIC_STATIC,"main([Ljava/lang/String;)V");
    }

    public void finish(){
        emit(Instruction.RETURN);
        emitDirective(Directive.END_METHOD);
        emitBufferedContent();
        emitDirective(Directive.END_CLASS);
        super.finish();
    }

}
