package backend;

import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;

public class IfStatementExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        ProgramGenerator generator = ProgramGenerator.getInstance();
        ICodeNode res = executeChild(root,0);
        Integer val = (Integer) res.getAttribute(ICodeKey.VALUE);
        copy(root,res);
        if(val!=null&&val!=0||BaseExecutor.isCompileMode){
            generator.increaseIfElseEmbed();
            boolean b = BaseExecutor.inIfElseStatement;
            BaseExecutor.inIfElseStatement = false;
            executeChild(root,1);
            BaseExecutor.inIfElseStatement = b;
            generator.decreaseIfElseEmbed();
        }

        if(BaseExecutor.inIfElseStatement==true){
            String branchOut = generator.getBranchOut();
            generator.emitString(Instruction.GOTO.toString()+" "+branchOut);
        }else{
            String curBranch = generator.getCurrentBranch();
            generator.emitString(curBranch+":\n");
        }

        return root;
    }
}
