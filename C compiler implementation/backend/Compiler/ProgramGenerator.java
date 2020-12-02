package backend.Compiler;

public class ProgramGenerator extends CodeGenerator{
    private static  ProgramGenerator instance = null;

    public static ProgramGenerator getInstance(){
        if(instance == null){
            instance = new ProgramGenerator();
        }
        return instance;
    }

    private ProgramGenerator(){}

    public String getProgramName(){return programName;}

    public void generate(){
        emitDirective(Directive.CLASS_PUBLIC,programName);
        emitDirective(Directive.SUPER,"java/lang/Object");
        generateMainMethod();
    }

    private void generateMainMethod(){
        emitBlankLine();
        emitDirective(Directive.METHOD_PUBLIC_STATIC,"main([Ljava/lang/String;)V");
    }

    public void finish(){
        emit(Instruction.RETURN);
        emitDirective(Directive.END_METHOD);
        emitBufferedContent();
        emitDirective(Directive.END_CLASS);
        super.finish();
    }

}
