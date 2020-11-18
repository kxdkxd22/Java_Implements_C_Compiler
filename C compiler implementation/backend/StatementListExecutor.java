package backend;

public class StatementListExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);
        return root;
    }
}
