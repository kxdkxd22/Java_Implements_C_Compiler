package backend;

import backend.Compiler.Instruction;
import backend.Compiler.ProgramGenerator;
import frontend.CGrammarInitializer;

public class BinaryExecutor extends BaseExecutor {
    @Override
    public Object Execute(ICodeNode root) {
        executeChildren(root);
        ICodeNode child;
        int production = (int) root.getAttribute(ICodeKey.PRODUCTION);
        switch(production){
            case CGrammarInitializer.Unary_TO_binary:
                child = root.getChildren().get(0);
                copy(root,child);
                break;

            case CGrammarInitializer.Binary_Plus_Binary_TO_Binary:
            case CGrammarInitializer.Binary_DivOp_Binary_TO_Binary:
            case CGrammarInitializer.Binary_Minus_Binary_TO_Binary:
            case CGrammarInitializer.Binary_Start_Binary_TO_Binary:

                int val1 = (Integer)root.getChildren().get(0).getAttribute(ICodeKey.VALUE);
                int val2 = (Integer)root.getChildren().get(1).getAttribute(ICodeKey.VALUE);
                if(production==CGrammarInitializer.Binary_Plus_Binary_TO_Binary){
                    String text = root.getChildren().get(0).getAttribute(ICodeKey.TEXT)+" plus "+root.getChildren().get(1).getAttribute(ICodeKey.TEXT);
                    root.setAttribute(ICodeKey.VALUE,val1+val2);
                    root.setAttribute(ICodeKey.TEXT,text);
                    System.out.println(text+" is "+(val1+val2));
                    ProgramGenerator.getInstance().emit(Instruction.IADD);
                }else if(production == CGrammarInitializer.Binary_Minus_Binary_TO_Binary){
                    String text = root.getChildren().get(0).getAttribute(ICodeKey.TEXT)+" minus "+root.getChildren().get(1).getAttribute(ICodeKey.TEXT);
                    root.setAttribute(ICodeKey.VALUE,val1-val2);
                    root.setAttribute(ICodeKey.TEXT,text);
                    System.out.println(text+" is "+(val1-val2));
                    ProgramGenerator.getInstance().emit(Instruction.ISUB);
                }else if(production == CGrammarInitializer.Binary_Start_Binary_TO_Binary){
                    String text = root.getChildren().get(0).getAttribute(ICodeKey.TEXT)+" minus "+root.getChildren().get(1).getAttribute(ICodeKey.TEXT);
                    root.setAttribute(ICodeKey.VALUE,val1*val2);
                    root.setAttribute(ICodeKey.TEXT,text);
                    System.out.println(text+" is "+(val1*val2));
                    ProgramGenerator.getInstance().emit(Instruction.IMUL);
                }else{
                    root.setAttribute(ICodeKey.VALUE,val1/val2);
                    System.out.println(root.getChildren().get(0).getAttribute(ICodeKey.TEXT)+" is divided by "+
                            root.getChildren().get(1).getAttribute(ICodeKey.TEXT)+" and result is "+(val1/val2));
                    ProgramGenerator.getInstance().emit(Instruction.IDIV);
                }

                break;
            case CGrammarInitializer.Binary_RelOP_Binary_TO_Binary:
                val1 = (int) root.getChildren().get(0).getAttribute(ICodeKey.VALUE);
                String operator = (String) root.getChildren().get(1).getAttribute(ICodeKey.TEXT);
                val2 = (int) root.getChildren().get(2).getAttribute(ICodeKey.VALUE);
                switch (operator){
                    case "==":
                        root.setAttribute(ICodeKey.VALUE,val1==val2?1:0);
                        break;
                    case ">":
                        root.setAttribute(ICodeKey.VALUE,val1>val2?1:0);
                        break;
                    case "<":
                        root.setAttribute(ICodeKey.VALUE,val1<val2?1:0);
                        break;
                    case ">=":
                        root.setAttribute(ICodeKey.VALUE,val1>=val2?1:0);
                        break;
                    case "<=":
                        root.setAttribute(ICodeKey.VALUE,val1<=val2?1:0);
                        break;
                    case "!=":
                        root.setAttribute(ICodeKey.VALUE,val1!=val2?1:0);
                        break;
                }
                break;
        }

        return root;
    }

}
