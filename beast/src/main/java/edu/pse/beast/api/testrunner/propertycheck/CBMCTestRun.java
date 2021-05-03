package edu.pse.beast.api.testrunner.propertycheck;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.pse.beast.api.CBMCTestCallback;
import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.codegen.loopbounds.LoopBoundHandler;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.api.testrunner.threadpool.WorkUnitState;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.gui.runs.CBMCTestRunGuiController;

public class CBMCTestRun implements CBMCTestCallback {
	private CBMCPropertyCheckWorkUnit workUnit;
	// TODO move to gui decorator
	private CBMCTestRunGuiController updateListener;

	private int V;
	private int S;
	private int C;

	private CodeGenOptions codeGenOptions;
	private LoopBoundHandler loopBoundHandler;
	private CBMCCodeFile cbmcCodeFile;

	private List<String> testOutput = new ArrayList<>();

	private boolean descrChanged = false;
	private boolean propDescrChanged = false;

	private CElectionDescription descr;
	private PreAndPostConditionsDescription propDescr;

	public CBMCTestRun(int v, int s, int c, CodeGenOptions codeGenOptions,
			LoopBoundHandler loopBoundHandler, CBMCCodeFile cbmcCodeFile,
			CElectionDescription descr,
			PreAndPostConditionsDescription propDescr) {
		V = v;
		S = s;
		C = c;
		this.codeGenOptions = codeGenOptions;
		this.loopBoundHandler = loopBoundHandler;
		this.cbmcCodeFile = cbmcCodeFile;
		this.descr = descr;
		this.propDescr = propDescr;
	}

	public void setAndInitializeWorkUnit(CBMCPropertyCheckWorkUnit workUnit) {
		workUnit.initialize(V, S, C, codeGenOptions, loopBoundHandler,
				cbmcCodeFile, descr, propDescr, this);
		this.workUnit = workUnit;
	}

	public void setUpdateListener(CBMCTestRunGuiController updateListener) {
		this.updateListener = updateListener;
	}

	public void removeUpdateListener() {
		this.updateListener = null;
	}

	public CBMCCodeFile getCbmcCodeFile() {
		return cbmcCodeFile;
	}

	public WorkUnitState getState() {
		if (workUnit == null)
			return WorkUnitState.NO_WORK_UNIT;
		return workUnit.getState();
	}

	public CBMCPropertyCheckWorkUnit getWorkUnit() {
		return workUnit;
	}

	public String getTestOutput() {
		synchronized (testOutput) {
			return String.join("\n", testOutput);
		}
	}

	private void updateGui() {
		if (updateListener != null)
			updateListener.handleRunUpdate();
	}

	@Override
	public void onPropertyTestAddedToQueue(CElectionDescription description,
			PreAndPostConditionsDescription propertyDescr, int s, int c, int v,
			String uuid) {
		synchronized (testOutput) {
			testOutput.clear();
		}
		updateGui();
	}

	@Override
	public void onPropertyTestStart(CElectionDescription description,
			PreAndPostConditionsDescription propertyDescr, int s, int c, int v,
			String uuid) {
		updateGui();
	}

	@Override
	public void onPropertyTestRawOutput(String sessionUUID,
			CElectionDescription description,
			PreAndPostConditionsDescription propertyDescr, int s, int c, int v,
			String uuid, String output) {
		synchronized (testOutput) {
			testOutput.add(output);
		}
		updateGui();
	}

	@Override
	public void onPropertyTestFinished(CElectionDescription description,
			PreAndPostConditionsDescription propertyDescr, int s, int c, int v,
			String uuid) {
		updateGui();
	}

	public void handleDescrCodeChange() {
		descrChanged = true;
		updateGui();
	}

	public boolean isDescrChanged() {
		return descrChanged;
	}

	public void handlePropDescrChanged() {
		propDescrChanged = true;
		updateGui();
	}

	public boolean isPropDescrChanged() {
		return propDescrChanged;
	}

	public void updateDataForCheck(CBMCCodeFile cbmcFile,
			LoopBoundHandler loopBoundHandler) {
		workUnit.updateDataForCheck(cbmcFile, loopBoundHandler);
		descrChanged = false;
		propDescrChanged = false;
		updateGui();
	}

}