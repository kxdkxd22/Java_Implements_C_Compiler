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


    public void term(){
        boolean handled = false;

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

        if(lexer.MatchToken(ThompsonLexer.Token.AT_BOL)){
            lexer.advance();
        }

        while(lexer.MatchToken(ThompsonLexer.Token.CCL_END)==false||
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
