package edu.pse.beast.zzz.electionsimulator;

import static edu.pse.beast.zzz.toolbox.CCodeHelper.lineBreak;
import static edu.pse.beast.zzz.toolbox.CCodeHelper.one;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.scene.layout.GridPane;
import edu.pse.beast.datatypes.electiondescription.ElectionTypeContainer;
import edu.pse.beast.highlevel.javafx.GUIController;
import edu.pse.beast.highlevel.javafx.MenuBarInterface;
import edu.pse.beast.highlevel.javafx.NEWRowOfValues;
import edu.pse.beast.types.InputType;
import edu.pse.beast.zzz.codeareajavafx.SaverLoader;
import edu.pse.beast.zzz.electionsimulator.model.ElectionSimulationModel;
import edu.pse.beast.zzz.toolbox.valueContainer.cbmcValueContainers.CBMCResultValue;
import edu.pse.beast.zzz.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueArray;
import edu.pse.beast.zzz.toolbox.valueContainer.cbmcValueContainers.CBMCResultValueWrapper;

/**
 * The Class ElectionSimulation.
 *
 * @author Lukas Stapelbroek
 */
public final class ElectionSimulation implements MenuBarInterface {
    /** The Constant CSV_SEPARATOR. */
    private static final String CSV_SEPARATOR = ",";

    /** The Constant GAP_SIZE. */
    private static final int GAP_SIZE = 10;

    /** The container. */
    private ElectionTypeContainer container;

    /** The model. */
    private ElectionSimulationModel model;

    /** The input grid pane. */
    private GridPane inputGridPane;

    /** The voter grid pane. */
    private GridPane voterGridPane;

    /** The candidate grid pane. */
    private GridPane candidateGridPane;

    /** The saver loader. */
    private SaverLoader saverLoader;

    /**
     * Instantiates a new election simulation.
     *
     * @param elecTypeContainer
     *            the elec type container
     * @param inputDataGridPane
     *            the input data grid pane
     * @param votGridPane
     *            the vot grid pane
     * @param candGridPane
     *            the cand grid pane
     */
    public ElectionSimulation(final ElectionTypeContainer elecTypeContainer,
                              final GridPane inputDataGridPane,
                              final GridPane votGridPane,
                              final GridPane candGridPane) {
        this.container = elecTypeContainer;
        this.saverLoader =
                new SaverLoader(".elecIn", "Election Input Data", this);
        this.inputGridPane = inputDataGridPane;
        this.voterGridPane = votGridPane;
        this.candidateGridPane = candGridPane;
        inputDataGridPane.setHgap(GAP_SIZE);
        inputDataGridPane.setVgap(GAP_SIZE);
        votGridPane.setHgap(GAP_SIZE);
        votGridPane.setVgap(GAP_SIZE);
        candGridPane.setHgap(GAP_SIZE);
        candGridPane.setVgap(GAP_SIZE);
        model = new ElectionSimulationModel(elecTypeContainer,
                                            inputDataGridPane,
                                            votGridPane, candGridPane);
    }

    /**
     * Update election type container.
     *
     * @param elecTypeContainer
     *            the election type container
     */
    public void updateContainer(final ElectionTypeContainer elecTypeContainer) {
        this.container = elecTypeContainer;
        model.changeContainer(elecTypeContainer);
    }

    /**
     * Update rows.
     */
    public synchronized void updateRows() {
        for (Iterator<NEWRowOfValues> iterator =
                model.getRows().iterator();
                iterator.hasNext();) {
            final NEWRowOfValues row = iterator.next();
            row.update();
        }
    }

    // TODO
    /**
     * Resets all fields and returns the view back to its original state.
     */
    public void reset() {
        inputGridPane.getChildren().clear();
        voterGridPane.getChildren().clear();
        candidateGridPane.getChildren().clear();
        model = new ElectionSimulationModel(container, inputGridPane,
                                            voterGridPane, candidateGridPane);
        model.setAmountCandidates(1);
        model.setAmountVoters(1);
        GUIController.getController().getInputVoters().setText(one());
        GUIController.getController().getInputCandidates().setText(one());
        GUIController.getController().getInputSeats().setText(one());
    }

    /**
     * Gets the num voters.
     *
     * @return the num voters
     */
    public int getNumVoters() {
        return model.getAmountVoters();
    }

    /**
     * Gets the num candidates.
     *
     * @return the num candidates
     */
    public int getNumCandidates() {
        return model.getAmountCandidates();
    }

    /**
     * Gets the num seats.
     *
     * @return the num seats
     */
    public int getNumSeats() {
        return model.getAmountSeats();
    }

    /**
     * Gets the voting data.
     *
     * @return the voting data
     */
    public ElectionSimulationData getVotingData() {
        // TODO When multiple Model checkers would be installed, some
        // distinction would have to be made here.
        final List<NEWRowOfValues> rows = model.getRows();
        final InputType inType = container.getInputType();
        final CBMCResultValueArray topArrayValue = new CBMCResultValueArray();
        final List<CBMCResultValueWrapper> valueWrappers =
                new ArrayList<CBMCResultValueWrapper>();

        for (NEWRowOfValues row: rows) {
            final CBMCResultValue result =
                    inType.convertRowToResultValue(row);
            valueWrappers.add(new CBMCResultValueWrapper(result));
        }
        topArrayValue.setValue(valueWrappers);

        final ElectionSimulationData toReturn =
                new ElectionSimulationData(getNumVoters(), getNumCandidates(),
                                           getNumSeats(),
                                           new CBMCResultValueWrapper(topArrayValue));
        return toReturn;
    }

