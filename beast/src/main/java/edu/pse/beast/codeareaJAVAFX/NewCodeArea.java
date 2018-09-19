package edu.pse.beast.codeareaJAVAFX;

import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.IntFunction;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.fxmisc.richtext.LineNumberFactory;
import org.fxmisc.richtext.model.PlainTextChange;
import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;
import org.reactfx.collection.LiveList;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.CVariableErrorFinder;
import edu.pse.beast.codearea.ErrorHandling.CodeError;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionDescriptionChangeListener;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.highlevel.javafx.MenuBarInterface;
import edu.pse.beast.saverloader.ElectionDescriptionSaverLoader;
import edu.pse.beast.toolbox.CCodeHelper;
import edu.pse.beast.toolbox.Triplet;
import edu.pse.beast.types.InputType;
import edu.pse.beast.types.OutputType;
import edu.pse.beast.types.cbmctypes.inputplugins.SingleChoice;
import edu.pse.beast.types.cbmctypes.outputplugins.SingleCandidate;
import javafx.scene.Node;
import javafx.scene.control.IndexRange;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;

public class NewCodeArea extends AutoCompletionCodeArea implements MenuBarInterface {

	private final SaverLoader saverLoader;

	private final ElectionDescriptionSaverLoader electionSaverLoader = new ElectionDescriptionSaverLoader();

	private static final String[] KEYWORDS = new String[] { "auto", "break", "case", "const", "continue", "default",
			"do", "else", "error", "const", "continue", "default", "do", "else", "enum", "extern", "for", "goto", "if",
			"return", "signed", "sizeof", "static", "struct", "switch", "typedef", "union", "unsigned", "volatile",
			"while" };

	private static final String[] PREPROCESSOR = new String[] { "#define", "#elif", "#endif", "#ifdef", "#ifndef",
			"#include" };

	private static final String[] DATATYPES = new String[] { "char", "double", "enum", "float", "int", "long",
			"register", "void" };

	private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
	private static final String PREPROCESSOR_PATTERN = "(" + String.join("|", PREPROCESSOR) + ")";
	private static final String DATATYPE_PATTERN = "(" + String.join("|", DATATYPES) + ")";
	private static final String POINTER_PATTERN = "\\b("
			+ String.join("|", Arrays.stream(DATATYPES).map(s -> "\\*[\\s]*" + s).toArray(String[]::new)) + ")\\b";
	private static final String METHOD_PATTERN = "[\\w]+[\\s]*\\(";
	private static final String INCLUDE_PATTERN = "[.]*include[\\s]+[<|\"].+\\.[\\w]*[>|\"]";
	private static final String PAREN_PATTERN = "\\(|\\)";
	private static final String BRACE_PATTERN = "\\{|\\}";
	private static final String BRACKET_PATTERN = "\\[|\\]";
	private static final String SEMICOLON_PATTERN = "\\;";
	private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
	private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

	private static final Pattern PATTERN = Pattern.compile("(?<KEYWORD>" + KEYWORD_PATTERN + ")" + "|(?<PREPROCESSOR>"
			+ PREPROCESSOR_PATTERN + ")" + "|(?<DATATYPE>" + DATATYPE_PATTERN + ")" + "|(?<POINTER>" + POINTER_PATTERN
			+ ")" + "|(?<METHOD>" + METHOD_PATTERN + ")" + "|(?<INCLUDE>" + INCLUDE_PATTERN + ")" + "|(?<PAREN>"
			+ PAREN_PATTERN + ")" + "|(?<BRACE>" + BRACE_PATTERN + ")" + "|(?<BRACKET>" + BRACKET_PATTERN + ")"
			+ "|(?<SEMICOLON>" + SEMICOLON_PATTERN + ")" + "|(?<STRING>" + STRING_PATTERN + ")" + "|(?<COMMENT>"
			+ COMMENT_PATTERN + ")");

	private static Set<String> recommendations = new TreeSet<String>();

	private ElectionDescription elecDescription;

	private List<ElectionDescriptionChangeListener> listeners = new ArrayList<ElectionDescriptionChangeListener>();

	private int lockedLineStart = 0;

	private int lockedLineEnd = 10;

	private int lockedBracePos;

