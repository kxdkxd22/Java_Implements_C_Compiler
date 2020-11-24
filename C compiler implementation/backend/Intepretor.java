package backend;

public class Intepretor implements Executor {
    private static Intepretor intepretor = null;

    private Intepretor(){

    }

    public static Intepretor getIntepretor(){
        if(intepretor==null){
            intepretor = new Intepretor();
        }
        return intepretor;
    }

    @Override
    public Object Execute(ICodeNode root) {
        if(root == null){
            return null;
        }
        ExecutorFactory factory = ExecutorFactory.getExecutorFactory();
        Executor executor = factory.getExecutor(root);
        executor.Execute(root);

        return root;
    }

}
