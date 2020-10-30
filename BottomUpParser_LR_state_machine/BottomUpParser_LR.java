public class BottomUpParser_LR {

    public static void main(String[] args) {
        ProductionManager productionManager = ProductionManager.getProductionManager();
        productionManager.initProductions();
        productionManager.printAllProductions();

        GrammarStateManager stateManager = GrammarStateManager.getGrammarManager();
        stateManager.buildTransitionStateMachine();
    }

}
