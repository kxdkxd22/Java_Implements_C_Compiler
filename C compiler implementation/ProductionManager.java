import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProductionManager {

    private static ProductionManager self = null;
    private HashMap<Integer, ArrayList<Production>> productionMap = new HashMap<Integer,ArrayList<Production>>();
    FirstSetBuilder firstSetBuilder = new FirstSetBuilder();

    public static ProductionManager getProductionManager(){
        if(self==null){
            self = new ProductionManager();
        }
        return self;
    }

    public void initProductions(){
        CGrammarInitializer cGrammarInitializer = CGrammarInitializer.getInstance();

        productionMap = cGrammarInitializer.getProductionMap();

    }

    public void printAllProductions(){
        for(Map.Entry<Integer,ArrayList<Production>>entry:productionMap.entrySet()){
            ArrayList<Production> list = entry.getValue();
            for(int i = 0; i < list.size(); i++){
                list.get(i).print();
                System.out.print("\n");
            }
        }
    }

    private void addProduction(Production production){
        ArrayList<Production> productionList = productionMap.get(production.getLeft());
        if(productionList==null){
            productionList = new ArrayList<Production>();
            productionMap.put(production.getLeft(),productionList);
        }

        if(productionList.contains(production)==false){
            productionList.add(production);
        }

    }

    public FirstSetBuilder getFirstSetBuilder(){return firstSetBuilder;}

    public ArrayList<Production> getProduction(int left){return productionMap.get(left);}

    private ProductionManager(){

    }

    public void runFirstSetAlgorithm(){firstSetBuilder.runFirstSets();}

    public Production getProductionByIndex(int index){
        for(Map.Entry<Integer,ArrayList<Production>>item:productionMap.entrySet()){
            ArrayList<Production> productionList = item.getValue();
            for(int i = 0; i < productionList.size(); i++){
                if(productionList.get(i).getProductionNum()==index){
                    return productionList.get(i);
                }
            }
        }

        return null;
    }

}
