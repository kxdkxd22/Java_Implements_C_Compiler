import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DfaGroupManager {

    private List<DfaGroup> groupList = new ArrayList<DfaGroup>();

    public DfaGroupManager(){

    }

    public DfaGroup createNewGroup(){
        DfaGroup group = DfaGroup.createDfaGroup();
        groupList.add(group);
        return group;
    }

    public DfaGroup getContainingGroup(int dfaStateNum){
        Iterator it = groupList.iterator();
        while(it.hasNext()){
            DfaGroup group = (DfaGroup) it.next();
            if(groupContainsDfa(group,dfaStateNum)){
                return group;
            }
        }
        return null;
    }

    public boolean groupContainsDfa(DfaGroup group,int dfaStateNum){

        for(int i = 0; i < group.size(); i++){
            Dfa dfa = group.get(i);
            if(dfa.stateNum == dfaStateNum){
                return true;
            }
        }
        return false;
    }

    public int size(){
        return groupList.size();
    }

    public DfaGroup get(int i){
        return groupList.get(i);
    }

}
