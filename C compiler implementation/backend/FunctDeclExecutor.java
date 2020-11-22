package backend;
import frontend.CGrammarInitializer;
public class FunctDeclExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        switch (production){
            case CGrammarInitializer.NewName_LP_RP_TO_FunctDecl:
                root.reverseChildren();
                copy(root,root.getChildren().get(0));
                break;
        }
        return root;
    }
}
