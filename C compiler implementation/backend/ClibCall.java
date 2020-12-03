package backend;

import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;
import frontend.Declarator;
import frontend.Symbol;
import frontend.TypeSystem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClibCall {
    private Set<String> apiSet;
    private ClibCall(){
        apiSet = new HashSet<String>();
        apiSet.add("printf");
        apiSet.add("malloc");
        apiSet.add("sizeof");
    }

    private static ClibCall instance = null;

    public static ClibCall getInstance(){
        if(instance==null){
            instance = new ClibCall();
        }
        return instance;
    }

    public boolean isAPICall(String funcName){
        return apiSet.contains(funcName);
    }

    public Object invokeAPI(String funcName){
        switch (funcName){
            case "printf":
                return handlePrintCall();
            case "malloc":
                return handleMallocCall();
            case "sizeof":
                return handleSizeOfCall();
            default:
                return null;
        }
    }

    private Object handleMallocCall(){
        ArrayList<Object> argsList = FunctionArgumentList.getFunctionArgumentList().getFuncArgList(false);
        int size = (Integer)argsList.get(0);
        int addr = 0;

        if(size>0){
            MemoryHeap memoryHeap = MemoryHeap.getInstance();
            addr = memoryHeap.allocMem(size);
        }

        return addr;
    }

    private Object handleSizeOfCall(){
        ArrayList<Object> symList = FunctionArgumentList.getFunctionArgumentList().getFuncArgSymsList(false);
        Integer totalSize = calculateVarSize((Symbol) symList.get(0));

        return totalSize;
    }

    private Integer calculateVarSize(Symbol symbol){
        int size = 0;
        if(symbol.getArgList()==null){
            size = symbol.getByteSize();
        }else{
            Symbol head = symbol.getArgList();
            while(head!=null){
                size+=calculateVarSize(head);
                head = head.getNextSymbol();
            }

        }

        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator!=null){
            size = size * declarator.getNumberOfElements();
        }

        return size;
    }

    private Object handlePrintCall(){
        ArrayList<Object> argsList = FunctionArgumentList.getFunctionArgumentList().getFuncArgList(false);
        String argStr = (String) argsList.get(0);
        String formatStr = "";

        int i = 0;
        int argCount = 1;
        while (i<argStr.length()){
            if(argStr.charAt(i)=='%'&&argStr.charAt(i+1)=='d'&&i+1<argStr.length()){
                generateJavaAssemblyForPrintf(formatStr);
                formatStr+=argsList.get(argCount);
                i+=2;

                argCount++;
                printInteger();
            }else{
                formatStr+=argStr.charAt(i);
                i++;
            }
        }

        System.out.println(formatStr);
        generateJavaAssemblyForPrintf("\n");
        return null;
    }

    private void generateJavaAssemblyForPrintf(String s){
        ProgramGenerator generator = ProgramGenerator.getInstance();
        generator.emit(Instruction.GETSTATIC,"java/lang/System/out Ljava/io/PrintStream;");
        generator.emit(Instruction.LDC,"\""+s+"\"");
        String printMethod = "java/io/PrintStream/print(Ljava/lang/String;)V";
        generator.emit(Instruction.INVOKEVIRTUAL,printMethod);

    }

    private void printInteger(){
        ProgramGenerator generator = ProgramGenerator.getInstance();
        String funcName = generator.getCurrentFuncName();
        TypeSystem typeSystem = TypeSystem.getTypeSystem();
        ArrayList<Symbol> list = typeSystem.getSymbolsByScope(funcName);
        int localVariableNum = list.size();

        generator.emit(Instruction.ISTORE,""+localVariableNum);
        generator.emit(Instruction.GETSTATIC,"java/lang/System/out Ljava/io/PrintStream;");
        generator.emit(Instruction.ILOAD,""+localVariableNum);
        String printMethod = "java/io/PrintStream/print(I)V";
        generator.emit(Instruction.INVOKEVIRTUAL,printMethod);

    }

}
