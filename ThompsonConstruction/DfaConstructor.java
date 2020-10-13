import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class DfaConstructor {

    private NfaPair nfaPair = null;
    private NfaIntepretor nfaIntepretor = null;

    private static final int ASCII_COUNT = 128;
    private static final int MAX_DFA_STATE_COUNT = 254;
    private static final int STATE_FAILURE = -1;

    private ArrayList<Dfa> dfaList = new ArrayList<Dfa>();

    private int[][] dfaStateTransformTable = new int[MAX_DFA_STATE_COUNT][ASCII_COUNT+1];


    public DfaConstructor(NfaPair nfaPair,NfaIntepretor nfaIntepretor){

        this.nfaPair = nfaPair;
        this.nfaIntepretor = nfaIntepretor;
        initTransformTable();
    }

    private void initTransformTable(){
        for(int i = 0; i < MAX_DFA_STATE_COUNT; i++)
            for(int j = 0; j <= ASCII_COUNT; j++){
                dfaStateTransformTable[i][j] = STATE_FAILURE;
            }

    }

    public int[][] convertNfaToDfa(){

        Set<Nfa> input = new HashSet<Nfa>();
        input.add(nfaPair.startNode);
        Set<Nfa> set = nfaIntepretor.e_closure(input);
        Dfa start = Dfa.getDfaFromNfaSet(set);

        dfaList.add(start);

        System.out.println("Create DFA start node: ");
        printDfa(start);

        int currentIndex = 0 ;
        int nextState = STATE_FAILURE;

        while(currentIndex < dfaList.size()){
            Dfa currentDfa = dfaList.get(currentIndex);

            for(char c = 0; c <= ASCII_COUNT; c++){
                Set<Nfa> move = nfaIntepretor.move(currentDfa.nfaStates,c);

                if(move.isEmpty()){
                    nextState = STATE_FAILURE;
                }else{
                    Set<Nfa> closure = nfaIntepretor.e_closure(move);
                    Dfa dfa = isNfaStatesExistInDfa(closure);

                    if(dfa==null){

                        Dfa newDfa = Dfa.getDfaFromNfaSet(closure);
                        dfaList.add(newDfa);
                        System.out.println("Create DFA node: ");
                        printDfa(newDfa);
                        nextState = newDfa.stateNum;

                    }else{
                        System.out.println("Get a existed DFA node: ");
                        printDfa(dfa);
                        nextState = dfa.stateNum;
                    }

                }

                if(nextState!=STATE_FAILURE){
                    System.out.println("DFA from state: "+currentDfa.stateNum+"to state: "+nextState+" on: "+c);
                }

                dfaStateTransformTable[currentDfa.stateNum][c] = nextState;
            }

            currentIndex++;

        }

        return dfaStateTransformTable;
    }

    private Dfa isNfaStatesExistInDfa(Set<Nfa> closure){
        Iterator it = dfaList.iterator();
        while(it.hasNext()){
            Dfa dfa = (Dfa) it.next();
            if(dfa.hasNfaStates(closure)){
                return dfa;
            }

        }

        return null;
    }

    private void printDfa(Dfa dfa){
        System.out.print("Dfa state: "+dfa.stateNum+" its nfa states are: ");
        Iterator it = dfa.nfaStates.iterator();
        while(it.hasNext()){
            Nfa nfa = (Nfa) it.next();
            System.out.print(nfa.getStateNum());
            if(it.hasNext()){
                System.out.print(',');
            }
        }
        System.out.print('\n');
    }

    public void printDFA(){
        int dfaNum = dfaList.size();
        for(int i = 0; i < dfaNum; i++)
            for(int j = 0; j < dfaNum; j++){
                if(isOnNumberClass(i,j)){
                    System.out.println("From state "+i+" to state "+j+" on D");
                }
                if(isOnDot(i,j)){
                    System.out.println("From state "+i+" to state "+j+" on Dot");
                }
            }
    }

    private boolean isOnNumberClass(int from,int to){
        char c;
        for(c = '0'; c < '9'; c++){
            if(dfaStateTransformTable[from][c]!=to){
                return false;
            }
        }
        return true;
    }

    private boolean isOnDot(int from,int to){
        if(dfaStateTransformTable[from]['.']!=to){
            return false;
        }
        return true;
    }

}
