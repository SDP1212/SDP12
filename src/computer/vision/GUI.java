package computer.vision;

import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.border.BevelBorder;

/**
 * The GUI of the system.
 */
public class GUI extends javax.swing.JFrame {

	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	public static String className = null;
	private Viewer viewer;
    public Color blueRefColor=new Color(ImageProcessor.blueRef[0],ImageProcessor.blueRef[1],ImageProcessor.blueRef[2]);
    public Color yellowRefColor=new Color(ImageProcessor.yellRef[0],ImageProcessor.yellRef[1],ImageProcessor.yellRef[2]);
    public Color redRefColor=new Color(ImageProcessor.redRef[0],ImageProcessor.redRef[1],ImageProcessor.redRef[2]);
	private int[] refColorPointer=null;

	/** Creates new form GUI */
	public GUI() {
		initComponents();
	}

	// <editor-fold defaultstate="collapsed"
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup2 = new javax.swing.ButtonGroup();
        onScreenImage = new javax.swing.JLabel();
        debugSlider = new javax.swing.JSlider();
        modeLabel = new javax.swing.JLabel();
        modeSlider = new javax.swing.JSlider();
        labelDebug = new javax.swing.JLabel();
        sliderContrast = new javax.swing.JSlider();
        sliderSaturation = new javax.swing.JSlider();
        sliderBrightness = new javax.swing.JSlider();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        barrelCheckbox = new javax.swing.JCheckBox();
        pitchMainRadio = new javax.swing.JRadioButton();
        pitchSideRadio = new javax.swing.JRadioButton();
        bluePanel = new javax.swing.JPanel();
        blueThreshSlider = new javax.swing.JSlider();
        blueRefChanger = new javax.swing.JPanel();
        blueRefThreshSlider = new javax.swing.JSlider();
        yellowPanel = new javax.swing.JPanel();
        yellowRefChanger = new javax.swing.JPanel();
        yellThreshSlider = new javax.swing.JSlider();
        yellowRefThreshSlider = new javax.swing.JSlider();
        ballPanel = new javax.swing.JPanel();
        redRefChanger = new javax.swing.JPanel();
        ballSearchSlider = new javax.swing.JSlider();

        buttonGroup2.add(pitchMainRadio);
        buttonGroup2.add(pitchSideRadio);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Vision"); // NOI18N
        setResizable(false);

