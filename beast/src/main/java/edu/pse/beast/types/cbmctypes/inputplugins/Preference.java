package edu.pse.beast.types.cbmctypes.inputplugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.highlevel.javafx.NEWRowOfValues;
import edu.pse.beast.toolbox.CodeArrayListBeautifier;
import edu.pse.beast.toolbox.UnifiedNameContainer;
import edu.pse.beast.toolbox.valueContainer.ResultValueWrapper;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValue;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueArray;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueSingle;
import edu.pse.beast.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueWrapper;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.InternalTypeRep;
import edu.pse.beast.types.cbmctypes.CBMCInputType;

/**
 * The Class Preference.
 */
public class Preference extends CBMCInputType {

    /** The Constant DIMENSIONS. */
    private static final int DIMENSIONS = 2;

    /** The Constant SIZE_OF_DIMENSIONS. */
    private static final String[] SIZE_OF_DIMENSIONS = {
            UnifiedNameContainer.getVoter(),
            UnifiedNameContainer.getCandidate()
    };

    /**
     * The constructor.
     */
    public Preference() {
        super(true, DataType.INT, DIMENSIONS, SIZE_OF_DIMENSIONS);
    }

    @Override
    public String getInputIDinFile() {
        return "PREFERENCE";
    }

    @Override
    public String getMinimalValue() {
        return "0";
    }

    @Override
    public String getMaximalValue() {
        return UnifiedNameContainer.getCandidate();
    }

    @Override
    public boolean hasVariableAsMinValue() {
        return false;
    }

    @Override
    public boolean hasVariableAsMaxValue() {
        return true;
    }

    @Override
    public boolean isVotingForOneCandidate() {
        return false;
    }

    @Override
    public String vetValue(final ElectionTypeContainer container,
                           final List<NEWRowOfValues> row,
                           final int rowNumber,
                           final int positionInRow,
                           final String newValue) {
        final int number;
        try {
            number = Integer.parseInt(newValue);
        } catch (NumberFormatException e) {
            return "0";
        }
        final String result;
        if (number < 0 || number > row.get(rowNumber).getAmountCandidates()) {
            result = "0";
        } else if (row.get(rowNumber).getValues().contains(newValue)) {
            result = "0";
        } else {
            result = newValue;
        }
        return result;
    }

    @Override
    public void restrictVotes(final String voteName,
                              final CodeArrayListBeautifier code) {
        code.add("for(int loop_r_0 = 0; loop_r_0 < V; loop_r_0++) {");
        code.add("for(int loop_r_1 = 0; loop_r_1 < C; loop_r_1++) {");
        code.add("for(int loop_r_2 = 0; loop_r_2 < C; loop_r_2++) {");
        code.add("if (loop_r_1 != loop_r_2) {");
        code.add("assume(" + voteName + ".arr[loop_r_0][loop_r_1] != "
                + voteName + ".arr[loop_r_0][loop_r_2]);");
        code.add("}");
        code.add("}");
        code.add("}");
        code.add("}");
    }

    @Override
    public String[] getVotePoints(final String[][] votes,
                                  final int amountCandidates,
                                  final int amountVoters) {
        String[] result = new String[amountCandidates];
        Arrays.fill(result, 0L);
        for (int i = 0; i < amountVoters; i++) {
            String[] vote = votes[i];
            for (int j = 0; j < amountCandidates; j++) {
                String chosenCandidate = vote[j];
                int iChosenCandidate = Integer.parseInt(chosenCandidate);
                result[iChosenCandidate] += amountCandidates - 1 - j;
            }
        }
        return result;
    }

    @Override
    public String[] getVotePoints(final String[] votes,
                                  final int amountCandidates,
                                  final int amountVoters) {
        return super.wrongInputTypeArray(amountCandidates, amountVoters);
    }

