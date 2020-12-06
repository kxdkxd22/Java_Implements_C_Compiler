package backend.Compiler;

import backend.ArrayValueSetter;
import frontend.*;

import java.util.*;

public class ProgramGenerator extends CodeGenerator{
    private static  ProgramGenerator instance = null;
    private Stack<String> nameStack = new Stack<String>();
    private boolean isInitArguments = false;
    private Map<String,String> arrayNameMap = new HashMap<String,String>();
    private ArrayList<String> structNameList = new ArrayList<String>();

    public void putStructToClassDeclaration(Symbol symbol){
        Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
        if(sp == null){
            return;
        }

        StructDefine struct = sp.getStructObj();
        if(structNameList.contains(struct.getTag())){
            return;
        }else{
            structNameList.add(struct.getTag());
        }

        if(symbol.getValueSetter()==null){
            this.emit(Instruction.NEW,struct.getTag());
            this.emit(Instruction.DUP);
            this.emit(Instruction.INVOKESPECIAL,struct.getTag()+"/"+"<init>()V");
            int idx = this.getLocalVariableIndex(symbol);
            this.emit(Instruction.ASTORE,""+idx);
        }

        declareStructAsClass(struct);
    }

    private void declareStructAsClass(StructDefine struct){
        this.setClassDefinition(true);
        this.emitDirective(Directive.CLASS_PUBLIC,struct.getTag());
        this.emitDirective(Directive.SUPER,"java/lang/Object");

        Symbol fields = struct.getFields();
        do{
            String fieldName = fields.getName()+" ";
            if(fields.getDeclarator(Declarator.ARRAY)!=null){
                fieldName+="[";
            }

            if(fields.hasType(Specifier.INT)){
                fieldName+="I";
            }else if(fields.hasType(Specifier.CHAR)){
                fieldName+="C";
            }else if(fields.hasType(Specifier.CHAR)&&fields.getDeclarator(Declarator.POINTER)!=null){
                fieldName+="Ljava/lang/String;";
            }

            this.emitDirective(Directive.FIELD_PUBLIC,fieldName);
            fields = fields.getNextSymbol();

        }while(fields!=null);

        this.emitDirective(Directive.METHOD_PUBLIC,"<init>()V");
        this.emit(Instruction.ALOAD,"0");
        String superInit = "java/lang/Object/<init>()V";
        this.emit(Instruction.INVOKESPECIAL,superInit);

        fields = struct.getFields();
        do{
            this.emit(Instruction.ALOAD,"0");
            String fieldName = struct.getTag()+"/"+fields.getName();
            String fieldType = "";
            if(fields.hasType(Specifier.INT)){
                fieldType="I";
                this.emit(Instruction.SIPUSH,"0");
            }else if(fields.hasType(Specifier.CHAR)){
                fieldType="C";
                this.emit(Instruction.SIPUSH,"0");
            }else if(fields.hasType(Specifier.CHAR)&&fields.getDeclarator(Declarator.POINTER)!=null){
                fieldType = "Ljava/lang/String;";
                this.emit(Instruction.LDC," ");
            }
            String classField = fieldName+" "+fieldType;
            this.emit(Instruction.PUTFIELD,classField);
            fields = fields.getNextSymbol();

        }while(fields!=null);

        this.emit(Instruction.RETURN);
        this.emitDirective(Directive.END_METHOD);
        this.emitDirective(Directive.END_CLASS);
        this.setClassDefinition(false);
    }

    public void createStructArray(Symbol structSymArray){
        Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStructObj();
        if(structNameList.contains(struct.getTag())){
            return;
        }else{
            structNameList.add(struct.getTag());
        }

        Declarator declarator = structSymArray.getDeclarator(Declarator.ARRAY);
        int eleCount = declarator.getNumberOfElements();
        this.emit(Instruction.SIPUSH,""+eleCount);
        this.emit(Instruction.ANEWARRAY,struct.getTag());

        int idx = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ASTORE,""+idx);