	final KeyCombination selectAllCombination = new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN); // select
																												// all

	final KeyCombination backspaceCombination = new KeyCodeCombination(KeyCode.BACK_SPACE); // backspace

	final KeyCombination deleteCombination = new KeyCodeCombination(KeyCode.DELETE); // delete

	final KeyCombination enterCombination = new KeyCodeCombination(KeyCode.ENTER); // enter

	final KeyCombination undoCombination = new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN); // undo

	final KeyCombination redoCombination = new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN); // redo

	final KeyCombination pasteCombination = new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN); // paste

	final KeyCombination cutCombination = new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN); // paste

	public NewCodeArea() {

		// add all standard recommendations
		recommendations.addAll(Arrays.asList(KEYWORDS));
		recommendations.addAll(Arrays.asList(PREPROCESSOR));
		recommendations.addAll(Arrays.asList(DATATYPES));

		saverLoader = new SaverLoader(".elec", "BEAST election description");

		ElectionDescription startElecDescription = new ElectionDescription("New description", new SingleChoice(),
				new SingleCandidate(), 0, 0, 0, 0, true);

		this.setNewElectionDescription(startElecDescription);

		saverLoader.resetHasSaveFile();

		List<String> code = new ArrayList<String>();
		code.add("");
		// code.add(source.getContainer().getInputType().);

		String sampleCode = "";

		String stylesheet = this.getClass().getResource("codeAreaSyntaxHighlight.css").toExternalForm();

		this.getStylesheets().add(stylesheet);

		IntFunction<Node> lineNumbers = LineNumberFactory.get(this);

		this.setParagraphGraphicFactory(lineNumbers);

		this.addEventFilter(KeyEvent.KEY_TYPED, event -> {

			if (event != null) {
				event.consume();
			}

			String replacement = "";

			String value = (event.getCharacter()).replaceAll("\\p{Cntrl}", "");

			if (value.length() != 1) {
				return;
			} else {

				replacement = value;

				switch (value) {
				case "(":
					replacement = "()";
					break;

				case "[":
					replacement = "[]";
					break;

				case "{":
					replacement = "{\n\n}";
					break;

				default:
					break;
				}

				lockedLineSaveInsertText(replacement, false, false);
			}
		});

		this.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
			if (event != null) {
				event.consume();
			}
			;
		});

		this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {

			if (selectAllCombination.match(event)) { // we just want to select all
				this.selectAll();
				if (event != null) {
					event.consume();
				}
				;
			} else if (backspaceCombination.match(event)) {
				lockedLineSaveInsertText("", true, false);
				if (event != null) {
					event.consume();
				}
				;
			} else if (deleteCombination.match(event)) {
				delete(event);
			} else if (enterCombination.match(event)) {
				lockedLineSaveInsertText("\n", false, false);
				if (event != null) {
					event.consume();
				}
			} else if (pasteCombination.match(event)) {

				paste(event);

			} else if (cutCombination.match(event)) {

				cut(event);
			}
		});

		this.richChanges().filter(ch -> !ch.getInserted().equals(ch.getRemoved())).subscribe(change ->

		{
			this.setStyleSpans(0, computeHighlighting(this.getText()));
			elecDescription.setCode(this.getText());
		});
		this.replaceText(0, 0, sampleCode);

	}

	/**
	 * @param event
	 */
	private void delete(KeyEvent event) {
		lockedLineSaveInsertText("", false, true);
		if (event != null) {
			event.consume();
		}
		;
	}

	/**
	 * @param event
	 */
	private void paste(KeyEvent event) {
		String clipboardText = "";

		try {
			clipboardText = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
		} catch (HeadlessException e) {
			return;
		} catch (UnsupportedFlavorException e) {
			return;
		} catch (IOException e) {
			return;
		}

		lockedLineSaveInsertText(clipboardText, false, false);

		if (event != null) {
			event.consume();
		}
		;
	}

	/**
	 * @param event
	 */
	private void cut(KeyEvent event) {
		String selectedText = this.getSelectedText();

		boolean isValid = lockedLineSaveInsertText("", true, false);

		if (isValid) {
			StringSelection selection = new StringSelection(selectedText);
			Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, selection);
		}
		if (event != null) {
			event.consume();
		}
	}

	/**
	 * @param event
	 */
	private void redo(KeyEvent event) {
		LiveList<?> redoList = this.getUndoManager().nextRedoProperty().asList();

		if (redoList.size() == 1) {
			List<?> list = (List<?>) redoList.get(0);

			PlainTextChange change = (PlainTextChange) list.get(0);

			System.out.println("redo length:  " + change.getNetLength());

			updateLockedLineNumber(change.getRemovalEnd(), change.getRemoved().length());

		} else {
			if (event != null) {
				event.consume();
			}
		}
	}

	/**
	 * @param event
	 */
	private void undo(KeyEvent event) {
		LiveList<?> undoList = this.getUndoManager().nextUndoProperty().asList();

		if (undoList.size() == 1) {
			List<?> list = (List<?>) undoList.get(0);

			PlainTextChange change = (PlainTextChange) list.get(0);

			updateLockedLineNumber(change.getInsertionEnd(), -1 * change.getNetLength());

		} else {
			if (event != null) {
				event.consume();
			}
			;
		}
	}

	private boolean lockedLineSaveInsertText(String replacement, boolean backspace, boolean delete) {

		IndexRange selectionRange = this.getSelection();

		int selectionStart = selectionRange.getStart();
		int selectionEnd = selectionRange.getEnd();

		int selectionLength = selectionEnd - selectionStart;

		if (selectionLength == 0) {
			selectionStart = this.getCaretPosition();
			selectionEnd = this.getCaretPosition();
		}

		if (backspace && delete) {
			return false;
		}

		if (backspace || delete) {
			replacement = "";
		}

		if (selectionLength == 0) { // update the values
			if (backspace) {
				selectionStart = Math.max(0, selectionStart - 1);
				selectionLength = selectionEnd - selectionStart;
			} else if (delete) {
				selectionEnd = Math.min(this.getText().length(), selectionEnd + 1);
				selectionLength = selectionEnd - selectionStart;
			}
		}

		// if (selectionEnd - selectionStart > 0) { // we have a selected range
		// // find out, if the selected range overlaps with a locked line anywhere

		boolean NotOverlapping = ((selectionEnd < lockedLineStart) || (lockedLineEnd <= selectionStart))
				&& (((selectionEnd <= lockedBracePos) || (lockedBracePos < selectionStart)));

		if (NotOverlapping) {
			this.replaceText(selectionStart, selectionEnd, replacement);

			int lengthChange = replacement.length() - selectionLength;

			updateLockedLineNumber(selectionEnd, lengthChange);

			return true;
		} else {
			return false;
		}
	}

	private void updateLockedLineNumber(int changePosition, int lengthChange) {
		if (changePosition <= lockedLineStart) {
			lockedLineStart = lockedLineStart + lengthChange;
			lockedLineEnd = lockedLineEnd + lengthChange;

			lockedBracePos = lockedBracePos + lengthChange;
		} else if (changePosition <= lockedBracePos) {
			lockedBracePos = lockedBracePos + lengthChange;
		} else {
			// don't increase anything
		}
		
		test();
	}

	private void test() {
		
		System.out.println("test");

		Set<String> style = Collections.singleton("-fx-background-fill: blue;");
		
		
		//this.setStyle(paragraph, style);
		//this.setStyle(value);
		//this.setStyle("-fx-background-color: blue;");
		
		this.setStyle(lockedLineStart, lockedLineEnd, style);
		
		
		System.out.println("start: " + lockedLineStart);
		
		System.out.println("end: " + lockedLineEnd);
		
		//this.setStyle(lockedLineStart, lockedLineEnd, style);
		//this.setStyle(lockedBracePos, lockedBracePos + 1,  style);
	}

	private static StyleSpans<Collection<String>> computeHighlighting(String text) {
		Matcher matcher = PATTERN.matcher(text);
		int lastKwEnd = 0;
		StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
		while (matcher.find()) {
			String styleClass = matcher.group("KEYWORD") != null ? "keyword"
					: matcher.group("PREPROCESSOR") != null ? "preprocessor"
							: matcher.group("METHOD") != null ? "method"
									: matcher.group("DATATYPE") != null ? "datatype"
											: matcher.group("POINTER") != null ? "pointer"
													: matcher.group("INCLUDE") != null ? "include"
															: matcher.group("PAREN") != null ? "paren"
																	: matcher.group("BRACE") != null ? "brace"
																			: matcher.group("BRACKET") != null
																					? "bracket"
																					: matcher.group("SEMICOLON") != null
																							? "semicolon"
																							: matcher.group(
																									"STRING") != null
																											? "string"
																											: matcher
																													.group("COMMENT") != null
																															? "comment"
																															: null;
			/* never happens */ assert styleClass != null;

			spansBuilder.add(Collections.emptyList(), matcher.start() - lastKwEnd);
			spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
			lastKwEnd = matcher.end();
		}
		spansBuilder.add(Collections.emptyList(), text.length() - lastKwEnd);
		return spansBuilder.create();
	}

	/**
	 * 
	 * @return a deep copy of the election description, which doesn't update when
	 *         the text field gets another input
	 */
	public ElectionDescription getElectionDescription() {
		return elecDescription.getDeepCopy();
	}

	public void displayErrors(List<CodeError> codeErrors) {

		String toDisplay = "";

		for (Iterator<CodeError> iterator = codeErrors.iterator(); iterator.hasNext();) {
			CodeError codeError = (CodeError) iterator.next();
			toDisplay = toDisplay + "line: " + codeError.getLine() + "| Message: " + codeError.getMsg() + "\n";
		}

		GUIController.setErrorText(toDisplay);
	}

	public void createNew(InputType newIn, OutputType newOut) {
		for (Iterator<ElectionDescriptionChangeListener> iterator = listeners.iterator(); iterator.hasNext();) {
			ElectionDescriptionChangeListener listener = (ElectionDescriptionChangeListener) iterator.next();
			listener.inputChanged(newIn);
			listener.outputChanged(newOut);
		}
	}

	public void addListener(ElectionDescriptionChangeListener listener) {
		listeners.add(listener);
	}

	public void setNewElectionDescription(ElectionDescription newDescription) {
		
		this.elecDescription = newDescription;
		
		if (newDescription.isNew()) {
			String declarationString = CCodeHelper.generateDeclString(newDescription.getContainer());

			lockedLineStart = 1;

			lockedLineEnd = lockedLineStart + declarationString.length();

			this.replaceText("\n" + declarationString + "\n\n}");
			
			lockedBracePos = lockedLineEnd + 2;
			
			this.elecDescription.setNotNew();
		} else {

			this.replaceText(newDescription.getCodeAsString());
			
			lockedLineStart = newDescription.getLockedLineStart();
			
			lockedLineEnd = newDescription.getLockedLineEnd();
			
			lockedBracePos = newDescription.getLockedBracePos();

			this.setStyleSpans(0, computeHighlighting(this.getText()));

			saverLoader.resetHasSaveFile();
		}

		this.elecDescription.setLockedPositions(lockedLineStart, lockedLineEnd, lockedBracePos);
		
		this.setStyleSpans(0, computeHighlighting(this.getText()));

		saverLoader.resetHasSaveFile();

		this.getUndoManager().forgetHistory(); // force the undo manager to not be able to return to previous text
		// or delete the locked lines

		test();
	}

