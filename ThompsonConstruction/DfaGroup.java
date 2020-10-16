import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DfaGroup {
    private static int GROUP_COUNT = 0;
    private int group_num = 0;

    List<Dfa> dfaGroup = new ArrayList<Dfa>();
    List<Dfa> tobeRemove = new ArrayList<Dfa>();

    private DfaGroup(){
        group_num = GROUP_COUNT;
    }

    public static DfaGroup createDfaGroup(){
        DfaGroup group = new DfaGroup();
        GROUP_COUNT++;
        return group;
    }

    public void tobeRemove(Dfa dfa){
        tobeRemove.add(dfa);
    }

    public void commitRemove(){
        Iterator it = tobeRemove.iterator();
        while(it.hasNext()){
            dfaGroup.remove(it.next());
        }
        tobeRemove.clear();
    }

    public void add(Dfa dfa){
        dfaGroup.add(dfa);
    }

    public boolean contains(Dfa dfa){
        return dfaGroup.contains(dfa);
    }

    public int size(){
        return dfaGroup.size();
    }

    public Dfa get(int i){
        if(i < dfaGroup.size()){
            return dfaGroup.get(i);
        }
        return null;
    }

    public void printGroup(){

        System.out.println("Dfa Group num: "+group_num+" it has dfa: ");
        Iterator it  = dfaGroup.iterator();
        while(it.hasNext()){
            Dfa dfa = (Dfa) it.next();
            System.out.print(dfa.stateNum+" ");
        }
        System.out.print("\n");

    }

    public int groupNum(){
        return group_num;
    }

}
