package edu.pse.beast.booleanexpeditor;

import edu.pse.beast.celectiondescriptioneditor.GUI.CCodeEditorGUI;
import edu.pse.beast.highlevel.DisplaysStringsToUser;
import edu.pse.beast.stringresource.StringLoaderInterface;
import edu.pse.beast.toolbox.ActionIdAndListener;
import edu.pse.beast.toolbox.MenuBarHandler;

import java.util.ArrayList;

/**
 * MenuBarHandler for the BooleanExpEditor.
 * @author NikolaiLMS
 */
public class BooleanExpEditorMenubarHandler extends MenuBarHandler implements DisplaysStringsToUser{
    private BooleanExpEditorWindow window;

    /**
     * Constructor
     * @param headingIds the String Array of header IDs for the Menus
     * @param window the BooleanExpEditorWindow object whos menu this class handles
     * @param actionIdAndListener the ActionIdAndListener ArrayListArrayList which contains UserActions and
     *                            Actionlisteners for this menu
     * @param stringif the StringLoaderInterface to load the language dependent strings for this menu
     */
    BooleanExpEditorMenubarHandler(String[] headingIds, BooleanExpEditorWindow window,
            ArrayList<ArrayList<ActionIdAndListener>> actionIdAndListener, StringLoaderInterface stringif) {
        super(headingIds, actionIdAndListener, stringif.getBooleanExpEditorStringResProvider().getMenuStringRes());
        this.window = window;
        this.window.setJMenuBar(createdMenuBar);
    }

    @Override
    public void updateStringRes(StringLoaderInterface stringResIF) {
        updateStringResLoader(stringResIF.getBooleanExpEditorStringResProvider().getMenuStringRes());
        window.setJMenuBar(createdMenuBar);
    }
}