//	public void setElectionDescription(ElectionDescription newDescription) {
//		this.elecDescription = newDescription;
//
//		this.replaceText(newDescription.getCodeAsString());
//
//		this.setStyleSpans(0, computeHighlighting(this.getText()));
//
//		saverLoader.resetHasSaveFile();
//	}

	public void bringToFront() {
		GUIController.getController().getMainTabPane().getSelectionModel()
				.select(GUIController.getController().getCodeTab());

		List<CodeError> errors = CVariableErrorFinder.findErrors(elecDescription.getDeepCopy().getCode());

		displayErrors(errors);
	}

	@Override
	public void open() {
		String json = saverLoader.load();

		openJson(json, true);
	}

	private void openJson(String json, boolean bringToFront) {
		if (!json.equals("")) {

			ElectionDescription newDescription = null;

			try {
				newDescription = electionSaverLoader.createFromSaveString(json);
			} catch (Exception e) {
				System.err.println(e.getMessage());
			}

			if (newDescription != null) {
				setNewElectionDescription(newDescription);

				if (bringToFront) {
					bringToFront();
				}
			}

		}
	}

	public void open(File elecDescFile) {
		String json = saverLoader.load(elecDescFile);
		openJson(json, false);
	}

	@Override
	public void save() {
		ElectionDescription toSave = elecDescription.getDeepCopy();

		String json = electionSaverLoader.createSaveString(toSave);

		saverLoader.save("", json);
	}

	@Override
	public void saveAs() {

		ElectionDescription toSave = elecDescription.getDeepCopy();

		String json = electionSaverLoader.createSaveString(toSave);

		saverLoader.saveAs("", json);
	}

	public void saveAs(File file) {
		ElectionDescription toSave = elecDescription.getDeepCopy();

		String json = electionSaverLoader.createSaveString(toSave);

		saverLoader.saveAs(file, json);
	}

	@Override
	public void undo() {
		undo(null);
		super.undo();
	}

	@Override
	public void redo() {
		redo(null);
		super.redo();
	}

	@Override
	public void cut() {
		cut(null);
	}

	@Override
	public void copy() {
		super.copy();
	}

	@Override
	public void paste() {
		this.paste(null);
	}

	@Override
	public void delete() {
		this.delete(null);
	}

	public void resetSaveFile() {
		this.saverLoader.resetHasSaveFile();
	}

	@Override
	public void autoComplete() {
		Triplet<List<String>, Integer, Integer> completion = getCompletions(recommendations);
		processAutocompletion(completion.first, completion.second, completion.third);
	}

	@Override
	public void insertAutoCompletion(int start, int end, String toInsert) {
		lockedLineSaveInsertText(toInsert, false, false);
	}
}