    /**
     * Gets the num voting points.
     *
     * @return the num voting points
     */
    public int getNumVotingPoints() {
        return container.getInputType().getNumVotingPoints(null);
    }

    /**
     * Gets the party name.
     *
     * @param index
     *            the index
     * @return the party name
     */
    public String getPartyName(final int index) {
        return model.getCandidates().get(index).getText();
    }

    /**
     * Gets the voter name.
     *
     * @param index
     *            the index
     * @return the voter name
     */
    public String getVoterName(final int index) {
        return model.getVoters().get(index).getText();
    }

    /**
     * Num voters changed.
     *
     * @param numVoters
     *            the num voters
     */
    public void numVotersChanged(final int numVoters) {
        model.setAmountVoters(numVoters);
    }

    /**
     * Num candidates changed.
     *
     * @param numCandidates
     *            the num candidates
     */
    public void numCandidatesChanged(final int numCandidates) {
        model.setAmountCandidates(numCandidates);
    }

    /**
     * Num seats changed.
     *
     * @param numSeats
     *            the num seats
     */
    public void numSeatsChanged(final int numSeats) {
        model.setAmountSeats(numSeats);
    }

    /**
     * Sets the and vet voter number.
     *
     * @param toVet
     *            the to vet
     * @return the string
     */
    public String setAndVetVoterNumber(final String toVet) {
        final int vetted =
                container.getInputType()
                .vetAmountVoters(Integer.parseInt(toVet));
        model.setAmountVoters(vetted);
        return "" + vetted;
    }

    /**
     * Sets the and vet candidate number.
     *
     * @param toVet
     *            the to vet
     * @return the string
     */
    public String setAndVetCandidateNumber(final String toVet) {
        final int vetted =
                container.getInputType()
                .vetAmountCandidates(Integer.parseInt(toVet));
        model.setAmountCandidates(vetted);
        return "" + vetted;
    }

    /**
     * Sets the and vet seat number.
     *
     * @param toVet
     *            the to vet
     * @return the string
     */
    public String setAndVetSeatNumber(final String toVet) {
        final int vetted =
                container.getInputType()
                .vetAmountSeats(Integer.parseInt(toVet));
        model.setAmountSeats(vetted);
        return "" + vetted;
    }

    /**
     * Save as.
     *
     * @param file
     *            the file
     */
    public void saveAs(final File file) {
        saverLoader.save(file, generateSaveString());
    }

    @Override
    public void saveAs() {
        saverLoader.saveAs("", generateSaveString());
    }

    /**
     * Open.
     *
     * @param file
     *            the file
     */
    public void open(final File file) {
        if (file.exists()) {
            final String input = saverLoader.load(file);
            openInput(input, false);
        }
    }

    @Override
    public void open() {
        final String input = saverLoader.load();
        openInput(input, true);
    }

    /**
     * Open input.
     *
     * @param input
     *            the input
     * @param bringToFront
     *            the bring to front
     */
    private void openInput(final String input, final boolean bringToFront) {
        reset();
        if (!"".equals(input)) {
            final String[] lines = input.split(lineBreak());
            for (int y = 0; y < lines.length; y++) {
                final String[] values =
                        lines[y].replaceAll("\\r", "").split(CSV_SEPARATOR);
                if (y == 0) {
                    GUIController.getController().getInputVoters()
                            .setText("" + values[0]);
                    GUIController.getController().getInputCandidates()
                            .setText("" + values[1]);
                    GUIController.getController().getInputSeats()
                            .setText("" + values[2]);
                } else {
                    for (int x = 0; x < values.length; x++) {
                        model.setValue(x, y - 1, values[x]);
                    }
                }
            }
        }
        if (bringToFront) {
            bringToFront();
        }
    }

    /**
     * Bring to front.
     */
    public void bringToFront() {
        GUIController.getController().getMainTabPane().getSelectionModel()
                .select(GUIController.getController().getInputTab());
    }

    @Override
    public void save() {
        saverLoader.save("", generateSaveString());
    }

    /**
     * Generate save string.
     *
     * @return the string
     */
    private String generateSaveString() {
        String saveString = "";
        saveString =
                lineBreak(model.getAmountVoters() + CSV_SEPARATOR
                            + model.getAmountCandidates() + CSV_SEPARATOR
                            + model.getAmountSeats());
        final List<NEWRowOfValues> rows = model.getRows();

        for (Iterator<NEWRowOfValues> iterator = rows.iterator();
                iterator.hasNext();) {
            final NEWRowOfValues row = iterator.next();
            for (Iterator<String> valueIterator =
                    row.getValues().iterator();
                    valueIterator.hasNext();) {
                final String value = valueIterator.next();
                saveString += value;
                if (valueIterator.hasNext()) { // another value will follow
                    saveString += CSV_SEPARATOR;
                }
            }
            if (iterator.hasNext()) {
                saveString += lineBreak(saveString);
            }
        }
        return saveString;
    }

    @Override
    public void undo() {
        // TODO Auto-generated method stub
    }

    @Override
    public void redo() {
        // TODO Auto-generated method stub
    }

    @Override
    public void cut() {
        // TODO Auto-generated method stub
    }

    @Override
    public void copy() {
        // TODO Auto-generated method stub
    }

    @Override
    public void paste() {
        // TODO Auto-generated method stub
    }

    @Override
    public void delete() {
        // TODO Auto-generated method stub
    }

    @Override
    public void autoComplete() {
        // do nothing
    }
}