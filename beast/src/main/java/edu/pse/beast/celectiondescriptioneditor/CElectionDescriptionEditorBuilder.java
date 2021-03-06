/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.text.BadLocationException;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.CElectionCodeArea;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.CElectionCodeAreaBuilder;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling.CErrorDisplayer;
import edu.pse.beast.celectiondescriptioneditor.ElectionTemplates.ElectionTemplateHandler;
import edu.pse.beast.celectiondescriptioneditor.UserActions.ElectionCopyUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.ElectionCutUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.ElectionPasteUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.ElectionRedoUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.ElectionUndoUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.LoadElectionUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.NewElectionUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.PresentOptionsUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.SaveAsElectionUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.SaveElectionUserAction;
import edu.pse.beast.celectiondescriptioneditor.UserActions.StaticErrorFindingUserAction;
import edu.pse.beast.celectiondescriptioneditor.View.CCodeEditorWindow;
import edu.pse.beast.celectiondescriptioneditor.View.CEditorWindowStarter;
import edu.pse.beast.celectiondescriptioneditor.View.ErrorWindow;
import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.pluginhandler.TypeLoader;
import edu.pse.beast.saverloader.ElectionDescriptionSaverLoader;
import edu.pse.beast.saverloader.FileChooser;
import edu.pse.beast.toolbox.ActionIdAndListener;
import edu.pse.beast.toolbox.CCodeHelper;
import edu.pse.beast.toolbox.ImageResourceProvider;
import edu.pse.beast.toolbox.ObjectRefsForBuilder;
import edu.pse.beast.toolbox.UserAction;
import edu.pse.beast.types.InputType;
import edu.pse.beast.types.OutputType;

/**
 * This class creates a celectiondescriptioneditor object and all useractions
 * which can be performed on this object
 * 
 * @author Holger-Desktop
 */
public class CElectionDescriptionEditorBuilder {

    private final String[] menuHeadingIds = {"file", "edit", "editor", "code" };
    private CElectionCodeAreaBuilder codeAreaBuilder;
    private UserAction newAcc;
    private UserAction load;
    private UserAction save;
    private UserAction saveAs;
    private UserAction options;
    private UserAction staticErrorFinding;
    private UserAction undo;
    private UserAction redo;
    private UserAction cut;
    private UserAction copy;
    private UserAction paste;

    /**
     * creates a CElectionDescriptionEditor object and returns it
     * 
     * @param objRefsForBuilder
     *            the references to interfaces needed to build objects
     * @return the created editor
     */
    public CElectionDescriptionEditor createCElectionDescriptionEditor(ObjectRefsForBuilder objRefsForBuilder) {
        CEditorWindowStarter starter = new CEditorWindowStarter();
        CCodeEditorWindow window = starter.getGUIWindow();
        window.updateStringRes(objRefsForBuilder.getStringIF());

        // create new ErrorWindow
        ErrorWindow errorWindow = new ErrorWindow(window.getErrorPane(), objRefsForBuilder.getStringIF());
        codeAreaBuilder = new CElectionCodeAreaBuilder(objRefsForBuilder);

        // codeAreaObject.setSyntaxHLRegexAndColorList()
        CElectionCodeArea codeArea = codeAreaBuilder.createCElectionCodeArea(window.getCodeArea(),
                window.getCodeAreaScrollPane(),
                new CErrorDisplayer(window.getCodeArea(), objRefsForBuilder.getStringIF()));

        // create FileChooser
        FileChooser fileChooser = new FileChooser(
                objRefsForBuilder.getStringIF().getCElectionEditorStringResProvider().getMenuStringRes(),
                new ElectionDescriptionSaverLoader(), window);

        // create new ChangeHandler
        CElectionDescriptionEditorChangeHandler cElectionDescriptionEditorChangeHandler
            = new CElectionDescriptionEditorChangeHandler(
                codeArea.getPane());

        // create new CElectionEditor
        CElectionDescriptionEditor editor = new CElectionDescriptionEditor(codeArea, window, codeAreaBuilder,
                errorWindow, cElectionDescriptionEditorChangeHandler, objRefsForBuilder.getStringIF(), fileChooser,
                objRefsForBuilder);

        CElectionEditorMenubarHandler menuBarHandler = new CElectionEditorMenubarHandler(menuHeadingIds, window,
                createActionIdAndListenerList(objRefsForBuilder, editor), objRefsForBuilder.getStringIF());

        // toolbar: new load save save_as copy cut paste undo redo
        ActionIdAndListener[] idAndListener = {createFromUserAction(newAcc), createFromUserAction(load),
                createFromUserAction(save), createFromUserAction(saveAs), createFromUserAction(undo),
                createFromUserAction(redo), createFromUserAction(cut), createFromUserAction(copy),
                createFromUserAction(paste) };

        ImageResourceProvider imageRes = ImageResourceProvider.getToolbarImages();

        CElectionEditorToolbarHandler toolbarHandler = new CElectionEditorToolbarHandler(idAndListener, imageRes,
                objRefsForBuilder.getStringIF(), window);

        starter.start();

        ElectionTemplateHandler templateHandler = new ElectionTemplateHandler();

        try {
        	
        	InputType inType = TypeLoader.getStandartInputType();
        	
        	OutputType outType = TypeLoader.getStandartOutputType();
        	
        	ElectionTypeContainer container = new ElectionTypeContainer(inType, outType);
        	
            editor.loadElectionDescription(new CCodeHelper().generateElectionDescription(container,
                    "new_election", templateHandler,
                    objRefsForBuilder.getStringIF().getCElectionEditorStringResProvider().getElectionStringRes()));
        } catch (BadLocationException ex) {
            Logger.getLogger(CElectionDescriptionEditorBuilder.class.getName()).log(Level.SEVERE, null, ex);
        }
        objRefsForBuilder.getLanguageOpts().addStringDisplayer(editor);

        editor.setcElectionEditorMenubarHandler(menuBarHandler);
        editor.setcElectionEditorToolbarHandler(toolbarHandler);
        return editor;
    }

