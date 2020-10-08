import java.util.Stack;

public class NfaManager {

    private Stack<Nfa> nfaStack;
    private Nfa[] nfaArray;
    private int nfaStates = 0;
    private int nfaAlloc = 0;
    private final int NFA_MAX = 256;

    public NfaManager() throws Exception {
        nfaArray = new Nfa[NFA_MAX];
        nfaStack = new Stack<Nfa>();
        for(int i = 0; i < NFA_MAX; i++){
            nfaArray[i] = new Nfa();
        }
        if(nfaStack == null || nfaArray == null){
            ErrorHandler.parseErr(ErrorHandler.Error.E_MEM);
        }

    }

    public Nfa newNfa() throws Exception {

        if(++nfaStates >= NFA_MAX){
            ErrorHandler.parseErr(ErrorHandler.Error.E_LENGTH);
        }

        Nfa nfa = null;

        if(nfaStack.isEmpty() == false){
            nfa = nfaStack.pop();
        }else{
            nfa = nfaArray[nfaAlloc++];
        }

        nfa.clearState();
        nfa.setStateNum(nfaStates);
        nfa.setEdge(Nfa.EPSILON);
        return nfa;

    }

    public void discardNfa(Nfa nfaDiscard){
        --nfaStates;
        nfaDiscard.clearState();
        nfaStack.push(nfaDiscard);

    }

}
