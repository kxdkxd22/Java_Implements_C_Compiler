import java.util.Set;

public class NfaMachineConstructor {

    private ThompsonLexer lexer;
    private NfaManager nfaManager = null;

    public NfaMachineConstructor(ThompsonLexer lexer) throws Exception {
        nfaManager = new NfaManager();
        this.lexer = lexer;
        while(lexer.MatchToken(ThompsonLexer.Token.EOS)){
            lexer.advance();
        }
    }

    public void expr(NfaPair nfaPair) throws Exception {
        cat_expr(nfaPair);
        NfaPair nfaPair1 = new NfaPair();

        while(lexer.MatchToken(ThompsonLexer.Token.OR)){
            lexer.advance();
            cat_expr(nfaPair1);
            Nfa startNode = nfaManager.newNfa();
            Nfa endNode = nfaManager.newNfa();

            startNode.next = nfaPair.startNode;
            startNode.next2 = nfaPair1.startNode;

            nfaPair.endNode.next = endNode;
            nfaPair1.endNode.next = endNode;

            nfaPair.startNode = startNode;
            nfaPair.endNode = endNode;

        }

    }

    private boolean constructExprInParen(NfaPair nfaPair) throws Exception {

        if(lexer.MatchToken(ThompsonLexer.Token.OPEN_PAREN)==true){
            lexer.advance();
            expr(nfaPair);

            if(lexer.MatchToken(ThompsonLexer.Token.CLOSE_PAREN)==true){
                lexer.advance();
            }else{
                ErrorHandler.parseErr(ErrorHandler.Error.E_PAREN);
            }
            return true;
        }

        return false;
    }

    public boolean constructStarClosure(NfaPair nfaPair) throws Exception {
        Nfa start,end;
     //   term(nfaPair);

        if(lexer.MatchToken(ThompsonLexer.Token.CLOSURE)==false){
            return false;
        }

        start = nfaManager.newNfa();
        end = nfaManager.newNfa();

        start.next = nfaPair.startNode;
        nfaPair.endNode.next2 = nfaPair.startNode;
        nfaPair.endNode.next = end;
        start.next2 = end;

        nfaPair.startNode = start;
        nfaPair.endNode = end;

        lexer.advance();

        return true;
    }

    public boolean constructPlusClosure(NfaPair nfaPair) throws Exception {

        Nfa start,end;
     //   term(nfaPair);

        if(lexer.MatchToken(ThompsonLexer.Token.PLUS_CLOSE)==false){
            return false;
        }

        start = nfaManager.newNfa();
        end = nfaManager.newNfa();

        start.next = nfaPair.startNode;
        nfaPair.endNode.next2 = nfaPair.startNode;
        nfaPair.endNode.next = end;

        nfaPair.startNode = start;
        nfaPair.endNode = end;

        lexer.advance();

        return true;
    }

    public boolean constructOptionsClosure(NfaPair nfaPair) throws Exception {

        Nfa start,end;
      //  term(nfaPair);

        if(lexer.MatchToken(ThompsonLexer.Token.OPTIONAL)==false){
            return false;
        }

        start = nfaManager.newNfa();
        end  = nfaManager.newNfa();

        start.next = nfaPair.startNode;
        nfaPair.endNode.next = end;
        start.next2 = end;

        nfaPair.startNode = start;
        nfaPair.endNode = end;

        lexer.advance();

        return true;
    }

    public void factor(NfaPair nfaPair) throws Exception {
        term(nfaPair);
        boolean handled = false;
        handled = constructStarClosure(nfaPair);
        if(handled == false){
            handled = constructPlusClosure(nfaPair);
        }
        if(handled == false){
            handled = constructOptionsClosure(nfaPair);
        }

    }

    public void cat_expr(NfaPair nfaPair) throws Exception {
        if(first_in_cat(lexer.getCurrentToken())){
            factor(nfaPair);
        }

        while(first_in_cat(lexer.getCurrentToken())){
            NfaPair nfaPair1 = new NfaPair();
          //  if(lexer.MatchToken(ThompsonLexer.Token.OR)){
           //     lexer.advance();
           // }

            factor(nfaPair1);

            nfaPair.endNode.next = nfaPair1.startNode;
            nfaPair.endNode = nfaPair1.endNode;

        }

    }

