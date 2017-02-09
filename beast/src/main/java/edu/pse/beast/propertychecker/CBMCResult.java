package edu.pse.beast.propertychecker;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.pse.beast.datatypes.FailureExample;
import edu.pse.beast.highlevel.ResultPresenterElement;
import edu.pse.beast.toolbox.ErrorForUserDisplayer;
import edu.pse.beast.toolbox.ErrorLogger;

/**
 * 
 * @author Lukas
 *
 */
public class CBMCResult extends Result {

    private FailureExample failureExample = null;

    private final String segmentEnder = "-----------------------------------";

    @Override
    public void presentTo(ResultPresenterElement presenter) {
        if (!isFinished()) {
            ErrorLogger.log("Result isn't ready yet");
            return;
        } else if (isTimedOut()) {
            presenter.presentTimeOut();
        } else if (!isValid()) {
            presenter.presentFailure(getError());
        } else if (isSuccess()) {
            presenter.presentSuccess();
        } else {
            if (failureExample != null) {
                presenter.presentFailureExample(failureExample);
            } else {
                if (getError() != null) {
                    setError(new ArrayList<String>());
                }
                presenter.presentFailure(getError());
            }
        }
    }

    @Override
    public void setResult(List<String> result) {
        super.setResult(result);
        failureExample = createFailureExample();
    }

    public FailureExample createFailureExample() {

        // datermine the elect values
        List<CBMCResultWrapperLong> elect = readLongs("elect", getResult());

        // define these arrays, because switch case doesn't let me reassign the
        // same name,
        // and i am a bit worried, that they won't get created properly;
        List<CBMCResultWrapperMultiArray> votesList;
        List<CBMCResultWrappersingleArray> seatsList;
        List<CBMCResultWrappersingleArray> singleVotesList;

        switch (getElectionType()) {

        case APPROVAL:

            votesList = readTwoDimVar("votes", getResult());

            seatsList = readOneDimVar("seats", getResult());

            return new FailureExample(getElectionType(), null, votesList, elect, seatsList, getNumCandidates(),
                    getNumSeats(), getNumVoters());

        case PREFERENCE:

            singleVotesList = readOneDimVar("votes", getResult());

            return new FailureExample(getElectionType(), singleVotesList, null, elect, null, getNumCandidates(),
                    getNumSeats(), getNumVoters());

        case SINGLECHOICE:

            singleVotesList = readOneDimVar("votes", getResult());

            return new FailureExample(getElectionType(), singleVotesList, null, elect, null, getNumCandidates(),
                    getNumSeats(), getNumVoters());

        case WEIGHTEDAPPROVAL:

            votesList = readTwoDimVar("votes", getResult());

            seatsList = readOneDimVar("seats", getResult());

            return new FailureExample(getElectionType(), null, votesList, elect, seatsList, getNumCandidates(),
                    getNumSeats(), getNumVoters());

        default:
            ErrorForUserDisplayer.displayError(
                    "This votingtype you are using hasn't been implemented yet to be displaye. "
                    + "Please do so in the class CBMC_Result");
            this.setError("This votingtype hasn't been implemented yet please do so in the class CBMC_Result");
            return null;
        }
    }

    private List<CBMCResultWrapperLong> readLongs(String name, List<String> toExtract) {

        List<CBMCResultWrapperLong> toReturn = new ArrayList<CBMCResultWrapperLong>();

        Pattern correctChecker = Pattern.compile("(\\b" + name + "[0-9]+=[0-9]+u)(.*)");

        Pattern longExtractor = Pattern.compile("(\\b" + name + "[0-9]+)(.*)");

        Iterator<String> iterator = getResult().iterator();
        String line = mergeLinesToOne(iterator, segmentEnder);

        line = mergeLinesToOne(iterator, segmentEnder);
        
        while (line.length() > 0) {

            Matcher checkerMatcher = correctChecker.matcher(line);
            if (checkerMatcher.find()) {
                Matcher longMatcher = longExtractor.matcher(checkerMatcher.group(0));
                if (longMatcher.find()) {

                    String longLine = longMatcher.group(1);
                    // replace all no number characters
                    String number = longLine.replaceAll(("[^-?0-9]*"), "");
                    int electIndex = Integer.parseInt(number);

                    // split at the "(" and ")" to extract the bit value
                    String valueAsString = line.split("\\(")[1].split("\\)")[0];
                    // prase the binary value to a long
                    Long value = Long.parseLong(valueAsString, 2);

                    boolean added = false;

                    for (Iterator<CBMCResultWrapperLong> innerIterator = toReturn.iterator(); innerIterator
                            .hasNext();) {
                        CBMCResultWrapperLong wrapper = (CBMCResultWrapperLong) innerIterator.next();
                        if (wrapper.getMainIndex() == electIndex) {
                            wrapper.setValue(value);
                            added = true;
                        }
                    }

                    if (!added) {
                        toReturn.add(new CBMCResultWrapperLong(electIndex, name));
                        toReturn.get(toReturn.size() - 1).setValue(value);
                    }
                }
            }
            line = mergeLinesToOne(iterator, segmentEnder);
        }
        return toReturn;
    }

