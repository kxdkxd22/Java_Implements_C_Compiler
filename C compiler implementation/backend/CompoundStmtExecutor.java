package backend;
import frontend.CGrammarInitializer;

public class CompoundStmtExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        return executeChild(root,0);
    }
}
