/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.codearea.InputToCode;

import edu.pse.beast.codearea.InputToCode.NewlineInserter.NewlineInserter;
import edu.pse.beast.codearea.InputToCode.NewlineInserter.NewlineInserterChooser;
import edu.pse.beast.codearea.SaveTextBeforeRemove;
import edu.pse.beast.codearea.StoppedTypingContinuouslyListener;
import edu.pse.beast.codearea.StoppedTypingContinuouslyMessager;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.StyledDocument;

/**
 *
 * @author Holger-Desktop
 */
public class UserInsertToCode implements CaretListener, StoppedTypingContinuouslyListener {
    
    private JTextPane pane;
    private StyledDocument styledDoc;
    private OpenCloseCharList openCloseCharList;
    private LockedLinesHandler lockedLines;
    private NewlineInserterChooser newlineInserterChooser;
    private NewlineInserter currentInserter;
    private int currentCaretPosition;
    private LineHandler lineHandler;
    private TabInserter tabInserter;
    private LineBeginningTabsHandler lineBeginningTabsHandler;
    private SaveTextBeforeRemove saveBeforeRemove;
    private StoppedTypingContinuouslyMessager stoppedTypingContMsger;
    private boolean dontTypeClosingChar;
    private LockedLinesDisplay lockedLinesDisplay;
    
    public UserInsertToCode(JTextPane pane, OpenCloseCharList openCloseCharList,
            SaveTextBeforeRemove saveBeforeRemove) {
        this.pane = pane;
        this.pane.addCaretListener(this);
        this.styledDoc = pane.getStyledDocument(); 
        this.openCloseCharList = openCloseCharList; 
        this.saveBeforeRemove = saveBeforeRemove;
        setupObjects();
    }

    public SaveTextBeforeRemove getSaveBeforeRemove() {
        return saveBeforeRemove;
    }
    
    public void insertNewline() throws BadLocationException {  
        currentInserter.insertNewlineAtCurrentPosition(pane, tabInserter, 
                lineBeginningTabsHandler, currentCaretPosition);               
        newlineInserterChooser.getNewlineInserter();
    }

