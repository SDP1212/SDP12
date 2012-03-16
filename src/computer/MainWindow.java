/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * MainWindow.java
 *
 * Created on 25-Jan-2012, 10:04:59
 */
package computer;

/**
 *
 * @author Diana Crisan
 * @author Matt Jeffryes
 */
import brick.Brick;
import computer.ai.*;
import computer.simulator.Direction;
import computer.simulator.Pitch;
import computer.simulator.Robot;
import java.awt.Cursor;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.Rectangle;

public class MainWindow extends javax.swing.JFrame {

    /** Creates new form MainWindow */
    
    public final static int STATUS_DISCONNECTED = 0;
    public final static int STATUS_CONNECTING = 1;
    public final static int STATUS_CONNECTED = 2;
    
    private ApplicationController appController;
    private Communication commController;
    private int connected = STATUS_DISCONNECTED;
    
    public MainWindow(ApplicationController controller) {
        appController = controller;
        commController = controller.getCommunicationController();
        initComponents();
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice defaultScreen = ge.getDefaultScreenDevice();
        Rectangle rect = defaultScreen.getDefaultConfiguration().getBounds();
        int x = (int)rect.getMinX();
        int y = (int)rect.getMaxY()-getHeight();
        setLocation(x,y);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                appController.close();
                System.exit(0);
            }});
            spinnerRadius.setValue(Integer.valueOf(20));
            for (Class aiClass : new Class[]{SmartAI.class, PathSearchAI.class, Shooter.class, Creeper.class, DumbAI.class, PenaltyGoalie.class, PenaltyShooter.class, GoalkeeperAI.class, DefendAndPlayAI.class, InterceptorAI.class}) {
                comboAI.addItem(aiClass);
            }
    }
    
    public void setStatus(int newConnected) {
        connected = newConnected;
        this.rootPane.setCursor(Cursor.getDefaultCursor());
        if (connected == STATUS_CONNECTED) {
            connectButton.setText("Disconnect");
        } else if (connected == STATUS_CONNECTING) { 
            this.rootPane.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        } else {
            connectButton.setText("Connect");
        }
    }
    
    public int status() {
        return connected;
    }
    
    public void showRetryDialog() {
        Object[] values = {"Retry", "Cancel"};
        int answer = JOptionPane.showOptionDialog(this.rootPane, "Connecting failed", "Connection", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, values, values[0]);
        if (answer == 0) {
            appController.connect();
        }
    }

    /** This method is called from within the constructor to
     * initialise the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup = new javax.swing.ButtonGroup();
        groupSpeed = new javax.swing.ButtonGroup();
        connectButton = new javax.swing.JButton();
        stopButton = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        comboTarget = new javax.swing.JComboBox();
        comboRobot = new javax.swing.JComboBox();
        comboAI = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        buttonRunVision = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radioForward = new javax.swing.JRadioButton();
        radioBackward = new javax.swing.JRadioButton();
        kickRadio = new javax.swing.JRadioButton();
        radioSlow = new javax.swing.JRadioButton();
        radioMedium = new javax.swing.JRadioButton();
        radioFast = new javax.swing.JRadioButton();
        radioArcLeft = new javax.swing.JRadioButton();
        radioArcRight = new javax.swing.JRadioButton();
        spinnerRadius = new javax.swing.JSpinner();
        radioRotate = new javax.swing.JRadioButton();
        spinnerRotate = new javax.swing.JSpinner();
        goButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        fileMenu = new javax.swing.JMenu();
        openMenuItem = new javax.swing.JMenuItem();
        saveMenuItem = new javax.swing.JMenuItem();
        saveAsMenuItem = new javax.swing.JMenuItem();
        exitMenuItem = new javax.swing.JMenuItem();
        editMenu = new javax.swing.JMenu();
        cutMenuItem = new javax.swing.JMenuItem();
        copyMenuItem = new javax.swing.JMenuItem();
        pasteMenuItem = new javax.swing.JMenuItem();
        deleteMenuItem = new javax.swing.JMenuItem();
        helpMenu = new javax.swing.JMenu();
        contentsMenuItem = new javax.swing.JMenuItem();
        aboutMenuItem = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        connectButton.setText("Connect");
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectButtonActionPerformed(evt);
            }
        });

        stopButton.setText("Stop ");
        stopButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopButtonActionPerformed(evt);
            }
        });

        comboTarget.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Left", "Right" }));

        comboRobot.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Blue", "Yellow" }));
        comboRobot.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboRobotActionPerformed(evt);
            }
        });

        jLabel1.setText("AI");

        buttonRunVision.setText("Run Vision");
        buttonRunVision.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                buttonRunVisionActionPerformed(evt);
            }
        });

        jLabel2.setText("Attacking");

        jLabel3.setText("We are");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(comboAI, javax.swing.GroupLayout.PREFERRED_SIZE, 212, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel3)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 29, Short.MAX_VALUE)
                                        .addComponent(comboRobot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel1Layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(comboTarget, 0, 79, Short.MAX_VALUE)))
                                .addGap(292, 292, 292)))
                        .addGap(27, 27, 27))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(buttonRunVision, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addContainerGap(355, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboAI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(comboTarget, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(comboRobot, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 66, Short.MAX_VALUE)
                .addComponent(buttonRunVision)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Play", jPanel1);

        buttonGroup.add(radioForward);
        radioForward.setText("Forward");

        buttonGroup.add(radioBackward);
        radioBackward.setText("Backward");
        radioBackward.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioBackwardActionPerformed(evt);
            }
        });

        buttonGroup.add(kickRadio);
        kickRadio.setText("Kick");
        kickRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                kickRadioActionPerformed(evt);
            }
        });

        groupSpeed.add(radioSlow);
        radioSlow.setText("Slow");

        groupSpeed.add(radioMedium);
        radioMedium.setText("Medium");

        groupSpeed.add(radioFast);
        radioFast.setText("Fast");
        radioFast.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioFastActionPerformed(evt);
            }
        });

        buttonGroup.add(radioArcLeft);
        radioArcLeft.setText("Arc Left");

        buttonGroup.add(radioArcRight);
        radioArcRight.setText("Arc Right");

        buttonGroup.add(radioRotate);
        radioRotate.setText("Rotate");

        goButton.setText("Go");
        goButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                goButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(radioForward)
                            .addComponent(radioBackward)
                            .addComponent(radioRotate)
                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addGroup(jPanel2Layout.createSequentialGroup()
                                    .addComponent(kickRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addGap(29, 29, 29))
                                .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                    .addComponent(radioArcRight, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(radioArcLeft, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 123, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addGap(18, 18, 18)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(spinnerRadius)
                            .addComponent(spinnerRotate, javax.swing.GroupLayout.DEFAULT_SIZE, 62, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 87, Short.MAX_VALUE)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(radioMedium, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(radioSlow)
                            .addComponent(radioFast, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGap(93, 93, 93))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(goButton, javax.swing.GroupLayout.DEFAULT_SIZE, 184, Short.MAX_VALUE)
                        .addGap(276, 276, 276))))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioForward)
                    .addComponent(radioSlow))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioBackward)
                    .addComponent(radioMedium))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(radioFast)
                    .addComponent(radioRotate)
                    .addComponent(spinnerRotate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioArcLeft)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(radioArcRight)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(kickRadio))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(10, 10, 10)
                        .addComponent(spinnerRadius, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(goButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jTabbedPane1.addTab("Test", jPanel2);

        fileMenu.setMnemonic('f');
        fileMenu.setText("File");

        openMenuItem.setMnemonic('o');
        openMenuItem.setText("Open");
        fileMenu.add(openMenuItem);

        saveMenuItem.setMnemonic('s');
        saveMenuItem.setText("Save");
        fileMenu.add(saveMenuItem);

        saveAsMenuItem.setMnemonic('a');
        saveAsMenuItem.setText("Save As ...");
        saveAsMenuItem.setDisplayedMnemonicIndex(5);
        fileMenu.add(saveAsMenuItem);

        exitMenuItem.setMnemonic('x');
        exitMenuItem.setText("Exit");
        exitMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuItemActionPerformed(evt);
            }
        });
        fileMenu.add(exitMenuItem);

        menuBar.add(fileMenu);

        editMenu.setMnemonic('e');
        editMenu.setText("Edit");

        cutMenuItem.setMnemonic('t');
        cutMenuItem.setText("Cut");
        editMenu.add(cutMenuItem);

        copyMenuItem.setMnemonic('y');
        copyMenuItem.setText("Copy");
        editMenu.add(copyMenuItem);

        pasteMenuItem.setMnemonic('p');
        pasteMenuItem.setText("Paste");
        editMenu.add(pasteMenuItem);

        deleteMenuItem.setMnemonic('d');
        deleteMenuItem.setText("Delete");
        editMenu.add(deleteMenuItem);

        menuBar.add(editMenu);

        helpMenu.setMnemonic('h');
        helpMenu.setText("Help");

        contentsMenuItem.setMnemonic('c');
        contentsMenuItem.setText("Contents");
        helpMenu.add(contentsMenuItem);

        aboutMenuItem.setMnemonic('a');
        aboutMenuItem.setText("About");
        helpMenu.add(aboutMenuItem);

        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectButton, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                .addGap(287, 287, 287))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(stopButton, javax.swing.GroupLayout.PREFERRED_SIZE, 109, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(375, Short.MAX_VALUE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 484, Short.MAX_VALUE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectButton)
                .addGap(22, 22, 22)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 267, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(stopButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void exitMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuItemActionPerformed
        System.exit(0);
    }//GEN-LAST:event_exitMenuItemActionPerformed

    private void goButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_goButtonActionPerformed
        int speed = Brick.SLOW;
        if (radioSlow.isSelected()) {
            speed = Brick.SLOW;
        } else if (radioMedium.isSelected()) {
            speed = Brick.MEDIUM;
        } else {
            speed = Brick.FAST;
        }
        if (radioForward.isSelected()) {
            commController.forward(speed);
        } else if (radioBackward.isSelected()) {
            commController.backward(speed);
        } else if (kickRadio.isSelected()) {
            commController.kick();
        } else if (radioRotate.isSelected()) {
            commController.rotate(Math.toRadians(((Integer)spinnerRotate.getValue()).intValue()));
        } else if (radioArcLeft.isSelected()) {
			commController.arcLeft(((Integer)spinnerRadius.getValue()).intValue());
		} else if (radioArcRight.isSelected()) {
			commController.arcRight(((Integer)spinnerRadius.getValue()).intValue());
		}
    }//GEN-LAST:event_goButtonActionPerformed

    private void connectButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectButtonActionPerformed
        if (status() == STATUS_DISCONNECTED) {
            appController.connect();
        } else if (status() == STATUS_CONNECTED) {
            appController.disconnect();
			appController.stopEngine();
        }
    }//GEN-LAST:event_connectButtonActionPerformed

    private void stopButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopButtonActionPerformed
        appController.stopEngine();
        commController.stop();
    }//GEN-LAST:event_stopButtonActionPerformed

    private void radioBackwardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioBackwardActionPerformed

    }//GEN-LAST:event_radioBackwardActionPerformed

    private void kickRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_kickRadioActionPerformed

        // TODO add your handling code here:}//GEN-LAST:event_kickRadioActionPerformed
    }
        private void radioFastActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioFastActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioFastActionPerformed

    private void buttonRunVisionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_buttonRunVisionActionPerformed
        appController.stopEngine();
		short colour;
		short target;
		if (comboTarget.getSelectedIndex() == 0) {
			target = Pitch.TARGET_LEFT_GOAL;
		} else {
			target = Pitch.TARGET_RIGHT_GOAL;
		}
		if (comboRobot.getSelectedIndex() == 0) {
			colour = Robot.YELLOW_PLATE;
		} else {
			colour = Robot.BLUE_PLATE;
		}
		appController.startEngine((Class<AI>)comboAI.getSelectedItem(), target, colour);
    }//GEN-LAST:event_buttonRunVisionActionPerformed

	private void comboRobotActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboRobotActionPerformed
		if (comboRobot.getSelectedIndex() == 0) {
			appController.setOurColour(Robot.BLUE_PLATE);
		} else {
			appController.setOurColour(Robot.YELLOW_PLATE);
		}
	}//GEN-LAST:event_comboRobotActionPerformed
    
    /**
     * @param args the command line arguments
     */
    public static void setup(ApplicationController controller) {
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
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainWindow.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        final ApplicationController c = controller;
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                MainWindow window = new MainWindow(c);
                window.setVisible(true);
                c.setWindow(window);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem aboutMenuItem;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JButton buttonRunVision;
    private javax.swing.JComboBox comboAI;
    private javax.swing.JComboBox comboRobot;
    private javax.swing.JComboBox comboTarget;
    private javax.swing.JButton connectButton;
    private javax.swing.JMenuItem contentsMenuItem;
    private javax.swing.JMenuItem copyMenuItem;
    private javax.swing.JMenuItem cutMenuItem;
    private javax.swing.JMenuItem deleteMenuItem;
    private javax.swing.JMenu editMenu;
    private javax.swing.JMenuItem exitMenuItem;
    private javax.swing.JMenu fileMenu;
    private javax.swing.JButton goButton;
    private javax.swing.ButtonGroup groupSpeed;
    private javax.swing.JMenu helpMenu;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JRadioButton kickRadio;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem openMenuItem;
    private javax.swing.JMenuItem pasteMenuItem;
    private javax.swing.JRadioButton radioArcLeft;
    private javax.swing.JRadioButton radioArcRight;
    private javax.swing.JRadioButton radioBackward;
    private javax.swing.JRadioButton radioFast;
    private javax.swing.JRadioButton radioForward;
    private javax.swing.JRadioButton radioMedium;
    private javax.swing.JRadioButton radioRotate;
    private javax.swing.JRadioButton radioSlow;
    private javax.swing.JMenuItem saveAsMenuItem;
    private javax.swing.JMenuItem saveMenuItem;
    private javax.swing.JSpinner spinnerRadius;
    private javax.swing.JSpinner spinnerRotate;
    private javax.swing.JButton stopButton;
    // End of variables declaration//GEN-END:variables
}