    private List<CBMCResultWrappersingleArray> readOneDimVar(String name, List<String> toExtract) {

        List<CBMCResultWrappersingleArray> list = new ArrayList<CBMCResultWrappersingleArray>();

        // this pattern searches for words of the form
        // "votesNUMBER[NUMBER]" where "NUMBER" can by any positive
        // number. Also, the next character has to be an equals sign
        Pattern votesExtractor = null;

        Iterator<String> iterator = getResult().iterator();
        String line = mergeLinesToOne(iterator, segmentEnder);

        while ((line = mergeLinesToOne(iterator, segmentEnder)).length() > 0) {

            if (line.contains("[")) {

                votesExtractor = Pattern.compile("(\\b" + name + "[0-9]+\\[[0-9]+[a-zA-Z]*\\])(=.*)");

                Matcher votesMatcher = votesExtractor.matcher(line);

                if (votesMatcher.find()) {
                    String newLine = votesMatcher.group(1);

                    // find out the number of this votes array
                    int mainIndex = Integer.parseInt(newLine.split("=")[0].split(name)[1].split("\\[")[0]);

                    // get the first index for this array value
                    int arrayIndex = Integer
                            .parseInt((newLine.split("\\[")[1].split("\\]")[0]).replaceAll("[^\\d.]", ""));

                    // split at the "(" and ")" to extract the value
                    String valueAsString = line.split("\\(")[1].split("\\)")[0];

                    long value = Long.parseLong(valueAsString, 2);

                    boolean added = false;

                    for (Iterator<CBMCResultWrappersingleArray> innerIterator = list.iterator(); innerIterator
                            .hasNext();) {
                        CBMCResultWrappersingleArray wrapper = (CBMCResultWrappersingleArray) innerIterator.next();

                        if (wrapper.getMainIndex() == mainIndex) {
                            wrapper.addTo(arrayIndex, value);
                            added = true;
                        }
                    }

                    if (!added) {
                        list.add(new CBMCResultWrappersingleArray(mainIndex, name));
                        list.get(list.size() - 1).addTo(arrayIndex, value);
                    }

                }
            } else if (line.contains("{")) {

                votesExtractor = Pattern.compile("(\\b" + name + "[0-9]+)(=\\{.*)");

                Matcher votesMatcher = votesExtractor.matcher(line);

                if (votesMatcher.find()) {
                    String newLine = votesMatcher.group(1);

                    // find out the number of this votes array
                    int mainIndex = Integer.parseInt(newLine.split("=")[0].split(name)[1]);

                    String values = line.split("\\(")[1].split("\\)")[0];

                    // strip away whitespaces and the double braces that
                    // represent
                    // the whole array
                    // also remove all opening braces
                    values = values.replaceAll(" +", "").replaceAll("\\{+", "").replace("}", "}");

                    String[] subValueArray = values.split("\\}")[0].split(",");

                    for (int i = 0; i < subValueArray.length; i++) {
                        if (!subValueArray[i].equals("")) {

                            boolean added = false;

                            for (Iterator<CBMCResultWrappersingleArray> innerIterator = list.iterator(); innerIterator
                                    .hasNext();) {
                                CBMCResultWrappersingleArray wrapper = (CBMCResultWrappersingleArray) innerIterator
                                        .next();

                                if (wrapper.getMainIndex() == mainIndex) {
                                    wrapper.addTo(i, Long.parseLong(subValueArray[i], 2));
                                    added = true;
                                }
                            }

                            if (!added) {
                                list.add(new CBMCResultWrappersingleArray(mainIndex, name));
                                list.get(list.size() - 1).addTo(i, Long.parseLong(subValueArray[i], 2));
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private List<CBMCResultWrapperMultiArray> readTwoDimVar(String name, List<String> toExtract) {

        List<CBMCResultWrapperMultiArray> list = new ArrayList<CBMCResultWrapperMultiArray>();

        Pattern votesExtractor = null;

        Iterator<String> iterator = getResult().iterator();
        String line = mergeLinesToOne(iterator, segmentEnder);

        while ((line = mergeLinesToOne(iterator, segmentEnder)).length() > 0) {

            // System.out.println("next line: " + line);
            // for (Iterator<String> iterator = toExtract.iterator();
            // iterator.hasNext();) {
            // String line = (String) iterator.next();

            if (line.contains("[")) {

                // this pattern searches for words of the form
                // "votesNUMBER[NUMBER][NUMBER]" where "NUMBER" can by any
                // positive
                // number. Also, the next character has to be an equals sign
                votesExtractor = Pattern.compile("(\\b" + name + "[0-9]+\\[[0-9]+[a-z]*\\]\\[[0-9]+[a-zA-z]*\\])(=.*)");

                Matcher votesMatcher = votesExtractor.matcher(line);

                if (votesMatcher.find()) {

                    String newLine = votesMatcher.group(1);

                    // find out the number of this votes array
                    int mainIndex = Integer.parseInt(newLine.split("=")[0].split(name)[1].split("\\[")[0]);

                    // System.out.println("mainindex " + mainIndex);

                    // get the first index for this array value
                    int arrayIndexOne = Integer
                            .parseInt(newLine.split("\\[")[1].split("\\]")[0].replaceAll("[^\\d.]", ""));

                    // get the second index for this array value
                    int arrayIndexTwo = Integer
                            .parseInt(newLine.split("\\[")[2].split("\\]")[0].replaceAll("[^\\d.]", ""));

                    // split at the "(" and ")" to extract the value
                    String valueAsString = line.split("\\(")[1].split("\\)")[0];

                    long value = Long.parseLong(valueAsString, 2);

                    // System.out.println("value: " + value);
                    // System.out.println(line);
                    boolean added = false;

                    for (Iterator<CBMCResultWrapperMultiArray> innerIterator = list.iterator(); innerIterator
                            .hasNext();) {
                        CBMCResultWrapperMultiArray wrapper = (CBMCResultWrapperMultiArray) innerIterator.next();

                        if (wrapper.getMainIndex() == mainIndex) {
                            wrapper.addTo(arrayIndexOne, arrayIndexTwo, value);
                            added = true;
                        }
                    }

                    if (!added) {
                        list.add(new CBMCResultWrapperMultiArray(mainIndex, name));
                        list.get(list.size() - 1).addTo(arrayIndexOne, arrayIndexTwo, value);
                    }
                }
            } else if (line.contains("{")) {
                // Pattern votesExtractor = Pattern.compile("(\\b" + name +
                // "[0-9]+])(=\\{.*)");

                // votesNUMBER={....}
                votesExtractor = Pattern.compile("(\\b" + name + "[0-9]+)(=\\{.*)");

                Matcher votesMatcher = votesExtractor.matcher(line);

                if (votesMatcher.find()) {
                    String newLine = votesMatcher.group(1);

                    // find out the number of this votes array
                    int mainIndex = Integer.parseInt(newLine.split("=")[0].split(name)[1]);

                    String values = line.split("\\(")[1].split("\\)")[0];

                    // strip away whitespaces and the double braces that
                    // represent
                    // the whole array
                    // also remove all opening braces
                    values = values.replaceAll(" +", "").replaceAll("\\{+", "").replaceAll("} *}+", "");

                    // every sub array is now seperated by these two characters
                    String[] subArrys = values.split("\\},");

                    for (int i = 0; i < subArrys.length; i++) {

                        String subValues[] = subArrys[i].split(",");
                        for (int j = 0; j < subValues.length; j++) {

                            if (!subValues[j].equals("")) {

                                boolean added = false;

                                for (Iterator<CBMCResultWrapperMultiArray> innerIterator = list
                                        .iterator(); innerIterator.hasNext();) {
                                    CBMCResultWrapperMultiArray wrapper = (CBMCResultWrapperMultiArray) innerIterator
                                            .next();

                                    if (wrapper.getMainIndex() == mainIndex) {
                                        wrapper.addTo(i, j, Long.parseLong(subValues[j], 2));

                                        added = true;
                                    }
                                }

                                if (!added) {
                                    list.add(new CBMCResultWrapperMultiArray(mainIndex, name));
                                    list.get(list.size() - 1).addTo(i, j, Long.parseLong(subValues[j], 2));
                                }
                            }
                        }
                    }
                }
            }
        }
        return list;
    }

    private String mergeLinesToOne(Iterator<String> toMerge, String regexToEndAt) {

        String toReturn = "";
        boolean notEnded = true;
        while (notEnded) {
            if (toMerge.hasNext()) {

                String nextLine = toMerge.next();

                if (nextLine.contains(regexToEndAt)) {
                    // we found the end of the segment
                    notEnded = false;
                } else {
                    // add the next line, sepearated by a whitespace
                    toReturn = toReturn + " " + nextLine;
                }
            } else {
                return toReturn;
            }
        }
        return toReturn;
    }
}