    private ArrayList<ArrayList<ActionIdAndListener>> createActionIdAndListenerList(
            ObjectRefsForBuilder objRefsForBuilder, CElectionDescriptionEditor editor) {
        ArrayList<ArrayList<ActionIdAndListener>> created = new ArrayList<>();

        ArrayList<ActionIdAndListener> fileList = new ArrayList<>();

        newAcc = createNewElectionUserAction(editor);
        load = createLoadElectionUserAction(editor);
        saveAs = createSaveAsElectionUserAction(editor);
        save = createSaveElectionUserAction(editor);
        undo = createElectionUndoUserAction(editor);
        redo = createElectionRedoUserAction(editor);
        cut = createElectionCutUserAction(editor);
        copy = createElectionCopyUserAction(editor);
        paste = createElectionPasteUserAction(editor);

        editor.addUserAction('n', newAcc);
        editor.addUserAction('o', load);
        editor.addUserAction('s', save);
        editor.addUserAction('x', cut);
        editor.addUserAction('c', copy);
        editor.addUserAction('v', paste);

        fileList.add(createFromUserAction(newAcc));
        fileList.add(createFromUserAction(load));
        fileList.add(createFromUserAction(save));
        fileList.add(createFromUserAction(saveAs));

        ArrayList<ActionIdAndListener> editList = new ArrayList<>();
        editList.add(createFromUserAction(undo));
        editList.add(createFromUserAction(redo));
        editList.add(createFromUserAction(cut));
        editList.add(createFromUserAction(copy));
        editList.add(createFromUserAction(paste));

        ArrayList<ActionIdAndListener> editorList = new ArrayList<>();
        options = createPresentOptionsUserAction(objRefsForBuilder, editor);
        editorList.add(createFromUserAction(options));

        ArrayList<ActionIdAndListener> codeList = new ArrayList<>();
        staticErrorFinding = createStaticErrorFindingUserAction(editor);
        codeList.add(createFromUserAction(staticErrorFinding));

        created.add(fileList);
        created.add(editList);
        created.add(editorList);
        created.add(codeList);

        return created;
    }
    // file

    private NewElectionUserAction createNewElectionUserAction(CElectionDescriptionEditor editor) {
        return new NewElectionUserAction(editor);
    }

    private SaveElectionUserAction createSaveElectionUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new SaveElectionUserAction(cElectionDescriptionEditor);
    }

    private SaveAsElectionUserAction createSaveAsElectionUserAction(
            CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new SaveAsElectionUserAction(cElectionDescriptionEditor);
    }

    private LoadElectionUserAction createLoadElectionUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new LoadElectionUserAction(cElectionDescriptionEditor);
    }

    private ElectionCopyUserAction createElectionCopyUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new ElectionCopyUserAction(cElectionDescriptionEditor);
    }

    private ElectionCutUserAction createElectionCutUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new ElectionCutUserAction(cElectionDescriptionEditor);
    }

    private ElectionPasteUserAction createElectionPasteUserAction(
            CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new ElectionPasteUserAction(cElectionDescriptionEditor);
    }

    private ElectionRedoUserAction createElectionRedoUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new ElectionRedoUserAction(cElectionDescriptionEditor);
    }

    private ElectionUndoUserAction createElectionUndoUserAction(CElectionDescriptionEditor cElectionDescriptionEditor) {
        return new ElectionUndoUserAction(cElectionDescriptionEditor);
    }

    // editor
    private PresentOptionsUserAction createPresentOptionsUserAction(ObjectRefsForBuilder refs,
            CElectionDescriptionEditor editor) {
        return new PresentOptionsUserAction(refs.getOptionIF().getCElectionEditorOptions(editor),
                refs.getOptionIF().getOptionPresenter(refs));
    }

    // code
    private StaticErrorFindingUserAction createStaticErrorFindingUserAction(CElectionDescriptionEditor editor) {
        return new StaticErrorFindingUserAction(editor);
    }

    private ActionIdAndListener createFromUserAction(UserAction userAc) {
        return new ActionIdAndListener(userAc.getId(), new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                userAc.perform();
            }
        });
    }
}
