package edu.pse.beast.propertychecker;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.pse.beast.datatypes.electiondescription.ElectionType;

public class CBMCResult_Test {

    CBMCResult result = null;

    List<String> lines = null;

    @Before
    public void create() {
        lines = new ArrayList<String>();

        lines.add("elect1=4u (00000000000000000000000000000100)");
        lines.add("----------------------------------------------------");
        
        lines.add("votes1={ 2u, 4u, 8u, 16u } ({ 00000000000000000000000000000010, 00000000000000000000000000000100, 00000000000000000000000000001000, 00000000000000000000000000010000 })");

        lines.add("path to file that is checked");
        lines.add("----------------------------------------------------");

        
        lines.add("votes2={ { 1u, 2u, 3u, 4u, 5u }, { 6u, 7u, 8u, 9u, 10u }, { 11u, 12u, 13u, 14u, 15u }, { 16u, 17u, 18u, 19u, 20u } } ({ { 00000000000000000000000000000001, 00000000000000000000000000000010, 00000000000000000000000000000011, 00000000000000000000000000000100, 00000000000000000000000000000101 }, { 00000000000000000000000000000110, 00000000000000000000000000000111, 00000000000000000000000000001000, 00000000000000000000000000001001, 00000000000000000000000000001010 }, { 00000000000000000000000000001011, 00000000000000000000000000001100, 00000000000000000000000000001101, 000000000000000000000000000001110, 00000000000000000000000000001111 }, { 000000000000000000000000000010000, 00000000000000000000000000010001, 00000000000000000000000000010010, 00000000000000000000000000010011, 00000000000000000000000000010100 } })");

        lines.add("path to file that is checked");
        lines.add("----------------------------------------------------");
                
        
        lines.add("----------------------------------------------------");
        lines.add("votes3[0l][0l]=3ul (00000000000000000000000000000011)");
        lines.add("path to file that is checked");
        lines.add("----------------------------------------------------");
        
        
        lines.add("----------------------------------------------------");
        lines.add("votes4[0l]=5ul (00000000000000000000000000000101)");
        lines.add("path to file that is checked");
        lines.add("----------------------------------------------------");
        
        result = new CBMCResult();

       // result.setResult(lines);
    }

    @Test
    public void singleChoice() {
        result.setElectionType(ElectionType.SINGLECHOICE);
        singleDim();
    }

    @Test
    public void preference() {
        result.setElectionType(ElectionType.PREFERENCE);
        multiDim();
    }

    @Test
    public void approval() {
        result.setElectionType(ElectionType.APPROVAL);
        multiDim();
    }

    @Test
    public void wightedApproval() {
        result.setElectionType(ElectionType.WEIGHTEDAPPROVAL);
        multiDim();
    }

    @Test
    public void nullType() {
        result.setElectionType(null);
        result.setResult(lines);
        assertNull(result.getFailureExample());
    }

    public void singleDim() {

        result.setResult(lines);

        List<CBMCResultWrappersingleArray> singleDim = result.getFailureExample().getVotes();

        CBMCResultWrappersingleArray zero = singleDim.get(0);

        assertEquals(zero.getName(), "votes");

        assertEquals(zero.getMainIndex(), 1);

        Long[] vars = zero.getArray();

        assertEquals((long) (vars[0]), 2l);

        assertEquals((long) (vars[1]), 4l);

        CBMCResultWrappersingleArray one = singleDim.get(1);

        assertEquals(one.getName(), "votes");

        assertEquals(one.getMainIndex(), 4);

        Long[] varsO = one.getArray();

        assertEquals((long) (varsO[0]), 5l);
    }

    public void multiDim() {

        result.setResult(lines);

        List<CBMCResultWrapperMultiArray> multiDim = result.getFailureExample().getVoteList();

        CBMCResultWrapperMultiArray zero = multiDim.get(0);

        assertEquals(zero.getName(), "votes");

        assertEquals(zero.getMainIndex(), 2);

        Long[][] vars = zero.getArray();

        assertEquals((long) (vars[0][0]), 1l);

        assertEquals((long) (vars[1][1]), 7l);

        CBMCResultWrapperMultiArray one = multiDim.get(1);

        assertEquals(one.getName(), "votes");

        assertEquals(one.getMainIndex(), 3);

        Long[][] varsO = one.getArray();

        assertEquals((long) (varsO[0][0]), 3l);
    }

}
