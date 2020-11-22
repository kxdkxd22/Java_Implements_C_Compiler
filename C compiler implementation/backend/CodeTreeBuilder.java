package backend;

import frontend.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

public class CodeTreeBuilder {
    private Stack<ICodeNode> codeNodeStack = new Stack<ICodeNode>();
    private LRStateTableParser parser = null;
    private TypeSystem typeSystem = null;
    private HashMap<String,ICodeNode> funcMap = new HashMap<String,ICodeNode>();
    private String functionName;

    private static CodeTreeBuilder treeBuilder = null;
    public static CodeTreeBuilder getCodeTreeBuilder(){
        if(treeBuilder==null){
            treeBuilder = new CodeTreeBuilder();
        }
        return treeBuilder;
    }

    public ICodeNode getFunctionNodeByName(String name){return funcMap.get(name);}

    public void setParser(LRStateTableParser parser){
        this.parser = parser;
        typeSystem = parser.getTypeSystem();

    }

    public ICodeNode buildCodeTree(int production,String text){
        ICodeNode node = null;
        Symbol symbol = null;

        switch(production){
            case CGrammarInitializer.Number_TO_Unary:
            case CGrammarInitializer.Name_TO_Unary:
            case CGrammarInitializer.String_TO_Unary:
                node = ICodeFactory.createICodeNode(CTokenType.UNARY);
                if(production == CGrammarInitializer.Name_TO_Unary){
                    symbol = typeSystem.getSymbolByText(text,parser.getCurrentLevel());
                    node.setAttribute(ICodeKey.SYMBOL,symbol);
                }
                node.setAttribute(ICodeKey.TEXT,text);
                break;
            case CGrammarInitializer.Unary_Incop_TO_Unary:
                node = ICodeFactory.createICodeNode(CTokenType.UNARY);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.Unary_TO_binary:
                node = ICodeFactory.createICodeNode(CTokenType.BINARY);
                ICodeNode child = codeNodeStack.pop();
                String t = (String) child.getAttribute(ICodeKey.TEXT);
                node.setAttribute(ICodeKey.TEXT,child.getAttribute(ICodeKey.TEXT));
                Symbol sym = (Symbol) child.getAttribute(ICodeKey.SYMBOL);
                node.addChild(child);
                break;
            case CGrammarInitializer.Binary_TO_NoCommaExpr:
            case CGrammarInitializer.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr:
                node = ICodeFactory.createICodeNode(CTokenType.NO_COMMA_EXPR);
                child = codeNodeStack.pop();
                node.addChild(child);
                if(production == CGrammarInitializer.NoCommaExpr_Equal_NoCommaExpr_TO_NoCommaExpr){
                    child = codeNodeStack.pop();
                    node.addChild(child);
                }
                break;
            case CGrammarInitializer.Binary_Plus_Binary_TO_Binary:
                node = ICodeFactory.createICodeNode(CTokenType.BINARY);

                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                break;

            case CGrammarInitializer.Binary_RelOP_Binary_TO_Binary:
                node = ICodeFactory.createICodeNode(CTokenType.BINARY);
                node.addChild(codeNodeStack.pop());
                ICodeNode operator = ICodeFactory.createICodeNode(CTokenType.RELOP);
                operator.setAttribute(ICodeKey.TEXT,parser.getRelOperationText());
                node.addChild(operator);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.NoCommaExpr_TO_Expr:
                node = ICodeFactory.createICodeNode(CTokenType.EXPR);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.Expr_Semi_TO_Statement:
            case CGrammarInitializer.CompountStmt_TO_Statement:
                node = ICodeFactory.createICodeNode(CTokenType.STATEMENT);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.LocalDefs_TO_Statement:
                node = ICodeFactory.createICodeNode(CTokenType.STATEMENT);
                break;
            case CGrammarInitializer.Statement_TO_StmtList:
                node = ICodeFactory.createICodeNode(CTokenType.STMT_LIST);
                if(codeNodeStack.size()>0){
                    node.addChild(codeNodeStack.pop());
                }
                break;
            case CGrammarInitializer.FOR_OptExpr_Test_EndOptExpr_Statement_TO_Statement:
                node = ICodeFactory.createICodeNode(CTokenType.STATEMENT);
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.StmtList_Statement_TO_StmtList:
                node = ICodeFactory.createICodeNode(CTokenType.STMT_LIST);
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.Unary_LB_Expr_RB_TO_Unary:
                node = ICodeFactory.createICodeNode(CTokenType.UNARY);
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.Expr_TO_Test:
                node = ICodeFactory.createICodeNode(CTokenType.TEST);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.If_Test_Statement_TO_IFStatement:
                node = ICodeFactory.createICodeNode(CTokenType.IF_STATEMENT);
                node.addChild(codeNodeStack.pop());//statement
                node.addChild(codeNodeStack.pop());//test
                break;
            case CGrammarInitializer.IfElseStatement_Else_Statement_TO_IfElseStatement:
                node = ICodeFactory.createICodeNode(CTokenType.IF_ELSE_STATEMENT);
                node.addChild(codeNodeStack.pop());//statement
                node.addChild(codeNodeStack.pop());//Ifstatement
                break;
            case CGrammarInitializer.Expr_Semi_TO_OptExpr:
            case CGrammarInitializer.Semi_TO_OptExpr:
                node = ICodeFactory.createICodeNode(CTokenType.OPT_EXPR);
                if(production == CGrammarInitializer.Expr_Semi_TO_OptExpr){
                    node.addChild(codeNodeStack.pop());
                }
                break;
            case CGrammarInitializer.Expr_TO_EndOpt:
                node = ICodeFactory.createICodeNode(CTokenType.END_OPT_EXPR);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.LocalDefs_StmtList_TO_CompoundStmt:
                node = ICodeFactory.createICodeNode(CTokenType.COMPOUND_STMT);
                node.addChild(codeNodeStack.pop());
                break;
            case CGrammarInitializer.NewName_LP_RP_TO_FunctDecl:
                node = ICodeFactory.createICodeNode(CTokenType.FUNCT_DECL);
                node.addChild(codeNodeStack.pop());
                child = node.getChildren().get(0);
                functionName = (String) child.getAttribute(ICodeKey.TEXT);
                break;

            case CGrammarInitializer.NewName_TO_VarDecl:
                codeNodeStack.pop();
                break;
            case CGrammarInitializer.NAME_TO_NewName:
                node = ICodeFactory.createICodeNode(CTokenType.NEW_NAME);
                node.setAttribute(ICodeKey.TEXT,text);
                break;
            case CGrammarInitializer.OptSpecifiers_FunctDecl_CompoundStmt_TO_ExtDef:
                node = ICodeFactory.createICodeNode(CTokenType.EXT_DEF);
                node.addChild(codeNodeStack.pop());
                node.addChild(codeNodeStack.pop());
                funcMap.put(functionName,node);
                break;
            case CGrammarInitializer.Unary_LP_RP_TO_Unary:
                node = ICodeFactory.createICodeNode(CTokenType.UNARY);
                node.addChild(codeNodeStack.pop());
                break;
        }

        if(node!=null){
            node.setAttribute(ICodeKey.PRODUCTION,production);
            codeNodeStack.push(node);
        }

        return node;
    }



    public ICodeNode getCodeTreeRoot(){
        ICodeNode mainNode = funcMap.get("main");
        return mainNode;
    }

}
