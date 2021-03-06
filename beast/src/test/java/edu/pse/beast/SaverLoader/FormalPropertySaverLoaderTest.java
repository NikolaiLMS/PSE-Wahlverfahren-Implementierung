package edu.pse.beast.SaverLoader;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.pse.beast.datatypes.propertydescription.FormalPropertiesDescription;
import edu.pse.beast.saverloader.StaticSaverLoaders.FormalPropertySaverLoader;

/**
 * JUnit Testclass for saverloader.StaticSaverLoaders.FormalPropertySaverLoader.
 * @author NikolaiLMS
 */
public class FormalPropertySaverLoaderTest {
    private static FormalPropertiesDescription description;

    @BeforeClass
    public static void setUpClass() {
        description = new FormalPropertiesDescription("CODECODEOCDEOASD ASDAOSDASOD ;;; ;ASODAOSD");
    }

    /**
     * Tests the FormalElectionDescriptionSaverLoader by creating a saveString from a
     * FormalPropertiesDescription object, then recreating that object from the saveString and checking its
     * integrity.
     */
    @Test
    public void testCreateFromSaveString() throws Exception {
        String saveString = FormalPropertySaverLoader.createSaveString(description);
        FormalPropertiesDescription recreatedFormalPropertiesDescription =
                FormalPropertySaverLoader.createFromSaveString(saveString);

        assert (recreatedFormalPropertiesDescription.getCode().equals("CODECODEOCDEOASD ASDAOSDASOD ;;; ;ASODAOSD"));
    }
}
