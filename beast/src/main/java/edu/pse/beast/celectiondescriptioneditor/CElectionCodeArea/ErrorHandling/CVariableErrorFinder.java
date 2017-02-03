/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.ErrorHandling;

import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.Antlr.CAntlrHandler;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.Antlr.CBaseListener;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.Antlr.CBaseVisitor;
import edu.pse.beast.celectiondescriptioneditor.CElectionCodeArea.Antlr.CParser;
import edu.pse.beast.codearea.ErrorHandling.CodeError;
import edu.pse.beast.codearea.ErrorHandling.ErrorFinder;
import java.util.ArrayList;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

/**
 *
 * @author Holger-Desktop
 */
public class CVariableErrorFinder extends CBaseListener implements ErrorFinder {

    private CAntlrHandler antlrHandler;
    private ArrayList<CodeError> foundErrors = new ArrayList<>();
    
    public CVariableErrorFinder(CAntlrHandler antlrHandler) {
        this.antlrHandler = antlrHandler;
    }
    
    @Override
    public ArrayList<CodeError> getErrors() {
        foundErrors.clear();
        ParseTree parsetree = antlrHandler.getCParseTree();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(this, parsetree);
        return foundErrors;
    }
    
    @Override 
    public void enterAssignmentExpression(CParser.AssignmentExpressionContext ctx) { 
        int i = 0;
    }
    
}
