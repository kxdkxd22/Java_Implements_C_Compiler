package backend;

import frontend.Symbol;

import java.util.Map;

public class PointerValueSetter implements IValueSetter{
    private Symbol symbol;
    private int index = 0;
    @Override
    public void setValue(Object object) throws Exception {
        int addr = (Integer)symbol.getValue();
        MemoryHeap memoryHeap = MemoryHeap.getInstance();
        Map.Entry<Integer,byte[]> entry = memoryHeap.getMem(addr);
        byte[] content = entry.getValue();
        int i = (Integer) object;

        if(symbol.getByteSize()==4){
            content[index] = (byte) ((i>>24)&(0xff));
            content[index+1] = (byte) ((i>>16)&(0xff));
            content[index+2] = (byte) ((i>>8)&(0xff));
            content[index+3] = (byte) ((i)&(0xff));
        }else{
            content[index] = (byte) (i&(0xff));
        }

    }

    public PointerValueSetter(Symbol symbol,int index){
        this.symbol = symbol;
        this.index = index;
    }
}
