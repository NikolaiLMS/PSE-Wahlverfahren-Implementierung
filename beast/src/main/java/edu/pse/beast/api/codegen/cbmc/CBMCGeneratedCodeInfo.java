package edu.pse.beast.api.codegen.cbmc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class CBMCGeneratedCodeInfo {
	private String code;
	private Map<Integer, String> voteNumberToVariableName = new HashMap<>();
	private String amtMemberVarName;
	private String listMemberVarName;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void addVotingVariableName(int voteNumber, String varName) {
		voteNumberToVariableName.put(voteNumber, varName);
	}

	public void setVoteNumberToVariableName(
			Map<Integer, String> voteNumberToVariableName) {
		this.voteNumberToVariableName = voteNumberToVariableName;
	}

	public Map<Integer, String> getVoteNumberToVariableName() {
		return Collections.unmodifiableMap(voteNumberToVariableName);
	}

	public String getAmtMemberVarName() {
		return amtMemberVarName;
	}

	public String getListMemberVarName() {
		return listMemberVarName;
	}

	public void setAmtMemberVarName(String amtMemberVarName) {
		this.amtMemberVarName = amtMemberVarName;
	}

	public void setListMemberVarName(String listMemberVarName) {
		this.listMemberVarName = listMemberVarName;
	}

}