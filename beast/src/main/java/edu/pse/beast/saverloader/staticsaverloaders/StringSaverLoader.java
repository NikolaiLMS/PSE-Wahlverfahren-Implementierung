package edu.pse.beast.saverloader.staticsaverloaders;

/**
 * Implements static methods for creating saveStrings from Strings and vice
 * versa. Replaces certain characters to ensure custom user inputs. Note that
 * methods are static for convenience.
 *
 * @author Nikolai Schnell
 */
public final class StringSaverLoader {
    /**
     * Instantiates a new string saver loader.
     */
    private StringSaverLoader() { }

    /**
     * Creates a String from a given, by createSaveString() generated,
     * saveString.
     *
     * @param saveString
     *            the saveString
     * @return the String
     */
    public static String createFromSaveString(final String saveString) {
        return saveString.replace(">>", ">");
    }

    /**
     * Creates a String from a given String, that can then be saved to a file
     * and later given to createFromSaveString() to retrieve the saved object.
     *
     * @param string
     *            the String
     * @return the saveString
     */
    public static String createSaveString(final String string) {
        return string.replace(">", ">>");
    }
}
