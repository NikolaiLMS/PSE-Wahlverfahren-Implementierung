package edu.pse.beast.booleanexpeditor.UserActions;

import edu.pse.beast.booleanexpeditor.BooleanExpEditor;
import edu.pse.beast.datatypes.propertydescription.PreAndPostConditionsDescription;
import edu.pse.beast.propertylist.PropertyList;
import edu.pse.beast.propertylist.Model.PropertyItem;
import edu.pse.beast.toolbox.UserAction;

/**
 * UserAction subclass responsible loading a PostAndPreConditionsDescription object from the file system into the editor
 * /propertylist.
 * @author NikolaiLMS
 */
public class LoadPropsUserAction extends UserAction {

    private final BooleanExpEditor booleanExpEditor;
    private PropertyList propertyList;

    /**
     * Constructor
     * @param booleanExpEditor the BooleanExpEditor object
     */
    public LoadPropsUserAction(BooleanExpEditor booleanExpEditor) {
        super("load");
        this.booleanExpEditor = booleanExpEditor;
    }

    /**
     * Second constructor used by PropertyListWindow
     * @param booleanExpEditor the BooleanExpEditor object
     * @param controller the PropertyList controller object
     */
    public LoadPropsUserAction(BooleanExpEditor booleanExpEditor, PropertyList controller) {
        super("load");
        this.booleanExpEditor = booleanExpEditor;
        this.propertyList = controller;
    }

    @Override
    public void perform() {
        if (booleanExpEditor.getChangeHandler().hasChanged() && booleanExpEditor.getLoadedFromPropertyList()) {
            if (!booleanExpEditor.getFileChooser().openSaveChangesDialog(booleanExpEditor.
                    getCurrentlyLoadedPreAndPostCondition())) {
                return;
            }
        }
        PreAndPostConditionsDescription loadedPreAndPostConditionsDescription
                = (PreAndPostConditionsDescription) booleanExpEditor.getFileChooser().loadObject();
        if (loadedPreAndPostConditionsDescription != null) {
            booleanExpEditor.loadNewProperties(loadedPreAndPostConditionsDescription);
            booleanExpEditor.getPropertyListController().addDescription(new PropertyItem(
                    loadedPreAndPostConditionsDescription));
        }
    }

    /**
     * Method used by PropertyListWindow
     */
    public void loadIntoPropertyList() {
        PreAndPostConditionsDescription loadedPreAndPostConditionsDescription
                = (PreAndPostConditionsDescription) booleanExpEditor.getFileChooser().loadObject();
        if (loadedPreAndPostConditionsDescription != null) {
            booleanExpEditor.loadNewProperties(loadedPreAndPostConditionsDescription);
            propertyList.addDescription(new PropertyItem(loadedPreAndPostConditionsDescription));
        }
    }
}