    // @Override
    // public void addMarginMainCheck(CodeArrayListBeautifier code, int margin,
    // List<String> origResult) {
    // code.add("int "
    // + UnifiedNameContainer.getNewVotesName() + "1["
    // + UnifiedNameContainer.getVoter() + "]["
    // + UnifiedNameContainer.getCandidate() + "];");
    //
    // code.add("for (int i = 0; i < V; i++) {"); // go over all voters
    // code.addTab();
    // code.add("for (int j = 0; i < C; i++) {"); // go over all candidates
    // code.addTab();
    // code.add("int changed = nondet_int();"); // determine, if we want to
    // // changed votes for
    // // this
    // // voter - candidate
    // // pair
    // code.add("assume(0 <= changed);");
    // code.add("assume(changed <= 1);");
    // code.add("if(changed) {");
    // code.addTab();
    // code.add("total_diff++;"); // if we changed the vote, we keep track
    // // of it
    // code.add("" + UnifiedNameContainer.getNewVotesName() + "1[i][j] =
    // nondet_int();");
    // // set the vote to (0-100), but different from original
    // code.add("assume(" + UnifiedNameContainer.getNewVotesName()
    // + "1[i][j] != ORIG_VOTES[i][j]);");
    // code.add("assume(0 <= " + UnifiedNameContainer.getNewVotesName() +
    // "1[i][j]);");
    // code.add("assume(" + UnifiedNameContainer.getNewVotesName() + "1[i][j] <=
    // 100);");
    // code.deleteTab();
    // code.add("} else {");
    // code.addTab();
    // code.add("" + UnifiedNameContainer.getNewVotesName() + "1[i][j] =
    // ORIG_VOTES[i][j];");
    // code.deleteTab();
    // code.add("}");
    // code.deleteTab();
    // code.add("}");
    // code.deleteTab();
    // code.add("}"); // end of the double for loop
    // code.add("assume(total_diff <= MARGIN);"); // no more changes than
    // // margin allows
    // }

    @Override
    public void addExtraCodeAtEndOfCodeInit(final CodeArrayListBeautifier code,
                                            final String valueName,
                                            final List<String> loopVariables) {
        String ownLoopVar = code.getNotUsedVarName("j_prime");

        String loopHead = "for (unsigned int " + ownLoopVar + " = 0; "
                + ownLoopVar + " < " + loopVariables.get(1) + "; " + ownLoopVar
                + "++) {";
        code.add(loopHead);

        code.add("assume (" + valueName + "."
                + UnifiedNameContainer.getStructValueName() + "["
                + loopVariables.get(0) + "]" + "[" + loopVariables.get(1)
                + "] != " + valueName + "."
                + UnifiedNameContainer.getStructValueName() + "["
                + loopVariables.get(0) + "]" + "[" + ownLoopVar + "]);");
        code.deleteTab();
        code.add("}");
    }

    @Override
    public void addCodeForVoteSum(final CodeArrayListBeautifier code,
                                  final boolean unique) {
        code.add("if(arr[i][0] == candidate) sum++;");
    }

    @Override
    public InternalTypeContainer getInternalTypeContainer() {
        return new InternalTypeContainer(
                new InternalTypeContainer(
                        new InternalTypeContainer(InternalTypeRep.INTEGER),
                        InternalTypeRep.CANDIDATE),
                InternalTypeRep.VOTER
        );
    }

    @Override
    public int vetAmountCandidates(final int amountCandidates) {
        if (amountCandidates < 1) {
            return 1;
        } else {
            return amountCandidates;
        }
    }

    @Override
    public int vetAmountVoters(final int amountVoters) {
        if (amountVoters < 1) {
            return 1;
        } else {
            return amountVoters;
        }
    }

    @Override
    public int getNumVotingPoints(final ResultValueWrapper result) {
        return GUIController.getController().getElectionSimulation()
                .getNumVoters();
    }

    @Override
    public int vetAmountSeats(final int amountSeats) {
        if (amountSeats < 1) {
            return 1;
        } else {
            return amountSeats;
        }
    }

    @Override
    public String otherToString() {
        return "Preference";
    }

    @Override
    public CBMCResultValue convertRowToResultValue(final NEWRowOfValues row) {
        final List<String> values = row.getValues();
        final List<CBMCResultValueWrapper> wrappedValues =
                new ArrayList<CBMCResultValueWrapper>();
        for (final Iterator<String> iterator = values.iterator();
                iterator.hasNext();) {
            String value = iterator.next();
            CBMCResultValueWrapper wrapper = new CBMCResultValueWrapper();
            CBMCResultValueSingle toWrap = new CBMCResultValueSingle();
            toWrap.setValue("int", value, INT_LENGTH);
            wrapper.setValue(toWrap);
        }
        final CBMCResultValueArray toReturn = new CBMCResultValueArray();
        toReturn.setValue(wrappedValues);
        return toReturn;
    }
}
