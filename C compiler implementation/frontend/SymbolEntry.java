package frontend;

public class SymbolEntry {
    private Symbol symbol;
    private SymbolEntry prev,next;

    public SymbolEntry(Symbol sym){
        this.symbol = sym;
    }

    public SymbolEntry getPrev(){
        return prev;
    }

    public void setPrev(SymbolEntry prev){
        this.prev = prev;
    }

    public SymbolEntry getNext(){
        return next;
    }

    public void setNext(SymbolEntry next){
        this.next = next;
    }


}
