package computer.vision;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.border.BevelBorder;
import javax.swing.filechooser.FileFilter;

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
    public Color grnRefColor=new Color(ImageProcessor.grnRef[0],ImageProcessor.grnRef[1],ImageProcessor.grnRef[2]);
	private int[] refColorPointer=null;
    private boolean moveLimits=false;
    private boolean north=false,east=false,south=false,west=false;
    private boolean northwall=false,eastwall=false,southwall=false,westwall=false;
    private static final FileFilter visionSaveFileFilter=new FileFilter() {

            @Override
            public boolean accept(File file) {
                return  file.isDirectory() || file.getName().endsWith(".sdp1212vision");
            }

            @Override
            public String getDescription() {
                return "Stored vision setup file";
            }
        };

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
        noiseCheckbox = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();
        loadButton = new javax.swing.JButton();
        platePanel = new javax.swing.JPanel();
        grnRefChanger = new javax.swing.JPanel();
        grnThreshSlider = new javax.swing.JSlider();

        buttonGroup2.add(pitchMainRadio);
        buttonGroup2.add(pitchSideRadio);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setName("Vision"); // NOI18N

        onScreenImage.setText("Image goes here");
        onScreenImage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                onScreenImageMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                onScreenImageMouseReleased(evt);
            }
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                onScreenImageMouseClicked(evt);
            }
        });
        onScreenImage.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseMoved(java.awt.event.MouseEvent evt) {
                onScreenImageMouseMoved(evt);
            }
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                onScreenImageMouseDragged(evt);
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

        sliderContrast.setMaximum(127);
        sliderContrast.setValue(viewer.CONTRAST);
        sliderContrast.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                contrastSliderChanged(evt);
            }
        });

        sliderSaturation.setMaximum(127);
        sliderSaturation.setValue(viewer.SATURATION);
        sliderSaturation.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                saturationSliderChanged(evt);
            }
        });

        sliderBrightness.setMaximum(255);
        sliderBrightness.setValue(viewer.BRIGHTNESS);
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
                    .addComponent(blueRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
                    .addComponent(blueThreshSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)))
        );
        bluePanelLayout.setVerticalGroup(
            bluePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(blueRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(bluePanelLayout.createSequentialGroup()
                .addComponent(blueThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(blueRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
                    .addComponent(yellowRefThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE)
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
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refChangerMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refChangerMouseEntered(evt);
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
                .addComponent(ballSearchSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
        );
        ballPanelLayout.setVerticalGroup(
            ballPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(redRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(ballSearchSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        noiseCheckbox.setText("Enable Noise Reduction");
        noiseCheckbox.setToolTipText("Gaussian blur");
        noiseCheckbox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noiseCheckboxActionPerformed(evt);
            }
        });

        saveButton.setText("Save");
        saveButton.setToolTipText("Store settings to a file of you choosing!");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        loadButton.setText("Load");
        loadButton.setToolTipText("Load settings from a file of your choosing!");
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadButtonActionPerformed(evt);
            }
        });

        platePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(), "Plate Detection"));

        grnRefChanger.setBackground(grnRefColor);
        grnRefChanger.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        grnRefChanger.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                grnRefChangerMouseClicked(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                refChangerMouseExited(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                refChangerMouseEntered(evt);
            }
        });

        javax.swing.GroupLayout grnRefChangerLayout = new javax.swing.GroupLayout(grnRefChanger);
        grnRefChanger.setLayout(grnRefChangerLayout);
        grnRefChangerLayout.setHorizontalGroup(
            grnRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 20, Short.MAX_VALUE)
        );
        grnRefChangerLayout.setVerticalGroup(
            grnRefChangerLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 12, Short.MAX_VALUE)
        );

        grnThreshSlider.setMaximum(17500);
        grnThreshSlider.setToolTipText("Yellow Detection Threshold");
        grnThreshSlider.setValue((int)(ImageProcessor.grnThreshold*100));
        grnThreshSlider.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                grnThreshSliderStateChanged(evt);
            }
        });

        javax.swing.GroupLayout platePanelLayout = new javax.swing.GroupLayout(platePanel);
        platePanel.setLayout(platePanelLayout);
        platePanelLayout.setHorizontalGroup(
            platePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(platePanelLayout.createSequentialGroup()
                .addComponent(grnRefChanger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(grnThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, 174, Short.MAX_VALUE))
        );
        platePanelLayout.setVerticalGroup(
            platePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(grnRefChanger, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(grnThreshSlider, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 639, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(yellowPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(debugSlider, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(labelDebug, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(bluePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(ballPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(platePanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(saveButton, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(loadButton, javax.swing.GroupLayout.DEFAULT_SIZE, 105, Short.MAX_VALUE))
                    .addComponent(modeLabel, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(modeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(barrelCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(noiseCheckbox, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(sliderSaturation, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(pitchMainRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 109, Short.MAX_VALUE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pitchSideRadio, javax.swing.GroupLayout.DEFAULT_SIZE, 107, Short.MAX_VALUE))
                    .addComponent(sliderContrast, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(jLabel3, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 216, Short.MAX_VALUE)
                    .addComponent(sliderBrightness, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(onScreenImage, javax.swing.GroupLayout.PREFERRED_SIZE, 479, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
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
                        .addComponent(platePanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(modeSlider, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(barrelCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(noiseCheckbox)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
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
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sliderBrightness, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(loadButton)
                            .addComponent(saveButton))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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
//		blueThreshSlider.setValue(8750);
//		ImageProcessor.blueThreshold = 87.5;
//        blueRefThreshSlider.setValue(1250);
//        ImageProcessor.blueRefThresh=12.5;
//		yellThreshSlider.setValue(8750);
//		ImageProcessor.yellThreshold = 87.5;
//        yellowRefThreshSlider.setValue(1250);
//        ImageProcessor.yellRefThresh=12.5;
//		ballSearchSlider.setValue(700);
//		ImageProcessor.searchdistance = 700;
//		modeSlider.setValue(5);
//		ImageProcessor.mode = 5;
		ImageProcessor.xlowerlimit = 0;
		ImageProcessor.xupperlimit = 630;
		ImageProcessor.ylowerlimit = 85;
		ImageProcessor.yupperlimit = 410;
	}//GEN-LAST:event_pitchMainRadioActionPerformed

	private void pitchSideRadioActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pitchSideRadioActionPerformed
//		blueThreshSlider.setValue(8750);
//		ImageProcessor.blueThreshold = 87.5;
//        blueRefThreshSlider.setValue(1250);
//        ImageProcessor.blueRefThresh=12.5;
//		yellThreshSlider.setValue(8750);
//		ImageProcessor.yellThreshold = 87.5;
//        yellowRefThreshSlider.setValue(1250);
//        ImageProcessor.yellRefThresh=12.5;
//		ballSearchSlider.setValue(700);
//		ImageProcessor.searchdistance = 700;
//		modeSlider.setValue(5);
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
        grnRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
        onScreenImage.setCursor(Cursor.getDefaultCursor());
        refColorPointer=null;
	}//GEN-LAST:event_onScreenImageMouseClicked

    private void blueRefThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_blueRefThreshSliderStateChanged
        ImageProcessor.blueRefThresh=blueRefThreshSlider.getValue()/100.0;
    }//GEN-LAST:event_blueRefThreshSliderStateChanged

    private void yellowRefThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_yellowRefThreshSliderStateChanged
        ImageProcessor.yellRefThresh=yellowRefThreshSlider.getValue()/100.0;
    }//GEN-LAST:event_yellowRefThreshSliderStateChanged

    private void noiseCheckboxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noiseCheckboxActionPerformed
        ImageProcessor.ENABLE_NOISE_REDUCTION_FILTER=noiseCheckbox.isSelected();
    }//GEN-LAST:event_noiseCheckboxActionPerformed

    private void onScreenImageMouseMoved(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onScreenImageMouseMoved
        if(refColorPointer!=null)
            return;
        
        int x=evt.getX(),y=evt.getY();
        north=ImageProcessor.ylowerlimit-5<=y && y<=ImageProcessor.ylowerlimit+5;
        east=ImageProcessor.xupperlimit-5<=x && x<=ImageProcessor.xupperlimit+5;
        south=ImageProcessor.yupperlimit-5<=y && y<=ImageProcessor.yupperlimit+5;
        west=ImageProcessor.xlowerlimit-5<=x && x<=ImageProcessor.xlowerlimit+5;
        
        
        northwall=ImageProcessor.nePos.y-5<=y && y<=ImageProcessor.nwPos.y+5 && ImageProcessor.DEBUG_LEVEL>0;
        eastwall=ImageProcessor.nePos.x-5<=x && x<=ImageProcessor.sePos.x+5 && ImageProcessor.DEBUG_LEVEL>0;
        southwall=ImageProcessor.sePos.y-5<=y && y<=ImageProcessor.swPos.y+5 && ImageProcessor.DEBUG_LEVEL>0;
        westwall=ImageProcessor.swPos.x-5<=x && x<=ImageProcessor.nwPos.x+5 && ImageProcessor.DEBUG_LEVEL>0;

        if((north && east) || (northwall && eastwall))
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.NE_RESIZE_CURSOR));
        else if((north && west) || (northwall && westwall))
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.NW_RESIZE_CURSOR));
        else if((south && west) || (southwall && westwall))
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
        else if((south && east) || (southwall && eastwall))
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
        else if(north || northwall)
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.N_RESIZE_CURSOR));
        else if(east || eastwall)
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
        else if(south || southwall)
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
        else if(west || westwall)
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
        else
            onScreenImage.setCursor(Cursor.getDefaultCursor());
    }//GEN-LAST:event_onScreenImageMouseMoved

    private void onScreenImageMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onScreenImageMousePressed
//        if(refColorPointer!=null)
//            return;
//        moveLimits=true;
    }//GEN-LAST:event_onScreenImageMousePressed

    private void onScreenImageMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onScreenImageMouseReleased
//        moveLimits=false;
    }//GEN-LAST:event_onScreenImageMouseReleased

    private void onScreenImageMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_onScreenImageMouseDragged
        if(refColorPointer==null){
            if(north)
                ImageProcessor.ylowerlimit=Math.max(0,evt.getY());
            if(east)
                ImageProcessor.xupperlimit=Math.min(640,evt.getX());
            if(south)
                ImageProcessor.yupperlimit=Math.min(480,evt.getY());
            if(west)
                ImageProcessor.xlowerlimit=Math.max(0,evt.getX());
            if(ImageProcessor.DEBUG_LEVEL>0){
                if(northwall){
                    ImageProcessor.nwPos.y=Math.max(0,evt.getY());
                    ImageProcessor.nePos.y=Math.max(0,evt.getY());
                }
                if(eastwall){
                    ImageProcessor.nePos.x=Math.min(640, evt.getX());
                    ImageProcessor.sePos.x=Math.min(640, evt.getX());
                }
                if(southwall){
                    ImageProcessor.swPos.y=Math.min(480,evt.getY());
                    ImageProcessor.sePos.y=Math.min(480,evt.getY());
                }
                if(westwall){
                    ImageProcessor.swPos.x=Math.max(0, evt.getX());
                    ImageProcessor.nwPos.x=Math.max(0, evt.getX());
                }
            }
        }
    }//GEN-LAST:event_onScreenImageMouseDragged

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        JFileChooser fc=new JFileChooser();
        fc.setDialogTitle("Save vision settings");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(visionSaveFileFilter);
        if(fc.showSaveDialog(this)==JFileChooser.APPROVE_OPTION){
            File file=fc.getSelectedFile();
            FileWriter out;
            try {
                out = new FileWriter(file);
                out.write(Integer.toString(ImageProcessor.DEBUG_LEVEL)+'\n');
                out.write(Double.toString(ImageProcessor.blueThreshold)+'\n');
                out.write(Double.toString(ImageProcessor.blueRefThresh)+'\n');
                out.write(Double.toString(ImageProcessor.yellThreshold)+'\n');
                out.write(Double.toString(ImageProcessor.yellRefThresh)+'\n');
                out.write(Integer.toString(ImageProcessor.searchdistance)+'\n');
                out.write(Integer.toString(ImageProcessor.mode)+'\n');
                out.write(Boolean.toString(ImageProcessor.useBarrelDistortion)+'\n');
                out.write(Boolean.toString(ImageProcessor.ENABLE_NOISE_REDUCTION_FILTER)+'\n');
                out.write(Boolean.toString(pitchMainRadio.isSelected())+'\n');
                out.write(Integer.toString(Viewer.CONTRAST)+'\n');
                out.write(Integer.toString(Viewer.SATURATION)+'\n');
                out.write(Integer.toString(Viewer.BRIGHTNESS)+'\n');
                out.write(Integer.toString(ImageProcessor.xlowerlimit)+'\n');
                out.write(Integer.toString(ImageProcessor.xupperlimit)+'\n');
                out.write(Integer.toString(ImageProcessor.ylowerlimit)+'\n');
                out.write(Integer.toString(ImageProcessor.yupperlimit)+'\n');
                out.write(Integer.toString(ImageProcessor.nwPos.x)+'\n');
                out.write(Integer.toString(ImageProcessor.nePos.y)+'\n');
                out.write(Integer.toString(ImageProcessor.sePos.x)+'\n');
                out.write(Integer.toString(ImageProcessor.swPos.y)+'\n');
                out.write(Double.toString(ImageProcessor.grnThreshold)+'\n');
                out.close();
                if(!file.getName().endsWith(".sdp1212vision")){
                    file.renameTo(new File(file.getParentFile(), file.getName()+".sdp1212vision"));
                }
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }//GEN-LAST:event_saveButtonActionPerformed

    private void loadButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadButtonActionPerformed
        JFileChooser fc=new JFileChooser();
        fc.setDialogTitle("Load stored vision settings");
        fc.setAcceptAllFileFilterUsed(false);
        fc.addChoosableFileFilter(visionSaveFileFilter);
        if(fc.showOpenDialog(this)==JFileChooser.APPROVE_OPTION){
            File file=fc.getSelectedFile();
            try {
                Scanner in=new Scanner(file);
                debugSlider.setValue(ImageProcessor.DEBUG_LEVEL=in.nextInt());
                blueThreshSlider.setValue((int)((ImageProcessor.blueThreshold=in.nextDouble())*100));
                blueRefThreshSlider.setValue((int)((ImageProcessor.blueRefThresh=in.nextDouble())*100));
                yellThreshSlider.setValue((int)((ImageProcessor.yellThreshold=in.nextDouble())*100));
                yellowRefThreshSlider.setValue((int)((ImageProcessor.yellRefThresh=in.nextDouble())*100));
                ballSearchSlider.setValue(ImageProcessor.searchdistance=in.nextInt());
                modeSlider.setValue(ImageProcessor.mode=in.nextInt());
                barrelCheckbox.setSelected(ImageProcessor.useBarrelDistortion=in.nextBoolean());
                noiseCheckbox.setSelected(ImageProcessor.ENABLE_NOISE_REDUCTION_FILTER=in.nextBoolean());
                pitchMainRadio.setSelected(in.nextBoolean());
                pitchSideRadio.setSelected(!pitchMainRadio.isSelected());
                sliderContrast.setValue(in.nextInt());
                viewer.setContrast(sliderContrast.getValue());
                sliderSaturation.setValue(in.nextInt());
                viewer.setSaturation(sliderSaturation.getValue());
                sliderBrightness.setValue(in.nextInt());
                viewer.setBrightness(sliderBrightness.getValue());
                ImageProcessor.xlowerlimit=in.nextInt();
                ImageProcessor.xupperlimit=in.nextInt();
                ImageProcessor.ylowerlimit=in.nextInt();
                ImageProcessor.yupperlimit=in.nextInt();
                ImageProcessor.nwPos.x=ImageProcessor.swPos.x=in.nextInt();
                ImageProcessor.nwPos.y=ImageProcessor.nePos.y=in.nextInt();
                ImageProcessor.nePos.x=ImageProcessor.sePos.x=in.nextInt();
                ImageProcessor.swPos.y=ImageProcessor.sePos.y=in.nextInt();
                grnThreshSlider.setValue((int)((ImageProcessor.grnThreshold=in.nextDouble())*100));
                in.close();
            } catch (IOException ex) {
                ex.printStackTrace(System.err);
            }
        }
    }//GEN-LAST:event_loadButtonActionPerformed

    private void grnRefChangerMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_grnRefChangerMouseClicked
        if(refColorPointer==null){
            onScreenImage.setCursor(Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR));
            grnRefChanger.setBorder(new BevelBorder(BevelBorder.LOWERED));
            refColorPointer=ImageProcessor.grnRef;
        }
        else{
            onScreenImage.setCursor(Cursor.getDefaultCursor());
            grnRefChanger.setBorder(new BevelBorder(BevelBorder.RAISED));
            refColorPointer=null;
        }
    }//GEN-LAST:event_grnRefChangerMouseClicked

    private void grnThreshSliderStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_grnThreshSliderStateChanged
        ImageProcessor.grnThreshold = grnThreshSlider.getValue()/100.0;
    }//GEN-LAST:event_grnThreshSliderStateChanged

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
    private javax.swing.JPanel grnRefChanger;
    private javax.swing.JSlider grnThreshSlider;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel labelDebug;
    private javax.swing.JButton loadButton;
    private javax.swing.JLabel modeLabel;
    private javax.swing.JSlider modeSlider;
    private javax.swing.JCheckBox noiseCheckbox;
    private javax.swing.JLabel onScreenImage;
    private javax.swing.JRadioButton pitchMainRadio;
    private javax.swing.JRadioButton pitchSideRadio;
    private javax.swing.JPanel platePanel;
    private javax.swing.JPanel redRefChanger;
    private javax.swing.JButton saveButton;
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
            grnRefChanger.setBackground(grnRefColor);
	}

	public javax.swing.JLabel getImage() {
		return onScreenImage;
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
