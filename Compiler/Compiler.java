public class Compiler {
    public static void main(String[] args) {
        Lexer lexer = new Lexer();
        //lexer.runlexer();
        //BasicParser bp = new BasicParser(lexer);
        //bp.statements();
        //ImprovedParser im = new ImprovedParser(lexer);
        //im.statements();
      //  Parser p = new Parser(lexer);
       // p.statements();
      //  PdaParser pdaParser = new PdaParser(lexer);
      //  pdaParser.parse();
      //  System.out.println("pdaParser accept input string");
      //  ArgumentedParser argumentedParser = new ArgumentedParser(lexer);
        //argumentedParser.Statements();
       // AttributedParser attributedParser = new AttributedParser(lexer);
        //attributedParser.Statements();
       // AttributedPDAParser parser = new AttributedPDAParser(lexer);
       // parser.parse();
        //TopdownParserWithParserTable parser = new TopdownParserWithParserTable(lexer);
        //parser.parse();

      //  ParserTableBuilder parserTableBuilder = new ParserTableBuilder();
       // parserTableBuilder.runFirstSets();
        ParserTableBuilder parserTableBuilder = new ParserTableBuilder();
        parserTableBuilder.runFollowSets();

    }
}
