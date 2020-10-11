import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.Stack;

public class NfaIntepretor {

    private Input input;
    private Nfa start;

    public NfaIntepretor(Input input,Nfa start){
        this.input = input;
        this.start = start;
    }

    private Set<Nfa> e_closure(Set<Nfa> input){

        if(input == null||input.isEmpty()){
            return null;
        }

        System.out.print("ε-Closure( "+strFromNfaSet(input)+" ) = ");

        Stack<Nfa> nfaStack = new Stack<Nfa>();
        Iterator it = input.iterator();
        while(it.hasNext()){
            nfaStack.push((Nfa) it.next());
        }

        while(nfaStack.isEmpty()==false){
            Nfa nfa = nfaStack.pop();
            if(nfa.next!=null&&nfa.getEdge()==Nfa.EPSILON){
                if(input.contains(nfa.next)==false){
                    input.add(nfa.next);
                    nfaStack.push(nfa.next);
                }
            }

            if(nfa.next2!=null&&nfa.getEdge()==Nfa.EPSILON){
                if(input.contains(nfa.next2)==false){
                    input.add(nfa.next2);
                    nfaStack.push(nfa.next2);
                }
            }
        }

        if(input!=null){
            System.out.println("{"+strFromNfaSet(input)+"}");
        }

        return input;
    }

    private Set<Nfa> move(Set<Nfa> input,char c){

        Set<Nfa> out = new HashSet<Nfa>();
        Iterator it = input.iterator();

        while(it.hasNext()){
            Nfa nfa = (Nfa) it.next();
            Byte cb = (byte)c;
            if(nfa.getEdge()==Nfa.CCL){
                if(nfa.inputSet.contains(cb)){
                    out.add(nfa.next);
                }
            }
            else if(nfa.getEdge()==c){
                out.add(nfa.next);
            }

        }

        if(out.isEmpty()==false){
            System.out.print("move({"+strFromNfaSet(input)+"}, '"+c+"')=");
            System.out.println("{"+strFromNfaSet(out)+"}");
        }
        return out;
    }


    private String strFromNfaSet(Set<Nfa> input){
        String cur = "";
        Iterator it = input.iterator();
        while(it.hasNext()){
            Nfa nfa = (Nfa) it.next();
            cur+=nfa.getStateNum();
            if(it.hasNext()){
                cur+=",";
            }
        }

        return cur;
    }

    public void IntepretorNfa(){

        System.out.println("Input String: ");
        input.ii_newfile(null);
        input.ii_advance();
        input.ii_pushback(1);

        Set<Nfa> current = null;
        Set<Nfa> next = new HashSet<Nfa>();

        next.add(start);
        next = e_closure(next);
        String inputStr = "";
        char c;
        boolean lastAccepted = false;

        while((c= (char) input.ii_advance())!=Input.EOF){

            current = move(next,c);
            next = e_closure(current);

            if(next!=null){
                if(hasAcceptedStates(next)){
                    lastAccepted = true;
                }
            }else{
                break;
            }

            inputStr+=c;
        }

        if(lastAccepted){
            System.out.println("The NFA Machine can recognize string: "+inputStr);
        }

    }

    private boolean hasAcceptedStates(Set<Nfa> input){
        boolean isAccepted = false;
        if(input == null || input.isEmpty()){
            return false;
        }

        Iterator it = input.iterator();

        while(it.hasNext()){
            Nfa nfa = (Nfa) it.next();
            if(nfa.next == null && nfa.next2 == null){

            }


        }


        return isAccepted;
    }

}
