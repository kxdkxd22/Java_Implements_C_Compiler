import java.util.ArrayList;

public class Production {

    private int dotPos = 0;
    private boolean printDot = false;
    private int left = 0;
    private ArrayList<Integer> right = null;
    ArrayList<Integer> lookAhead = new ArrayList<Integer>();

    public Production(int left,int dot,ArrayList<Integer> right){

        this.left = left;
        this.right = right;

        if(dot>=right.size()){
            dot = right.size();
        }

        lookAhead.add(SymbolDefine.EOI);

        this.dotPos = dot;
    }

    public Production dotForward(){

        Production product =  new Production(left,dotPos+1,right);
        product.lookAhead = new ArrayList<Integer>();

        for(int i = 0; i < this.lookAhead.size(); i++){
            product.lookAhead.add(this.lookAhead.get(i));
        }

        return product;
    }

    public Production cloneSelf(){
        Production product = new Production(this.left,dotPos,this.right);

        product.lookAhead = new ArrayList<Integer>();
        for(int i = 0; i < lookAhead.size(); i++){
            product.lookAhead.add(this.lookAhead.get(i));
        }

        return product;
    }

    public ArrayList<Integer> computeFirstSetOfBetaAndC(){
        ArrayList<Integer> set = new ArrayList<Integer>();
        for(int i = dotPos+1; i < right.size(); i++){
            set.add(right.get(i));
        }
        set.addAll(lookAhead);

        ProductionManager productionManager = ProductionManager.getProductionManager();
        ArrayList<Integer> firstSet = new ArrayList<Integer>();

        for(int i = 0; i < set.size(); i++){
            ArrayList<Integer> lookAhead =  productionManager.getFirstSetBuilder().getFirstSet(set.get(i));

            for(int j = 0; j < lookAhead.size();j++){
                if(firstSet.contains(lookAhead.get(j))==false){
                    firstSet.add(lookAhead.get(j));
                }
            }

            if(productionManager.getFirstSetBuilder().isSymbolNullable(set.get(i))==false){
                break;
            }
        }

        return firstSet;
    }

    public int getLeft() {
        return left;
    }

    public ArrayList<Integer> getRight(){return right;}

    public int getDotPosition(){return dotPos;}

    public int getDotSymbol(){
        if(dotPos>=right.size()){
            return SymbolDefine.UNKNOWN_SYMBOL;
        }
        return right.get(dotPos);
    }

    public boolean equals(Object obj){
        Production product = (Production) obj;
        if(this.productionEquals(product)&&this.lookAheadSetComparing(product)==0){
            return true;
        }
        return false;
    }

    public boolean productionEquals(Object obj){
        Production product = (Production) obj;
        if(this.left!=product.getLeft()){
            return false;
        }

        if(this.dotPos!=product.getDotPosition()){
            return false;
        }

        if(this.right.equals(product.getRight())==false){
            return false;
        }

        return true;
    }

    public boolean coverUp(Production product){
        if(this.productionEquals(product) && this.lookAheadSetComparing(product)==1){
            return true;
        }
        return false;
    }

    private int lookAheadSetComparing(Production product){
        if(this.lookAhead.size()>product.lookAhead.size()){
            return 1;
        }

        if(this.lookAhead.size()<product.lookAhead.size()){
            return -1;
        }

        if(this.lookAhead.size()==product.lookAhead.size()){
            for(int i = 0; i < this.lookAhead.size(); i++){
                if(this.lookAhead.get(i)!=product.lookAhead.get(i)){
                    return -1;
                }
            }
        }

        return 0;

    }

    public void addLookAheadSet(ArrayList<Integer> list){
       /* for(int i = 0; i < list.size(); i++){
            if(lookAhead.contains(list.get(i))==false){
                lookAhead.add(list.get(i));
            }
        }*/
       lookAhead = list;
    }

    public void print(){
        System.out.print(SymbolDefine.getSymbolStr(left)+" -> ");
        for(int i = 0; i < right.size(); i++){
            if(i == dotPos){
                printDot = true;
                System.out.print(".");
            }
            System.out.print(SymbolDefine.getSymbolStr(right.get(i)) + " ");
        }

        if(!printDot){
            System.out.print(".");
        }

        System.out.print("look ahead set: { ");
        for(int i = 0; i < lookAhead.size(); i++){
            System.out.print(SymbolDefine.getSymbolStr(lookAhead.get(i))+" ");
        }
        System.out.println("}");

    }

}
