package backend;

import backend.Compiler.Directive;
import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;
import com.sun.org.apache.xpath.internal.objects.XObject;
import frontend.CGrammarInitializer;
import frontend.Declarator;
import frontend.Specifier;
import frontend.Symbol;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

public class UnaryNodeExecutor extends BaseExecutor implements IExecutorReceiver{
    private Symbol structObjSymbol = null;
    private Symbol monitorSymbol = null;

    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);

        int production = (Integer) root.getAttribute(ICodeKey.PRODUCTION);
        String text;
        Symbol symbol;
        Object value;
        ICodeNode child;

        switch (production){
            case CGrammarInitializer.Number_TO_Unary:
                text = (String) root.getAttribute(ICodeKey.TEXT);
                boolean isFloat = text.indexOf('.')!=-1;
                if(isFloat){
                    value = Float.valueOf(text);
                    root.setAttribute(ICodeKey.VALUE,value);
                }else{
                    value = Integer.valueOf(text);
                    root.setAttribute(ICodeKey.VALUE,value);
                }
                //ProgramGenerator.getInstance().emit(Instruction.SIPUSH,value.toString());
                break;
            case CGrammarInitializer.Name_TO_Unary:
                symbol = (Symbol)root.getAttribute(ICodeKey.SYMBOL);
                if(symbol!=null){
                    root.setAttribute(ICodeKey.VALUE,symbol.getValue());
                    root.setAttribute(ICodeKey.TEXT,symbol.getName());

                    ICodeNode func = CodeTreeBuilder.getCodeTreeBuilder().getFunctionNodeByName(symbol.getName());
                    if(func == null && symbol.getValue()!=null){
                      /*  ProgramGenerator generator = ProgramGenerator.getInstance();
                        int idx = generator.getLocalVariableIndex(symbol);
                        generator.emit(Instruction.ILOAD,""+idx);
                        */
                    }

                }
                break;

            case CGrammarInitializer.String_TO_Unary:
                text = (String) root.getAttribute(ICodeKey.TEXT);
               // symbol = (Symbol)root.getAttribute(ICodeKey.SYMBOL);
                root.setAttribute(ICodeKey.VALUE,text);
               // root.setAttribute(ICodeKey.TEXT,symbol.getName());
                break;
            case CGrammarInitializer.Unary_LB_Expr_RB_TO_Unary:
                child = root.getChildren().get(0);
                symbol = (Symbol) child.getAttribute(ICodeKey.SYMBOL);
                child = root.getChildren().get(1);
                int index = 0;
                if(child.getAttribute(ICodeKey.VALUE)!=null){
                    index = (int) child.getAttribute(ICodeKey.VALUE);
                }
                Object idxObj = child.getAttribute(ICodeKey.SYMBOL);

                try {
                    Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
                    if(declarator!=null){
                        Object val = declarator.getElement(index);
                        root.setAttribute(ICodeKey.VALUE,val);
                        ArrayValueSetter setter = null;

                        if(idxObj==null){
                            setter = new ArrayValueSetter(symbol,index);
                        }else {
                            setter = new ArrayValueSetter(symbol,idxObj);
                        }

                        root.setAttribute(ICodeKey.SYMBOL,setter);
                        root.setAttribute(ICodeKey.TEXT,symbol.getName());
                        /*
                        if(symbol.getSpecifierByType(Specifier.STRUCTURE)==null){
                            ProgramGenerator.getInstance().createArray(symbol);
                            ProgramGenerator.getInstance().readArrayElement(symbol,index);
                        }*/

                    }
                    Declarator pointer = symbol.getDeclarator(Declarator.POINTER);
                    if(pointer!=null){
                        setPointerValue(root,symbol,index);
                        PointerValueSetter pv = new PointerValueSetter(symbol,index);
                        root.setAttribute(ICodeKey.SYMBOL,pv);
                        root.setAttribute(ICodeKey.TEXT,symbol.getName());
                    }

                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    System.exit(1);
                }
                break;
            case CGrammarInitializer.Unary_Incop_TO_Unary:
            case CGrammarInitializer.Unary_DecOp_TO_Unary:
                symbol = (Symbol) root.getChildren().get(0).getAttribute(ICodeKey.SYMBOL);
                Integer val = (Integer) symbol.getValue();
                IValueSetter setter = (IValueSetter) symbol;

                int i = generator.getLocalVariableIndex(symbol);
                generator.emit(Instruction.ILOAD,""+i);
                generator.emit(Instruction.SIPUSH,""+1);
                try {
                    if(production == CGrammarInitializer.Unary_Incop_TO_Unary){
                        if(BaseExecutor.isCompileMode ==false){
                            setter.setValue(val + 1);
                        }
                        generator.emit(Instruction.IADD);
                    }else {
                        if(BaseExecutor.isCompileMode ==false){
                            setter.setValue(val - 1);
                        }
                        generator.emit(Instruction.ISUB);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    System.err.println("Runtime Error: Assign Value Error");
                }
                generator.emit(Instruction.ISTORE,""+i);
                break;
            case CGrammarInitializer.LP_Expr_RP_TO_Unary:
                child = root.getChildren().get(0);
                copy(root,child);
                break;
            case CGrammarInitializer.Start_Unary_TO_Unary:
                child = root.getChildren().get(0);
                int addr = (int) child.getAttribute(ICodeKey.VALUE);
                MemoryHeap memoryHeap = MemoryHeap.getInstance();
                Map.Entry<Integer,byte[]> entry = memoryHeap.getMem(addr);
                if(entry!=null){
                    int offset = addr - entry.getKey();
                    byte[] memByte = entry.getValue();
                    root.setAttribute(ICodeKey.VALUE,memByte[offset]);
                }

                DirectMemValueSetter directMemValueSetter = new DirectMemValueSetter(addr);
                root.setAttribute(ICodeKey.SYMBOL,directMemValueSetter);
                break;
            case CGrammarInitializer.Unary_LP_RP_TO_Unary:
            case CGrammarInitializer.Unary_LP_ARGS_RP_TO_Unary:

                boolean reEntry = false;
                String funcName = (String) root.getChildren().get(0).getAttribute(ICodeKey.TEXT);

                if(funcName != ""&& funcName.equals(BaseExecutor.funcName)){
                    reEntry = true;
                }

                ArrayList<Object> argList = null;
                ArrayList<Object> symList = null;

                if(production== CGrammarInitializer.Unary_LP_ARGS_RP_TO_Unary){
                    ICodeNode argsNode = root.getChildren().get(1);
                    argList = (ArrayList<Object>) argsNode.getAttribute(ICodeKey.VALUE);
                    FunctionArgumentList.getFunctionArgumentList().setFuncArgList(argList);

                    symList = (ArrayList<Object>) argsNode.getAttribute(ICodeKey.SYMBOL);
                    FunctionArgumentList.getFunctionArgumentList().setFuncArgSymbolList(symList);
                }

                ICodeNode func = CodeTreeBuilder.getCodeTreeBuilder().getFunctionNodeByName(funcName);

                if(func!=null){
                    BaseExecutor.funcName = funcName;
                    int count = 0;
                    while(count < argList.size()){
                        Object objVal = argList.get(count);
                        Object objSym = symList.get(count);
                        if(objSym!=null){
                            Symbol param = (Symbol) objSym;
                            int idx = generator.getLocalVariableIndex(param);
                            if(param.getDeclarator(Declarator.ARRAY)!=null){
                                generator.emit(Instruction.ALOAD,""+idx);
                            }else{
                                generator.emit(Instruction.ILOAD,""+idx);
                            }
                        }else{
                            int v = (int)objVal;
                            generator.emit(Instruction.SIPUSH,""+v);
                        }

                        count++;
                    }


                    if(BaseExecutor.isCompileMode==true&&reEntry==false){
                        Executor executor = ExecutorFactory.getExecutorFactory().getExecutor(func);
                        ProgramGenerator.getInstance().setBufferedContent(true);
                        executor.Execute(func);
                        symbol = (Symbol) root.getChildren().get(0).getAttribute(ICodeKey.SYMBOL);
                        emitReturnInstruction(symbol);
                        ProgramGenerator.getInstance().emitDirective(Directive.END_METHOD);
                        ProgramGenerator.getInstance().setBufferedContent(false);
                        ProgramGenerator.getInstance().popFuncName();

                    }
                    compileFunctionCall(funcName);

                    Object returnVal = func.getAttribute(ICodeKey.VALUE);
                    if(returnVal!=null){
                        System.out.println("function call with name "+funcName+" has return value that is "+returnVal.toString());
                        root.setAttribute(ICodeKey.VALUE,returnVal);
                    }
                }else{
                    ClibCall libCall = ClibCall.getInstance();
                    if(libCall.isAPICall(funcName)){
                        Object obj = libCall.invokeAPI(funcName);
                        root.setAttribute(ICodeKey.VALUE,obj);
                    }
                }

                break;
            case CGrammarInitializer.Unary_StructOP_Name_TO_Unary:
                child = root.getChildren().get(0);
                String fileldName = (String) root.getAttribute(ICodeKey.TEXT);
                Object object = child.getAttribute(ICodeKey.SYMBOL);
                boolean isStructArray = false;

                if(object instanceof ArrayValueSetter){
                    symbol = getStructSymbolFromStructArray(object);
                    symbol.addValueSetter(object);
                    isStructArray = true;
                }else{
                    symbol = (Symbol) child.getAttribute(ICodeKey.SYMBOL);
                }

                if(isStructArray == true){
                    ArrayValueSetter vs =(ArrayValueSetter)object;
                    Symbol structArray = vs.getSymbol();
                    structArray.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                }else{
                    symbol.addScope(ProgramGenerator.getInstance().getCurrentFuncName());
                }

                ProgramGenerator.getInstance().putStructToClassDeclaration(symbol);

                if(isSymbolStructPointer(symbol)){
                    copyBetweenStructAndMem(symbol,false);
                }

                Symbol args = symbol.getArgList();
                while(args!=null){
                    if(args.getName().equals(fileldName)){
                        args.setStructParent(symbol);
                        break;
                    }
                    args = args.getNextSymbol();
                }

                if(args==null){
                    System.err.println("access a filed not in struct object!");
                    System.exit(1);
                }

                if(args.getValue()!=null){
                    ProgramGenerator.getInstance().readValueFromStructMember(symbol,args);
                }

                root.setAttribute(ICodeKey.SYMBOL,args);
                root.setAttribute(ICodeKey.VALUE,args.getValue());

                if(isSymbolStructPointer(symbol)==true){
                    checkValidPointer(symbol);
                    structObjSymbol = symbol;
                    monitorSymbol = args;
                    ExecutorBrocasterImpl.getInstance().registerReceiverForAfterExe(this);
                }else{
                    structObjSymbol = null;
                }
                break;
        }
        return root;
    }

    private Symbol getStructSymbolFromStructArray(Object object){
        ArrayValueSetter vs = (ArrayValueSetter) object;
        Symbol symbol = vs.getSymbol();
        int idx = (int) vs.getIndex();
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator == null){
            return null;
        }
        ProgramGenerator.getInstance().createStructArray(symbol);

        Symbol struct = null;
        try {
            struct = (Symbol) declarator.getElement(idx);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if(struct == null){
            struct = symbol.copy();

            try {
                declarator.addElements(idx,(Object) struct);
                ProgramGenerator.getInstance().createInstanceForStructArray(symbol,idx);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        return struct;
    }

    private void emitReturnInstruction(Symbol symbol){
        if(symbol.hasType(Specifier.INT)){
            ProgramGenerator.getInstance().emit(Instruction.IRETURN);
        }else{
            ProgramGenerator.getInstance().emit(Instruction.RETURN);
        }
    }

    private void compileFunctionCall(String funcName){
        ProgramGenerator generator = ProgramGenerator.getInstance();
        String declaration = generator.getDeclarationByName(funcName);
        String call = generator.getProgramName()+"/"+declaration;
        generator.emit(Instruction.INVOKESTATIC,call);
    }

    private boolean isSymbolStructPointer(Symbol symbol){
        if(symbol.getDeclarator(Declarator.POINTER)!=null&&symbol.getArgList()!=null){
            return true;
        }
        return false;
    }

    private void checkValidPointer(Symbol symbol){
        if(symbol.getDeclarator(Declarator.POINTER)!=null&&symbol.getValue()==null){
            System.err.println("Access Empty Pointer");
            System.exit(1);
        }
    }

    private void setPointerValue(ICodeNode root,Symbol symbol,int index){
        MemoryHeap memoryHeap = MemoryHeap.getInstance();
        int addr = (Integer)symbol.getValue();
        Map.Entry<Integer,byte[]> entry = memoryHeap.getMem(addr);
        byte[] content = entry.getValue();
        if(symbol.getByteSize()==1){
            root.setAttribute(ICodeKey.VALUE,content[index]);
        }else{
            ByteBuffer buffer = ByteBuffer.allocate(4);
            buffer.put(content,index,4);
            buffer.flip();
            root.setAttribute(ICodeKey.VALUE,buffer.getInt());
        }
    }

    @Override
    public void handleExecutorMessage(ICodeNode node) {
        int productNum = (int) node.getAttribute(ICodeKey.PRODUCTION);
        if(productNum!=CGrammarInitializer.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr){
            return;
        }

        Object object = node.getAttribute(ICodeKey.SYMBOL);
        if(object==null){
            return;
        }

        Symbol symbol = null;
        if(object instanceof IValueSetter){
            symbol = ((IValueSetter)object).getSymbol();
        }else{
            symbol = (Symbol)object;
        }

        if(symbol==monitorSymbol){
            System.out.println("UnaryNodeExecutor receive msg for assign execution");
            copyBetweenStructAndMem(structObjSymbol,true);
        }
    }

    private void copyBetweenStructAndMem(Symbol symbol,boolean isFromStructToMem){
        int addr = (int) symbol.getValue();
        MemoryHeap memoryHeap = MemoryHeap.getInstance();
        Map.Entry<Integer,byte[]> entry = memoryHeap.getMem(addr);
        byte[] mems = entry.getValue();
        Stack<Symbol> stack = reverseStructSymbolList(symbol);
        int offset = 0;

        while(stack.empty()!=true){
            Symbol sym = stack.pop();

            if(isFromStructToMem==true){
                offset+=writeStructVariablesToMem(sym,mems,offset);
            }else{
                offset+=writeMemToStructVariables(sym,mems,offset);
            }

        }

    }

    private int writeStructVariablesToMem(Symbol symbol,byte[] mem,int offset){
        if(symbol.getArgList()!=null){
            return writeStructVariablesToMem(symbol,mem,offset);
        }

        int sz = symbol.getByteSize();
        if(symbol.getValue()==null&&symbol.getDeclarator(Declarator.ARRAY)==null){
            return sz;
        }

        if(symbol.getDeclarator(Declarator.ARRAY)==null){
            Integer val = (Integer)symbol.getValue();
            byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
            for(int i = 3; i>= 4-sz;i--){
                mem[offset+3-i]=bytes[i];
            }
            return sz;
        }else{
            return copyArrayVariableToMem(symbol,mem,offset);
        }
    }

    private int writeMemToStructVariables(Symbol symbol,byte[] mem,int offset){
        if(symbol.getArgList()!=null){
            return writeMemToStructVariables(symbol,mem,offset);
        }

        int sz =symbol.getByteSize();
        int val = 0;
        if(symbol.getDeclarator(Declarator.ARRAY)==null){
            val = fromByteArrayToInteger(mem,offset,sz);
            symbol.setValue(val);
        }else{
            return copyMemToArrayVariable(symbol,mem,offset);
        }

        return sz;
    }

    private int fromByteArrayToInteger(byte[] mem,int offset,int sz){
        int val = 0;
        switch (sz){
            case 1:
                val = mem[offset];
                break;
            case 2:
                val = (mem[offset+1]<<8)|mem[offset];
                break;
            case 4:
                val = (mem[offset+3]<<24)|(mem[offset+2]<<16)|(mem[offset+1]<<8)|mem[offset];
                break;
        }
        return val;
    }

    private int copyArrayVariableToMem(Symbol symbol,byte[] mem,int offset){
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator==null){
            return 0;
        }

        int sz = symbol.getByteSize();
        int elemCount = declarator.getNumberOfElements();
        for(int i = 0; i < elemCount; i++){
            try {
                Integer val = (Integer) declarator.getElement(i);
                byte[] bytes = ByteBuffer.allocate(4).putInt(val).array();
                for(int j = 0; j<sz;j++){
                    mem[offset+j] = bytes[j];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return sz*elemCount;
    }

    private int copyMemToArrayVariable(Symbol symbol,byte[] mem,int offset){
        int sz = symbol.getByteSize();
        Declarator declarator = symbol.getDeclarator(Declarator.ARRAY);
        if(declarator == null){
            return 0;
        }
        int size = 0;
        int elemCount = declarator.getNumberOfElements();

        for(int i = 0; i< elemCount;i++){
            int val = fromByteArrayToInteger(mem,offset+size,sz);
            size+=sz;
            try {
                declarator.addElements(i,val);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return size;
    }

    private Stack<Symbol> reverseStructSymbolList(Symbol symbol){
        Stack<Symbol> stack = new Stack<Symbol>();
        Symbol sym = symbol.getArgList();
        while(sym!=null){
            stack.push(sym);
            sym=sym.getNextSymbol();
        }
        return stack;
    }
}
