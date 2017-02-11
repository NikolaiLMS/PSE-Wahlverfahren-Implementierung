/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.codearea.Autocompletion;

import edu.pse.beast.codearea.InputToCode.JTextPaneToolbox;
import edu.pse.beast.codearea.InputToCode.UserInsertToCode;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JTextPane;
import javax.swing.text.BadLocationException;

/**
 *
 * @author Holger-Desktop
 */
public class AutocompletionOption {
    private String similarString;
    private String insertString;
    private int moveCaretAfter = 0;
    public AutocompletionOption(String similarString, String insertString) {
        this.insertString = insertString;
        this.similarString = similarString;
    }
    
    public AutocompletionOption(String similarString, String insertString, int moveCaretAfter) {
        this.insertString = insertString;
        this.similarString = similarString;
        this.moveCaretAfter = moveCaretAfter;
    }
    
    public boolean equals(AutocompletionOption other) {
        return other.similarString.equals(similarString) && other.insertString.equals(insertString);
    }

    public String getInsertString() {
        return insertString;
    }

    public String getSimilarString() {
        return similarString;
    }

    void insertInto(JTextPane pane, int caretPosition, UserInsertToCode insertToCode) {
        try {
            int wordBeginning = JTextPaneToolbox.getWordBeginningAtCursor(pane);
            int dist = pane.getCaretPosition() - wordBeginning;
            String removedWord = pane.getStyledDocument().getText(wordBeginning, dist);            
            for(int i = 0; i < removedWord.length(); ++i) {
                insertToCode.getSaveBeforeRemove().save();
                insertToCode.removeToTheLeft();
            }
            for (int i = 0; i < insertString.length(); i++) {
                if(insertString.charAt(i) == '\n') {
                    insertToCode.insertNewline();
                } else {                    
                    insertToCode.insertChar(insertString.charAt(i)); 
                }               
            }
            
            pane.setCaretPosition(pane.getCaretPosition() + moveCaretAfter);
        } catch (BadLocationException ex) {
            Logger.getLogger(AutocompletionOption.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
}