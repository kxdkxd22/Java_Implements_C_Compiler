package backend;

public class InitializerExecutor extends BaseExecutor {

    @Override
    public Object Execute(ICodeNode root) {

        executeChild(root,0);
        copy(root,root.getChildren().get(0));
        return root;
    }
}
