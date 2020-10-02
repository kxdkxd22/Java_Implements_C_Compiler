public class FMSTable implements FMS{

    private final int ASCII_COUNT = 128;
    private final int STATE_COUNT = 6;

    private int[][] fmsTable = new int[STATE_COUNT][ASCII_COUNT];
    private boolean[] accept = new boolean[]{false,true,true,false,true,false};

    public FMSTable(){
        for(int i = 0;i < STATE_COUNT;i++){
            for(int j = 0;j < ASCII_COUNT;j++){
                fmsTable[i][j] = STATE_FAILURE;
            }
        }

        initFMSTable(0,1);
        initFMSTable(1,1);
        initFMSTable(3,2);
        initFMSTable(2,2);
        initFMSTable(5,4);
        initFMSTable(4,4);

        fmsTable[0]['.']=3;
        fmsTable[1]['.']=2;
        fmsTable[1]['e']=5;
        fmsTable[2]['e']=5;

    }

    public void initFMSTable(int state,int val){

        for(int i=0 ; i<=9 ; i++){
            fmsTable[state][i+'0']=val;
        }
    }

    @Override
    public int yy_next(int state, byte c) {
        if(state == FMS.STATE_FAILURE || c>=ASCII_COUNT){
            return FMS.STATE_FAILURE;
        }
        return fmsTable[state][c];
    }

    @Override
    public boolean isAcceptState(int state) {
        if(state!=FMS.STATE_FAILURE){
            return accept[state];
        }else{
            return false;
        }
    }

}
