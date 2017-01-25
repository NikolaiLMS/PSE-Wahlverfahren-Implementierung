/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.propertychecker;

import edu.pse.beast.datatypes.boolexp.BooleanExpressionNode;
import edu.pse.beast.datatypes.boolexp.BooleanListNode;
import edu.pse.beast.datatypes.boolexp.CBMCCodeGenerationNodeVisitor;
import edu.pse.beast.datatypes.descofvoting.ElectionDescription;
import edu.pse.beast.datatypes.propertydescription.PostAndPrePropertiesDescription;
import edu.pse.beast.datatypes.propertydescription.SymbolicVariable;
import java.util.ArrayList;
import java.util.LinkedList;
import edu.pse.beast.datatypes.internal.InternalTypeContainer;
import edu.pse.beast.datatypes.propertydescription.FormalPropertiesDescription;
import edu.pse.beast.toolbox.ErrorLogger;

/**
 *
 * @author Niels
 */
public class CBMCCodeGenerator {

    private ArrayList<String> code;
    private final ElectionDescription electionDescription;
    private final PostAndPrePropertiesDescription postAndPrePropertiesDescription;

    public CBMCCodeGenerator(ElectionDescription electionDescription, PostAndPrePropertiesDescription postAndPrePropertiesDescription) {
        this.electionDescription = electionDescription;
        this.postAndPrePropertiesDescription = postAndPrePropertiesDescription;
        code = new ArrayList<>();
        generateCode();
    }

    public ArrayList<String> getCode() {
        return code;
    }

    private void generateCode() {
        addIncludes();
        
        code.addAll(electionDescription.getCode());
        
        addMainMethod();
    }

    private void addIncludes() {
        code.add("#inlcude <stdlib.h>");
        code.add("#inlcude <stdint.h>");
        code.add("#inlcude <assert.h>");
        code.add("");
        code.add("int nondet_uint();");
        code.add("int nondet_int();");
        code.add("");
        code.add("#define assert2(x, y) __CPROVER_assert(x, y)");
        code.add("#define assume(x) __CPROVER_assume(x)");
        code.add("");
    }

    /**
     * adds the main method the main method declares the boolean expression. In
     * the main method the votingmethod is called
     */
    private void addMainMethod() {
        code.add("int main(int argc, char *argv[]) {");
        
        addSymbVarInitialisation();

        addPreProperties();

        addVotingCalls();

        addPostProperties();

        code.add("}");
    }

    private void addSymbVarInitialisation() {
        LinkedList<SymbolicVariable> symbolicVariableList = postAndPrePropertiesDescription.getSymbolicVariableList();
        symbolicVariableList.forEach((symbVar) -> {
            InternalTypeContainer internalType = symbVar.getInternalTypeContainer();
            String id = symbVar.getId();
            if (!internalType.isList()) {
                switch (internalType.getInternalType()) {

                    case VOTER:
                        code.add("unsigned int " + id + " = nondet_uint();");
                        // a Voter is basically an unsigned int.
                        // The number shows which vote from votesX (the Array of all votes) belongs to the voter.
                        code.add("assume(0 <= " + id + " && " + id + " < V);");
                        // The Voter has to be in the range of possible Voters. V is the total amount of Voters.
                        break;
                    case CANDIDATE:
                        code.add("unsigned int " + id + " = nondet_uint();");
                        // a Candidate is basically an unsigned int. Candidate 1 is 1 and so on
                        code.add("assume(0 < " + id + " && " + id + " <= C);");
                        // C is the number of total Candidates. 0 is NOT a Candidate
                        break;
                    case SEAT:
                        // a Seat is a also an unsigned int. 
                        // The return of a votingmethod (an Array) gives you the elected candidate(value) of the seat(id)
                        code.add("unsigned int " + id + " = nondet_uint();");
                        // there are S seats. From 0 to S-1
                        code.add("assume(0 <= " + id + " && " + id + " < S);");
                        break;
                    default:
                        reportUnsupportedType(id);

                }
            } else {
                reportUnsupportedType(id);
            }
        });

    }

    private void addPreProperties() {
        FormalPropertiesDescription prePropertiesDescription = this.postAndPrePropertiesDescription.getPrePropertiesDescription();
        BooleanListNode ast = prePropertiesDescription.getAST();
        CBMCCodeGenerationNodeVisitor preVisitor = new CBMCCodeGenerationNodeVisitor("assume");
        ast.getBooleanExpressions().forEach((node) -> {
            node.getVisited(preVisitor);
        });

    }

    private void addPostProperties() {
        FormalPropertiesDescription postPropertiesDescription = this.postAndPrePropertiesDescription.getPostPropertiesDescription();
        BooleanListNode ast = postPropertiesDescription.getAST();
        CBMCCodeGenerationNodeVisitor postVisitor = new CBMCCodeGenerationNodeVisitor("assert");
        ast.getBooleanExpressions().forEach((node) -> {
            node.getVisited(postVisitor);
        });
    }

    private void addVotingCalls() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void reportUnsupportedType(String id) {
        ErrorLogger.log("Der Typ der symbolischen Variable " + id + " wird nicht unterstützt");
    }

    private void addVotingMethod() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}