package edu.pse.beast.saverloader.StaticSaverLoaders;

import java.util.ArrayList;

import edu.pse.beast.datatypes.electioncheckparameter.ElectionCheckParameter;
import edu.pse.beast.datatypes.electioncheckparameter.TimeOut;

/**
 * Implements static methods for creating saveStrings from ElectionCheckParameter objects and vice versa.
 * @author NikolaiLMS
 */
public class ElectionCheckParameterSaverLoader {

    /**
     * Creates a String from a given ElectionCheckParameter, that can then be saved to a file and later given to
     * createFromSaveString() to retrieve the saved object.
     * @param electionCheckParameter the ElectionCheckParameter
     * @return the saveString
     */
    public static String createSaveString(ElectionCheckParameter electionCheckParameter) {
        String amountVotersMin = "<amountVotersMin>\n"
                + electionCheckParameter.getAmountVoters().get(0)
                + "\n</amountVotersMin>\n";
        String amountVotersMax = "<amountVotersMax>\n"
                + electionCheckParameter.getAmountVoters().get(
                        electionCheckParameter.getAmountVoters().size() - 1)
                + "\n</amountVotersMax>\n";
        String amountCandidatesMin = "<amountCandidatesMin>\n"
                + electionCheckParameter.getAmountCandidates().get(0)
                + "\n</amountCandidatesMin>\n";
        String amountCandidatesMax = "<amountCandidatesMax>\n"
                + electionCheckParameter.getAmountCandidates().get(
                        electionCheckParameter.getAmountCandidates().size() - 1)
                + "\n</amountCandidatesMax>\n";
        String amountSeatsMin = "<amountSeatsMin>\n"
                + electionCheckParameter.getAmountSeats().get(0)
                + "\n</amountSeatsMin>\n";
        String amountSeatsMax = "<amountSeatsMax>\n"
                + electionCheckParameter.getAmountSeats().get(
                        electionCheckParameter.getAmountSeats().size() - 1)
                + "\n</amountSeatsMax>\n";
        String timeout = "<timeout>\n" + TimeOutSaverLoader.createSaveString(
                electionCheckParameter.getTimeout()) + "\n</timeout>\n";
        String processes = "<processes>\n"
                + electionCheckParameter.getProcesses() + "\n</processes>\n";
        String argument = "<argument>\n"
                + electionCheckParameter.getArgument() + "\n</argument>\n";
        return (amountVotersMin + amountVotersMax + amountCandidatesMin + amountCandidatesMax + amountSeatsMin
                + amountSeatsMax + timeout + processes + argument);
    }

    /**
     * Creates an Object from a given, by createSaveString() generated, saveString
     * @param s the SaveString
     * @return the Object
     * @throws ArrayIndexOutOfBoundsException if the saveString does not contain a valid format
     */
    public static Object createFromSaveString(String s) throws ArrayIndexOutOfBoundsException {
        String split[] = s.split("\n</amountVotersMin>\n");
        int amountVotersMin = Integer.parseInt(split[0].replace("<amountVotersMin>\n", ""));
        split = split[1].split("\n</amountVotersMax>\n");
        int amountVotersMax = Integer.parseInt(split[0].replace("<amountVotersMax>\n", ""));
        ArrayList<Integer> amountVoters = new ArrayList<>();
        for (int i = amountVotersMin; i <= amountVotersMax; i++) {
            amountVoters.add(i);
        }
        split = split[1].split("\n</amountCandidatesMin>\n");
        int amountCandidatesMin = Integer.parseInt(split[0].replace("<amountCandidatesMin>\n", ""));
        split = split[1].split("\n</amountCandidatesMax>\n");
        int amountCandidatesMax = Integer.parseInt(split[0].replace("<amountCandidatesMax>\n", ""));
        ArrayList<Integer> amountCandidates = new ArrayList<>();
        for (int i = amountCandidatesMin; i <= amountCandidatesMax; i++) {
            amountCandidates.add(i);
        }
        split = split[1].split("\n</amountSeatsMin>\n");
        int amountSeatsMin = Integer.parseInt(split[0].replace("<amountSeatsMin>\n", ""));
        split = split[1].split("\n</amountSeatsMax>\n");
        int amountSeatsMax = Integer.parseInt(split[0].replace("<amountSeatsMax>\n", ""));
        ArrayList<Integer> amountSeats = new ArrayList<>();
        for (int i = amountSeatsMin; i <= amountSeatsMax; i++) {
            amountSeats.add(i);
        }
        split = split[1].split("\n</timeout>\n");
        TimeOut timeout = TimeOutSaverLoader.createFromSaveString(split[0].replace("<timeout>\n", ""));
        split = split[1].split("\n</processes>\n");
        int processes = Integer.parseInt(split[0].replace("<processes>\n", ""));
        split = split[1].split("\n</argument>\n");
        String argument = split[0].replace("<argument>\n", "");
        return new ElectionCheckParameter(amountVoters, amountCandidates, amountSeats, timeout, processes, argument);
    }
}
