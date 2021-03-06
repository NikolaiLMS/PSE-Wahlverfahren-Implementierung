package edu.pse.beast.types.cbmctypes.inputplugins;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.electionSimulator.ElectionSimulation;
import edu.pse.beast.electionSimulator.Model.RowOfValues;
import edu.pse.beast.propertychecker.CBMCResultWrapperMultiArray;
import edu.pse.beast.propertychecker.CBMCResultWrapperSingleArray;
import edu.pse.beast.toolbox.CodeArrayListBeautifier;
import edu.pse.beast.types.InternalTypeContainer;
import edu.pse.beast.types.InternalTypeRep;
import edu.pse.beast.types.OutputType;
import edu.pse.beast.types.cbmctypes.CBMCInputType;

public class SingleChoiceStack extends CBMCInputType {
	
	@Override
	public String getInputString() {
		return "[C]";
	}

	@Override
	public String getInputIDinFile() {
		return "SINGLE_CHOICE_STACK";
	}

	@Override
	public String getMinimalValue(ElectionTypeContainer container) {
		return "0";
	}

	@Override
	public String getMaximalValue(ElectionTypeContainer container) {
		return "V";
	}

	@Override
	public boolean isVotingForOneCandidate() {
		return true;
	}

	@Override
	public void addVerifyMethod(CodeArrayListBeautifier code, OutputType outType) {
		code.add("void verify() {");
		code.addTab();
		
			code.add("int total_diff = 0;");
			code.add("int pos_diff = 0;");
			
			code.add("int new_votes1[C];");
			code.add("int diff[C];");
			
			code.add("for (int i = 0; i < C; i++) {"); // go over all voters
			code.addTab();
			
				code.add("diff[i] = nondet_int();"); 
				
				code.add("assume(-1 * MARGIN <= diff[i]);");
				code.add("assume(diff[i] <= MARGIN);");
				
				code.add("assume(0 <= ORIG_VOTES[i] + diff[i]);");
			
			code.deleteTab();
			code.add("}");
			
			code.add("for (int i = 0; i < C; i++) {"); // go over all voters
			code.addTab();

				code.add("new_votes1[i] = ORIG_VOTES[i] + diff[i];");
				code.add("if (0 < diff[i]) pos_diff += diff[i];");
				code.add("total_diff += diff[i];");
				
			code.deleteTab();
			code.add("}");
			
			code.add("assume(pos_diff <= MARGIN);");
			code.add("assume(total_diff == 0);");
			
			outType.addVerifyOutput(code);
		
		code.deleteTab();
		code.add("}"); // end of the function
	}

	@Override
	public boolean isTwoDim() {
		return getDimension() == 2;
	}

	@Override
	public CBMCResultWrapperMultiArray extractVotesWrappedMulti(List<String> result, int numberCandidates) {
		List<CBMCResultWrapperSingleArray> singleVotesList = super.helper.readOneDimVarLong("votes", result);

		List<CBMCResultWrapperMultiArray> toReturn = new ArrayList<CBMCResultWrapperMultiArray>();

		for (Iterator<CBMCResultWrapperSingleArray> iterator = singleVotesList.iterator(); iterator.hasNext();) {
			CBMCResultWrapperSingleArray cbmcResultWrapperSingleArray = (CBMCResultWrapperSingleArray) iterator.next();
			toReturn.add(cbmcResultWrapperSingleArray.wrapInTwoDim(1, "new_votes", numberCandidates));
		}

		return toReturn.get(0);
	}

	@Override
	public String vetValue(String newValue, ElectionTypeContainer container, RowOfValues row) {

		int number;

		try {
			number = Integer.parseInt(newValue);
		} catch (NumberFormatException e) {
			return "0";
		}
		
		if (number < 0) {
			newValue = "0";
		}
		return newValue;
	}

	@Override
	public List<CBMCResultWrapperMultiArray> readVoteList(List<String> toExtract) {
		return null;
	}

	@Override
	public List<CBMCResultWrapperSingleArray> readSingleVoteList(List<String> toExtract) {
		return super.helper.readOneDimVarLong("votes", toExtract);
	}

	@Override
	public String[] getVotePoints(String[][] votes, int amountCandidates, int amountVoters) {
		return super.wrongInputTypeArray(amountCandidates, amountVoters);
	}

	@Override
	public String[] getVotePoints(String[] votes, int amountCandidates, int amountVoters) {
		Long[] result = new Long[amountCandidates];
		Arrays.fill(result, 0L);

		for (int i = 0; i < amountVoters; i++) {
			int vote = Integer.parseInt(votes[i]);
			result[vote]++;

		}

		String[] toReturn = new String[amountCandidates];

		for (int i = 0; i < result.length; i++) {
			toReturn[i] = "" + result[i];
		}
		return toReturn;
	}

	

	@Override
	public List<String> getVotingResultCode(String[][] votingData) {
		List<String> toReturn = new ArrayList<String>();

		toReturn.add("int ORIG_VOTES[" + votingData[0].length + "] = {");

		//we only have the candidates, and only one "pseudo" voter, 
		
		String tmp = "" + votingData[0][0];
		
		for (int i = 1; i < votingData[0].length; i++) {
			tmp = tmp + "," + votingData[0][i]; 
		}
		
		toReturn.add(tmp);
		
		
		
		toReturn.add("};"); // close the array declaration
		
		return toReturn;
	}

	@Override
	public String getArrayType() {
		return "[V]";
	}

	@Override
	public int getDimension() {
		return 1;
	}

	@Override
	public void addExtraCodeAtEndOfCodeInit(CodeArrayListBeautifier code, int voteNumber) {
	}

	@Override
	public void addCodeForVoteSum(CodeArrayListBeautifier code, boolean unique) {
		code.add("if(arr[i] == candidate) sum++;");
	}

	@Override
	public List<List<String>> getNewVotes(List<String> lastFailedRun) {
		List<List<String>> toReturn = new ArrayList<List<String>>();
		
		toReturn.add(super.helper.readOneDimVarLong("new_votes", lastFailedRun).get(0).getList());
		
		return toReturn;
	}

	@Override
	public InternalTypeContainer getInternalTypeContainer() {
		return new InternalTypeContainer(new InternalTypeContainer(InternalTypeRep.CANDIDATE), InternalTypeRep.VOTER);
	}
	
	@Override
	public int vetAmountCandidates(int amountCandidates) {
		if(amountCandidates < 1) {
			return 1;
		} else {
			return amountCandidates;
		}
	}

	@Override
	public int vetAmountVoters(int amountVoters) {
		return 1;
	}

	@Override
	public int vetAmountSeats(int amountSeats) {
		if(amountSeats < 1) {
			return 1;
		} else {
			return amountSeats;
		}
	}
	
	@Override
	public int getNumVotingPoints(String[][] votingData) {
		int sum = 0;
		
		for (int i = 0; i < votingData.length; i++) {
			for (int j = 0; j < votingData[0].length; j++) {
				sum = sum + Integer.parseInt(votingData[i][j]);
			}
		}
		
		return sum;
	}
	
}
