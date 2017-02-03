/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.BooleanExpEditor.booleanExpCodeArea.errorFinder;

import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.BooleanExpANTLRHandler;
import edu.pse.beast.booleanexpeditor.booleanExpCodeArea.errorFinder.BooleanExpEditorVariableErrorFinder;
import edu.pse.beast.celectiondescriptioneditor.ElectionTemplates.ElectionTemplateHandler;
import edu.pse.beast.codearea.ErrorHandling.CodeError;
import edu.pse.beast.datatypes.descofvoting.ElectionTypeContainer;
import edu.pse.beast.datatypes.internal.InternalTypeContainer;
import edu.pse.beast.datatypes.internal.InternalTypeRep;
import edu.pse.beast.stringresource.StringLoaderInterface;
import edu.pse.beast.stringresource.StringResourceLoader;
import edu.pse.beast.stringresource.StringResourceProvider;
import edu.pse.beast.toolbox.antlr.booleanexp.GenerateAST.BooleanExpScopehandler;
import java.util.ArrayList;
import javax.swing.JTextPane;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Holger-Desktop
 */
public class BooleanExpEditorVariableErrorFinderTest {
    private BooleanExpANTLRHandler aNTLRHandler;
    private BooleanExpEditorVariableErrorFinder finder;
    private JTextPane pane;
    private StringResourceLoader stringRes;
    public BooleanExpEditorVariableErrorFinderTest() {
        pane = new JTextPane();
        aNTLRHandler = new BooleanExpANTLRHandler(pane.getStyledDocument());
        finder = new BooleanExpEditorVariableErrorFinder(aNTLRHandler);
        stringRes = new StringLoaderInterface("de").getBooleanExpEditorStringResProvider().getBooleanExpErrorStringRes();
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    @Test
    public void testFindsCantCompareErrorSymbolicVars() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("list_of_candidates_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("v", new InternalTypeContainer(InternalTypeRep.VOTER));
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        
        String exp = "v == c;";
                
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        ArrayList<CodeError> errs = finder.getErrors();
        assertEquals(1, errs.size());
        for(CodeError er : errs) {
            assertEquals("incomparable_types", er.getId());
        }
    }
    
    @Test
    public void testFindsCantCompareErrorDifferentLists() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("list_of_candidates_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("v", new InternalTypeContainer(InternalTypeRep.VOTER));
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        
        String exp = "VOTES1 == c;";
                
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        ArrayList<CodeError> errs = finder.getErrors();
        assertEquals(1, errs.size());
        for(CodeError er : errs) {
            assertEquals("incomparable_list_sizes", er.getId());
        }
    }
    
    @Test
    public void testFindsWrongVarPassedToVotesum() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("list_of_candidates_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("v", new InternalTypeContainer(InternalTypeRep.VOTER));
        
        String exp = "VOTE_SUM_FOR_CANDIDATE1(v) == 3;";
                
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        ArrayList<CodeError> errs = finder.getErrors();
        assertEquals(1, errs.size());
        for(CodeError er : errs) {
            assertEquals("wrong_var_passed_to_votesum", er.getId());
        }
    }
    
    
    @Test
    public void testGetVarNotDeclError() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("list_of_candidates_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        
        String exp = "v == c;";
                
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        ArrayList<CodeError> errs = finder.getErrors();
        assertEquals(2, errs.size());
        for(CodeError er : errs) {
            assertEquals("var_not_decl", er.getId());
        }
    }
    
    @Test
    public void testTooManyVarsPassedElect() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("list_of_candidates_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        
        String exp = "ELECT1(c) == c;";
        
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        
        ArrayList<CodeError> errs = finder.getErrors();
        
        assertEquals(1, errs.size());
        assertEquals("too_many_vars_passed", errs.get(0).getId());        
    }
    
    @Test
    public void testTooManyVarsPassedVote() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("one_candidate_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        scopehandler.addVariable("v", new InternalTypeContainer(InternalTypeRep.VOTER));
        scopehandler.addVariable("s", new InternalTypeContainer(InternalTypeRep.SEAT));
        
        String exp = "VOTES1(v)(c)(s) == c;";
        
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        
        ArrayList<CodeError> errs = finder.getErrors();
        
        assertEquals(2, errs.size());
        assertEquals("too_many_vars_passed", errs.get(0).getId());    
        assertEquals("too_many_vars_passed", errs.get(1).getId());          
    }
    
    @Test 
    public void testWrongVarTypePassed() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("one_candidate_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        
        String exp = "VOTES1(c) == c;";
        
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        
        ArrayList<CodeError> errs = finder.getErrors();
        
        assertEquals(1, errs.size());
        assertEquals("wrong_var_type_passed", errs.get(0).getId());        
    }
    
    @Test 
    public void handlesGrammerErrorsTest() {
        ElectionTypeContainer input = new ElectionTemplateHandler().getById("one_candidate_per_voter");
        ElectionTypeContainer res = new ElectionTemplateHandler().getById("one_candidate_or_zero");
        
        BooleanExpScopehandler scopehandler = new BooleanExpScopehandler();
        scopehandler.enterNewScope();
        scopehandler.addVariable("c", new InternalTypeContainer(InternalTypeRep.CANDIDATE));
        
        String exp = "VOTES1()()()()()   209435 unsdfjgkvn ::::: ()()() ==> ==> && dsjfnb daf9gu 34t8z23r (c) == asdasdasd    c";
        
        pane.setText(exp);
        
        finder.setUp(scopehandler, input, res);
        
        ArrayList<CodeError> errs = finder.getErrors();
    }
}