    public void insertString(String string) {
        try {
            if (lockedLines.isLineLocked(lineHandler.transformToLineNumber(pane.getCaretPosition()))) {
                System.out.println("Can't insert into locked lines.");
            } else {
                if (pane.getSelectedText() == null) {
                    pane.getStyledDocument().insertString(pane.getCaretPosition(), string,null);
                } else {
                    int selectionStart = pane.getSelectionStart();
                    int selectionEnd = pane.getSelectionEnd();
                    try {
                        pane.getStyledDocument().remove(selectionStart, selectionEnd - selectionStart);
                        pane.getStyledDocument().insertString(selectionStart, string, null);
                    } catch (BadLocationException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }

    public void insertTab() throws BadLocationException {
        if(wouldChangedLocked()) return;       
        if(lockedLines.isLineLocked(lineHandler.transformToLineNumber(currentCaretPosition))) return;        
        tabInserter.insertTabAtPos(currentCaretPosition);
    }

    public void insertChar(char keyChar) throws BadLocationException {
        if(wouldChangedLocked()) return;
        if(pane.getSelectedText() != null) {
            pane.getStyledDocument().remove(
                    pane.getSelectionStart(),
                    pane.getSelectionEnd() - pane.getSelectionStart());
        }
        if(openCloseCharList.isOpenChar(keyChar)) {
            OpenCloseChar occ = openCloseCharList.getOpenCloseChar(keyChar);dontTypeClosingChar = true;
            occ.insertIntoDocument(pane, currentCaretPosition);
            dontTypeClosingChar = true;                        
        } else if(dontTypeClosingChar && openCloseCharList.isCloseChar(keyChar)) {
            dontTypeClosingChar = false;
            pane.setCaretPosition(currentCaretPosition + 1);
            return;
        } else {
            styledDoc.insertString(currentCaretPosition, Character.toString(keyChar), null);
        }
    }
    
    public void lockLine(int line) {
        lockedLines.lockLine(line);        
        this.currentInserter = this.newlineInserterChooser.getNewlineInserter();        
    }
    
    @Override
    public void caretUpdate(CaretEvent ce) {
        currentCaretPosition = ce.getDot();
        currentInserter = newlineInserterChooser.getNewlineInserter();        
    }
    
    private void setupObjects() {
        stoppedTypingContMsger = new StoppedTypingContinuouslyMessager(pane);
        stoppedTypingContMsger.addListener(this);
        this.lineHandler = new LineHandler(this.pane);
        this.tabInserter = new TabInserter(this.pane, lineHandler);
        this.lockedLines = new LockedLinesHandler(styledDoc, lineHandler, saveBeforeRemove);
        this.lineBeginningTabsHandler = new CurlyBracesLineBeginningTabHandler(pane, lineHandler);
        this.newlineInserterChooser = new NewlineInserterChooser(pane, lockedLines);
        this.currentInserter = this.newlineInserterChooser.getNewlineInserter();       
        this.lockedLinesDisplay = new LockedLinesDisplay(pane, lineHandler, lockedLines);
    }

    void moveToEndOfCurrentLine() {
        int end = lineHandler.getClosestLineBeginningAfter(currentCaretPosition);
        pane.setCaretPosition(end);
    }

    void moveToStartOfCurrentLine() {
        int start = lineHandler.getClosestLineBeginning(currentCaretPosition);
        pane.setCaretPosition(start + 1);
    }

    void removeToTheRight() {
        if(wouldChangedLocked()) return;
        try {
            if(pane.getStyledDocument().getText(currentCaretPosition, 1).equals("\n") && 
                    !pane.getStyledDocument().getText(currentCaretPosition - 1, 1).equals("\n")) {
                if(lockedLines.isLineLocked(lineHandler.transformToLineNumber(currentCaretPosition) + 1)) {
                    return;
                }
            }
        } catch (BadLocationException ex) {
            
        }
            
        try {
            if(pane.getSelectedText() != null) {
                pane.getStyledDocument().remove(
                        pane.getSelectionStart(),
                        pane.getSelectionEnd() - pane.getSelectionStart());
                return;
            }
            pane.getStyledDocument().remove(currentCaretPosition, 1);
        } catch (BadLocationException ex) {
            Logger.getLogger(UserInsertToCode.class.getName()).log(Level.SEVERE, null, ex);
        }
            
        
    }

    void removeToTheLeft() {
        if(wouldChangedLocked()) return;
        if(currentCaretPosition == 0) return;
        try {
            if(pane.getSelectedText() != null) {
                pane.getStyledDocument().remove(
                        pane.getSelectionStart(),
                        pane.getSelectionEnd() - pane.getSelectionStart());
                return;
            }
            
            pane.getStyledDocument().remove(currentCaretPosition - 1, 1);
            } catch (BadLocationException ex) {
                Logger.getLogger(UserInsertToCode.class.getName()).log(Level.SEVERE, null, ex);
            }
    }

    private boolean wouldChangedLocked() {
        try {
            int line = lineHandler.transformToLineNumber(currentCaretPosition);
            if(lockedLines.isLineLocked(line)) return true;
            ArrayList<Integer> lines = lineHandler.getLinesBetween(pane.getSelectionStart(), pane.getSelectionEnd());
            for(int i : lines) {
                if(lockedLines.isLineLocked(i)) return true;
            }
            return false;
        } catch (BadLocationException ex) {
            Logger.getLogger(UserInsertToCode.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    public void StoppedTypingContinuously(int newPos) {
        dontTypeClosingChar = false;
    }

    public OpenCloseCharList getOccList() {
        return this.openCloseCharList;
    }

    public void unlockAll() {
        lockedLines.unlockAll();
    }
}
