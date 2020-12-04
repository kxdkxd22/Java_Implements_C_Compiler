package backend.Compiler;

import frontend.Declarator;
import frontend.Specifier;
import frontend.Symbol;
import frontend.TypeSystem;

import java.util.*;

public class ProgramGenerator extends CodeGenerator{
    private static  ProgramGenerator instance = null;
    private Stack<String> nameStack = new Stack<String>();
    private boolean isInitArguments = false;
    private Map<String,String> arrayNameMap = new HashMap<String,String>();

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
        Collections.reverse(localVariables);

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

    public void createArray(Symbol symbol){
        if(arrayNameMap.containsKey(symbol.getScope())){
            if(arrayNameMap.get(symbol.getScope()).equals(symbol.getName())){
                return;
            }
        }

        arrayNameMap.put(symbol.getScope(),symbol.getName());

        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator == null){
            return;
        }

        String type = "";
        if(symbol.hasType(Specifier.INT)){
            type = "int";
        }

        int num = declarator.getNumberOfElements();
        this.emit(Instruction.SIPUSH,""+num);
        this.emit(Instruction.NEWARRAY,type);
        int idx = getLocalVariableIndex(symbol);
        this.emit(Instruction.ASTORE,""+idx);

    }

    public void readArrayElement(Symbol symbol,int index){
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator == null){
            return;
        }

        int idx = getLocalVariableIndex(symbol);
        this.emit(Instruction.ALOAD,""+idx);
        this.emit(Instruction.SIPUSH,""+index);
        this.emit(Instruction.IALOAD);
    }

    public void writeArrayElement(Symbol symbol,int index,Object value){
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator == null){
            return;
        }

        int idx = getLocalVariableIndex(symbol);
        if(symbol.hasType(Specifier.INT)){
            int val = (int)value;
            this.emit(Instruction.ALOAD,""+idx);
            this.emit(Instruction.SIPUSH,""+index);
            this.emit(Instruction.IDIV.SIPUSH,""+val);
            this.emit(Instruction.IASTORE);
        }
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
