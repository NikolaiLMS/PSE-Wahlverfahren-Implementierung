package edu.pse.beast.api.descr.c_electiondescription;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import edu.pse.beast.api.c_parser.ExtractedCLoop;
import edu.pse.beast.api.codegen.loopbounds.CodeGenLoopBoundHandler;
import edu.pse.beast.api.descr.c_electiondescription.function.CElectionDescriptionFunction;
import edu.pse.beast.api.descr.c_electiondescription.function.SimpleTypeFunction;
import edu.pse.beast.api.descr.c_electiondescription.function.VotingSigFunction;

/**
 * All data which describes an election description in c code
 * @author holge
 *
 */
public class CElectionDescription {
    private List<CElectionDescriptionFunction> functions = new ArrayList<>();
    private Set<String> functionNames = new HashSet<>();
    private VotingSigFunction votingFunction;

    private String name;
    private String uuid;

    private VotingInputTypes inputType;
    private VotingOutputTypes outputType;

    public CElectionDescription(VotingInputTypes inputType,
            VotingOutputTypes outputType, String name) {
        this.inputType = inputType;
        this.outputType = outputType;
        this.name = name;
        votingFunction = createNewVotingSigFunctionAndAdd("voting");
        this.uuid = UUID.randomUUID().toString();
    }

    public CElectionDescription(String uuid, String name,
            VotingInputTypes inputType, VotingOutputTypes outputType) {
        this.inputType = inputType;
        this.outputType = outputType;
        this.name = name;
        votingFunction = createNewVotingSigFunctionAndAdd("voting");
        this.uuid = uuid;
    }

    public void setFunctions(List<CElectionDescriptionFunction> functions) {
        this.functions = functions;
    }

    public void setVotingFunction(VotingSigFunction votingFunction) {
        this.votingFunction = votingFunction;
    }

    public boolean hasFunctionName(String name) {
        return functionNames.contains(name);
    }

    public VotingSigFunction getVotingFunction() {
        return votingFunction;
    }

    public VotingSigFunction createNewVotingSigFunctionAndAdd(String name) {
        VotingSigFunction created = new VotingSigFunction(name, inputType,
                outputType);
        functions.add(created);
        functionNames.add(name);
        return created;
    }

    public void removeFunction(CElectionDescriptionFunction func) {
        functionNames.remove(func.getName());
        functions.remove(func);
    }

    @Override
    public String toString() {
        return name;
    }

    public void setInputType(VotingInputTypes inputType) {
        this.inputType = inputType;
        for (CElectionDescriptionFunction f : functions) {
            if (f.getClass().equals(VotingSigFunction.class)) {
                ((VotingSigFunction) f).setInputType(inputType);
            }
        }
    }

    public void setOutputType(VotingOutputTypes outputType) {
        this.outputType = outputType;
        for (CElectionDescriptionFunction f : functions) {
            if (f.getClass().equals(VotingSigFunction.class)) {
                ((VotingSigFunction) f).setOutputType(outputType);
            }
        }
    }

    public VotingInputTypes getInputType() {
        return inputType;
    }

    public VotingOutputTypes getOutputType() {
        return outputType;
    }

    public String getName() {
        return name;
    }

    public List<CElectionDescriptionFunction> getFunctions() {
        return functions;
    }

    public String getUuid() {
        return uuid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void addSimpleFunction(SimpleTypeFunction f) {
        functions.add(f);
        functionNames.add(f.getName());
    }

    public CodeGenLoopBoundHandler generateLoopBoundHandler() {
        CodeGenLoopBoundHandler boundHandler = new CodeGenLoopBoundHandler();

        for (CElectionDescriptionFunction f : functions) {
            List<ExtractedCLoop> loops = f.getExtractedLoops();
            boundHandler.addFunction(f.getName());
            for (ExtractedCLoop l : loops) {
                boundHandler.addLoopBound(l.generateLoopBound());
            }
        }

        return boundHandler;
    }

}