    private boolean first_in_cat(ThompsonLexer.Token tok) throws Exception {

        switch(tok){
            case CLOSE_PAREN:
            case AT_EOL:
            case OR:
            case EOS:
                return false;
            case CLOSURE:
            case PLUS_CLOSE:
            case OPTIONAL:
                ErrorHandler.parseErr(ErrorHandler.Error.E_CLOSE);
                return false;
            case CCL_END:
                ErrorHandler.parseErr(ErrorHandler.Error.E_BRACKET);
                return false;
            case AT_BOL:
                ErrorHandler.parseErr(ErrorHandler.Error.E_BOL);
                return false;
        }
        return true;
    }


    public void term(NfaPair nfaPair) throws Exception {
        boolean handled = constructExprInParen(nfaPair);
        if(handled == false){
            handled = constructNfaForSingleCharacter(nfaPair);
        }

        if(handled == false){
            handled = constructNfaForCharacterSet(nfaPair);
        }

        if(handled == false){
            handled = constructNfaForDot(nfaPair);
        }

    }

    public boolean constructNfaForSingleCharacter(NfaPair nfaPair) throws Exception {
        if(lexer.MatchToken(ThompsonLexer.Token.L)==false){
            return false;
        }

        nfaPair.startNode = nfaManager.newNfa();
        nfaPair.endNode = nfaPair.startNode.next = nfaManager.newNfa();

        nfaPair.startNode.setEdge(lexer.getLexeme());

        lexer.advance();

        return true;
    }

    public boolean constructNfaForDot(NfaPair nfaPair) throws Exception {
        if(lexer.MatchToken(ThompsonLexer.Token.ANY)==false){
            return false;
        }

        nfaPair.startNode = nfaManager.newNfa();
        nfaPair.endNode = nfaPair.startNode.next = nfaManager.newNfa();

        nfaPair.startNode.setEdge(Nfa.CCL);
        nfaPair.startNode.addToSet((byte) '\r');
        nfaPair.startNode.addToSet((byte) '\n');
        nfaPair.startNode.setComplement();

        lexer.advance();

        return true;
    }

    public boolean constructNfaForCharacterSet(NfaPair nfaPair) throws Exception {
        if(lexer.MatchToken(ThompsonLexer.Token.CCL_START)==false){
            return false;
        }

        lexer.advance();
        boolean negative = false;
        if(lexer.MatchToken(ThompsonLexer.Token.AT_BOL)){
            negative = true;
        }

        nfaPair.startNode = nfaManager.newNfa();
        nfaPair.endNode = nfaPair.startNode.next = nfaManager.newNfa();
        nfaPair.startNode.setEdge(Nfa.CCL);

        if(lexer.MatchToken(ThompsonLexer.Token.CCL_END)==false){
            Dash(nfaPair.startNode.inputSet);
        }

        if(lexer.MatchToken(ThompsonLexer.Token.CCL_END)==false){
            ErrorHandler.parseErr(ErrorHandler.Error.E_BADEXPR);
        }

        if(negative){
            nfaPair.startNode.setComplement();
        }

        lexer.advance();

        return true;
    }

    public boolean constructNfaForCharacterWithoutNegative(){

        return false;
    }

    public void Dash(Set<Byte> inputSet){

        int first = 0;

        while(lexer.MatchToken(ThompsonLexer.Token.CCL_END)==false &&
                lexer.MatchToken(ThompsonLexer.Token.EOS)==false){
            if(lexer.MatchToken(ThompsonLexer.Token.DASH)==false){
                first = lexer.getLexeme();
                inputSet.add((byte) lexer.getLexeme());
            }else{
                lexer.advance();
                for(;first<=lexer.getLexeme();first++){
                    inputSet.add((byte) first);
                }
            }
            lexer.advance();
        }

    }

}
