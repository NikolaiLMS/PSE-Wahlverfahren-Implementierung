package edu.pse.beast.parametereditor;

import edu.pse.beast.highlevel.DisplaysStringsToUser;
import edu.pse.beast.stringresource.ParameterEditorStringResProvider;
import edu.pse.beast.stringresource.StringLoaderInterface;
import edu.pse.beast.stringresource.StringResourceLoader;
import javax.swing.JSpinner;
/**
 *
 * @author Jonas
 */
public class ParameterEditorWindow extends javax.swing.JFrame implements DisplaysStringsToUser{

    private AdvancedWindow advWindow = new AdvancedWindow();
    /**
     * Creates new form ParameterEditorWindow
     */
    public ParameterEditorWindow() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        toolbar = new javax.swing.JToolBar();
        voters = new javax.swing.JLabel();
        voterMin = new javax.swing.JSpinner();
        voterTo = new javax.swing.JLabel();
        voterMax = new javax.swing.JSpinner();
        candidates = new javax.swing.JLabel();
        candMin = new javax.swing.JSpinner();
        candTo = new javax.swing.JLabel();
        candMax = new javax.swing.JSpinner();
        seats = new javax.swing.JLabel();
        seatMin = new javax.swing.JSpinner();
        seatTo = new javax.swing.JLabel();
        seatMax = new javax.swing.JSpinner();
        timeout = new javax.swing.JLabel();
        timeoutNum = new javax.swing.JSpinner();
        timeoutUnit = new javax.swing.JComboBox<>();
        processes = new javax.swing.JLabel();
        amountProcessesSpinner = new javax.swing.JSpinner();
        advancedButton = new javax.swing.JButton();
        menubar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        loadMenuItem = new javax.swing.JMenuItem();
        projectMenu = new javax.swing.JMenu();
        startMenuItem = new javax.swing.JMenuItem();
        stopMenuItem = new javax.swing.JMenuItem();
        optionsMenu = new javax.swing.JMenu();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("ParameterEditor");
        setMinimumSize(new java.awt.Dimension(458, 327));

        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setMaximumSize(new java.awt.Dimension(458, 73));
        toolbar.setMinimumSize(new java.awt.Dimension(458, 73));
        toolbar.setPreferredSize(new java.awt.Dimension(458, 73));

        voters.setText("Wähler");

        voterMin.setMaximumSize(new java.awt.Dimension(0, 10000));

        voterTo.setText("bis");

        voterMax.setMaximumSize(new java.awt.Dimension(0, 10000));

        candidates.setText("Kandidaten");

        candMin.setMaximumSize(new java.awt.Dimension(0, 10000));

        candTo.setText("bis");

        candMax.setMaximumSize(new java.awt.Dimension(0, 10000));

        seats.setText("Sitze");

        seatMin.setMaximumSize(new java.awt.Dimension(0, 10000));

        seatTo.setText("bis");

        seatMax.setMaximumSize(new java.awt.Dimension(0, 10000));

        timeout.setText("Dauer");

        timeoutNum.setMaximumSize(new java.awt.Dimension(0, 32767));

        timeoutUnit.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Sekunden", "Minuten", "Stunden", "Tage" }));

        processes.setText("Max. Prozesse");

        amountProcessesSpinner.setMaximumSize(new java.awt.Dimension(0, 50));

