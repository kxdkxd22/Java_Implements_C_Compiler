package backend;

import frontend.CTokenType;
import frontend.Production;

import java.util.ArrayList;

public class bak {
   public void initVariableDecalationProduction(){

        productionMap.clear();

        /*LB:{RB:}
         *
         * C variable declaration grammar
         *  PROGRAM -> EXT_DEF_LIST
         *
         * EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
         *
         * EXT_DEF->OPT_SPECIFIERS EXT_DECL_LIST SEMI
         *          | OPT_SPECIFIERS SEMI
         *
         * EXT_DECL_LIST->EXT_DECL
         *               | EXT_DECL_LIST COMMA EXT_DECL
         *
         * EXT_DECL -> VAR_DECL
         *
         * OPT_SPECIFIERS->CLASS TTYPE
         *                  | TTYPE
         *                  | SPECIFIERS
         *                  | EMPTY?
         *
         * SPECIFIERS -> TYPE_OR_CLASS
         *               | SPECIFIERS TYPE_OR_CLASS
         *
         * TYPE_OR_CLASS-> TYPE_SPECIFIER
         *               | CLASS
         *
         * TYPE_SPECIFIER -> TYPE
         *
         * NEW_NAME ->NAME
         *
         * NAME_NT -> NAME
         *
         * VAR_DECL ->| NEW_NAME
         *            | START VAR_DECL
         *
         */

        //PROGRAM -> EXT_DEF_LIST

        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.EXT_DEF_LIST.ordinal()});
        Production production = new Production(productionNum, CTokenType.PROGRAM.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //EXT_DEF_LIST -> EXT_DEF_LIST EXT_DEF
        right = getProductionRight(new int[]{CTokenType.EXT_DEF_LIST.ordinal(), CTokenType.EXT_DEF.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DEF_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //EXT_DEF->OPT_SPECIFIERS EXT_DECL_LIST SEMI
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(), CTokenType.EXT_DECL_LIST.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DEF->OPT_SPECIFIERS SEMI
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL_LIST->EXT_DECL
        right = getProductionRight(new int[]{CTokenType.EXT_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL_LIST->EXT_DECL_LIST COMMA EXT_DECL
        right = getProductionRight(new int[]{CTokenType.EXT_DECL_LIST.ordinal(), CTokenType.COMMA.ordinal(), CTokenType.EXT_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXT_DECL -> VAR_DECL
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.EXT_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_SPECIFIERS->CLASS TTYPE
        /*right = getProductionRight(new int[]{CTokenType.CLASS.ordinal(),CTokenType.TTYPE.ordinal()});
        production = new Production(productionNum,CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //OPT_SPECIFIERS->TTYPE
        right = getProductionRight(new int[]{CTokenType.TTYPE.ordinal()});
        production = new Production(productionNum,CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);
        */
        //OPT_SPECIFIERS->SPECIFIERS
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal()});
        production = new Production(productionNum, CTokenType.OPT_SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //SPECIFIERS -> TYPE_OR_CLASS
        right = getProductionRight(new int[]{CTokenType.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum, CTokenType.SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //SPECIFIERS -> SPECIFIERS TYPE_OR_CLASS
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal(), CTokenType.TYPE_OR_CLASS.ordinal()});
        production = new Production(productionNum, CTokenType.SPECIFIERS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_OR_CLASS-> TYPE_SPECIFIER
        right = getProductionRight(new int[]{CTokenType.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, CTokenType.TYPE_OR_CLASS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_OR_CLASS-> CLASS
        /*right = getProductionRight(new int[]{CTokenType.CLASS.ordinal()});
        production = new Production(productionNum,CTokenType.TYPE_OR_CLASS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);*/

        //TYPE_SPECIFIER -> TYPE
        right = getProductionRight(new int[]{CTokenType.TYPE.ordinal()});
        production = new Production(productionNum, CTokenType.TYPE_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NEW_NAME -> NAME
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum, CTokenType.NEW_NAME.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NAME_NT -> NAME
        /*right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum,CTokenType.NAME_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);*/

        //VAR_DECL ->| NEW_NAME
        right = getProductionRight(new int[]{CTokenType.NEW_NAME.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL ->| START VAR_DECL
        right = getProductionRight(new int[]{CTokenType.STAR.ordinal(), CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);



    }

    private void initFunctionProductions(){
        /*
         * production num begin with 15
         *
         * EXT_DEF->OPT_SPECIFIERS FUNCT_DECL SEMI
         *
         * FUNCT_DECL->NEW_NAME LP VAR_LIST RP
         *             |NEW_NAME LP RP
         *
         * VAR_LIST->PARAM_DECLARATION
         *           |VAR_LIST COMMA PARAM_DECLARATION
         *
         * PARAM_DECLARATION->TYPE_NT VAR_DECL
         *
         * TYPE_NT->TYPE_SPECIFIER
         *         |TYPE TYPE_SPECIFIER
         */

        //EXT_DEF->OPT_SPECIFIERS FUNCT_DECL SEMI
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(), CTokenType.FUNCT_DECL.ordinal(), CTokenType.SEMI.ordinal()});
        Production production = new Production(productionNum, CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //FUNCT_DECL->NEW_NAME LP VAR_LIST RP
        right = null;
        right = getProductionRight(new int[]{CTokenType.NEW_NAME.ordinal(), CTokenType.LP.ordinal(), CTokenType.VAR_LIST.ordinal(), CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.FUNCT_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //FUNCT_DECL->NEW_NAME LP RP
        right = null;
        right = getProductionRight(new int[]{CTokenType.NEW_NAME.ordinal(), CTokenType.LP.ordinal(), CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.FUNCT_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_LIST->PARAM_DECLARATION
        right = null;
        right = getProductionRight(new int[]{CTokenType.PARAM_DECLARATION.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_LIST->VAR_LIST COMMA PARAM_DECLARATION
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_LIST.ordinal(), CTokenType.COMMA.ordinal(), CTokenType.PARAM_DECLARATION.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //PARAM_DECLARATION->TYPE_NT VAR_DECL
        right = null;
        right = getProductionRight(new int[]{CTokenType.TYPE_NT.ordinal(), CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.PARAM_DECLARATION.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_NT->TYPE_SPECIFIER
        right = null;
        right = getProductionRight(new int[]{CTokenType.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, CTokenType.TYPE_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TYPE_NT->TYPE TYPE_SPECIFIER
        right = null;
        right = getProductionRight(new int[]{CTokenType.TYPE.ordinal(), CTokenType.TYPE_SPECIFIER.ordinal()});
        production = new Production(productionNum, CTokenType.TYPE_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

    }

    private void initStructureProductions(){
        /*
         *production number begin from 23
         *
         * TYPE_SPECIFIER->STRUCT_SPECIFIER
         *
         * STRUCT_SPECIFIER->STRUCT OPT_TAG LC DEF_LIST RC
         *                   |STRUCT TAG
         *
         * OPT_TAG->TAG
         *
         * TAG->NAME
         *
         * DEF_LIST->DEF_LIST DEF
         *
         * DEF->SPECIFIERS DECL_LIST SEMI
         *       |SPECIFIERS SEMI
         *
         * DECL_LIST->DECL
         *           |DECL_LIST COMMA DECL
         *
         * DECL->VAR_DECL
         *
         *VAR_DECL->NEW_NAME
         *          |VAR_DECL LP RP
         *          |VAR_DECL LP VAR_LIST RP
         *          |LP VAR_DECL RP
         *          |START VAR_DECL
         *
         */

        //TYPE_SPECIFIER->STRUCT_SPECIFIER  (23)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.STRUCT_SPECIFIER.ordinal()});
        Production production = new Production(productionNum, CTokenType.TYPE_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STRUCT_SPECIFIER->STRUCT OPT_TAG LC DEF_LIST RC  (24)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STRUCT.ordinal(), CTokenType.OPT_TAG.ordinal(), CTokenType.LC.ordinal(), CTokenType.DEF_LIST.ordinal(), CTokenType.RC.ordinal()});
        production = new Production(productionNum, CTokenType.STRUCT_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STRUCT_SPECIFIER->STRUCT TAG  (25)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STRUCT.ordinal(), CTokenType.TAG.ordinal()});
        production = new Production(productionNum, CTokenType.STRUCT_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_TAG->TAG  (26)
        right = null;
        right = getProductionRight(new int[]{CTokenType.TAG.ordinal()});
        production = new Production(productionNum, CTokenType.OPT_TAG.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //TAG->NAME  (27)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum, CTokenType.TAG.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DEF_LIST->DEF (28)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DEF.ordinal()});
        production = new Production(productionNum, CTokenType.DEF_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DEF_LIST->DEF_LIST DEF (29)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DEF_LIST.ordinal(), CTokenType.DEF.ordinal()});
        production = new Production(productionNum, CTokenType.DEF_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DEF->SPECIFIERS DECL_LIST SEMI  (30)
        right = null;
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal(), CTokenType.DECL_LIST.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DEF->SPECIFIERS SEMI  (31)
        right = null;
        right = getProductionRight(new int[]{CTokenType.SPECIFIERS.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DECL_LIST->DECL  (32)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DECL.ordinal()});
        production = new Production(productionNum, CTokenType.DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DECL_LIST->DECL_LIST COMMA DECL   (33)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DECL_LIST.ordinal(), CTokenType.COMMA.ordinal(), CTokenType.DECL.ordinal()});
        production = new Production(productionNum, CTokenType.DECL_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DECL->VAR_DECL  (34)
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL->NEW_NAME  (35)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NEW_NAME.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL->VAR_DECL LP RP  (36)
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal(), CTokenType.LP.ordinal(), CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        // addProduction(production,false);

        //VAR_DECL->VAR_DECL LP VAR_LIST RP  (37)
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal(), CTokenType.LP.ordinal(), CTokenType.VAR_LIST.ordinal(), CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        // addProduction(production,false);

        //VAR_DECL->LP VAR_DECL RP  (38)
        right = null;
        right = getProductionRight(new int[]{CTokenType.LP.ordinal(), CTokenType.VAR_DECL.ordinal(), CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL->STAR VAR_DECL  (39)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STAR.ordinal(), CTokenType.VAR_DECL.ordinal()});
        production = new Production(productionNum, CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);
    }

    private void initEmunProductions(){
        /*
         *begin from production number 40
         *
         */
        //ENUM_SPECIFIER -> ENUM_NT NAME_NT OPT_ENUM_LIST (40)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.ENUM_NT.ordinal(), CTokenType.NAME_NT.ordinal(), CTokenType.OPT_ENUM_LIST.ordinal()});
        Production production = new Production(productionNum, CTokenType.ENUM_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ENUM_NT->ENUM (41)
        right = null;
        right = getProductionRight(new int[]{CTokenType.ENUM.ordinal()});
        production = new Production(productionNum, CTokenType.ENUM_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ENUMERATOR_LIST->ENUMERATOR (42)
        right = null;
        right = getProductionRight(new int[]{CTokenType.ENUMERATOR.ordinal()});
        production = new Production(productionNum, CTokenType.ENUMERATOR_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ENUMERATOR_LIST->ENUMERATOR_LIST COMMA ENUMERATOR (43)
        right = null;
        right = getProductionRight(new int[]{CTokenType.ENUMERATOR_LIST.ordinal(), CTokenType.COMMA.ordinal(), CTokenType.ENUMERATOR.ordinal()});
        production = new Production(productionNum, CTokenType.ENUMERATOR_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ENUMERATOR->NAME_NT (44)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME_NT.ordinal()});
        production = new Production(productionNum, CTokenType.ENUMERATOR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NAME_NT->NAME (45)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum, CTokenType.NAME_NT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ENUMERATOR->NAME_NT EQUAL CONST_EXPR(46)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME_NT.ordinal(), CTokenType.EQUAL.ordinal(), CTokenType.CONST_EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.ENUMERATOR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //CONST_EXPR->NUMBER (47)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NUMBER.ordinal()});
        production = new Production(productionNum, CTokenType.CONST_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_ENUM_LIST->LC ENUMERATOR_LIST RC (48)
        right = null;
        right = getProductionRight(new int[]{CTokenType.LC.ordinal(), CTokenType.ENUMERATOR_LIST.ordinal(), CTokenType.RC.ordinal()});
        production = new Production(productionNum, CTokenType.OPT_ENUM_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //TYPE_SPECIFIER -> ENUM_SPECIFIER (49)
        right = null;
        right = getProductionRight(new int[]{CTokenType.ENUM_SPECIFIER.ordinal()});
        production = new Production(productionNum, CTokenType.TYPE_SPECIFIER.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

    }

    private void initFunctionDefinition(){
        /*
         *begin production number 50
         */

        //EXT_DEF -> OPT_SPECIFIERS FUNCT_DECL COMPOUND_STMT (50)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.OPT_SPECIFIERS.ordinal(), CTokenType.FUNCT_DECL.ordinal(), CTokenType.COMPOUND_STMT.ordinal()});
        Production production = new Production(productionNum, CTokenType.EXT_DEF.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //COMPOUND_STMT-> LC LOCAL_DEFS STMT_LIST RC (51)
        right = null;
        right = getProductionRight(new int[]{CTokenType.LC.ordinal(), CTokenType.LOCAL_DEFS.ordinal(), CTokenType.STMT_LIST.ordinal(), CTokenType.RC.ordinal()});
        production = new Production(productionNum, CTokenType.COMPOUND_STMT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //LOCAL_DEFS -> DEF_LIST (52)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DEF_LIST.ordinal()});
        production = new Production(productionNum, CTokenType.LOCAL_DEFS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //EXPR -> NO_COMMA_EXPR (53)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NO_COMMA_EXPR -> NO_COMMA_EXPR EQUAL NO_COMMA_EXPR (54)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NO_COMMA_EXPR.ordinal(), CTokenType.EQUAL.ordinal(), CTokenType.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.NO_COMMA_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NO_COMMA_EXPR -> NO_COMMA_EXPR QUEST  NO_COMMA_EXPR COLON NO_COMMA_EXPR (55)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NO_COMMA_EXPR.ordinal(), CTokenType.QUEST.ordinal(), CTokenType.NO_COMMA_EXPR.ordinal(), CTokenType.COLON.ordinal(), CTokenType.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.NO_COMMA_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //NO_COMMA_EXPR -> BINARY  (56)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.NO_COMMA_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> UNARY (57)
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY -> NUMBER (58)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NUMBER.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY -> NAME (59)
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY -> STRING  (60)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STRING.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STMT_LIST -> STMT_LIST STATEMENT (61)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STMT_LIST.ordinal(), CTokenType.STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.STMT_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //STMT_LIST ->  STATEMENT (62)
        right = null;
        right = getProductionRight(new int[]{CTokenType.STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.STMT_LIST.ordinal(),0,right);
        productionNum++;
        addProduction(production,true);

        //STATEMENT -> EXPR SEMI (63)
        right = null;
        right = getProductionRight(new int[]{CTokenType.EXPR.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT -> RETURN EXPR SEMI (64)
        right = null;
        right = getProductionRight(new int[]{CTokenType.RETURN.ordinal(), CTokenType.EXPR.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY RELOP BINARY (65)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.RELOP.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY EQUOP BINARY (66)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.EQUOP.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY START BINARY (67)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.STAR.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT -> LOCAL_DEFS  (68)
        right = null;
        right = getProductionRight(new int[]{CTokenType.LOCAL_DEFS.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

    }

    private void initFunctionDefinition2(){
        //COMPOUND_STMT->LC RC (69)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.LC.ordinal(), CTokenType.RC.ordinal()});
        Production production = new Production(productionNum, CTokenType.COMPOUND_STMT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //COMPOUND_STMT->LC STMT_LIST RC (70)
        right = null;
        right = getProductionRight(new int[]{CTokenType.LC.ordinal(), CTokenType.STMT_LIST.ordinal(), CTokenType.RC.ordinal()});
        production = new Production(productionNum, CTokenType.COMPOUND_STMT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);
    }

    private void initFunctionDefinitionWithIfElse(){
        // STATEMENT -> COMPOUND_STMT  (71)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.COMPOUND_STMT.ordinal()});
        Production production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //IF_STATEMENT -> IF LP TEST RP STATEMENT (72)
        right = null;
        right = getProductionRight(new int[]{CTokenType.IF.ordinal(), CTokenType.LP.ordinal(), CTokenType.TEST.ordinal(), CTokenType.RP.ordinal(), CTokenType.STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.IF_STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //IF_ELSE_STATEMENT -> IF_STATEMENT (73)
        right = null;
        right = getProductionRight(new int[]{CTokenType.IF_STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.IF_ELSE_STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //IF_ELSE_STATEMENT ->IF_ELSE_STATEMENT ELSE STATEMENT (74)
        right = null;
        right = getProductionRight(new int[]{CTokenType.IF_ELSE_STATEMENT.ordinal(), CTokenType.ELSE.ordinal(), CTokenType.STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.IF_ELSE_STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT -> IF_ELSE_STATEMENT  (75)
        right = null;
        right = getProductionRight(new int[]{CTokenType.IF_ELSE_STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TEST -> EXPR  (76)
        right = null;
        right = getProductionRight(new int[]{CTokenType.EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.TEST.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //DECL -> VAR_DECL EQUAL INITIALIZER  (77)
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal(), CTokenType.EQUAL.ordinal(), CTokenType.INITIALIZER.ordinal()});
        production = new Production(productionNum, CTokenType.DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //INITIALIZER -> EXPR  (78)
        right = null;
        right = getProductionRight(new int[]{CTokenType.EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.INITIALIZER.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

    }

    private void initFunctionDefinitionWithSwitchCase(){
        //STATEMENT->SWITCH LP EXPR RP COMPOUND_STATEMENT (79)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.SWITCH.ordinal(), CTokenType.LP.ordinal(), CTokenType.EXPR.ordinal(), CTokenType.RP.ordinal(), CTokenType.COMPOUND_STMT.ordinal()});
        Production production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT->CASE CONST_EXPR COLON  (80)
        right = null;
        right = getProductionRight(new int[]{CTokenType.CASE.ordinal(), CTokenType.CONST_EXPR.ordinal(), CTokenType.COLON.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT->DEFAULT COLON (81)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DEFAULT.ordinal(), CTokenType.COLON.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT->BREAK SEMI;  (82)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BREAK.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

    }

    private void initFunctionDefinitionWithLoop(){
        //STATEMENT->WHILE LP TEST RP STATEMENT  (83)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.WHILE.ordinal(), CTokenType.LP.ordinal(), CTokenType.TEST.ordinal(), CTokenType.RP.ordinal(), CTokenType.STATEMENT.ordinal()});
        Production production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT -> FOR LP OPT_EXPR TEST SEMI END_OPT_EXPR RP STATEMENT (84)
        right = null;
        right = getProductionRight(new int[]{CTokenType.FOR.ordinal(), CTokenType.LP.ordinal(), CTokenType.OPT_EXPR.ordinal(), CTokenType.TEST.ordinal(), CTokenType.SEMI.ordinal(), CTokenType.END_OPT_EXPR.ordinal(), CTokenType.RP.ordinal(), CTokenType.STATEMENT.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_EXPR->EXPR SEMI (85)
        right = null;
        right = getProductionRight(new int[]{CTokenType.EXPR.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.OPT_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //OPT_EXPR->SEMI  (86)
        right = null;
        right = getProductionRight(new int[]{CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.OPT_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //END_OPT_EXPR->EXPR  (87)
        right = null;
        right = getProductionRight(new int[]{CTokenType.EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.END_OPT_EXPR.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT->DO STATEMENT WHILE LP TEST RP SEMI (88)
        right = null;
        right = getProductionRight(new int[]{CTokenType.DO.ordinal(), CTokenType.STATEMENT.ordinal(), CTokenType.WHILE.ordinal(), CTokenType.LP.ordinal(), CTokenType.TEST.ordinal(), CTokenType.RP.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

    }

    private void initComputingOperation(){
        //BINARY -> BINARY STAR BINARY (89)
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.STAR.ordinal(), CTokenType.BINARY.ordinal()});
        Production production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY DIVOP BINARY (90)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.DIVOP.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY->BINARY SHIFTOP BINARY (91)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.SHIFTOP.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY-> BINARY AND BINARY  (92)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.AND.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY ->BINARY XOR BINARY  (93)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.XOR.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY PLUS BINARY  (94)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.PLUS.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //BINARY -> BINARY MINUS BINARY  (95)
        right = null;
        right = getProductionRight(new int[]{CTokenType.BINARY.ordinal(), CTokenType.MINUS.ordinal(), CTokenType.BINARY.ordinal()});
        production = new Production(productionNum, CTokenType.BINARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY -> UNARY INCOP  i++(96)
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal(), CTokenType.INCOP.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY -> INCOP UNARY  ++i(97)
        right = null;
        right = getProductionRight(new int[]{CTokenType.INCOP.ordinal(), CTokenType.UNARY.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->MINUS UNARY a=-a
        right = null;
        right = getProductionRight(new int[]{CTokenType.MINUS.ordinal(), CTokenType.UNARY.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->STAR UNARY b=*a
        right = null;
        right = getProductionRight(new int[]{CTokenType.STAR.ordinal(), CTokenType.UNARY.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->UNARY STRUCTOP NAME a=tag->name
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal(), CTokenType.STRUCTOP.ordinal(),CTokenType.UNARY.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->UNARY LB EXPR RB b=a[2]
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal(),CTokenType.LB.ordinal(), CTokenType.EXPR.ordinal(),CTokenType.RB.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->UNARY LP ARGS RP fun(a,b,c)
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal(), CTokenType.LP.ordinal(),CTokenType.ARGS.ordinal(),CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //UNARY->UNARY LP RP fun()
        right = null;
        right = getProductionRight(new int[]{CTokenType.UNARY.ordinal(), CTokenType.LP.ordinal(),CTokenType.RP.ordinal()});
        production = new Production(productionNum, CTokenType.UNARY.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ARGS->NO_COMMA_EXPR
        right = null;
        right = getProductionRight(new int[]{CTokenType.NO_COMMA_EXPR.ordinal()});
        production = new Production(productionNum, CTokenType.ARGS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //ARGS->NO_COMMA_EXPR COMMA ARGS
        right = null;
        right = getProductionRight(new int[]{CTokenType.NO_COMMA_EXPR.ordinal(), CTokenType.COMMA.ordinal(),CTokenType.ARGS.ordinal()});
        production = new Production(productionNum, CTokenType.ARGS.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);


    }

    private void initRemaindingProduction(){
        //STATEMENT -> TARGET COLON STATEMENT
        ArrayList<Integer> right = null;
        right = getProductionRight(new int[]{CTokenType.TARGET.ordinal(), CTokenType.COLON.ordinal(), CTokenType.STATEMENT.ordinal()});
        Production production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //STATEMENT -> GOTO TARGET SEMI
        right = null;
        right = getProductionRight(new int[]{CTokenType.GOTO.ordinal(), CTokenType.TARGET.ordinal(), CTokenType.SEMI.ordinal()});
        production = new Production(productionNum, CTokenType.STATEMENT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //TARGET -> NAME
        right = null;
        right = getProductionRight(new int[]{CTokenType.NAME.ordinal()});
        production = new Production(productionNum, CTokenType.TARGET.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //VAR_DECL->VAR_DECL LB CONST_EXPR RB a[5] (109)
        right = null;
        right = getProductionRight(new int[]{CTokenType.VAR_DECL.ordinal(),CTokenType.LB.ordinal(),CTokenType.CONST_EXPR.ordinal(),CTokenType.RB.ordinal()});
        production = new Production(productionNum,CTokenType.VAR_DECL.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);

        //COMPOUND_STMT->LC STMT_LIST RC (51)
        right = getProductionRight(new int[]{CTokenType.LC.ordinal(),CTokenType.STMT_LIST.ordinal(),CTokenType.RC.ordinal()});
        production = new Production(productionNum,CTokenType.COMPOUND_STMT.ordinal(),0,right);
        productionNum++;
        addProduction(production,false);
    }

}
