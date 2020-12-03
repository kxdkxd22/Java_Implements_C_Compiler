package backend.Compiler;

import frontend.Symbol;
import frontend.TypeSystem;

import java.util.ArrayList;

public class ProgramGenerator extends CodeGenerator{
    private static  ProgramGenerator instance = null;
    private String funcName = "";

    public void setCurrentFuncName(String name){
        this.funcName = name;
    }

    public String getCurrentFuncName(){
        return this.funcName;
    }

    public int getLocalVariableIndex(Symbol symbol){
        TypeSystem typeSystem = TypeSystem.getTypeSystem();
        ArrayList<Symbol> list = typeSystem.getSymbolsByScope(symbol.getScope());

        for(int i = 0; i < list.size(); i++){
            if(list.get(i)==symbol){
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
