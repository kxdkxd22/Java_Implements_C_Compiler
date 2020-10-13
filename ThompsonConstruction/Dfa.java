import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Dfa {

    private static int STATE_NUM = 0;

    int stateNum = 0;
    Set<Nfa> nfaStates = new HashSet<Nfa>();
    boolean accepted = false;

    public static Dfa getDfaFromNfaSet(Set<Nfa> input){
        Iterator it = input.iterator();
        Dfa dfa = new Dfa();

        while(it.hasNext()){
            Nfa nfa = (Nfa) it.next();
            dfa.nfaStates.add(nfa);
            if(nfa.next2==null&&nfa.next==null){
                dfa.accepted = true;
            }
        }

        dfa.stateNum = STATE_NUM++;
        return dfa;
    }

    public boolean hasNfaStates(Set<Nfa> input){
        if(this.nfaStates.equals(input)){
            return true;
        }
        return false;
    }

}
