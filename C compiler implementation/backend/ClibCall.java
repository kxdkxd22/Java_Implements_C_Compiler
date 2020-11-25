package backend;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class ClibCall {
    private Set<String> apiSet;
    private ClibCall(){
        apiSet = new HashSet<String>();
        apiSet.add("printf");
    }

    private static ClibCall instance = null;

    public static ClibCall getInstance(){
        if(instance==null){
            instance = new ClibCall();
        }
        return instance;
    }

    public boolean isAPICall(String funcName){
        return apiSet.contains(funcName);
    }

    public Object invokeAPI(String funcName){
        switch (funcName){
            case "printf":
                return handlePrintCall();
            default:
                return null;
        }
    }

    private Object handlePrintCall(){
        ArrayList<Object> argsList = FunctionArgumentList.getFunctionArgumentList().getFuncArgList(false);
        String argStr = (String) argsList.get(0);
        String formatStr = "";

        int i = 0;
        int argCount = 1;
        while (i<argStr.length()){
            if(argStr.charAt(i)=='%'&&argStr.charAt(i+1)=='d'&&i+1<argStr.length()){
                formatStr+=argsList.get(argCount);
                i+=2;
                argCount++;
            }else{
                formatStr+=argStr.charAt(i);
                i++;
            }
        }

        System.out.println(formatStr);
        return null;
    }

}
