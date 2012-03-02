package computer.vision;

import java.awt.image.BufferedImage;

/**
 * The GUI of the system.
 */
public class GUI extends javax.swing.JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static String className = null;

	/** Creates new form GUI */
	public GUI() {
		initComponents();
	}

	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        radioOurTeam = new javax.swing.ButtonGroup();
        radioOurGoal = new javax.swing.ButtonGroup();
        onScreenImage = new javax.swing.JLabel();
        btnBarrelCorrection = new javax.swing.JButton();
        debugSlider = new javax.swing.JSlider();
        blueThreshSlider = new javax.swing.JSlider();
        yellThreshSlider = new javax.swing.JSlider();
        ballSearchSlider = new javax.swing.JSlider();
        blueThreshLabel = new javax.swing.JLabel();
        yellThreshLabel = new javax.swing.JLabel();
        ballSearchLabel = new javax.swing.JLabel();
        btnPitch1 = new javax.swing.JButton();
        btnPitch2 = new javax.swing.JButton();
        modeLabel = new javax.swing.JLabel();
        modeSlider = new javax.swing.JSlider();
        labelDebug = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Vision"); // NOI18N
        setResizable(false);

        onScreenImage.setText("Image goes here");

        btnBarrelCorrection.setText("Toggle Barrel Correction");
        btnBarrelCorrection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBarrelCorrectionActionPerformed(evt);
            }
        });

        debugSlider.setMaximum(5);
        debugSlider.setPaintLabels(true);
        debugSlider.setToolTipText("");
        debugSlider.setValue(3);
        debugSlider.setName("Debug"); // NOI18N
        debugSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                debugSliderStateChanged(evt);
            }
        });

        blueThreshSlider.setMaximum(765);
        blueThreshSlider.setValue(350);
        blueThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blueThreshSliderStateChanged(evt);
            }
        });

        yellThreshSlider.setMaximum(765);
        yellThreshSlider.setValue(150);
        yellThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yellThreshSliderStateChanged(evt);
            }
        });

        ballSearchSlider.setMaximum(1000);
        ballSearchSlider.setValue(700);
        ballSearchSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ballSearchSliderStateChanged(evt);
            }
        });

        blueThreshLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        blueThreshLabel.setLabelFor(blueThreshSlider);
        blueThreshLabel.setText("Blue threshold:");

        yellThreshLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        yellThreshLabel.setLabelFor(yellThreshSlider);
        yellThreshLabel.setText("Yellow threshold:");

        ballSearchLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        ballSearchLabel.setLabelFor(ballSearchSlider);
        ballSearchLabel.setText("Ball search distance:");

        btnPitch1.setText("Pitch 1");
        btnPitch1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPitch1ActionPerformed(evt);
            }
        });

        btnPitch2.setText("Pitch 2");
        btnPitch2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPitch2ActionPerformed(evt);
            }
        });

        modeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modeLabel.setLabelFor(modeSlider);
        modeLabel.setText("Statistical mode for angle:");

        modeSlider.setMaximum(24);
        modeSlider.setMinimum(1);
        modeSlider.setValue(10);
        modeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modeSliderStateChanged(evt);
            }
        });

        labelDebug.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDebug.setLabelFor(debugSlider);
        labelDebug.setText("Debug level:");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnBarrelCorrection, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(ballSearchSlider, 0, 0, Short.MAX_VALUE)
                    .addComponent(yellThreshSlider, 0, 0, Short.MAX_VALUE)
                    .addComponent(debugSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnPitch1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 78, Short.MAX_VALUE)
                        .addComponent(btnPitch2))
                    .addComponent(blueThreshLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(blueThreshSlider, 0, 0, Short.MAX_VALUE)
                    .addComponent(labelDebug, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(yellThreshLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(ballSearchLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(modeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE)
                    .addComponent(modeSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 196, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(99, 99, 99)
                        .addComponent(labelDebug, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(debugSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blueThreshLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(blueThreshSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yellThreshLabel)
                        .addGap(2, 2, 2)
                        .addComponent(yellThreshSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ballSearchLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ballSearchSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnBarrelCorrection)
                        .addGap(1, 1, 1)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnPitch1)
                            .addComponent(btnPitch2)))
                    .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void btnBarrelCorrectionActionPerformed(
			java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnBarrelCorrectionActionPerformed
		ImageProcessor.useBarrelDistortion = !(ImageProcessor.useBarrelDistortion);

	}// GEN-LAST:event_btnBarrelCorrectionActionPerformed

	private void btnDebuggerActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnDebuggerActionPerformed
		// TODO: ITS HERE

	}// GEN-LAST:event_btnDebuggerActionPerformed

	private void btnPitch1ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPitch1ActionPerformed
		blueThreshSlider.setValue(350);
		ImageProcessor.blueThreshold = 350;
		yellThreshSlider.setValue(150);
		ImageProcessor.yellThreshold = 150;
		ballSearchSlider.setValue(700);
		ImageProcessor.searchdistance = 700;
		modeSlider.setValue(5);
		ImageProcessor.mode = 5;
		ImageProcessor.xlowerlimit = 0;
		ImageProcessor.xupperlimit = 630;
		ImageProcessor.ylowerlimit = 85;
		ImageProcessor.yupperlimit = 410;

	}// GEN-LAST:event_btnPitch1ActionPerformed

	private void btnPitch2ActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_btnPitch2ActionPerformed
		blueThreshSlider.setValue(350);
		ImageProcessor.blueThreshold = 350;
		yellThreshSlider.setValue(150);
		ImageProcessor.yellThreshold = 150;
		ballSearchSlider.setValue(700);
		ImageProcessor.searchdistance = 700;
		modeSlider.setValue(5);
		ImageProcessor.mode = 5;
		ImageProcessor.xlowerlimit = 10;
		ImageProcessor.xupperlimit = 640;
		ImageProcessor.ylowerlimit = 70;
		ImageProcessor.yupperlimit = 405;
	}// GEN-LAST:event_btnPitch2ActionPerformed

	private void debugSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_debugSliderStateChanged
		ImageProcessor.DEBUG_LEVEL = debugSlider.getValue();
		if (debugSlider.getValue() < 4) {
			// debugOutputBlue.setText("");
			// debugOutputYell.setText("");
		}
	}// GEN-LAST:event_debugSliderStateChanged

	private void blueThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_blueThreshSliderStateChanged
		ImageProcessor.blueThreshold = blueThreshSlider.getValue();
	}// GEN-LAST:event_blueThreshSliderStateChanged

	private void yellThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_yellThreshSliderStateChanged
		ImageProcessor.yellThreshold = yellThreshSlider.getValue();
	}// GEN-LAST:event_yellThreshSliderStateChanged

	private void ballSearchSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_ballSearchSliderStateChanged
		ImageProcessor.searchdistance = ballSearchSlider.getValue();
	}// GEN-LAST:event_ballSearchSliderStateChanged

	private void modeSliderStateChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_modeSliderStateChanged
		int v = modeSlider.getValue();
		if (v == 1) {
			ImageProcessor.method = 2;
		} else {
			ImageProcessor.method = 1;
			ImageProcessor.mode = v;
		}
	}// GEN-LAST:event_modeSliderStateChanged

	private void toggleMouseActionPerformed(java.awt.event.ActionEvent evt) {// GEN-FIRST:event_toggleStopActionPerformed
		ImageProcessor.useMouse = !ImageProcessor.useMouse;
	}// GEN-LAST:event_toggleMouseActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel ballSearchLabel;
    private javax.swing.JSlider ballSearchSlider;
    private javax.swing.JLabel blueThreshLabel;
    private javax.swing.JSlider blueThreshSlider;
    private javax.swing.JButton btnBarrelCorrection;
    private javax.swing.JButton btnPitch1;
    private javax.swing.JButton btnPitch2;
    private javax.swing.JSlider debugSlider;
    private javax.swing.JLabel labelDebug;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JSlider modeSlider;
    private javax.swing.JLabel onScreenImage;
    private javax.swing.ButtonGroup radioOurGoal;
    private javax.swing.ButtonGroup radioOurTeam;
    private javax.swing.JLabel yellThreshLabel;
    private javax.swing.JSlider yellThreshSlider;
    // End of variables declaration//GEN-END:variables

	public void setImage(BufferedImage image) {
		if (image != null)
			onScreenImage.getGraphics().drawImage(image, 0, 0,
					image.getWidth(), image.getHeight(), null);
	}

	public javax.swing.JLabel getImage() {
		return onScreenImage;
	}

	public static void setDebugOutputBlue(String text) {
		if (text != null) {
			// debugOutputBlue.setText(text);
		}
	}

	public static void setDebugOutputYell(String text) {
		if (text != null) {
			// debugOutputYell.setText(text);
		}
	}
}
