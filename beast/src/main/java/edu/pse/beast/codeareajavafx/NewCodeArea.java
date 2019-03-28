package edu.pse.beast.codeareajavafx;

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

import com.google.gson.JsonSyntaxException;

import edu.pse.beast.celectiondescriptioneditor.celectioncodearea.errorhandling.CVariableErrorFinder;
import edu.pse.beast.codearea.errorhandling.CodeError;
import edu.pse.beast.datatypes.electiondescription.ElectionDescription;
import edu.pse.beast.datatypes.electiondescription.ElectionDescriptionChangeListener;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.highlevel.javafx.MenuBarInterface;
import edu.pse.beast.saverloader.ElectionDescriptionSaverLoader;
import edu.pse.beast.toolbox.CCodeHelper;
import edu.pse.beast.toolbox.Triplet;
import edu.pse.beast.toolbox.Tuple;
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
    private static final String[] KEYWORDS =
    {
        "auto", "break", "case", "const", "continue", "default",
        "do", "else", "error", "const", "continue", "default",
        "do", "else", "enum", "extern", "for", "goto", "if",
        "return", "signed", "sizeof", "static", "struct", "switch",
        "typedef", "union", "unsigned", "volatile", "while"
    };
    private static final String[] PREPROCESSOR =
    {
        "#define", "#elif", "#endif", "#ifdef", "#ifndef", "#include"
    };
    private static final String[] DATATYPES =
    {
        "char", "double", "enum", "float", "int", "long", "register", "void"
    };
    private static final String KEYWORD_PATTERN = "\\b(" + String.join("|", KEYWORDS) + ")\\b";
    private static final String PREPROCESSOR_PATTERN = "(" + String.join("|", PREPROCESSOR) + ")";
    private static final String DATATYPE_PATTERN = "(" + String.join("|", DATATYPES) + ")";
    private static final String POINTER_PATTERN = "\\b("
            + String.join("|", Arrays.stream(DATATYPES).map(s
                ->
                "\\*[\\s]*" + s).toArray(String[]::new))
            + ")\\b";
    private static final String METHOD_PATTERN = "[\\w]+[\\s]*\\(";
    private static final String INCLUDE_PATTERN = "[.]*include[\\s]+[<|\"].+\\.[\\w]*[>|\"]";
    private static final String PAREN_PATTERN = "\\(|\\)";
    private static final String BRACE_PATTERN = "\\{|\\}";
    private static final String BRACKET_PATTERN = "\\[|\\]";
    private static final String SEMICOLON_PATTERN = "\\;";
    private static final String STRING_PATTERN = "\"([^\"\\\\]|\\\\.)*\"";
    private static final String COMMENT_PATTERN = "//[^\n]*" + "|" + "/\\*(.|\\R)*?\\*/";

    private static final String KEYWORD_STRING = "KEYWORD";
    private static final String PREPROCESSOR_STRING = "PREPROCESSOR";
    private static final String DATATYPE_STRING = "DATATYPE";
    private static final String POINTER_STRING = "POINTER";
    private static final String METHOD_STRING = "METHOD";
    private static final String INCLUDE_STRING = "INCLUDE";
    private static final String PAREN_STRING = "PAREN";
    private static final String BRACE_STRING = "BRACE";
    private static final String BRACKET_STRING = "BRACKET";
    private static final String SEMICOLON_STRING = "SEMICOLON";
    private static final String STRING_STRING = "STRING";
    private static final String COMMENT_STRING = "COMMENT";

    private static final Pattern PATTERN = Pattern.compile(
            "(?<" + KEYWORD_STRING + ">" + KEYWORD_PATTERN + ")"
            + "|(?<" + PREPROCESSOR_STRING + ">" + PREPROCESSOR_PATTERN + ")"
            + "|(?<" + DATATYPE_STRING + ">" + DATATYPE_PATTERN + ")"
            + "|(?<" + POINTER_STRING + ">" + POINTER_PATTERN + ")"
            + "|(?<" + METHOD_STRING  + ">" + METHOD_PATTERN + ")"
            + "|(?<" + INCLUDE_STRING + ">" + INCLUDE_PATTERN + ")"
            + "|(?<" + PAREN_STRING + ">" + PAREN_PATTERN + ")"
            + "|(?<" + BRACE_STRING + ">" + BRACE_PATTERN + ")"
            + "|(?<" + BRACKET_STRING + ">" + BRACKET_PATTERN + ")"
            + "|(?<" + SEMICOLON_STRING + ">" + SEMICOLON_PATTERN + ")"
            + "|(?<" + STRING_STRING + ">" + STRING_PATTERN + ")"
            + "|(?<" + COMMENT_STRING + ">" + COMMENT_PATTERN + ")");

    private static final String RESOURCE = "codeAreaSyntaxHighlight.css";

    private static Set<String> recommendations = new TreeSet<String>();

    final KeyCombination selectAllCombination =
            new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN); // select
    // all
    final KeyCombination backspaceCombination =
            new KeyCodeCombination(KeyCode.BACK_SPACE); // backspace
    final KeyCombination deleteCombination =
            new KeyCodeCombination(KeyCode.DELETE); // delete
    final KeyCombination enterCombination =
            new KeyCodeCombination(KeyCode.ENTER); // enter
    final KeyCombination undoCombination =
            new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN); // undo
    final KeyCombination redoCombination =
            new KeyCodeCombination(KeyCode.Y, KeyCombination.CONTROL_DOWN); // redo
    final KeyCombination pasteCombination =
            new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN); // paste
    final KeyCombination cutCombination =
            new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN); // paste
    final KeyCombination tabulatorCombination =
            new KeyCodeCombination(KeyCode.TAB); // tab key

    private final SaverLoader saverLoader;
    private final ElectionDescriptionSaverLoader electionSaverLoader =
            new ElectionDescriptionSaverLoader();

    private ElectionDescription elecDescription;
    private List<ElectionDescriptionChangeListener> listeners =
            new ArrayList<ElectionDescriptionChangeListener>();
    private int lockedLineStart = 0;
    private int lockedLineEnd = 10;
    private int lockedBracePos;
    // private int amountTabs = 0;
    private int spacesPerTab = 4;

    public NewCodeArea() {
        // add all standard recommendations
        recommendations.addAll(Arrays.asList(KEYWORDS));
        recommendations.addAll(Arrays.asList(PREPROCESSOR));
        recommendations.addAll(Arrays.asList(DATATYPES));
        saverLoader = new SaverLoader(".elec", "BEAST election description");
        ElectionDescription startElecDescription =
                new ElectionDescription("New description", new SingleChoice(),
                                        new SingleCandidate(), 0, 0, 0, 0, true);
        this.setNewElectionDescription(startElecDescription);
        saverLoader.resetHasSaveFile();
        List<String> code = new ArrayList<String>();
        code.add("");
        // code.add(source.getContainer().getInputType().);
        String sampleCode = "";
        String stylesheet =
                this.getClass().getResource(RESOURCE).toExternalForm();
        this.getStylesheets().add(stylesheet);
        IntFunction<Node> lineNumbers = LineNumberFactory.get(this);
        this.setParagraphGraphicFactory(lineNumbers);
        this.addEventFilter(KeyEvent.KEY_TYPED, event -> {
            if (event != null) {
                event.consume();
            }
            String replacement = "";
            String value = (event.getCharacter()).replaceAll("\\p{Cntrl}", "");
            System.out.println("value: " + event.getCharacter());
            System.out.println(value.equals("\t"));
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
                lockedLineSafeInsertText(replacement, false, false, null);
            }
        });

        this.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            if (event != null) {
                event.consume();
            }
        });

        this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            System.out.println(event.getCode());
            if (selectAllCombination.match(event)) { // we just want to select all
                this.selectAll();
                if (event != null) {
                    event.consume();
                }
            } else if (backspaceCombination.match(event)) {
                lockedLineSafeInsertText("", true, false, null);
                if (event != null) {
                    event.consume();
                }
            } else if (deleteCombination.match(event)) {
                delete(event);
            } else if (enterCombination.match(event)) {
                lockedLineSafeInsertText("\n", false, false, null);
                if (event != null) {
                    event.consume();
                }
            } else if (pasteCombination.match(event)) {
                paste(event);
            } else if (cutCombination.match(event)) {
                cut(event);
            } else if (tabulatorCombination.match(event)) {
                String whitespaces = new String(new char[spacesPerTab]).replace("\0", " ");
                lockedLineSafeInsertText(whitespaces, false, false, null);
                if (event != null) {
                    event.consume();
                }
            }
        });

        this.richChanges().filter(ch
            -> !ch.getInserted().equals(ch.getRemoved())).subscribe(
                change -> {
                    this.setStyleSpans(0, computeHighlighting(this.getText()));
                    elecDescription.setCode(this.getText());
                });
        this.replaceText(0, 0, sampleCode);
    }

    /**
     * @param event
     */
    private void delete(KeyEvent event) {
        lockedLineSafeInsertText("", false, true, null);
        if (event != null) {
            event.consume();
        }
    }

    /**
     * @param event
     */
    private void paste(KeyEvent event) {
        String clipboardText = "";
        try {
            clipboardText =
                    (String)
                        Toolkit.getDefaultToolkit()
                        .getSystemClipboard().getData(DataFlavor.stringFlavor);
        } catch (HeadlessException e) {
            return;
        } catch (UnsupportedFlavorException e) {
            return;
        } catch (IOException e) {
            return;
        }
        lockedLineSafeInsertText(clipboardText, false, false, null);
        if (event != null) {
            event.consume();
        }
    }

    /**
     * @param event
     */
    private void cut(KeyEvent event) {
        String selectedText = this.getSelectedText();
        boolean isValid = lockedLineSafeInsertText("", true, false, null);
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
        }
    }

    /**
     * tries to insert text into this text pane. It wont override locked lines
     *
     * @param replacement the text to be inserted
     * @param backspace   if the backspace key was pressed
     * @param delete      if the delete key was pressed
     * @param tuple       alternative variable to set the bounds where text should
     *                    be replaced. If it is null, the area to replace will be
     *                    inferred from the cursor or the marked area
     * @return
     */
    private boolean lockedLineSafeInsertText(String replacement, boolean backspace, boolean delete,
            Tuple<Integer, Integer> tuple) {
        int selectionStart = 0;
        int selectionEnd = 0;

        if (tuple != null) {
            selectionStart = tuple.x;
            selectionEnd = tuple.y;
        } else {
            IndexRange selectionRange = this.getSelection();
            selectionStart = selectionRange.getStart();
            selectionEnd = selectionRange.getEnd();
        }
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

        boolean notOverlapping =
                ((selectionEnd < lockedLineStart) || (lockedLineEnd <= selectionStart))
                && (((selectionEnd <= lockedBracePos) || (lockedBracePos < selectionStart)));
        if (notOverlapping) {
            this.replaceText(selectionStart, selectionEnd, replacement);
            int lengthChange = replacement.length() - selectionLength;
            updateLockedLineNumber(selectionEnd, lengthChange);
            return true;
        } else if (selectionEnd == lockedLineStart
                && (replacement.endsWith("\n") || backspace || delete)) {
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
            // do not increase anything
        }
        test();
    }

    private void test() {
        Set<String> style = Collections.singleton("-fx-background-fill: blue;");
        // this.setStyle(paragraph, style);
        // this.setStyle(value);
        // this.setStyle("-fx-background-color: blue;");
        this.setStyle(lockedLineStart, lockedLineEnd, style);
        // this.setStyle(lockedLineStart, lockedLineEnd, style);
        // this.setStyle(lockedBracePos, lockedBracePos + 1, style);
    }

    private static StyleSpans<Collection<String>> computeHighlighting(String text) {
        Matcher matcher = PATTERN.matcher(text);
        int lastKwEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass =
                matcher.group(KEYWORD_STRING) != null
                ? KEYWORD_STRING.toLowerCase()
                    : matcher.group(PREPROCESSOR_STRING) != null
                    ? PREPROCESSOR_STRING.toLowerCase()
                        : matcher.group(METHOD_STRING) != null
                        ? METHOD_STRING.toLowerCase()
                            : matcher.group(DATATYPE_STRING) != null
                            ? DATATYPE_STRING.toLowerCase()
                                : matcher.group(POINTER_STRING) != null
                                ? POINTER_STRING.toLowerCase()
                                    : matcher.group(INCLUDE_STRING) != null
                                    ? INCLUDE_STRING.toLowerCase()
                                        : matcher.group(PAREN_STRING) != null
                                        ? PAREN_STRING.toLowerCase()
                                            : matcher.group(BRACE_STRING) != null
                                            ? BRACE_STRING.toLowerCase()
                                                : matcher.group(BRACKET_STRING) != null
                                                ? BRACKET_STRING.toLowerCase()
                                                    : matcher.group(SEMICOLON_STRING) != null
                                                    ? SEMICOLON_STRING.toLowerCase()
                                                        : matcher.group(STRING_STRING) != null
                                                        ? STRING_STRING.toLowerCase()
                                                            : matcher.group(COMMENT_STRING) != null
                                                            ? COMMENT_STRING.toLowerCase()
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
     * @return a deep copy of the election description, which does not update when
     *         the text field gets another input
     */
    public ElectionDescription getElectionDescription() {
        return elecDescription.getDeepCopy();
    }

    public void displayErrors(List<CodeError> codeErrors) {
        String toDisplay = "";
        for (Iterator<CodeError> iterator = codeErrors.iterator(); iterator.hasNext();) {
            CodeError codeError = (CodeError) iterator.next();
            toDisplay +=
                    "line: " + codeError.getLine()
                    + "| Message: " + codeError.getMsg() + "\n";
        }
        GUIController.setErrorText(toDisplay);
    }

    public void createNew(InputType newIn, OutputType newOut) {
        for (Iterator<ElectionDescriptionChangeListener> iterator = listeners.iterator();
                iterator.hasNext();) {
            ElectionDescriptionChangeListener listener =
                    (ElectionDescriptionChangeListener) iterator.next();
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
            String declarationString =
                    CCodeHelper.generateDeclString(newDescription.getContainer());
            lockedLineStart = 0;
            lockedLineEnd = lockedLineStart + declarationString.length();
            this.replaceText(declarationString + "\n\n}");
            lockedBracePos = this.getText().length() - 1;
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
        // force the undo manager to not be able to return to previous text
        this.getUndoManager().forgetHistory();
        // or delete the locked lines
        test();
    }

    // public void setElectionDescription(ElectionDescription newDescription) {
    // this.elecDescription = newDescription;
    //
    // this.replaceText(newDescription.getCodeAsString());
    //
    // this.setStyleSpans(0, computeHighlighting(this.getText()));
    //
    // saverLoader.resetHasSaveFile();
    // }

    public void bringToFront() {
        GUIController.getController().getMainTabPane().getSelectionModel()
                .select(GUIController.getController().getCodeTab());
        List<CodeError> errors =
                CVariableErrorFinder.findErrors(elecDescription.getDeepCopy().getCode());
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
            } catch (JsonSyntaxException e) {
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
        lockedLineSafeInsertText(toInsert, false, false, new Tuple<Integer, Integer>(start, end));
    }
}