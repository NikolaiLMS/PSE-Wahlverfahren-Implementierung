
package edu.pse.beast.highlevel;

import edu.pse.beast.celectiondescriptioneditor.CElectionDescriptionEditor;
import edu.pse.beast.parametereditor.ParameterEditor;

/**
 * The CentralObjectProvider provides access to the interfaces for the packages used 
 * to run BEAST for the BEASTCommunicator.
 * @author Jonas
 */
public interface CentralObjectProvider {
    /**
     * Provides access to the ElectionDescriptionSource
     * @return ElectionDescriptionSource to which access is needed
     */
    ElectionDescriptionSource getElectionDescriptionSource();
    /**
     * Provides access to the PreAndPostConditionsDescriptionSource
     * @return PreAndPostConditionsDescriptionSource to which access is needed
     */
    PreAndPostConditionsDescriptionSource getPreAndPostConditionsSource();
    /**
     * Provides access to the ResultCheckerCommunicator
     * @return ResultCheckerCommunicator to which access is needed
     */
    ResultCheckerCommunicator getResultCheckerCommunicator();
    /**
     * Provides access to the ParameterSource
     * @return ParameterSource to which access is needed
     */
    ParameterSource getParameterSrc();
    /**
     * Provides access to the ResultPresenter
     * @return ResultPresenter to which access is needed
     */
    ResultPresenter getResultPresenter();
    /**
     * Provides access to the MainNotifier
     * @return MainNotifier to which access is needed
     */
    MainNotifier getMainNotifier();
    /**
     * Provides access to the CheckStatusDisplay
     * @return CheckStatusDisplay to which access is needed
     */
    CheckStatusDisplay getCheckStatusDisplay();
    
    /**
     * gives access to the ParameterEditor
     * @return the parameter Editor
     */
    ParameterEditor getParameterEditor();
    
    /**
     * gives access to the c election editor
     * @return the c election editor
     */
    CElectionDescriptionEditor getCElectionEditor();
}