        onScreenImage.setText("Image goes here");
        onScreenImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onScreenImageMouseClicked(evt);
            }
        });

        debugSlider.setMaximum(5);
        debugSlider.setPaintLabels(true);
        debugSlider.setToolTipText("");
        debugSlider.setValue(ImageProcessor.DEBUG_LEVEL);
        debugSlider.setName("Debug"); // NOI18N
        debugSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                debugSliderStateChanged(evt);
            }
        });

        modeLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        modeLabel.setLabelFor(modeSlider);
        modeLabel.setText("Statistical mode for angle:");

        modeSlider.setMaximum(24);
        modeSlider.setMinimum(1);
        modeSlider.setValue(ImageProcessor.mode);
        modeSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                modeSliderStateChanged(evt);
            }
        });

        labelDebug.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        labelDebug.setLabelFor(debugSlider);
        labelDebug.setText("Debug level:");

        sliderContrast.setMaximum(500);
        sliderContrast.setValue(170);
        sliderContrast.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSliderChanged(evt);
            }
        });

        sliderSaturation.setValue(100);
        sliderSaturation.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                saturationSliderChanged(evt);
            }
        });

        sliderBrightness.setMaximum(500);
        sliderBrightness.setValue(170);
        sliderBrightness.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                brightnessSliderChanged(evt);
            }
        });

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Contrast:");

        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("Saturation:");

        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("Brightness:");

        barrelCheckbox.setSelected(ImageProcessor.useBarrelDistortion);
        barrelCheckbox.setText("Enable Barrel Correction");
        barrelCheckbox.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        barrelCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                barrelCheckboxActionPerformed(evt);
            }
        });

        pitchMainRadio.setSelected(true);
        pitchMainRadio.setText("Main Pitch");
        pitchMainRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pitchMainRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pitchMainRadioActionPerformed(evt);
            }
        });

        pitchSideRadio.setText("Side Pitch");
        pitchSideRadio.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        pitchSideRadio.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pitchSideRadioActionPerformed(evt);
            }
        });

        bluePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Blue Detection"));

        blueThreshSlider.setMaximum(17500);
        blueThreshSlider.setToolTipText("Blue Detection Threshold");
        blueThreshSlider.setValue((int)(ImageProcessor.blueThreshold*100));
        blueThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blueThreshSliderStateChanged(evt);
            }
        });

        blueRefChanger.setBackground(blueRefColor);
        blueRefChanger.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        blueRefChanger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                blueRefChangerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refChangerMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout blueRefChangerLayout = new javax.swing.GroupLayout(blueRefChanger);
        blueRefChanger.setLayout(blueRefChangerLayout);
        blueRefChangerLayout.setHorizontalGroup(
            blueRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        blueRefChangerLayout.setVerticalGroup(
            blueRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        blueRefThreshSlider.setMaximum(2500);
        blueRefThreshSlider.setToolTipText("Blue Reference Auto-Update Threshold");
        blueRefThreshSlider.setValue((int)(ImageProcessor.blueRefThresh*100));
        blueRefThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                blueRefThreshSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout bluePanelLayout = new javax.swing.GroupLayout(bluePanel);
        bluePanel.setLayout(bluePanelLayout);
        bluePanelLayout.setHorizontalGroup(
            bluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(bluePanelLayout.createSequentialGroup()
                .addComponent(blueRefChanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(bluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(blueRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(blueThreshSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        bluePanelLayout.setVerticalGroup(
            bluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blueRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bluePanelLayout.createSequentialGroup()
                .addComponent(blueThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 16, Short.MAX_VALUE))
        );

        yellowPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Yellow Detection"));

        yellowRefChanger.setBackground(yellowRefColor);
        yellowRefChanger.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        yellowRefChanger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                yellowRefChangerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refChangerMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout yellowRefChangerLayout = new javax.swing.GroupLayout(yellowRefChanger);
        yellowRefChanger.setLayout(yellowRefChangerLayout);
        yellowRefChangerLayout.setHorizontalGroup(
            yellowRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        yellowRefChangerLayout.setVerticalGroup(
            yellowRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 34, Short.MAX_VALUE)
        );

        yellThreshSlider.setMaximum(17500);
        yellThreshSlider.setToolTipText("Yellow Detection Threshold");
        yellThreshSlider.setValue((int)(ImageProcessor.yellThreshold*100));
        yellThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yellThreshSliderStateChanged(evt);
            }
        });

        yellowRefThreshSlider.setMaximum(2500);
        yellowRefThreshSlider.setToolTipText("Yellow Reference Auto-Update Threshold");
        yellowRefThreshSlider.setValue((int)(ImageProcessor.yellRefThresh*100));
        yellowRefThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                yellowRefThreshSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout yellowPanelLayout = new javax.swing.GroupLayout(yellowPanel);
        yellowPanel.setLayout(yellowPanelLayout);
        yellowPanelLayout.setHorizontalGroup(
            yellowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, yellowPanelLayout.createSequentialGroup()
                .addComponent(yellowRefChanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(yellowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(yellowRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE)
                    .addComponent(yellThreshSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        yellowPanelLayout.setVerticalGroup(
            yellowPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(yellowRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(yellowPanelLayout.createSequentialGroup()
                .addComponent(yellThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(yellowRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        ballPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Ball Detection"));

        redRefChanger.setBackground(redRefColor);
        redRefChanger.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        redRefChanger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                redRefChangerMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refChangerMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refChangerMouseExited(evt);
            }
        });

        javax.swing.GroupLayout redRefChangerLayout = new javax.swing.GroupLayout(redRefChanger);
        redRefChanger.setLayout(redRefChangerLayout);
        redRefChangerLayout.setHorizontalGroup(
            redRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        redRefChangerLayout.setVerticalGroup(
            redRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        ballSearchSlider.setMaximum(765);
        ballSearchSlider.setToolTipText("Ball Search Distance - actually defines\nhow far away we look for the ball.");
        ballSearchSlider.setValue(ImageProcessor.searchdistance);
        ballSearchSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                ballSearchSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout ballPanelLayout = new javax.swing.GroupLayout(ballPanel);
        ballPanel.setLayout(ballPanelLayout);
        ballPanelLayout.setHorizontalGroup(
            ballPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ballPanelLayout.createSequentialGroup()
                .addComponent(redRefChanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ballSearchSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 155, Short.MAX_VALUE))
        );
        ballPanelLayout.setVerticalGroup(
            ballPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(redRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ballSearchSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 640, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(ballPanel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(yellowPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(debugSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(labelDebug, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(bluePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(modeLabel, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(modeSlider, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(sliderSaturation, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(barrelCheckbox, javax.swing.GroupLayout.PREFERRED_SIZE, 197, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(pitchMainRadio, javax.swing.GroupLayout.PREFERRED_SIZE, 99, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pitchSideRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 98, Short.MAX_VALUE))
                    .addComponent(sliderContrast, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.DEFAULT_SIZE, 197, Short.MAX_VALUE)
                    .addComponent(sliderBrightness, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 480, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(labelDebug, javax.swing.GroupLayout.PREFERRED_SIZE, 17, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(debugSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(bluePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(yellowPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(ballPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(barrelCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(pitchMainRadio)
                            .addComponent(pitchSideRadio))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderContrast, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderSaturation, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(sliderBrightness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(81, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

	private void saturationSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_saturationSliderChanged
		viewer.setSaturation(sliderSaturation.getValue());
	}//GEN-LAST:event_saturationSliderChanged

	private void brightnessSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_brightnessSliderChanged
		viewer.setBrightness(sliderBrightness.getValue());
	}//GEN-LAST:event_brightnessSliderChanged

	private void contrastSliderChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_contrastSliderChanged
		viewer.setContrast(sliderContrast.getValue());
	}//GEN-LAST:event_contrastSliderChanged

	private void barrelCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_barrelCheckboxActionPerformed
		ImageProcessor.useBarrelDistortion=barrelCheckbox.isSelected();
	}//GEN-LAST:event_barrelCheckboxActionPerformed

	private void debugSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_debugSliderStateChanged
		ImageProcessor.DEBUG_LEVEL = debugSlider.getValue();
	}//GEN-LAST:event_debugSliderStateChanged

	private void blueThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blueThreshSliderStateChanged
		ImageProcessor.blueThreshold = blueThreshSlider.getValue()/100.0;
	}//GEN-LAST:event_blueThreshSliderStateChanged

	private void yellThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yellThreshSliderStateChanged
		ImageProcessor.yellThreshold = yellThreshSlider.getValue()/100.0;
	}//GEN-LAST:event_yellThreshSliderStateChanged

	private void ballSearchSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_ballSearchSliderStateChanged
		ImageProcessor.searchdistance = ballSearchSlider.getValue();
	}//GEN-LAST:event_ballSearchSliderStateChanged

	private void modeSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_modeSliderStateChanged
		int v = modeSlider.getValue();
		if (v == 1) {
			ImageProcessor.method = 2;
		} else {
			ImageProcessor.method = 1;
			ImageProcessor.mode = v;
		}
	}//GEN-LAST:event_modeSliderStateChanged

	private void pitchMainRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pitchMainRadioActionPerformed
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
	}//GEN-LAST:event_pitchMainRadioActionPerformed

	private void pitchSideRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pitchSideRadioActionPerformed
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
	}//GEN-LAST:event_pitchSideRadioActionPerformed

	private void refChangerMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refChangerMouseEntered
		evt.getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	}//GEN-LAST:event_refChangerMouseEntered

	private void refChangerMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refChangerMouseExited
		evt.getComponent().setCursor(Cursor.getDefaultCursor());
	}//GEN-LAST:event_refChangerMouseExited

	private void blueRefChangerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_blueRefChangerMouseClicked
        if(refColorPointer==null){
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            blueRefChanger.setBorder(new BevelBorder(BevelBorder.LOWERED));
            refColorPointer=ImageProcessor.blueRef;
        }
        else{
            onScreenImage.setCursor(Cursor.getDefaultCursor());
            blueRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
            refColorPointer=null;
        }
	}//GEN-LAST:event_blueRefChangerMouseClicked

	private void yellowRefChangerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_yellowRefChangerMouseClicked
        if(refColorPointer==null){
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            yellowRefChanger.setBorder(new BevelBorder(BevelBorder.LOWERED));
            refColorPointer=ImageProcessor.yellRef;
        }
        else{
            onScreenImage.setCursor(Cursor.getDefaultCursor());
            yellowRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
            refColorPointer=null;
        }
	}//GEN-LAST:event_yellowRefChangerMouseClicked

	private void redRefChangerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_redRefChangerMouseClicked
        if(refColorPointer==null){
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            redRefChanger.setBorder(new BevelBorder(BevelBorder.LOWERED));
            refColorPointer=ImageProcessor.redRef;
        }
        else{
            onScreenImage.setCursor(Cursor.getDefaultCursor());
            redRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
            refColorPointer=null;
        }
	}//GEN-LAST:event_redRefChangerMouseClicked

	private void onScreenImageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onScreenImageMouseClicked
		if(refColorPointer==null)
			return;
        if(evt.getButton()==MouseEvent.BUTTON1)
            viewer.setNewReference(refColorPointer, evt.getX(), evt.getY());
        blueRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
        yellowRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
        redRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
        onScreenImage.setCursor(Cursor.getDefaultCursor());
        refColorPointer=null;
	}//GEN-LAST:event_onScreenImageMouseClicked

    private void blueRefThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blueRefThreshSliderStateChanged
        ImageProcessor.blueRefThresh=blueRefThreshSlider.getValue()/100.0;
    }//GEN-LAST:event_blueRefThreshSliderStateChanged

    private void yellowRefThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yellowRefThreshSliderStateChanged
        ImageProcessor.yellRefThresh=yellowRefThreshSlider.getValue()/100.0;
    }//GEN-LAST:event_yellowRefThreshSliderStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel ballPanel;
    private javax.swing.JSlider ballSearchSlider;
    private javax.swing.JCheckBox barrelCheckbox;
    private javax.swing.JPanel bluePanel;
    private javax.swing.JPanel blueRefChanger;
    private javax.swing.JSlider blueRefThreshSlider;
    private javax.swing.JSlider blueThreshSlider;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.JSlider debugSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelDebug;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JSlider modeSlider;
    private javax.swing.JLabel onScreenImage;
    private javax.swing.JRadioButton pitchMainRadio;
    private javax.swing.JRadioButton pitchSideRadio;
    private javax.swing.JPanel redRefChanger;
    private javax.swing.JSlider sliderBrightness;
    private javax.swing.JSlider sliderContrast;
    private javax.swing.JSlider sliderSaturation;
    private javax.swing.JSlider yellThreshSlider;
    private javax.swing.JPanel yellowPanel;
    private javax.swing.JPanel yellowRefChanger;
    private javax.swing.JSlider yellowRefThreshSlider;
    // End of variables declaration//GEN-END:variables

	public void setImage(BufferedImage image) {
		if (image != null)
			onScreenImage.getGraphics().drawImage(image, 0, 0,
					image.getWidth(), image.getHeight(), null);
	}

	public void setRefChangers(){
			blueRefChanger.setBackground(blueRefColor);
			yellowRefChanger.setBackground(yellowRefColor);
			redRefChanger.setBackground(redRefColor);
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

	/**
	 * @return the viewer
	 */
	public Viewer getViewer() {
		return viewer;
	}

	/**
	 * @param viewer the viewer to set
	 */
	public void setViewer(Viewer viewer) {
		this.viewer = viewer;
	}
}
