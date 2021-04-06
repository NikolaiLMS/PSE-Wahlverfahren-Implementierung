package edu.pse.beast.gui;

import java.util.List;

import edu.pse.beast.api.codegen.SymbolicCBMCVar;
import edu.pse.beast.api.codegen.SymbolicCBMCVar.CBMCVarType;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.gui.elements.PropertyEditorElement;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

public class PreAndPostPropertyEditor {
	private PropertyEditorElement preEditor;
	private PropertyEditorElement postEditor;
	private TreeView<String> variableTreeView;
	private PreAndPostConditionsDescription currentPropDescr;
	private MenuButton addSymbVarMenu;

	public PreAndPostPropertyEditor(PropertyEditorElement preEditor,
			PropertyEditorElement postEditor, TreeView<String> variableTreeView,
			MenuButton addSymbVarMenu) {
		this.preEditor = preEditor;
		this.postEditor = postEditor;
		this.variableTreeView = variableTreeView;
		this.addSymbVarMenu = addSymbVarMenu;
	}

	private void populateVariableList(List<SymbolicCBMCVar> vars) {
		TreeItem<String> voter = new TreeItem("Voter");
		TreeItem<String> candidate = new TreeItem("Candidate");
		for (SymbolicCBMCVar v : vars) {
			TreeItem<String> item = new TreeItem(v.getName());
			if (v.getVarType() == CBMCVarType.VOTER) {
				voter.getChildren().add(item);
			} else {
				candidate.getChildren().add(item);
			}
		}
		TreeItem<String> root = new TreeItem();
		root.getChildren().add(voter);
		root.getChildren().add(candidate);
		variableTreeView.setRoot(root);
		variableTreeView.setShowRoot(false);
	}

	public void loadProperty(PreAndPostConditionsDescription propDescr) {
		preEditor.clear();
		preEditor.insertText(0,
				propDescr.getPreConditionsDescription().getCode());
		postEditor.clear();
		postEditor.insertText(0,
				propDescr.getPostConditionsDescription().getCode());
		this.currentPropDescr = propDescr;
		populateVariableList(propDescr.getCbmcVariables());
	}

}
