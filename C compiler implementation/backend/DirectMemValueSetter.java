package backend;

import frontend.Symbol;

import java.util.Map;

public class DirectMemValueSetter implements IValueSetter {
    private int memAddr = 0;
    public DirectMemValueSetter(int memAddr){
        this.memAddr = memAddr;
    }
    @Override
    public void setValue(Object object) throws Exception {
        MemoryHeap memoryHeap = MemoryHeap.getInstance();
        Map.Entry<Integer,byte[]> entry = memoryHeap.getMem(memAddr);
        byte[] content = entry.getValue();
        int offset = memAddr-entry.getKey();
        int i = (Integer) object;
        content[offset] = (byte) (i&(0xff));
    }

    @Override
    public Symbol getSymbol() {
        return null;
    }
}
