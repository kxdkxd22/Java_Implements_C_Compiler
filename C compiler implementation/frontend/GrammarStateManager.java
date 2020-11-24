package frontend;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class GrammarStateManager {

    private ArrayList<GrammarState> stateList = new ArrayList<GrammarState>();
    private static GrammarStateManager self = null;
    private HashMap<GrammarState,HashMap<Integer, GrammarState>> transitionMap = new HashMap<GrammarState,HashMap<Integer, GrammarState>>();
    private ArrayList<GrammarState> compressedStateList = new ArrayList<GrammarState>();
    private HashMap<Integer,HashMap<Integer,Integer>> lrStateTable = new HashMap<Integer, HashMap<Integer, Integer>>();

    private boolean isTransitionTableCompressed = true;

    public static GrammarStateManager getGrammarManager(){
        if(self == null){
            self = new GrammarStateManager();
        }
        return self;
    }

    private GrammarStateManager(){

    }

    public HashMap<Integer,HashMap<Integer,Integer>> getLRStateTable(){
        Iterator it = null;
        if(isTransitionTableCompressed){
            it = compressedStateList.iterator();
        }else{
            it = stateList.iterator();
        }

        while(it.hasNext()){
            GrammarState state = (GrammarState) it.next();
            HashMap<Integer,Integer> jump = new HashMap<Integer, Integer>();
            HashMap<Integer, GrammarState> map = transitionMap.get(state);
            if(map!=null){
                for(Map.Entry<Integer, GrammarState> entry:map.entrySet()){
                    jump.put(entry.getKey(),entry.getValue().stateNum);
                }
            }

            HashMap<Integer,Integer> reduceMap = state.makeReduce();
            if(reduceMap.size() > 0){
                for(Map.Entry<Integer,Integer> item: reduceMap.entrySet()){
                    jump.put(item.getKey(),-(item.getValue()));
                }
            }

            lrStateTable.put(state.stateNum,jump);
        }
        return lrStateTable;
    }

    public void buildTransitionStateMachine(){
        ProductionManager productionManager = ProductionManager.getProductionManager();
        GrammarState state = getGrammarState(productionManager.getProduction(CTokenType.PROGRAM.ordinal()));
        state.createTransition();

       // printStateMap();
       // printReduceInfo();
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

    public GrammarState getGrammarState(Integer stateNum){
        Iterator it = null;
        if(isTransitionTableCompressed){
            it = compressedStateList.iterator();
        }else{
            it = stateList.iterator();
        }
        while(it.hasNext()){
            GrammarState state = (GrammarState) it.next();
            if(state.stateNum==stateNum){
                return state;
            }
        }

        return null;
    }

    public void addTransition(GrammarState from, GrammarState to, int on){
        if(isTransitionTableCompressed){
            from = getAndMergeSimilarStates(from);
            to = getAndMergeSimilarStates(to);
        }

        HashMap<Integer, GrammarState> map = transitionMap.get(from);
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

        if(compressedStateList.contains(returnState)==false){
            compressedStateList.add(returnState);
        }
        return returnState;
    }

    public void printStateMap(){
        System.out.println("the map size："+transitionMap.size());
        for(Map.Entry<GrammarState,HashMap<Integer, GrammarState>>entry:transitionMap.entrySet()){
            GrammarState from = entry.getKey();
            System.out.println("**********begin to print a map row*********");
            System.out.println("from state: ");
            from.print();

            HashMap<Integer, GrammarState> map = entry.getValue();
            for(Map.Entry<Integer, GrammarState>item:map.entrySet()){
                int on = item.getKey();
                System.out.println("on symbol: "+ CTokenType.getSymbolStr(on));
                System.out.println("to state: ");
                GrammarState to = item.getValue();
                to.print();
            }

            System.out.println("********end a map row*******");
        }
    }

    public void printReduceInfo(){
        System.out.println("\nShow reduce for each state: ");
        Iterator it = null;
        if(isTransitionTableCompressed){
            it = compressedStateList.iterator();
        }else{
            it = stateList.iterator();
        }

        while(it.hasNext()){
            GrammarState state = (GrammarState) it.next();
            state.print();
            HashMap<Integer,Integer> map = state.makeReduce();

            if(map.entrySet().size()==0){
                System.out.println("in this state, can not take any reduce action\n");
            }

            for(Map.Entry<Integer,Integer>entry:map.entrySet()){
                System.out.println("Reduce on symbol: "+ CTokenType.getSymbolStr(entry.getKey())+" to  Production "+entry.getValue());
            }

        }

    }

}
