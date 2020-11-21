package backend;

public class ElseStatementExecutor extends BaseExecutor{

    @Override
    public Object Execute(ICodeNode root) {

        ICodeNode res = executeChild(root,0);
        Integer val = (Integer) res.getAttribute(ICodeKey.VALUE);
        if(val==0){
            res = executeChild(root,1);
        }
        copy(root,res);
        return root;
    }
}
