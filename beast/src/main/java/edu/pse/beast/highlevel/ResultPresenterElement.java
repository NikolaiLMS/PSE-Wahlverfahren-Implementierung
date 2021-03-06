package edu.pse.beast.highlevel;

import java.util.List;

import edu.pse.beast.propertychecker.Result;

/**
 * Element which is specifically used to display a result of a check to the user.
 * @author Lukas
 *
 */
public interface ResultPresenterElement {
    
    /**
     * Presents that a timeOut stopped the checking
     * @param isTimeout true, if it was a timeout, false if it was by the user
     */
    void presentCanceled(boolean isTimeout);
    
    /**
     * Presents that the check was successfull
     */
    void presentSuccess();
    
    /**
     * Presents that the check was a failure
     * @param error what exactly went wrong
     */
    void presentFailure(List<String> error);
    
    /**
     * Presents the example that fails the property
     * @param example the example to be presented
     */
    void presentFailureExample(Result result);

	void present(Result cbmcResult);
}
