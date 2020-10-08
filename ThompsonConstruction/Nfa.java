import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Nfa {

    public enum ANCHOR{
        NONE,
        START,
        END,
        BOTH
    }

    public static final int EPSILON = -1;
    public static final int CCL = -2;
    public static final int EMPTY = -3;
    public static final int ASCII_COUNT = 127;

    public int edge;
    public Set<Byte> inputSet;
    public int stateNum;
    private ANCHOR anchor;
    public Nfa next;
    public Nfa next2;
    private boolean visited = false;

    public void setEdge(int type){
        this.edge = type;
    }

    public int getEdge(){
        return edge;
    }

    public void setVisited(){visited = true;}
    public boolean isVisited(){return visited;}

    public Nfa(){
        inputSet = new HashSet<Byte>();
        clearState();
    }

    public void addToSet(Byte b){
        inputSet.add(b);
    }

    public void setComplement(){
        Set<Byte> newSet = new HashSet<Byte>();
        for(Byte i = 0; i < ASCII_COUNT; i++){
            if(inputSet.contains(i)==false){
                newSet.add(i);
            }
        }
        inputSet = null;
        inputSet = newSet;
    }

    public void setStateNum(int num){
        this.stateNum = num;
    }

    public int getStateNum(){return stateNum;}

    public void setAnchor(ANCHOR anchor) {
        this.anchor = anchor;
    }
    public Nfa.ANCHOR getAnchor(){
        return anchor;
    }

    public void clearState(){
        next=next2=null;
        stateNum = -1;
        inputSet.clear();
        anchor = ANCHOR.NONE;
    }

    public void cloneNfa(Nfa nfa){
        this.stateNum = nfa.stateNum;
        this.inputSet.clear();
        Iterator<Byte> iter = nfa.inputSet.iterator();
        while(iter.hasNext()){
            this.inputSet.add(iter.next());
        }

        this.next = nfa.next;
        this.next2 = nfa.next2;
        this.anchor = nfa.anchor;
        this.edge = nfa.edge;
    }

}
