/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.pse.beast.celectiondescriptioneditor.GUI;

import edu.pse.beast.celectiondescriptioneditor.ElectionTemplates.ElectionTemplateChooser;
import edu.pse.beast.highlevel.DisplaysStringsToUser;
import edu.pse.beast.stringresource.StringLoaderInterface;

import javax.swing.*;

/**
 * @author Holger
 */
public class CCodeEditorGUI extends javax.swing.JFrame implements DisplaysStringsToUser{


    /**
     * Update the language dependent displayed Strings in this class.
     * @param stringLoaderInterface the new stringLoaderInterface
     */
    public void updateStringRes(StringLoaderInterface stringLoaderInterface) {
        setTitle("C-Editor");
        saveChanges = stringLoaderInterface.getBooleanExpEditorStringResProvider().
                getBooleanExpEditorWindowStringRes().getStringFromID("saveChanges");
        save = stringLoaderInterface.getBooleanExpEditorStringResProvider().
                getBooleanExpEditorWindowStringRes().getStringFromID("save");
        cancelOption = stringLoaderInterface.getBooleanExpEditorStringResProvider().
                getBooleanExpEditorWindowStringRes().getStringFromID("cancelOption");
        noOption = stringLoaderInterface.getBooleanExpEditorStringResProvider().
                getBooleanExpEditorWindowStringRes().getStringFromID("noOption");
        yesOption = stringLoaderInterface.getBooleanExpEditorStringResProvider().
                getBooleanExpEditorWindowStringRes().getStringFromID("yesOption");
    }

    /**
     * Creates new form CCodeEditor
     */
    public CCodeEditorGUI() {
        initComponents();
        jTextPane2.setEditable(false);
        setDefaultCloseOperation(ElectionTemplateChooser.HIDE_ON_CLOSE);
    }

    public JTextPane getCodeArea() {
        return jTextPane1;
    }
    
    public JScrollPane getCodeAreaScrollPane() {
        return jScrollPane3;
    }
    
    public void setMenuBar(JMenuBar menuBar) {
        setJMenuBar(menuBar);
    }
    
    public JToolBar getToolBar() {
        return jToolBar1;
    }

    public JTextPane getErrorPane() {
        return jTextPane2;
    }

    /**
     * Method that opens pane that asks the user whether he wants to save or not.
     * @return the option clicked by the user
     */
    public int showOptionPane(String electionDescriptionName) {
        Object[] options = {yesOption,
                noOption,
                cancelOption};
        return  JOptionPane.showOptionDialog(this,
                saveChanges + electionDescriptionName + save,
                "",
                JOptionPane.YES_NO_CANCEL_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                options,
                options[0]);
    }
    /**
     * Adds the given string to the window title, used for displaying name of currently loaded PostAndPrePropDescription
     * @param s
     */
    public void setWindowTitle(String s) {
        this.setTitle(title + " " + s);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jMenuItem9 = new javax.swing.JMenuItem();
        jPopupMenu1 = new javax.swing.JPopupMenu();
        jPopupMenu2 = new javax.swing.JPopupMenu();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane2 = new javax.swing.JTextPane();
        jToolBar1 = new javax.swing.JToolBar();

        jMenuItem1.setText("jMenuItem1");

        jMenuItem9.setText("jMenuItem9");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Wahleditor - EinfacheMehrheitswahl.c");

        jScrollPane3.setViewportView(jTextPane1);

        jScrollPane1.setViewportView(jTextPane2);

        jToolBar1.setFloatable(false);
        jToolBar1.setRollover(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addComponent(jScrollPane1)
            .addComponent(jToolBar1, javax.swing.GroupLayout.DEFAULT_SIZE, 715, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jToolBar1, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 372, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 101, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JMenuItem jMenuItem9;
    private javax.swing.JPopupMenu jPopupMenu1;
    private javax.swing.JPopupMenu jPopupMenu2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane1;
    private javax.swing.JTextPane jTextPane2;
    private javax.swing.JToolBar jToolBar1;
    // End of variables declaration//GEN-END:variables

    private String title = "C-Editor";
    private String saveChanges;
    private String save;
    private String yesOption;
    private String noOption;
    private String cancelOption;

    public void setNewCodeArea() {
        jTextPane1 = new JTextPane();
        jScrollPane3.setViewportView(jTextPane1);
    }
}
