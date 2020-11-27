package backend;

import java.util.HashMap;
import java.util.Map;

public class MemoryHeap {
    private static int initAddr = 10000;
    private static MemoryHeap instance = null;
    private static HashMap<Integer,byte[]> memMap = new HashMap<Integer, byte[]>();

    private MemoryHeap(){

    }

    public static MemoryHeap getInstance(){
        if(instance==null){
            instance = new MemoryHeap();
        }
        return instance;
    }

    public static int allocMem(int size){
        byte[] mem = new byte[size];
        memMap.put(initAddr,mem);
        int allocAddr = initAddr;
        initAddr = initAddr+size;

        return allocAddr;
    }

    public static Map.Entry<Integer,byte[]> getMem(int addr){
        int initAddr = 0;
        for(Map.Entry<Integer,byte[]>entry:memMap.entrySet()){
            if(entry.getKey()<=addr&&entry.getKey()>initAddr){
                initAddr=entry.getKey();
                byte[] mem = entry.getValue();

                if(initAddr+mem.length>addr){
                    return entry;
                }
            }
        }

        return null;
    }

}
