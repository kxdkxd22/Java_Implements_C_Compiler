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
         CTokenType type = (CTokenType) root.getAttribute(ICodeKey.TokenType);
         switch (type){
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
         }

         return null;

     }

}