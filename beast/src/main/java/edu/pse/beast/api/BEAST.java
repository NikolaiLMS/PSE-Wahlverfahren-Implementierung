package edu.pse.beast.api;

import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import edu.pse.beast.api.codegen.CodeGenOptions;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.api.testrunner.propertycheck.CBMCProcessStarter;
import edu.pse.beast.api.testrunner.propertycheck.PropertyCheckWorkSupplier;
import edu.pse.beast.api.testrunner.threadpool.ThreadPool;
import edu.pse.beast.booleanexpeditor.booleanexpcodearea.errorfinder.BooleanExpEditorGeneralErrorFinder;
import edu.pse.beast.celectiondescriptioneditor.celectioncodearea.errorhandling.CVariableErrorFinder;
import edu.pse.beast.codearea.errorhandling.CodeError;
import edu.pse.beast.datatypes.electioncheckparameter.ElectionCheckParameter;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.highlevel.javafx.ParentTreeItem;

public class BEAST {
	
	private ThreadPool tp;
	private ThreadPoolInterface tpi;
	private CBMCProcessStarter processStarter;
	
	public BEAST(CBMCProcessStarter processStarter) {
		this.tp = new ThreadPool(4);
		this.tpi = new ThreadPoolInterface(this.tp);
		this.processStarter = processStarter;
	}
	
	public void shutdown() {
		tp.shutdown();
	}

	//TODO pass the windows process and other setup stuff 
	public BEASTPropertyCheckSession createCheckSession(
			BEASTCallback cb, 
			CElectionDescription descr,
			List<PreAndPostConditionsDescription> propertiesToTest, 
			ElectionCheckParameter param,
			CodeGenOptions codeGenOptions) {

		// Test the election description for errors
//		final List<CodeError> electionCodeErrors = CVariableErrorFinder.findErrors(description);
//		if (!electionCodeErrors.isEmpty()) {
//			for (CodeError err : electionCodeErrors) {
//				cb.onElectionCodeError(err);
//			}
//			return new BEASTPropertyCheckSession();
//		}

//		// Test the Conditiondescriptions for errors
//		int idx = 0;
//		for (PreAndPostConditionsDescription prop : propertiesToTest) {
//			List<CodeError> propCodeErrors = BooleanExpEditorGeneralErrorFinder.getErrors(
//					prop, description.getContainer());
//			if (!propCodeErrors.isEmpty()) {
//				for (CodeError err : propCodeErrors) {
//					cb.onPropertyCodeError(err, idx);
//				}
//				return new BEASTPropertyCheckSession();
//			}
//			idx++;
//		}
		
		String sessionUUID = UUID.randomUUID().toString();
		return new BEASTPropertyCheckSession(
				sessionUUID,
				new PropertyCheckWorkSupplier(
						sessionUUID,
						descr, 
						propertiesToTest,
						param, 
						cb, 
						processStarter,
						codeGenOptions), 
				tpi);
	}
}
