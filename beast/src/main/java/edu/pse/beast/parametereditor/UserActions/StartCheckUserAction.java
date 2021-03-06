package edu.pse.beast.parametereditor.UserActions;

import edu.pse.beast.parametereditor.ParameterEditor;
import edu.pse.beast.toolbox.UserAction;
/**
 * UserAction for starting a check
 * @author Jonas
 */
public class StartCheckUserAction extends UserAction {
    private final ParameterEditor editor;
    /**
     * Constructor
     * @param editor ParameterEditor
     */
    public StartCheckUserAction(ParameterEditor editor) {
        super("start");
        this.editor = editor;
    }

    @Override
    public void perform() {
        if (editor.getReacts()) editor.startCheck();
    }
}
