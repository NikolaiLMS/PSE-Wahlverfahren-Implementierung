package edu.pse.beast.types;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

import edu.pse.beast.toolbox.CodeArrayListBeautifier;
import edu.pse.beast.toolbox.valueContainer.ResultValueWrapper;

public abstract class OutputType extends InOutType {
    //protected CommonHelpMethods helper; TODO remove

    public OutputType(String dataType, int dimensions, String[] sizeOfDimensions) {
        super(dataType, dimensions, sizeOfDimensions);
    }

    public static List<OutputType> getOutputTypes() {
        ServiceLoader<OutputType> loader = ServiceLoader.load(OutputType.class);

        List<OutputType> types = new ArrayList<OutputType>();

        for (Iterator<OutputType> iterator = loader.iterator(); iterator.hasNext();) {
            OutputType type = (OutputType) iterator.next();
            types.add(type);
        }
        return types;
    }

    @Override
    public String toString() {
        return otherToString();
    }

    //protected abstract void getHelper();

    /**
     *
     * @return the ID this output type uses in the string resources
     */
    public abstract String getOutputIDinFile();

    /**
     *
     * @return true, if the output is just one candidate
     */
    public abstract boolean isOutputOneCandidate();

//    /**
//     * extracts a variable with a given name from a checker output
//     * @param toExtract the raw data from which the data should be extracted
//     * @param variableMather a regex matcher of the variable to be extracted (can e.g. include trailing numbers)
//     * @return a list cotaining the last state this variable was seen in, and all the i
//     */
//    public List<ResultValueWrapper> readResult(List<String> toExtract, String variableMatcher) {
//    	return helper.extractVariable(variableMatcher, toExtract);
//    }

    public abstract CodeArrayListBeautifier addMarginVerifyCheck(CodeArrayListBeautifier code);

    public abstract CodeArrayListBeautifier addVotesArrayAndInit(CodeArrayListBeautifier code,
                                                                 int voteNumber);

    public abstract String getCArrayType();

    /**
     * returns the code with the added line of the margin main test method. The
     * method must end with an assertion that let's cbmc fail, so we can extract the
     * result.
     *
     * @param code       the code
     * @param voteNumber the vote number
     * @return the beautified code
     */
    public abstract CodeArrayListBeautifier addMarginMainTest(CodeArrayListBeautifier code,
                                                              int voteNumber);

//    public List<ResultValueWrapper> extractVariable(String variableMatcher, List<String> lastResult) {
//    	return helper.extractVariable(variableMatcher, lastResult);
//    } TODO remove
    
    public abstract InternalTypeContainer getInternalTypeContainer();

    public abstract void addVerifyOutput(CodeArrayListBeautifier code);

    public abstract void addLastResultAsCode(CodeArrayListBeautifier code, ResultValueWrapper origResult);

    public abstract String getResultDescriptionString(List<String> result);
    
	public String getInfo() { //TODO move later on further down
		return "output type information";
	}
}