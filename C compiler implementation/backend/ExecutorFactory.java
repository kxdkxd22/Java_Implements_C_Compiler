package backend;

import frontend.CTokenType;

public class ExecutorFactory {
     private static ExecutorFactory factory = null;
     private ExecutorFactory(){

     }

     public static ExecutorFactory getExecutorFactory(){
         if(factory==null){
             factory = new ExecutorFactory();
         }
         return factory;
     }

     public Executor getExecutor(ICodeNode root){
         if(root==null){
             return null;
         }
         CTokenType type = (CTokenType) root.getAttribute(ICodeKey.TokenType);
         switch (type){
             case DEF:
                 return new DefExecutor();
             case DEF_LIST:
                 return new DefListExecutor();
             case LOCAL_DEFS:
                 return new LocalDefExecutor();

             case UNARY:
                 return new UnaryNodeExecutor();
             case BINARY:
                 return new BinaryExecutor();
             case NO_COMMA_EXPR:
                 return new NoCommaExprExecutor();
             case  EXPR:
                 return new ExprExecutor();
             case STATEMENT:
                 return new StatementExecutor();
             case STMT_LIST:
                 return new StatementListExecutor();
             case TEST:
                 return new TestExecutor();
             case IF_STATEMENT:
                 return new IfStatementExecutor();
             case IF_ELSE_STATEMENT:
                 return new ElseStatementExecutor();
             case OPT_EXPR:
                 return new OptExprExecutor();
             case END_OPT_EXPR:
                 return new EndOptExecutor();
             case INITIALIZER:
                 return new InitializerExecutor();
             case COMPOUND_STMT:
                 return new CompoundStmtExecutor();
             case EXT_DEF:
                 return new ExtDefExecutor();
             case FUNCT_DECL:
                 return new FunctDeclExecutor();
             case ARGS:
                 return new ArgsExecutor();
         }

         return null;

     }

}
