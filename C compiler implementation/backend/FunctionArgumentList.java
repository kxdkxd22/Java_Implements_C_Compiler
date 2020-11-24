package backend;

import java.util.ArrayList;

public class FunctionArgumentList{
    private static FunctionArgumentList argumentList = null;
    private ArrayList<Object> funcArgList = new ArrayList<Object>();

    public static FunctionArgumentList getFunctionArgumentList(){
        if(argumentList==null){
            argumentList = new FunctionArgumentList();
        }
        return argumentList;
    }

    private FunctionArgumentList(){}

    public void setFuncArgList(ArrayList<Object> list){
        funcArgList = list;
    }

    public ArrayList<Object> getFuncArgList(){
        return funcArgList;
    }

}
