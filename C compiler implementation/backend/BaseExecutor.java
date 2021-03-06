package backend;

import backend.Compiler.ProgramGenerator;

import java.util.Collections;

public abstract class BaseExecutor implements Executor{
    private static boolean continueExecute = true;
    private static Object returnObj = null;
    IExecutorBrocaster executorBrocaster = null;
    ProgramGenerator generator;
    public static boolean inIfElseStatement = false;
    public static boolean isCompileMode = false;

    public static boolean resultOnStack = false;
    public static String funcName= "";

    public BaseExecutor(){
        executorBrocaster = ExecutorBrocasterImpl.getInstance();
        generator = ProgramGenerator.getInstance();
    }

    protected void setReturnObj(Object obj){
        this.returnObj = obj;
    }

    protected Object getReturnObj(){
        return returnObj;
    }

    protected void clearReturnObj(){
        this.returnObj = null;
    }

    protected void isContinueExecution(boolean execute){
        this.continueExecute = execute;
    }

    protected void executeChildren(ICodeNode root){
        ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
        //Collections.reverse(root.getChildren());
        root.reverseChildren();
        int i = 0;
        while(i<root.getChildren().size()){
            if(continueExecute!=true){
                break;
            }

            ICodeNode child = root.getChildren().get(i);

            executorBrocaster.brocastBeforeExecution(child);
            Executor executor = factory.getExecutor(child);
            if(executor!=null){
                executor.Execute(child);
            }else{
                System.err.println("Not suitable Executor found, node is: "+child.toString());
            }
            executorBrocaster.brocastAfterExecution(child);
            i++;
        }
    }

    protected void copy(ICodeNode root, ICodeNode child){
        root.setAttribute(ICodeKey.SYMBOL,child.getAttribute(ICodeKey.SYMBOL));
        root.setAttribute(ICodeKey.TEXT,child.getAttribute(ICodeKey.TEXT));
        root.setAttribute(ICodeKey.VALUE,child.getAttribute(ICodeKey.VALUE));
    }

    protected ICodeNode executeChild(ICodeNode root,int childIndex){
        //Collections.reverse(root.getChildren());
        root.reverseChildren();
        ICodeNode child;
        child = root.getChildren().get(childIndex);
        ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
        Executor executor =  factory.getExecutor(child);
        ICodeNode res= (ICodeNode) executor.Execute(child);
        //Collections.reverse(root.getChildren());

        return res;
    }

}
