package edu.pse.beast.gui;

import java.io.IOException;
import java.util.List;

import org.fxmisc.flowless.VirtualizedScrollPane;
import org.fxmisc.richtext.CodeArea;

import edu.pse.beast.api.c_parser.ExtractedCLoop;
import edu.pse.beast.api.electiondescription.CElectionDescription;
import edu.pse.beast.api.electiondescription.function.CElectionDescriptionFunction;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.gui.ceditor.CEditorCodeElement;
import edu.pse.beast.gui.ceditor.CElectionEditor;
import edu.pse.beast.gui.log.LogGuiController;
import edu.pse.beast.gui.options.OptionsCategory;
import edu.pse.beast.gui.options.OptionsGUIController;
import edu.pse.beast.gui.options.ProcessHandlerWindowsOptions;
import edu.pse.beast.gui.paths.PathHandler;
import edu.pse.beast.gui.processHandler.CBMCProcessHandlerCreator;
import edu.pse.beast.gui.propertyeditor.PreAndPostPropertyEditor;
import edu.pse.beast.gui.propertyeditor.PropertyEditorCodeElement;
import edu.pse.beast.gui.testconfigeditor.TestConfigTopLevelGUIHandler;
import edu.pse.beast.gui.testconfigeditor.treeview.TestConfigTreeItemSuper;
import edu.pse.beast.gui.workspace.BeastWorkspace;
import edu.pse.beast.gui.workspace.WorkspaceUpdateListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TabPane;
import javafx.scene.control.TitledPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class BeastGUIController implements WorkspaceUpdateListener {

	// =================options start
	private String optionsFXML = "/edu/pse/beast/optionsGUI.fxml";
	private OptionsGUIController optionsGUIController;
	private FXMLLoader optionsFXMLLoader = new FXMLLoader(
			getClass().getResource(optionsFXML));
	// =================options end

	// =================Menu start
	@FXML
	private MenuBar menuBar;
	// =================Menu end

	@FXML
	private Button testLoopBoundButton;
	@FXML
	private TabPane topLeveLTabPane;

	private Stage primaryStage;

	@FXML
	private Button loadElectionDescriptionButton;

	@FXML
	private AnchorPane codePane;

	@FXML
	private MenuButton addFunctionMenuButton;

	@FXML
	private Button removeFunctionButton;

	@FXML
	private ListView<ExtractedCLoop> loopBoundList;

	@FXML
	private ListView<CElectionDescriptionFunction> functionList;

	@FXML
	private TitledPane prePropertyPane;

	@FXML
	private TitledPane postPropertyPane;

	@FXML
	private TreeView<String> variableTreeView;

	@FXML
	private MenuButton addSymbVarMenu;

	@FXML
	private Button removeSymbVarButton;

	@FXML
	private TabPane propertyTestRunPane;

	@FXML
	private ChoiceBox<CElectionDescription> openedElectionDescriptionChoiceBox;
	@FXML
	private Button addElectionDescriptionButton;
	@FXML
	private Button saveElectionDescriptionButton;

	@FXML
	private ChoiceBox<PreAndPostConditionsDescription> openedPropertyDescriptionChoiceBox;
	@FXML
	private Button addPropDescrButton;
	@FXML
	private Button loadPropDescrButton;
	@FXML
	private Button savePropDescrButton;

	// TestConfigHandler
	@FXML
	private ChoiceBox<String> sortCriteriumChoiceBox;
	@FXML
	private TreeView<TestConfigTreeItemSuper> testConfigTreeView;
	@FXML
	private AnchorPane testConfigDetailsAnchorPane;
	// TestConfigHandler end

	@FXML
	private AnchorPane logAnchorPane;

	@FXML
	private Button loadWorkspaceButton;
	@FXML
	private Button saveWorkspaceButton;

	private CElectionEditor cElectionEditor;
	private PreAndPostPropertyEditor preAndPostPropertyEditor;
	private TestConfigTopLevelGUIHandler testConfigurationHandler;
	private LogGuiController logGuiController;
	private CBMCProcessHandlerCreator cbmcProcessHandlerCreator;

	private BeastWorkspace beastWorkspace;

	private void addChildToAnchorPane(AnchorPane pane, Node child, double top,
			double bottom, double left, double right) {
		codePane.getChildren().add(child);
		AnchorPane.setTopAnchor(child, top);
		AnchorPane.setLeftAnchor(child, left);
		AnchorPane.setRightAnchor(child, right);
		AnchorPane.setBottomAnchor(child, bottom);
	}

	private void initElectionEditor() {
		CodeArea funcDeclArea = new CodeArea();
		codePane.getChildren().add(funcDeclArea);
		AnchorPane.setTopAnchor(funcDeclArea, 0d);
		AnchorPane.setLeftAnchor(funcDeclArea, 0d);
		AnchorPane.setRightAnchor(funcDeclArea, 0d);

		CodeArea closingBracketArea = new CodeArea();
		codePane.getChildren().add(closingBracketArea);
		AnchorPane.setBottomAnchor(closingBracketArea, 0d);
		AnchorPane.setLeftAnchor(closingBracketArea, 0d);
		AnchorPane.setRightAnchor(closingBracketArea, 0d);

		CEditorCodeElement cEditorGUIElement = new CEditorCodeElement();
		VirtualizedScrollPane<CEditorCodeElement> cEditorGUIElementVsp = new VirtualizedScrollPane(
				cEditorGUIElement);
		addChildToAnchorPane(codePane, cEditorGUIElementVsp, 20, 100, 0, 0);

		cElectionEditor = new CElectionEditor(primaryStage,
				cEditorGUIElementVsp, addElectionDescriptionButton,
				loadElectionDescriptionButton, saveElectionDescriptionButton,
				addFunctionMenuButton, removeFunctionButton,
				testLoopBoundButton, cEditorGUIElement, funcDeclArea,
				closingBracketArea, functionList, loopBoundList,
				openedElectionDescriptionChoiceBox, beastWorkspace);
	}

	private void initPropertyEditor() {
		PropertyEditorCodeElement prePropertyEditor = new PropertyEditorCodeElement();
		PropertyEditorCodeElement postPropertyEditor = new PropertyEditorCodeElement();

		VirtualizedScrollPane<PropertyEditorCodeElement> preVsp = new VirtualizedScrollPane<>(
				prePropertyEditor);
		VirtualizedScrollPane<PropertyEditorCodeElement> postVsp = new VirtualizedScrollPane<>(
				postPropertyEditor);

		prePropertyPane.setContent(preVsp);
		postPropertyPane.setContent(postVsp);

		preAndPostPropertyEditor = new PreAndPostPropertyEditor(
				prePropertyEditor, postPropertyEditor, addPropDescrButton,
				loadPropDescrButton, savePropDescrButton, removeSymbVarButton,
				variableTreeView, addSymbVarMenu,
				openedPropertyDescriptionChoiceBox, beastWorkspace);
	}

	private void initTestConfigHandler() throws IOException {

		this.testConfigurationHandler = new TestConfigTopLevelGUIHandler(
				sortCriteriumChoiceBox, testConfigTreeView,
				testConfigDetailsAnchorPane, beastWorkspace);
	}

	private void initLogHandler(ErrorHandler errorHandler) {
		logGuiController = new LogGuiController(logAnchorPane, errorHandler);
	}

	private void initWorkspace(ErrorHandler errorHandler,
			CBMCProcessHandlerCreator cbmcProcessHandlerCreator2) {
		beastWorkspace = BeastWorkspace
				.getStandardWorkspace(cbmcProcessHandlerCreator);

		beastWorkspace.setErrorHandler(errorHandler);

		saveWorkspaceButton.setOnAction(e -> {
			beastWorkspace.saveWorkspace();
		});
		loadWorkspaceButton.setOnAction(e -> {
			beastWorkspace.letUserLoadWorkSpace();
		});
	}

	private void initOptionsController() throws IOException {
		ProcessHandlerWindowsOptions processHandlerWindowsOptions = new ProcessHandlerWindowsOptions();

		List<OptionsCategory> options = List.of(processHandlerWindowsOptions);
		optionsGUIController = new OptionsGUIController(options);
		optionsFXMLLoader.setController(optionsGUIController);
		optionsFXMLLoader.load();
	}

	private void initMenu() {
		Menu fileMenu = new Menu();
		fileMenu.setText("File");

		Menu prefMenu = new Menu();
		prefMenu.setText("Preferences");

		MenuItem optionsMenuItem = new MenuItem();
		optionsMenuItem.setText("options");
		optionsMenuItem.setOnAction(e -> {
			optionsGUIController.display();
		});
		prefMenu.getItems().add(optionsMenuItem);

		menuBar.getMenus().add(prefMenu);
	}

	@FXML
	public void initialize() throws IOException {
		// load options
		cbmcProcessHandlerCreator = new CBMCProcessHandlerCreator();

		//init gui
		ErrorHandler errorHandler = new ErrorHandler(this);
		PathHandler pathHandler = new PathHandler();

		initWorkspace(errorHandler, cbmcProcessHandlerCreator);
		initElectionEditor();
		initPropertyEditor();
		initTestConfigHandler();
		initLogHandler(errorHandler);
		initMenu();

		//option Controller
		initOptionsController();
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;

	}

	@Override
	public void handleWorkspaceUpdateGeneric() {
	}

	public void shutdown() {
		beastWorkspace.shutdown();
	}

}
