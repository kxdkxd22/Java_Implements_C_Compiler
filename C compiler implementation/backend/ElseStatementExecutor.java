package backend;

import backend.Compiler.ProgramGenerator;

public class ElseStatementExecutor extends BaseExecutor{
    private ProgramGenerator generator = ProgramGenerator.getInstance();
    @Override
    public Object Execute(ICodeNode root) {
        BaseExecutor.inIfElseStatement = true;

        ICodeNode res = executeChild(root,0);
        BaseExecutor.inIfElseStatement = false;
        String branch = generator.getCurrentBranch();
        branch = "\n"+branch+":\n";
        generator.emitString(branch);
        if(generator.getIfElseEmbedCount()==0){
            generator.increaseBranch();
        }

        Object obj = res.getAttribute(ICodeKey.VALUE);
        if((Integer)obj == 0 || BaseExecutor.isCompileMode){
            generator.increaseIfElseEmbed();
            res = executeChild(root,1);
            generator.decreaseIfElseEmbed();
        }

        copy(root,res);

        generator.emitBranchOut();

        return root;
    }
}
