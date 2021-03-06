package edu.pse.beast.SaverLoader;

import org.junit.BeforeClass;
import org.junit.Test;

import edu.pse.beast.saverloader.StaticSaverLoaders.StringSaverLoader;


/**
 * JUnit Testclass for saverloader.StaticSaverLoaders.StringSaverLoader.
 * @author NikolaiLMS
 */
public class StringSaverLoaderTest {
    private static String testString;

    @BeforeClass
    public static void setUpClass() {
        testString = "a fsfdgfd gfdko gfd gfd" +
                "g\fdgdfg\n\n???>>>><<>><";
    }

    /**
     * Tests the StringSaverLoader by creating a saveString from a String, then recreating
     * that object from the saveString and checking its integrity.
     */
    @Test
    public void testSaverLoader() {
        String saveString = StringSaverLoader.createSaveString(testString);
        String recreatedString = StringSaverLoader.createFromSaveString(saveString);
        assert (recreatedString.equals(testString));
    }
}
