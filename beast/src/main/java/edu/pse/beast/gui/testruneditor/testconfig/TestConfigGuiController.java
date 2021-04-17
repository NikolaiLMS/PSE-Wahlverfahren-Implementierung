package edu.pse.beast.gui.testruneditor.testconfig;

import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.gui.workspace.BeastWorkspace;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class TestConfigGuiController {

	@FXML
	private AnchorPane topLevelAnchorPane;
	@FXML
	private TextField nameTextField;
	@FXML
	private ChoiceBox<CElectionDescription> descrChoiceBox;
	@FXML
	private Button loadDescrButton;
	@FXML
	private ChoiceBox<PreAndPostConditionsDescription> propDescrChoiceBox;
	@FXML
	private Button loadPropDescrButton;

	private BeastWorkspace beastWorkspace;

	public TestConfigGuiController(BeastWorkspace beastWorkspace) {
		this.beastWorkspace = beastWorkspace;
	}

	public AnchorPane getTopLevelAnchorPane() {
		return topLevelAnchorPane;
	}

	public void display(TestConfiguration config) {
		nameTextField.setText(config.getName());
		descrChoiceBox.getSelectionModel().select(config.getDescr());
		propDescrChoiceBox.getSelectionModel().select(config.getPropDescr());
	}

	@FXML
	private void initialize() {
		descrChoiceBox.getItems().addAll(beastWorkspace.getLoadedDescrs());
		propDescrChoiceBox.getItems()
				.addAll(beastWorkspace.getLoadedPropDescrs());
	}
}
