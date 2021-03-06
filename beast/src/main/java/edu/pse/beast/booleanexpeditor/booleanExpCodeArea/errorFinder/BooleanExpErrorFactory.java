/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.booleanexpeditor.booleanExpCodeArea.errorFinder;

import org.antlr.v4.runtime.ParserRuleContext;

import edu.pse.beast.codearea.ErrorHandling.CodeError;
import edu.pse.beast.datatypes.booleanExpAST.otherValuedNodes.TypeExpression;
import edu.pse.beast.toolbox.antlr.booleanexp.FormalPropertyDescriptionParser;
import edu.pse.beast.types.InternalTypeContainer;

/**
 *
 * @author Holger-Desktop
 */
public class BooleanExpErrorFactory {

    private static final String[] ERRORIDS = {"antlr", "var_not_decl", "too_many_vars_passed", "wrong_var_type_passed",
            "incomparable_types", "incomparable_list_sizes", "wrong_var_passed_to_votesum",
            "number_must_be_greater_0" };

    private static int getErrorNum(String id) {
        for (int i = 0; i < ERRORIDS.length; ++i) {
            if (ERRORIDS[i].equals(id))
                return i;
        }
        return -1;
    }

    /**
     * creates an error when a variable was not created
     *
     * @param ctx
     *            the context* of the symbolic variable expression
     * @return the error object that describes the problem
     */
    public static CodeError createVarNotDeclaredErr(FormalPropertyDescriptionParser.SymbolicVarExpContext ctx) {
        CodeError err = generateStandardError(ctx, "var_not_decl");
        err.setExtraInfo("var_name", ctx.Identifier().getText());
        return err;
    }

    /**
     * creates an antlr error
     *
     * @param line
     *            the line the error is in
     * @param charInline
     *            the amount of characters in the line
     * @param msg
     *            the message to be displayed
     * @return the error that describes contains the information given to this
     *         function
     */
    public static CodeError createAntlrError(int line, int charInline, String msg) {
        CodeError err = new CodeError(line, charInline, "antlr", getErrorNum("antlr"), 0, 0);
        err.setExtraInfo("msg", msg);
        return err;
    }

    /**
     * creates an error when too many variables are passed
     *
     * @param ctx
     *            the context of the symbolic variable expression
     * @return the errorobject that describes the problem
     */
    public static CodeError createTooManyVarsPassedError(FormalPropertyDescriptionParser.PassTypeContext ctx) {

        CodeError err = generateStandardError(ctx, "too_many_vars_passed");
        err.setExtraInfo("var_name", ctx.getText());
        return err;
    }

    /**
     * creates an error when a wrong variable was passed
     *
     * @param cont
     *            the internal type
     * @param ctx
     *            the context of the symbolic variable expression
     * @param currentVarExp
     *            the current variable expression
     * @return the code error that describes the problem
     */
    static CodeError createWrongVarTypePassed(InternalTypeContainer cont,
            FormalPropertyDescriptionParser.PassTypeContext ctx, TypeExpression currentVarExp) {

        CodeError err = generateStandardError(ctx, "wrong_var_type_passed");

        err.setExtraInfo("var_name", ctx.getText());
        err.setExtraInfo("passed_type", currentVarExp.getInternalTypeContainer().getInternalType().toString());
        err.setExtraInfo("expected_type", cont.getAccesTypeIfList().toString());
        return err;
    }

    /**
     * creates an error when two not comparable things are being compared
     *
     * @param ctx
     *            the context of the symbolic variable expression
     * @param lhsCont
     *            the left hand side content
     * @param rhsCont
     *            the right hand side content
     * @return the error that describes the problem
     */
    static CodeError createCantCompareDifferentListLevels(FormalPropertyDescriptionParser.ComparisonExpContext ctx,
            InternalTypeContainer lhsCont, InternalTypeContainer rhsCont) {
        CodeError err = generateStandardError(ctx, "incomparable_list_sizes");
        err.setExtraInfo("lhs_list_size", String.valueOf(lhsCont.getListLvl()));
        err.setExtraInfo("rhs_list_size", String.valueOf(rhsCont.getListLvl()));
        return err;
    }

    /**
     * creates an error when two incomparable things get compared
     *
     * @param ctx
     *            the comparison context
     * @param lhsCont
     *            the left hand side content
     * @param rhsCont
     *            the right hand side content
     * @return the code error
     */
    static CodeError createCantCompareTypes(FormalPropertyDescriptionParser.ComparisonExpContext ctx,
            InternalTypeContainer lhsCont, InternalTypeContainer rhsCont) {
        CodeError err = generateStandardError(ctx, "incomparable_types");
        err.setExtraInfo("lhs_type", lhsCont.getInternalType().toString());
        err.setExtraInfo("rhs_type", rhsCont.getInternalType().toString());
        return err;
    }

    /**
     * create code error for a wront war to votesum
     *
     * @param ctx
     *            the sum expression contect
     * @param passedType
     *            the passed type
     * @return the code error
     */
    static CodeError createWrongVarToVotesumError(FormalPropertyDescriptionParser.VoteSumExpContext ctx,
            InternalTypeContainer passedType) {
        CodeError err = generateStandardError(ctx, "wrong_var_passed_to_votesum");
        err.setExtraInfo("var_type", passedType.getInternalType().toString());
        return err;
    }

    /**
     * create code error for a wront war to votesum
     *
     * @param ctx
     *            the sum expression contect
     * @param passedType
     *            the passed type
     * @return the code error
     */
    static CodeError createWrongVarToVotesumError(
            FormalPropertyDescriptionParser.VoteSumUniqueExpContext ctx,
            InternalTypeContainer passedType) {
        CodeError err = generateStandardError(ctx, "wrong_var_passed_to_votesum_unique");
        err.setExtraInfo("var_type", passedType.getInternalType().toString());
        return err;
    }

    /**
     * create the error that indicates that a number must be greater than zero
     * for a votesum
     *
     * @param ctx
     *            the vote sum expression context
     * @return the code error
     */
    static CodeError createNumberMustBeGreaterZeroVotesum(ParserRuleContext ctx) {
        CodeError err = generateStandardError(ctx, "number_must_be_greater_0");
        return err;
    }

    /**
     * creates an error if an "elect" value is smaller than zero
     *
     * @param ctx
     *            the election expression context
     * @return the code error
     */
    static CodeError createNumberMustBeGreaterZeroElect(FormalPropertyDescriptionParser.ElectExpContext ctx) {
        CodeError err = generateStandardError(ctx, "number_must_be_greater_0");
        return err;
    }

    /**
     * creates an error if an "votes" value is smaller than zero
     *
     * @param ctx
     *            the vote expression context
     * @return the code error
     */
    static CodeError createNumberMustBeGreaterZeroVotes(FormalPropertyDescriptionParser.VoteExpContext ctx) {
        CodeError err = generateStandardError(ctx, "number_must_be_greater_0");
        return err;
    }

    private static CodeError generateStandardError(ParserRuleContext ctx, String id) {
        int pos = ctx.getStart().getStartIndex();
        int endPos = ctx.getStop().getStopIndex();
        int line = ctx.getStart().getLine();
        int charInLine = ctx.getStart().getCharPositionInLine();
        CodeError err = new CodeError(line, charInLine, id, getErrorNum(id), pos, endPos);
        return err;
    }

}
