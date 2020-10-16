import java.util.Iterator;
import java.util.List;

public class MinimizeDFA {

    private DfaConstructor dfaConstructor = null;
    private DfaGroupManager groupManager = new DfaGroupManager();
    private static final int ASCII_NUM = 128;
    private int[][] dfaTransTable = null;
    int[][] minDfa = null;
    private List<Dfa> dfaList = null;
    private DfaGroup newGroup = null;

    private boolean addNewGroup = false;
    private static final int STATE_FAILURE = -1;

    public MinimizeDFA(DfaConstructor theConstructor){
        this.dfaConstructor = theConstructor;
        dfaTransTable = theConstructor.getDfaStateTransformTable();
        dfaList = theConstructor.getDfaList();
    }

    public int[][] minimize(){

        addNoAcceptingDfaToGroup();
        addAcceptingDfaToGroup();

        do{
            addNewGroup = false;
            doGroupSeperationOnNumber();
            doGroupSeperationOnCharacter();
        }while(addNewGroup);

        createMiniDfaTransTable();
        printMiniDfaTable();

        return minDfa;
    }

    public void doGroupSeperationOnNumber(){
        for(int i = 0; i < groupManager.size(); i++){
            int dfaCount = 1;
            newGroup = null;
            DfaGroup dfaGroup = groupManager.get(i);

            System.out.println("Handle seperation on group: ");
            dfaGroup.printGroup();

            Dfa first = dfaGroup.get(0);
            Dfa next = dfaGroup.get(dfaCount);

            while(next!=null){
                for(char c = '0'; c <= '9';c++){
                    if(doGroupSeperationOnInput(dfaGroup,first,next,c)==true){
                        addNewGroup = true;
                        break;
                    }
                }

                dfaCount++;
                next = dfaGroup.get(dfaCount);
            }
            dfaGroup.commitRemove();
        }
    }

    public void doGroupSeperationOnCharacter(){
        for(int i = 0; i < groupManager.size(); i++){
            int dfaCount = 1;
            newGroup = null;
            DfaGroup dfaGroup = groupManager.get(i);

            System.out.println("Handle seperation on group: ");
            dfaGroup.printGroup();

            Dfa first = dfaGroup.get(0);
            Dfa next = dfaGroup.get(dfaCount);

            while(next!=null){
                for(char c = 0; c <= ASCII_NUM;c++){
                    if(doGroupSeperationOnInput(dfaGroup,first,next,c)==true){
                        addNewGroup = true;
                        break;
                    }
                }

                dfaCount++;
                next = dfaGroup.get(dfaCount);
            }
            dfaGroup.commitRemove();
        }
    }

    public boolean doGroupSeperationOnInput(DfaGroup group,Dfa first,Dfa next,char c){
        int first_go = dfaTransTable[first.stateNum][c];
        int next_go = dfaTransTable[next.stateNum][c];

        if(groupManager.getContainingGroup(first_go)!=groupManager.getContainingGroup(next_go)){
            if(newGroup==null){
                newGroup = groupManager.createNewGroup();
            }

            group.tobeRemove(next);
            newGroup.add(next);

            System.out.println("Dfa:"+first.stateNum+" and Dfa: "+next.stateNum+" jump to different group on input char "+c);
            System.out.println("remove Dfa:"+next.stateNum+" from group:"+group.groupNum()+" and add it to group:"+newGroup.groupNum());
            return true;
        }

        return false;
    }

    public void createMiniDfaTransTable(){
        initMiniDfaTransTable();
        Iterator it = dfaList.iterator();

        while(it.hasNext()){
            int from = ((Dfa) it.next()).stateNum;
            for(char c = 0; c < ASCII_NUM; c++){
                int to = dfaTransTable[from][c];
                if(to!=STATE_FAILURE){
                    DfaGroup fromGroup = groupManager.getContainingGroup(from);
                    DfaGroup toGroup = groupManager.getContainingGroup(to);
                    minDfa[fromGroup.groupNum()][c] = toGroup.groupNum();
                }

            }
        }


    }

    public void initMiniDfaTransTable(){
        minDfa = new int[groupManager.size()][ASCII_NUM];
        for(int i = 0; i < groupManager.size(); i++)
            for(int j = 0; j < ASCII_NUM; j++){
                minDfa[i][j] = STATE_FAILURE;
            }
    }

    public void printMiniDfaTable(){
        for(int i = 0; i < groupManager.size(); i++)
            for(int j = 0; j < groupManager.size(); j++)
            {
                if(isOnNumberClass(i,j)){
                    System.out.println(" from "+i+" to "+j+" on D");
                }
                if(isOnDot(i,j)){
                    System.out.println(" from "+i+" to "+j+" on dot");
                }
            }

    }

    private boolean isOnNumberClass(int from,int to){

        for(char c ='0'; c <= '9'; c++){
            if(minDfa[from][c]!=to){
                return false;
            }
        }
        return true;
    }

    private boolean isOnDot(int from,int to){
        if(minDfa[from]['.']!=to){
            return false;
        }
        return true;
    }

    public void addNoAcceptingDfaToGroup(){
        Iterator it = dfaList.iterator();
        DfaGroup group = groupManager.createNewGroup();

        while(it.hasNext()){
            Dfa dfa = (Dfa) it.next();
            if(dfa.accepted == false){
                group.add(dfa);
            }
        }

        group.printGroup();
    }

    public void addAcceptingDfaToGroup(){
        Iterator it = dfaList.iterator();
        DfaGroup group = groupManager.createNewGroup();

        while(it.hasNext()){
            Dfa dfa = (Dfa) it.next();
            if(dfa.accepted == true){
                group.add(dfa);
            }
        }

        group.printGroup();
    }


}
