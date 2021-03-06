package frontend;

import backend.ClibCall;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TypeSystem {
    private static TypeSystem typeSystem = null;

    public static TypeSystem getTypeSystem(){
        if(typeSystem==null){
            typeSystem = new TypeSystem();
        }
        return typeSystem;

    }

    private TypeSystem(){

    }

    HashMap<String, ArrayList<Symbol>> symbolTable = new HashMap<String,ArrayList<Symbol>>();
    HashMap<String, StructDefine> structTable = new HashMap<String, StructDefine>();

    public void addStructToTable(StructDefine structDefine){
        if(structTable.containsKey(structDefine.getTag())){
            System.err.println("Struct with name: "+structDefine.getTag()+"is already exist!");
            return;
        }
        structTable.put(structDefine.getTag(),structDefine);
    }

    public StructDefine getStructObjFromTable(String name){
        return structTable.get(name);
    }

    public void addSymbolsToTable(Symbol headSymbol,String scope){
        while(headSymbol!=null){
            headSymbol.addScope(scope);

            ArrayList<Symbol> symbolList = symbolTable.get(headSymbol.name);
            if(symbolList==null){
                symbolList = new ArrayList<Symbol>();
                symbolList.add(headSymbol);
                symbolTable.put(headSymbol.name,symbolList);
            }else{
                handleDuplicateSymbol(headSymbol,symbolList);
            }
            headSymbol = headSymbol.getNextSymbol();
        }
    }

    public ArrayList<Symbol> getSymbol(String text){
        return symbolTable.get(text);
    }

    private void handleDuplicateSymbol(Symbol symbol, ArrayList<Symbol> symList){
        boolean harmless = true;
        Iterator it = symList.iterator();
        while(it.hasNext()){
            Symbol sym = (Symbol) it.next();
            if(sym.equals(symbol)==true){
                System.err.println("Symbol definition replicate: "+sym.name);
                System.exit(1);
            }
        }

        if(harmless==true){
            symList.add(symbol);
        }


    }

    public ArrayList<Symbol> getSymbolsByScope(String scope){
        ArrayList<Symbol> list = new ArrayList<Symbol>();
        for(Map.Entry<String,ArrayList<Symbol>>entry:symbolTable.entrySet()){
            ArrayList<Symbol> args = entry.getValue();
            for(int i = 0; i<args.size();i++){
                Symbol sym = args.get(i);
                if(sym.getScope().equals(scope)){
                    list.add(sym);
                }
            }
        }
        return list;
    }

    public TypeLink newType(String typeText){
        Specifier sp = null;
        int type = Specifier.CHAR;
        boolean isLong = false,isSigned = true;
        switch (typeText.charAt(0)){
            case 'c':
                if (typeText.charAt(1)=='h'){
                    type = Specifier.CHAR;
                }
                break;
            case 'd':
            case 'f':
                System.err.println("No floating point support");
                System.exit(1);
                break;
            case 'i':
                type = Specifier.INT;
                break;
            case 'l':
                isLong = true;
                break;
            case 'u':
                isSigned = false;
                break;
            case 'v':
                if(typeText.charAt(1)=='o'){
                    type = Specifier.VOID;
                }
                break;
            case 's':

                break;

        }

        sp = new Specifier();
        sp.setType(type);
        sp.setLong(isLong);
        sp.setSigned(isSigned);
        TypeLink link = new TypeLink(false,false,sp);

        return link;
    }

    public void specifierCpy(Specifier dst, Specifier org){
        dst.setSigned(org.isSigned());
        dst.setLong(org.getLong());
        dst.setConstantValue(org.getConstantValue());
        dst.setOutputClass(org.getOutputClass());
        dst.setStorageClass(org.getStorageClass());
        dst.setStatic(org.isStatic());
        dst.setExternal(org.isExternal());
    }

    public TypeLink newClass(String classText){
        Specifier sp = new Specifier();
        sp.setType(Specifier.NONE);
        setClassType(sp,classText.charAt(0));

        TypeLink link = new TypeLink(false,false,sp);
        return link;
    }

    private void setClassType(Specifier sp, char c){
        switch (c){
            case 0:
                sp.setStorageClass(Specifier.FIXED);
                sp.setExternal(false);
                sp.setStatic(false);
                break;
            case 't':
                sp.setStorageClass(Specifier.TYPEDEF);
                break;
            case 'r':
                sp.setStorageClass(Specifier.REGISTER);
                break;
            case 's':
                sp.setStatic(true);
                break;
            case 'e':
                sp.setExternal(true);
                break;
            default:
                System.err.println("Internal error, Invalid Class type");
                System.exit(1);
                break;

        }

    }

    public Symbol newSymbol(String name, int level){return new Symbol(name,level);}

    public Declarator addDeclarator(Symbol symbol, int declaratroType){
        Declarator declarator = new Declarator(declaratroType);
        TypeLink link = new TypeLink(true,false,declarator);
        symbol.addDeclarator(link);
        return declarator;
    }

    public void addSpecifierToDeclarator(TypeLink specifier, Symbol symbol){
        while(symbol!=null){
            symbol.addSpecifier(specifier);
            symbol=symbol.getNextSymbol();
        }
    }

  public Symbol getSymbolByText(String text, int level, String scope) {
      ClibCall libCall = ClibCall.getInstance();
      if (libCall.isAPICall(text)) {
          return null;
      }

      if (scope.equals(text)) {
          scope = LRStateTableParser.GLOBAL_SCOPE;
      }

      ArrayList<Symbol> symbolList = typeSystem.getSymbol(text);
      int i = 0;
      Symbol symbol = null;

      while (i < symbolList.size()) {

          if (symbol == null && symbolList.get(i).getScope().equals(LRStateTableParser.GLOBAL_SCOPE)) {
              symbol = symbolList.get(i);
          }

          if (symbolList.get(i).getScope().equals(scope)) {
              symbol = symbolList.get(i);
          }

          if (symbol != null && symbolList.get(i).getLevel() >= level) {
              symbol = symbolList.get(i);
          }

          i++;
      }

      return symbol;
  }

}
