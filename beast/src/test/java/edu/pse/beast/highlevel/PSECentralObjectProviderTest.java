package edu.pse.beast.highlevel;

import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

/**
 *
 * @author Jonas
 */
public class PSECentralObjectProviderTest {
    
    
    /**
     * Test the creation of all objects by the class PSECentralObjectProvider.
     */
    @Test
    public void testGetElectionDescriptionSource() {
        System.out.println("createObjects - PSECentralObjectProvider");
        PSECentralObjectProvider instance = new PSECentralObjectProvider(new BEASTCommunicator());
        ElectionDescriptionSource failResultEDS = null;
        ElectionDescriptionSource resultEDS = instance.getElectionDescriptionSource();
        assertNotEquals(failResultEDS, resultEDS);
        PreAndPostConditionsDescriptionSource failResultPNP = null;
        PreAndPostConditionsDescriptionSource resultPNP = instance.getPreAndPostConditionsSource();
        assertNotEquals(failResultPNP, resultPNP);
        ResultCheckerCommunicator failResultRCC = null;
        ResultCheckerCommunicator resultRCC = instance.getResultCheckerCommunicator();
        assertNotEquals(failResultRCC, resultRCC);
        ParameterSource failResultPS = null;
        ParameterSource resultPS = instance.getParameterSrc();
        assertNotEquals(failResultPS, resultPS);
        ResultPresenter failResultRP = null;
        ResultPresenter resultRP = instance.getResultPresenter();
        assertNotEquals(failResultRP, resultRP);
        MainNotifier failResultMN = null;
        MainNotifier resultMN = instance.getMainNotifier();
        assertNotEquals(failResultMN, resultMN);
        CheckStatusDisplay failResultCSD = null;
        CheckStatusDisplay resultCSD = instance.getCheckStatusDisplay();
        assertNotEquals(failResultCSD, resultCSD);
    }
    
}