        advancedButton.setText("Erweitert...");
        advancedButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                advancedButtonActionPerformed(evt);
            }
        });

        fileMenu.setText("Datei");

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText("Neues Projekt...");
        fileMenu.add(newMenuItem);

        saveMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveMenuItem.setText("Speichern");
        saveMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        saveAsMenuItem.setText("Speichern unter...");
        fileMenu.add(saveAsMenuItem);

        loadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        loadMenuItem.setText("Öffnen...");
        fileMenu.add(loadMenuItem);

        menubar.add(fileMenu);

        projectMenu.setText("Projekt");

        startMenuItem.setText("Teste Eigenschaften");
        projectMenu.add(startMenuItem);

        stopMenuItem.setText("Stoppe Test");
        projectMenu.add(stopMenuItem);

        menubar.add(projectMenu);

        optionsMenu.setText("Eigenschaften");
        menubar.add(optionsMenu);

        setJMenuBar(menubar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(advancedButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(seats, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(candidates, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(voters, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(timeout, javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(processes, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(voterMin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                            .addComponent(seatMin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(candMin, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(timeoutNum, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(amountProcessesSpinner, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(voterTo, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(candTo, javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addComponent(seatTo, javax.swing.GroupLayout.Alignment.TRAILING))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(voterMax, javax.swing.GroupLayout.DEFAULT_SIZE, 64, Short.MAX_VALUE)
                                    .addComponent(candMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(seatMax, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                            .addComponent(timeoutUnit, 0, 95, Short.MAX_VALUE))
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addComponent(toolbar, javax.swing.GroupLayout.PREFERRED_SIZE, 79, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(voters)
                    .addComponent(voterMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(voterTo)
                    .addComponent(voterMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(candTo)
                    .addComponent(candMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(candMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(candidates))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(seatMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seatTo)
                    .addComponent(seatMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(seats))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(timeout)
                    .addComponent(timeoutNum, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeoutUnit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(amountProcessesSpinner, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(processes))
                .addGap(18, 18, 18)
                .addComponent(advancedButton)
                .addContainerGap(44, Short.MAX_VALUE))
        );

        voterMin.getAccessibleContext().setAccessibleName("");

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void advancedButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_advancedButtonActionPerformed
        advWindow.setVisible(true);
    }//GEN-LAST:event_advancedButtonActionPerformed

    private void saveMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveMenuItemActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_saveMenuItemActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ParameterEditorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ParameterEditorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ParameterEditorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ParameterEditorWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ParameterEditorWindow().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton advancedButton;
    private javax.swing.JSpinner amountProcessesSpinner;
    private javax.swing.JSpinner candMax;
    private javax.swing.JSpinner candMin;
    private javax.swing.JLabel candTo;
    private javax.swing.JLabel candidates;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JMenuItem loadMenuItem;
    private javax.swing.JMenuBar menubar;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JMenu optionsMenu;
    private javax.swing.JLabel processes;
    private javax.swing.JMenu projectMenu;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JSpinner seatMax;
    private javax.swing.JSpinner seatMin;
    private javax.swing.JLabel seatTo;
    private javax.swing.JLabel seats;
    private javax.swing.JMenuItem startMenuItem;
    private javax.swing.JMenuItem stopMenuItem;
    private javax.swing.JLabel timeout;
    private javax.swing.JSpinner timeoutNum;
    private javax.swing.JComboBox<String> timeoutUnit;
    private javax.swing.JToolBar toolbar;
    private javax.swing.JSpinner voterMax;
    private javax.swing.JSpinner voterMin;
    private javax.swing.JLabel voterTo;
    private javax.swing.JLabel voters;
    // End of variables declaration//GEN-END:variables
    protected JSpinner getVoterMin() {
        return voterMin;
    }
    protected JSpinner getVoterMax() {
        return voterMax;
    }
    protected JSpinner getCandMin() {
        return candMin;
    }
    protected JSpinner getCandMax() {
        return candMax;
    }
    protected JSpinner getSeatMin() {
        return seatMin;
    }
    protected JSpinner getSeatMax() {
        return seatMax;
    }
    protected JSpinner getTimeoutNum() {
        return timeoutNum;
    }
    protected javax.swing.JComboBox<String> getTimeoutUnit() {
        return timeoutUnit;
    }
    protected JSpinner getAmountProcessesSpinner() {
        return amountProcessesSpinner;
    }
    protected AdvancedWindow getAdvancedWindow() {
        return advWindow;
    }
    public javax.swing.JToolBar getToolbar() {
        return toolbar;
    }

    @Override
    public void updateStringRes(StringLoaderInterface stringResIF) {
        ParameterEditorStringResProvider provider = stringResIF.getParameterEditorStringResProvider();
        
        
        StringResourceLoader other = provider.getOtherStringRes();
        setTitle(other.getStringFromID("title"));
        voters.setText(other.getStringFromID("voters"));
        candidates.setText(other.getStringFromID("candidates"));
        seats.setText(other.getStringFromID("seats"));
        timeout.setText(other.getStringFromID("timeout"));
        processes.setText(other.getStringFromID("max_processes"));
        voterTo.setText(other.getStringFromID("to"));
        candTo.setText(other.getStringFromID("to"));
        seatTo.setText(other.getStringFromID("to"));
        advancedButton.setText(other.getStringFromID("advanced"));
        timeoutUnit.removeAllItems();
        timeoutUnit.addItem(other.getStringFromID("seconds"));
        timeoutUnit.addItem(other.getStringFromID("minutes"));
        timeoutUnit.addItem(other.getStringFromID("hours"));
        timeoutUnit.addItem(other.getStringFromID("days"));
        //advWindow.updateStringRes(other); //TODO: Implement
        
        /*StringResourceLoader menu = provider.getMenuStringRes();
        fileMenu.setText(menu.getStringFromID("file"));
        projectMenu.setText(menu.getStringFromID("project"));
        optionsMenu.setText(menu.getStringFromID("options"));
        newMenuItem.setText(menu.getStringFromID("new"));
        saveMenuItem.setText(menu.getStringFromID("save"));
        saveAsMenuItem.setText(menu.getStringFromID("save_as"));
        loadMenuItem.setText(menu.getStringFromID("load"));
        startMenuItem.setText(menu.getStringFromID("start"));
        stopMenuItem.setText(menu.getStringFromID("stop"));
        
        StringResourceLoader toolbarTip = provider.getToolbarTipStringRes();
        newIcon.setToolTipText(toolbarTip.getStringFromID("new"));
        saveIcon.setToolTipText(toolbarTip.getStringFromID("save"));
        saveAsIcon.setToolTipText(toolbarTip.getStringFromID("save_as"));
        loadIcon.setToolTipText(toolbarTip.getStringFromID("load"));
        startIcon.setToolTipText(toolbarTip.getStringFromID("start"));
        stopIcon.setToolTipText(toolbarTip.getStringFromID("stop"));*/
    }
}