        declareStructAsClass(struct);
    }

    public void createInstanceForStructArray(Symbol structSymArray,int idx){
        int i = getLocalVariableIndex(structSymArray);
        this.emit(Instruction.ALOAD,""+i);
        this.emit(Instruction.SIPUSH,""+idx);

        Specifier sp = structSymArray.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStructObj();
        this.emit(Instruction.NEW,struct.getTag());
        this.emit(Instruction.DUP);
        this.emit(Instruction.INVOKESPECIAL,struct.getTag()+"/"+"<init>()V");
        this.emit(Instruction.AASTORE);
    }

    public void assignValueToStructMember(Symbol structSym, Symbol field, Object val){
        int idx = getLocalVariableIndex(structSym);
        this.emit(Instruction.ALOAD,""+idx);

        String value = "";
        String fieldType = "";
        if(field.hasType(Specifier.INT)){
            fieldType = "I";
            value+=(Integer)val;
            this.emit(Instruction.SIPUSH,value);
        }else if(field.hasType(Specifier.CHAR)){
            fieldType ="C";
            value+=(Integer) val;
            this.emit(Instruction.SIPUSH,value);
        }else if(field.hasType(Specifier.CHAR)&&field.getDeclarator(Declarator.POINTER)!=null){
            fieldType = "Ljava/lang/String;";
            value+=(String)val;
            this.emit(Instruction.LDC,value);
        }

        Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStructObj();
        String fieldContent = struct.getTag()+"/"+field.getName()+" "+fieldType;
        this.emit(Instruction.PUTFIELD,fieldContent);

    }

    public void assignValueToStructMemberFromArray(Object obj,Symbol field,Object val){
        ArrayValueSetter setter = (ArrayValueSetter)obj;
        int idx = setter.getIndex();
        Symbol symbol = setter.getSymbol();

        int i = getLocalVariableIndex(symbol);
        this.emit(Instruction.ALOAD,""+i);
        this.emit(Instruction.SIPUSH,""+idx);
        this.emit(Instruction.AALOAD);

        String value = "";
        String fieldType = "";
        if(field.hasType(Specifier.INT)){
            fieldType="I";
            value +=  (Integer)val;
            this.emit(Instruction.SIPUSH,value);
        }else if(field.hasType(Specifier.CHAR)){
            fieldType = "C";
            value+=(Integer)val;
            this.emit(Instruction.SIPUSH,value);
        }else if(field.hasType(Specifier.CHAR)&&field.getDeclarator(Declarator.POINTER)!=null){
            fieldType = "Ljava/lang/String;";
            value+=(String)val;
            this.emit(Instruction.LDC,value);
        }

        Specifier sp = symbol.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStructObj();
        String fieldContent = struct.getTag()+"/"+field.getName()+" "+fieldType;
        this.emit(Instruction.PUTFIELD,fieldContent);

    }

    public void readValueFromStructMember(Symbol structSym,Symbol field){
        ArrayValueSetter vs = (ArrayValueSetter)structSym.getValueSetter();
        if(vs!=null){
            structSym = vs.getSymbol();
        }

        int idx = getLocalVariableIndex(structSym);
        this.emit(Instruction.ALOAD,""+idx);

        if(vs!=null){
            int i = vs.getIndex();
            this.emit(Instruction.SIPUSH,""+i);
            this.emit(Instruction.AALOAD);
        }

        String fieldType = "";
        if(field.hasType(Specifier.INT)){
            fieldType ="I";
        }else if(field.hasType(Specifier.CHAR)){
            fieldType="C";
        }else if(field.hasType(Specifier.CHAR)&&field.getDeclarator(Declarator.POINTER)!=null){
            fieldType = "Ljava/lang/String;";
        }

        Specifier sp = structSym.getSpecifierByType(Specifier.STRUCTURE);
        StructDefine struct = sp.getStructObj();
        String fieldContent = struct.getTag()+"/"+field.getName()+" "+fieldType;
        this.emit(Instruction.GETFIELD,fieldContent);

    }

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
        emitClassDefinition();
        super.finish();
    }

}
