import java.util.ArrayList;
import java.util.HashMap;

public class TypeSystem {
    HashMap<String, ArrayList<Symbol>> symbolTable = new HashMap<String,ArrayList<Symbol>>();
    public void addSymbolsToTable(Symbol headSymbol){
        while(headSymbol!=null){
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

    private void handleDuplicateSymbol(Symbol symbol,ArrayList<Symbol> symList){

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
                if(typeText.charAt(1)=='h'){
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

    public void specifierCpy(Specifier dst,Specifier org){
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

    public Symbol newSymbol(String name,int level){return new Symbol(name,level);}

    public void addDeclarator(Symbol symbol,int declaratroType){
        Declarator declarator = new Declarator(declaratroType);
        TypeLink link = new TypeLink(true,false,declarator);
        symbol.addDeclarator(link);
    }

    public void addSpecifierToDeclarator(TypeLink specifier,Symbol symbol){
        while(symbol!=null){
            symbol.addSpecifier(specifier);
            symbol=symbol.getNextSymbol();
        }
    }

}
