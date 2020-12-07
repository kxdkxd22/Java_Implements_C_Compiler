package frontend;

import backend.BaseExecutor;
import backend.CodeTreeBuilder;
import backend.Compiler.ProgramGenerator;
import backend.Intepretor;
import com.sun.org.apache.bcel.internal.classfile.Code;

public class BottomUpParser_LR {

    public static void main(String[] args) {
        BaseExecutor.isCompileMode = true;
        ProductionManager productionManager = ProductionManager.getProductionManager();
        productionManager.initProductions();
       // productionManager.printAllProductions();
        productionManager.runFirstSetAlgorithm();

        GrammarStateManager stateManager = GrammarStateManager.getGrammarManager();
        stateManager.buildTransitionStateMachine();

        Lexer lexer = new Lexer();
        LRStateTableParser parser = new LRStateTableParser(lexer);
        parser.parser();

        ProgramGenerator generator = ProgramGenerator.getInstance();
        generator.generate();
    //

        CodeTreeBuilder treeBuilder = CodeTreeBuilder.getCodeTreeBuilder();
        Intepretor intepretor = Intepretor.getIntepretor();
        intepretor.Execute(treeBuilder.getCodeTreeRoot());

        generator.finish();
    }

}
