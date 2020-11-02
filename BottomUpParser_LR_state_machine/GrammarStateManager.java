import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GrammarStateManager {

    private ArrayList<GrammarState> stateList = new ArrayList<GrammarState>();
    private static GrammarStateManager self = null;
    private HashMap<GrammarState,HashMap<Integer,GrammarState>> transitionMap = new HashMap<GrammarState,HashMap<Integer, GrammarState>>();

    private boolean isTransitionTableCompressed = false;

    public static GrammarStateManager getGrammarManager(){
        if(self == null){
            self = new GrammarStateManager();
        }
        return self;
    }

    private GrammarStateManager(){

    }

    public void buildTransitionStateMachine(){
        ProductionManager productionManager = ProductionManager.getProductionManager();
        GrammarState state = getGrammarState(productionManager.getProduction(SymbolDefine.STMT));
        state.createTransition();

        printStateMap();
    }

    public GrammarState getGrammarState(ArrayList<Production> productionList){
        GrammarState state = new GrammarState(productionList);
        if(stateList.contains(state)==false){
            stateList.add(state);
            GrammarState.increateStateNum();
            return state;
        }

        for(int i = 0; i < stateList.size(); i++){
            if(stateList.get(i).equals(state)){
                state = stateList.get(i);
            }
        }
        return state;
    }

    public void addTransition(GrammarState from,GrammarState to,int on){
        if(isTransitionTableCompressed){
            from = getAndMergeSimilarStates(from);
            to = getAndMergeSimilarStates(to);
        }

        HashMap<Integer,GrammarState> map = transitionMap.get(from);
        if(map == null){
            map = new HashMap<Integer, GrammarState>();
        }

        map.put(on,to);
        transitionMap.put(from,map);

    }

    private GrammarState getAndMergeSimilarStates(GrammarState state){
        Iterator it = stateList.iterator();
        GrammarState currentState = null, returnState = state;

        while(it.hasNext()){
            currentState = (GrammarState) it.next();

            if(currentState.equals(state)==false&&currentState.checkProductionEqual(state,true)==true){
                if(currentState.stateNum<state.stateNum){
                    currentState.stateMerge(state);
                    returnState = currentState;
                }
                else{
                    state.stateMerge(currentState);
                    returnState = state;
                }
                break;
            }
        }

        return returnState;
    }

    public void printStateMap(){
        System.out.println("the map size："+transitionMap.size());
        for(Map.Entry<GrammarState,HashMap<Integer,GrammarState>>entry:transitionMap.entrySet()){
            GrammarState from = entry.getKey();
            System.out.println("**********begin to print a map row*********");
            System.out.println("from state: ");
            from.print();

            HashMap<Integer, GrammarState> map = entry.getValue();
            for(Map.Entry<Integer,GrammarState>item:map.entrySet()){
                int on = item.getKey();
                System.out.println("on symbol: "+SymbolDefine.getSymbolStr(on));
                System.out.println("to state: ");
                GrammarState to = item.getValue();
                to.print();
            }

            System.out.println("********end a map row*******");
        }
    }

}
