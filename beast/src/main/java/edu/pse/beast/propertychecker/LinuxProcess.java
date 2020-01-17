package edu.pse.beast.propertychecker;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import edu.pse.beast.toolbox.ErrorForUserDisplayer;
import edu.pse.beast.toolbox.ErrorLogger;
import edu.pse.beast.toolbox.FileLoader;
import edu.pse.beast.toolbox.SuperFolderFinder;
import edu.pse.beast.toolbox.UnifiedNameContainer;

/**
 * The Class LinuxProcess.
 */
public class LinuxProcess extends CBMCProcess {
    /** The Constant QUOTE. */
    private static final String QUOTE = "\"";

    /** The Constant EQUALS. */
    private static final String EQUALS = "=";

    /** The Constant PASS_C_CONST. */
    private static final String PASS_C_CONST = "-D ";

    /** The Constant RELATIVE_PATH_TO_CBMC_64. */
    private static final String RELATIVE_PATH_TO_CBMC_64 = "/linux/cbmcLin/cbmc";

    /** The Constant WAITING_TIME. */
    // the time in milliseconds to wait for the termination of the process on
    // linux
    private static final long WAITING_TIME = 3000;

    /** The Constant ENABLE_USER_INCLUDE. */
    private static final String ENABLE_USER_INCLUDE = "-I";

    /** The Constant USER_INCLUDE_FOLDER. */
    private static final String USER_INCLUDE_FOLDER = "/core/user_includes/";

    // we want to compile all available c files, so the user does not need to
    /** The Constant C_FILE_ENDING. */
    // specify anything
    private static final String C_FILE_ENDING = ".c";

    /**
     * Creates a new CBMC Checker for the windows OS.
     *
     * @param voters
     *            the amount of voters
     * @param candidates
     *            the amount of candidates
     * @param seats
     *            the amount of seats
     * @param advanced
     *            the string that represents the advanced options
     * @param toCheck
     *            the file to check with cbmc
     * @param parent
     *            the parent CheckerFactory, that has to be notified about
     *            finished checking
     * @param result
     *            the result
     */
    public LinuxProcess(final int voters, final int candidates, final int seats,
                        final String advanced, final File toCheck,
                        final CheckerFactory parent, final Result result) {
        super(voters, candidates, seats, advanced, toCheck, parent, result);
    }

    @Override
    protected String sanitizeArguments(final String toSanitize) {
        return toSanitize;
    }

    @Override
    public Process createProcess(final File toCheck, final int voters,
                                 final int candidates, final int seats,
                                 final String advanced) {
        List<String> arguments = new ArrayList<String>();
        String cbmc = QUOTE + new File(
                SuperFolderFinder.getSuperFolder() + RELATIVE_PATH_TO_CBMC_64).getPath()
                + QUOTE;
        // enable the usage of includes in cbmc
        String userIncludeAndPath = QUOTE + ENABLE_USER_INCLUDE
                + SuperFolderFinder.getSuperFolder()
                + USER_INCLUDE_FOLDER + QUOTE;
        // get all Files from the form "*.c" so we can include them into cbmc,
        List<String> allFiles = FileLoader.listAllFilesFromFolder(
                QUOTE + SuperFolderFinder.getSuperFolder()
                + USER_INCLUDE_FOLDER + QUOTE,
                C_FILE_ENDING);
        if (!new File(cbmc.replace(QUOTE, "")).exists()) {
            ErrorForUserDisplayer.displayError(
                    "Cannot find the cbmc program in the subfolder \"linux/cbmcLin/\", "
                            + "please download it from "
                            + "the cbmc website and place it there!");
        } else if (!new File(cbmc.replace(QUOTE, "")).canExecute()) {
            ErrorForUserDisplayer
                    .displayError("This program does not have the privileges "
                            + "to execute this program. \n "
                            + "Please change the access rights for the "
                            + "program \"/linux/cbmcLin/cbmc\" in the "
                            + "BEAST installation folder and try again");
        } else {
            arguments.add(cbmc.replace(QUOTE, ""));
            arguments.add(userIncludeAndPath.replace(QUOTE, ""));
            // wrap it in quotes, in case the path has spaces in it
            arguments.add(toCheck.getAbsolutePath().replace(QUOTE, ""));
            // iterate over all "*.c" files from the include folder, to include
            // them
            for (Iterator<String> iterator = allFiles.iterator();
                    iterator.hasNext();) {
                String toBeIncludedFile = iterator.next();
                arguments.add(toBeIncludedFile.replace(QUOTE, ""));
            }
            // here we supply this call with the correct values for the voters,
            // candidates and seats
            arguments.add(PASS_C_CONST + UnifiedNameContainer.getVoter()
                            + EQUALS + voters);
            arguments.add(PASS_C_CONST + UnifiedNameContainer.getCandidate()
                            + EQUALS + candidates);
            arguments.add(PASS_C_CONST + UnifiedNameContainer.getSeats()
                            + EQUALS + seats);
            // we need the trace command to track the output on the command line
            arguments.add("--xml-ui");
            if (advanced != null && advanced.length() > 0) {
                for (int i = 0; i < advanced.split(";").length; i++) {
                    String sanitized =
                            sanitizeArguments(advanced.split(";")[i]);
                    if (sanitized.trim().length() > 0) {
                        arguments.add(sanitized);
                    }
                }
            }
            Process startedProcess = null;
            ProcessBuilder prossBuild =
                    new ProcessBuilder(arguments.toArray(new String[0]));
            try {
                startedProcess = prossBuild.start();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return startedProcess;
        }
        return null;
    }

    @Override
    protected void stopProcess() {
        if (!getProcess().isAlive()) {
            ErrorLogger.log("Warning, process is not alive anymore");
            return;
        } else {
            getProcess().destroyForcibly();
        }
        try {
            Thread.sleep(WAITING_TIME);
        } catch (InterruptedException e) {
        }
        if (getProcess().isAlive()) {
            ErrorForUserDisplayer.displayError(
                    "Warning, the program was still alive after trying to stop it \n"
                            + "Please kill it manually if it is still alive, especially if "
                            + "it starts taking up a lot of ram");
        }
    }
}
