package computer.vision;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.Collections;

import javax.swing.JFrame;
import javax.swing.JLabel;

import au.edu.jcu.v4l4j.CaptureCallback;
import au.edu.jcu.v4l4j.DeviceInfo;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.ImageFormat;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.ImageFormatException;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import computer.simulator.VisionInterface;

/**
 * The main class for showing the video feed and processing the video
 * data. Identifies ball and robot locations, and robot orientations.
 */
public class Vision extends WindowAdapter {

    private VideoDevice videoDev;
    private JLabel label;
    private JFrame windowFrame;
    private FrameGrabber frameGrabber;
    private Thread captureThread;
    private boolean stop;
    private int width, height;
    private WorldState worldState;
    private ThresholdsState thresholdsState;
    private PitchConstants pitchConstants;
    private int errorChecker;
    //TODO Add an errorChecker value that increments every time we disregard a value for being bad. If this errorChecker goes to 5 (aka 5 errors in a row), then we set the value to that new angle and reset the ErrorChecker
    //private int[] xDistortion;
    //private int[] yDistortion;

    /**
     * Default constructor.
     *
     * @param videoDevice           The video device file to capture from.
     * @param width                 The desired capture width.
     * @param height                The desired capture height.
     * @param videoStandard         The capture standard.
     * @param channel               The capture channel.
     * @param compressionQuality    The JPEG compression quality.
     * @param worldState
     * @param thresholdsState
     * @param pitchConstants
     *
     * @throws V4L4JException   If any parameter if invalid.
     */
    public Vision(String videoDevice, int width, int height, int channel, int videoStandard,
            int compressionQuality, WorldState worldState, ThresholdsState thresholdsState,
            PitchConstants pitchConstants) throws V4L4JException {

        /* Set the state fields. */
        this.worldState = worldState;
        this.thresholdsState = thresholdsState;
        this.pitchConstants = pitchConstants;

        /* Initialise the GUI that displays the video feed. */
        initFrameGrabber(videoDevice, width, height, channel, videoStandard, compressionQuality);
        initGUI();
    }

    /**
     * Initialises a FrameGrabber object with the given parameters.
     *
     * @param videoDevice           The video device file to capture from.
     * @param inWidth               The desired capture width.
     * @param inHeight              The desired capture height.
     * @param channel               The capture channel.
     * @param videoStandard         The capture standard.
     * @param compressionQuality    The JPEG compression quality.
     *
     * @throws V4L4JException   If any parameter is invalid.
     */
    private void initFrameGrabber(String videoDevice, int inWidth, int inHeight, int channel,
            int videoStandard, int compressionQuality) throws V4L4JException {
        videoDev = new VideoDevice(videoDevice);

        DeviceInfo deviceInfo = videoDev.getDeviceInfo();

        if (deviceInfo.getFormatList().getNativeFormats().isEmpty()) {
            throw new ImageFormatException("Unable to detect any native formats for the device!");
        }
        ImageFormat imageFormat = deviceInfo.getFormatList().getNativeFormat(0);

        frameGrabber = videoDev.getJPEGFrameGrabber(inWidth, inHeight, channel, videoStandard,
                compressionQuality, imageFormat);

        frameGrabber.setCaptureCallback(new CaptureCallback() {

            public void exceptionReceived(V4L4JException e) {
                System.err.println("Unable to capture frame:");
                e.printStackTrace();
            }
            int imCount = 0;

            public void nextFrame(VideoFrame frame) {
                long before = System.currentTimeMillis();
                BufferedImage frameImage = frame.getBufferedImage();


                imCount += 1;
                if (imCount == 10) {
                    try {
                        File sampleImage = new File("TestImage.jpeg");
                        ImageIO.write(frameImage, "jpeg", sampleImage);
                    } catch (Exception e) {
                        //Try again in 10 frames
                        imCount = 0;
                    }
                }

                //ShadowProcessing  s = new ShadowProcessing(pitchConstants);
                //frameImage =  s.sideNormalise(frameImage);
                //frameImage = s.fullNormalise(frameImage);

                frame.recycle();

                BufferedImage backgroundImage = null;
                try {
                    backgroundImage = ImageIO.read(new File("TestImage.jpeg"));
                } catch (IOException e) {
                    backgroundImage = frameImage;
                }
                // Switch between angle methods using true or false here.
                // false is default.
                processAndUpdateImage(frameImage, backgroundImage, before, false);
            }
        });

        frameGrabber.startCapture();

        width = frameGrabber.getWidth();
        height = frameGrabber.getHeight();
    }

    /**
     * Creates the graphical interface components and initialises them
     */
    private void initGUI() {
        windowFrame = new JFrame("Vision Window");
        label = new JLabel();
        windowFrame.getContentPane().add(label);
        windowFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        windowFrame.addWindowListener(this);
        windowFrame.setVisible(true);
        windowFrame.setSize(width, height);
    }

    /**
     * Catches the window closing event, so that we can free up resources
     * before exiting.
     *
     * @param e         The window closing event.
     */
    public void windowClosing(WindowEvent e) {
        /* Dispose of the various swing and v4l4j components. */
        frameGrabber.stopCapture();
        videoDev.releaseFrameGrabber();

        windowFrame.dispose();

        System.exit(0);
    }

    /**
     * Processes an input image, extracting the ball and robot positions and robot
     * orientations from it, and then displays the image (with some additional graphics
     * layered on top for debugging) in the vision frame.
     *
     * @param image     The image to process and then show.
     */
    public void processAndUpdateImage(BufferedImage image, BufferedImage background, long before, boolean runAlternate) {

        /* NOTE!
         * The boolean "runAlternate" is used to differentiate between the normal method
         * for calculating the angle, and the vector method of calculating the angle.
         * True represents the vector method, false represents the normal method.
         */

        int ballX = 0;
        int ballY = 0;
        int numBallPos = 0;

        int blueX = 0;
        int blueY = 0;
        int numBluePos = 0;

        int yellowX = 0;
        int yellowY = 0;
        int numYellowPos = 0;

        int greenX = 0;
        int greenY = 0;
        int numGreenPos = 0;

        int greenX2 = 0;
        int greenY2 = 0;
        int numGreenPos2 = 0;

        ArrayList<Integer> ballXPoints = new ArrayList<Integer>();
        ArrayList<Integer> ballYPoints = new ArrayList<Integer>();
        ArrayList<Integer> blueXPoints = new ArrayList<Integer>();
        ArrayList<Integer> blueYPoints = new ArrayList<Integer>();
        ArrayList<Integer> yellowXPoints = new ArrayList<Integer>();
        ArrayList<Integer> yellowYPoints = new ArrayList<Integer>();
        ArrayList<Integer> greenXPoints = new ArrayList<Integer>();
        ArrayList<Integer> greenYPoints = new ArrayList<Integer>();

        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        int leftBuffer = pitchConstants.leftBuffer;
        int rightBuffer = pitchConstants.rightBuffer;

        /* For every pixel within the pitch, test to see if it belongs to the ball,
         * the yellow T, the blue T, either green plate or a grey circle. */
        for (int row = topBuffer; row < image.getHeight() - bottomBuffer; row++) {
            for (int column = leftBuffer; column < image.getWidth() - rightBuffer; column++) {

                /* The RGB colours and hsv values for the current pixel. */
                /* And the RBG values for the background image*/
                Color c = new Color(image.getRGB(column, row));
                Color c2 = new Color(background.getRGB(column, row));

                /* Subtract the RGB values of the current image from the background image  */
                int red = Math.abs(c.getRed() - c2.getRed());
                int blue = Math.abs(c.getBlue() - c2.getBlue());
                int green = Math.abs(c.getGreen() - c2.getGreen());

                float hsbvals[] = new float[3];

                int diffRange = 25;

                //if (red > diffRange || blue > diffRange || green > diffRange) {
                Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbvals);
                /* Debug graphics for the grey circles and green plates.
                 * TODO: Move these into the actual detection. */
                if (thresholdsState.isGrey_debug() && isGrey(c, hsbvals)) {
                    image.setRGB(column, row, 0xFFFF0099);
                }

                //if (thresholdsState.isGreen_debug() && isGreen(c, hsbvals)) {
                if (isGreen(c, hsbvals)) {
                    if (thresholdsState.isGreen_debug()) {
                        image.setRGB(column, row, 0xFFFF0099);
                    }
                    greenX += column;
                    greenY += row;
                    numGreenPos++;
                    greenXPoints.add(column);
                    greenYPoints.add(row);
                }

                /* Is this pixel part of the Blue T? */
                if (isBlue(c, hsbvals)) {

                    blueX += column;
                    blueY += row;
                    numBluePos++;

                    blueXPoints.add(column);
                    blueYPoints.add(row);

                    /* If we're in the "Blue" tab, we show what pixels we're looking at,
                     * for debugging and to help with threshold setting. */
                    if (thresholdsState.isBlue_debug()) {
                        image.setRGB(column, row, 0xFFFF0099);
                    }

                }

                /* Is this pixel part of the Yellow T? */
                if (isYellow(c, hsbvals)) {

                    yellowX += column;
                    yellowY += row;
                    numYellowPos++;

                    yellowXPoints.add(column);
                    yellowYPoints.add(row);

                    /* If we're in the "Yellow" tab, we show what pixels we're looking at,
                     * for debugging and to help with threshold setting. */
                    if (thresholdsState.isYellow_debug()) {
                        image.setRGB(column, row, 0xFFFF0099);
                    }
                }

                /* Is this pixel part of the Ball? */
                if (isBall(c, hsbvals)) {

                    ballX += column;
                    ballY += row;
                    numBallPos++;

                    ballXPoints.add(column);
                    ballYPoints.add(row);

                    /* If we're in the "Ball" tab, we show what pixels we're looking at,
                     * for debugging and to help with threshold setting. */
                    if (thresholdsState.isBall_debug()) {
                        image.setRGB(column, row, 0xFFFF0000);
                    }
                }
                //} else {
                //Color.RGBtoHSB(0, 0, 0, hsbvals);
                // Sets the non-object pixels to black using background image.
                //image.setRGB(column, row, 0x00000000);
                // }




            }
        }

         BlobDetection bd = new BlobDetection();
         BlobObject bo = bd.getCoordinatesOfBlob(blueXPoints, blueYPoints);
         ArrayList<Integer> blueFilteredX = bo.xpoints;
         ArrayList<Integer> blueFilteredY = bo.ypoints;

        /* Position objects to hold the centre point of the ball and both robots. */
        Position ball;
        Position blue;
        Position yellow;
        Position firstRobot;
        Position secondRobot;
        Position green;

        //These are new arraylists to keep track of the values of the Green plates
        ArrayList<Integer> firstRobotXPoints = new ArrayList<Integer>();
        ArrayList<Integer> firstRobotYPoints = new ArrayList<Integer>();

        ArrayList<Integer> secondRobotXPoints = new ArrayList<Integer>();
        ArrayList<Integer> secondRobotYPoints = new ArrayList<Integer>();

        //We have to iterate through the same green points and see which robot plate they belong to
        int greenfirstx = 0;
        int greenfirsty = 0;
        int numgreenFirst = 0;
        int greensecondx = 0;
        int greensecondy = 0;
        int numgreenSecond = 0;


        /*This should find the center of the green object */

        if (numGreenPos > 0) {

            if (worldState.getDetectTwo()) {
                //Find the max and min points along the Greens, assuming two different plates
                Integer max = Collections.max(greenXPoints);
                Integer min = Collections.min(greenXPoints);
                //  Find a midpoint to perform a division along the X Axis
                int average = (((int) max + (int) min) / 2);
                //System.out.println("Max: " + max + " Min: " + min+ " Avg: "+average);

                /*
                 * If the values have very similar X points (layered atop one another), this will not work. 
                 * So instead, if the values are too similar, then we will use the Y
                 */
                if (average + 35 > max && average - 35 < min) {
                    Integer maxY = Collections.max(greenYPoints);
                    Integer minY = Collections.min(greenYPoints);

                    average = (((int) maxY + (int) minY) / 2);
                    //System.out.println("MaxY: " + maxY + " MinY: " + minY+ " AvgY: "+average);

                    //Using the Y instead

                    for (int i = 0; i < greenXPoints.size(); i++) {
                        //Go through every point. If less than the average, it belongs to one robot. Else, the other
                        if (greenYPoints.get(i) < average) {
                            greenfirstx += greenXPoints.get(i);
                            greenfirsty += greenYPoints.get(i);
                            numgreenFirst++;
                            firstRobotXPoints.add(greenXPoints.get(i));
                            firstRobotYPoints.add(greenYPoints.get(i));
                        } else {
                            greensecondx += greenXPoints.get(i);
                            greensecondy += greenYPoints.get(i);
                            numgreenSecond++;
                            secondRobotXPoints.add(greenXPoints.get(i));
                            secondRobotYPoints.add(greenYPoints.get(i));
                        }

                    }

                } else {
                    for (int i = 0; i < greenXPoints.size(); i++) {
                        //Go through every point. If less than the average, it belongs to one robot. Else, the other
                        if (greenXPoints.get(i) < average) {
                            greenfirstx += greenXPoints.get(i);
                            greenfirsty += greenYPoints.get(i);
                            numgreenFirst++;
                            firstRobotXPoints.add(greenXPoints.get(i));
                            firstRobotYPoints.add(greenYPoints.get(i));
                        } else {
                            greensecondx += greenXPoints.get(i);
                            greensecondy += greenYPoints.get(i);
                            numgreenSecond++;
                            secondRobotXPoints.add(greenXPoints.get(i));
                            secondRobotYPoints.add(greenYPoints.get(i));
                        }

                    }
                }
            } else {
                greenX /= numGreenPos;
                greenY /= numGreenPos;

                green = new Position(greenX, greenY);
                green.fixValues(worldState.getGreenSoloX(), worldState.getGreenSoloY());
                green.filterPoints(greenXPoints, greenYPoints);

            }

        }


        if (numgreenFirst > 0) {

            //Find the middle position for both robots now
            //This is first robot

            greenfirstx /= numgreenFirst;
            greenfirsty /= numgreenFirst;

            firstRobot = new Position(greenfirstx, greenfirsty);
            firstRobot.fixValues(worldState.getGreenX(), worldState.getGreenY());
            firstRobot.filterPoints(firstRobotXPoints, firstRobotYPoints);



        } else {
            firstRobot = new Position(worldState.getGreenX(), worldState.getGreenY());
        }


        if (numgreenSecond > 0) {
            //This is second robot
            greensecondx /= numgreenSecond;
            greensecondy /= numgreenSecond;

            secondRobot = new Position(greensecondx, greensecondy);
            secondRobot.fixValues(worldState.getGreenX2(), worldState.getGreenY2());
            secondRobot.filterPoints(secondRobotXPoints, secondRobotYPoints);
        } else {
            secondRobot = new Position(worldState.getGreenX2(), worldState.getGreenY2());
        }



        /* If we have only found a few 'Ball' pixels, chances are that the ball 
         * has not actually been detected. */
        if (numBallPos > 5) {
            ballX /= numBallPos;
            ballY /= numBallPos;

            ball = new Position(ballX, ballY);
            ball.fixValues(worldState.getBallX(), worldState.getBallY());
            ball.filterPoints(ballXPoints, ballYPoints);
        } else {
            ball = new Position(worldState.getBallX(), worldState.getBallY());
        }

        /* If we have only found a few 'Blue' pixels, chances are that the 
         * blue robot has not actually been detected. */
        if (numBluePos > 0) {
            blueX /= numBluePos;
            blueY /= numBluePos;

            blue = new Position(blueX, blueY);
            blue.fixValues(worldState.getBlueX(), worldState.getBlueY());
            blue.filterPoints(blueXPoints, blueYPoints);
        } else {
            blue = new Position(worldState.getBlueX(), worldState.getBlueY());
        }


        /* If we have only found a few 'Yellow' pixels, chances are that the 
         * yellow robot has not actually been detected. */
        if (numYellowPos > 0) {
            yellowX /= numYellowPos;
            yellowY /= numYellowPos;

            yellow = new Position(yellowX, yellowY);
            yellow.fixValues(worldState.getYellowX(), worldState.getYellowY());
            yellow.filterPoints(yellowXPoints, yellowYPoints);
        } else {
            yellow = new Position(worldState.getYellowX(), worldState.getYellowY());
        }

        /* Attempt to find the blue robot's orientation. */

        //Debugging Attempt for Blob Detection
        
        
        double blueOrientation;
        double blueOrientation2;
        // double blueOrientation3;
        AngleHistory angleHistory = new AngleHistory();
        Preprocessing pp = new Preprocessing(pitchConstants, thresholdsState);
        try {
            if (runAlternate == false) {
                blueOrientation = getGeometricOrientation(blueXPoints, blueYPoints, blue.getX(), blue.getY(), image, true);
                // blueOrientation3 = getGeometricOrientation(blueFilteredX, blueFilteredY, blue.getX(), blue.getY(), image, false);
//                blueOrientation = findOrientation(blueXPoints, blueYPoints, blue.getX(), blue.getY(), image, true);
                worldState.addBlueAngle(blueOrientation);
                double testAngle = angleHistory.getMean(worldState.blueFiveAngles);
                //System.out.println("The mean of the last 5 blue angles was found as " + testAngle);
            } else {
                blueOrientation = getVectorAngle(image, blueXPoints, blueYPoints, blue.getX(), blue.getY());
            }

            double diff = Math.abs(blueOrientation - worldState.getBlueOrientation());
            if (diff > 0.1) {
                float angle = (float) Math.round(((blueOrientation / Math.PI) * 180) / 5) * 5;
                worldState.setBlueOrientation((float) (angle / 180 * Math.PI));
            }
        } catch (NoAngleException e) {
            worldState.setBlueOrientation(worldState.getBlueOrientation());
            //System.out.println("Blue robot: " + e.getMessage());
        }

        /* Attempt to find the yellow robot's orientation. */
        double yellowOrientation;
        try {
            if (true != false) {
                yellowOrientation = getGeometricOrientation(yellowXPoints, yellowYPoints, yellow.getX(), yellow.getY(), image, false);
                yellowOrientation = findOrientation(yellowXPoints, yellowYPoints, yellow.getX(), yellow.getY(), image, true);
                worldState.addYellowAngle(yellowOrientation);
                double testAngle = angleHistory.getMean(worldState.yellowFiveAngles);
                //System.out.println("The mean of the last 5 yellow angles was found as " + testAngle);
            } else {
                yellowOrientation = getVectorAngle(image, yellowXPoints, yellowYPoints, yellow.getX(), yellow.getY());
            }

            double diff = Math.abs(yellowOrientation - worldState.getYellowOrientation());
            if (yellowOrientation != 0 && diff > 0.1) {
                float angle = (float) Math.round(((yellowOrientation / Math.PI) * 180) / 5) * 5;
                worldState.setYellowOrientation((float) (angle / 180 * Math.PI));
            }
        } catch (NoAngleException e) {
            worldState.setYellowOrientation(worldState.getYellowOrientation());
            //System.out.println("Yellow robot: " + e.getMessage());
        }


        worldState.setBallX(ball.getX());
        worldState.setBallY(ball.getY());

        worldState.setBlueX(blue.getX());
        worldState.setBlueY(blue.getY());
        worldState.setYellowX(yellow.getX());
        worldState.setYellowY(yellow.getY());

        worldState.setGreenX(firstRobot.getX());
        worldState.setGreenY(firstRobot.getY());
        worldState.setGreenX2(secondRobot.getX());
        worldState.setGreenY2(secondRobot.getY());

        worldState.updateCounter();

        /* Draw the image onto the vision frame. */
        Graphics frameGraphics = label.getGraphics();
        Graphics imageGraphics = image.getGraphics();

        /* Only display these markers in non-debug mode. */
        if (!(thresholdsState.isBall_debug() || thresholdsState.isBlue_debug()
                || thresholdsState.isYellow_debug() || thresholdsState.isGreen_debug()
                || thresholdsState.isGrey_debug())) {
            imageGraphics.setColor(Color.red);
            imageGraphics.drawLine(0, ball.getY(), 640, ball.getY());
            imageGraphics.drawLine(ball.getX(), 0, ball.getX(), 480);
            imageGraphics.setColor(Color.green);

            Integer firstRobotMaxX = 0;
            Integer firstRobotMinX = 0;
            Integer firstRobotMaxY = 0;
            Integer firstRobotMinY = 0;
            Integer secondRobotMaxX = 0;
            Integer secondRobotMinX = 0;
            Integer secondRobotMaxY = 0;
            Integer secondRobotMinY = 0;
            Integer singleRobotMaxX = 0;
            Integer singleRobotMinX = 0;
            Integer singleRobotMaxY = 0;
            Integer singleRobotMinY = 0;

            if (worldState.getDetectTwo()) {

                try {
                    firstRobotMaxX = Collections.max(firstRobotXPoints);
                    firstRobotMinX = Collections.min(firstRobotXPoints);
                    firstRobotMaxY = Collections.max(firstRobotYPoints);
                    firstRobotMinY = Collections.min(firstRobotYPoints);

                } catch (Exception e) {
                    //No frame detected - square won't display
                }

                try {
                    secondRobotMaxX = Collections.max(secondRobotXPoints);
                    secondRobotMinX = Collections.min(secondRobotXPoints);
                    secondRobotMaxY = Collections.max(secondRobotYPoints);
                    secondRobotMinY = Collections.min(secondRobotYPoints);

                } catch (Exception e) {
                    //No frame detected - square won't display
                }
            } else {
                try {
                    singleRobotMaxX = Collections.max(greenXPoints);
                    singleRobotMaxY = Collections.max(greenYPoints);
                    singleRobotMinX = Collections.min(greenXPoints);
                    singleRobotMinY = Collections.min(greenYPoints);
                } catch (Exception e) {
                    //No frame detected - square won't display
                }
            }



            if (worldState.getBounding()) {
                if (worldState.getDetectTwo()) {
                    if (secondRobotMaxX != 0 && secondRobotMaxY != 0 && secondRobotMinX != 0 && secondRobotMinY != 0) {
                        imageGraphics.drawRect(secondRobotMinX, secondRobotMinY, secondRobotMaxX - secondRobotMinX, secondRobotMaxY - secondRobotMinY);
                    }

                    if (firstRobotMaxX != 0 && firstRobotMaxY != 0 && secondRobotMinX != 0 && secondRobotMinY != 0) {
                        imageGraphics.drawRect(firstRobotMinX, firstRobotMinY, firstRobotMaxX - firstRobotMinX, firstRobotMaxY - firstRobotMinY);
                    }
                } else {
                    if (singleRobotMaxX != 0 && singleRobotMaxY != 0 && singleRobotMinX != 0 && singleRobotMinY != 0) {
                        imageGraphics.drawRect(singleRobotMinX, singleRobotMinY, singleRobotMaxX - singleRobotMinX, singleRobotMaxY - singleRobotMinY);

                    }
                }

            }

        }

        /* Used to calculate the FPS. */
        long after = System.currentTimeMillis();

        /* Display the FPS that the vision system is running at. */
        float fps = (1.0f) / ((after - before) / 1000.0f);
        imageGraphics.setColor(Color.white);
        imageGraphics.drawString("FPS: " + fps, 15, 15);
        frameGraphics.drawImage(image, 0, 0, width, height, null);
    }

    /**
     * Determines if a pixel is part of the blue T, based on input RGB colours
     * and hsv values.
     *
     * @param color         The RGB colours for the pixel.
     * @param hsbvals       The HSV values for the pixel.
     *
     * @return              True if the RGB and HSV values are within the defined
     *                      thresholds (and thus the pixel is part of the blue T),
     *                      false otherwise.
     */
    private boolean isBlue(Color color, float[] hsbvals) {
        return hsbvals[0] <= thresholdsState.getBlue_h_high() && hsbvals[0] >= thresholdsState.getBlue_h_low()
                && hsbvals[1] <= thresholdsState.getBlue_s_high() && hsbvals[1] >= thresholdsState.getBlue_s_low()
                && hsbvals[2] <= thresholdsState.getBlue_v_high() && hsbvals[2] >= thresholdsState.getBlue_v_low()
                && color.getRed() <= thresholdsState.getBlue_r_high() && color.getRed() >= thresholdsState.getBlue_r_low()
                && color.getGreen() <= thresholdsState.getBlue_g_high() && color.getGreen() >= thresholdsState.getBlue_g_low()
                && color.getBlue() <= thresholdsState.getBlue_b_high() && color.getBlue() >= thresholdsState.getBlue_b_low();
    }

    /**
     * Determines if a pixel is part of the yellow T, based on input RGB colours
     * and hsv values.
     *
     * @param color         The RGB colours for the pixel.
     * @param hsbvals       The HSV values for the pixel.
     *
     * @return              True if the RGB and HSV values are within the defined
     *                      thresholds (and thus the pixel is part of the yellow T),
     *                      false otherwise.
     */
    private boolean isYellow(Color colour, float[] hsbvals) {
        return hsbvals[0] <= thresholdsState.getYellow_h_high() && hsbvals[0] >= thresholdsState.getYellow_h_low()
                && hsbvals[1] <= thresholdsState.getYellow_s_high() && hsbvals[1] >= thresholdsState.getYellow_s_low()
                && hsbvals[2] <= thresholdsState.getYellow_v_high() && hsbvals[2] >= thresholdsState.getYellow_v_low()
                && colour.getRed() <= thresholdsState.getYellow_r_high() && colour.getRed() >= thresholdsState.getYellow_r_low()
                && colour.getGreen() <= thresholdsState.getYellow_g_high() && colour.getGreen() >= thresholdsState.getYellow_g_low()
                && colour.getBlue() <= thresholdsState.getYellow_b_high() && colour.getBlue() >= thresholdsState.getYellow_b_low();
    }

    /**
     * Determines if a pixel is part of the ball, based on input RGB colours
     * and hsv values.
     *
     * @param color         The RGB colours for the pixel.
     * @param hsbvals       The HSV values for the pixel.
     *
     * @return              True if the RGB and HSV values are within the defined
     *                      thresholds (and thus the pixel is part of the ball),
     *                      false otherwise.
     */
    private boolean isBall(Color colour, float[] hsbvals) {
        
        return (hsbvals[0] <= thresholdsState.getBall_hlower_high() && hsbvals[0] >= thresholdsState.getBall_hlower_low() ||
                hsbvals[0] <= thresholdsState.getBall_hupper_high() && hsbvals[0] >= thresholdsState.getBall_hupper_low())
                && hsbvals[1] <= thresholdsState.getBall_s_high() && hsbvals[1] >= thresholdsState.getBall_s_low()
                && hsbvals[2] <= thresholdsState.getBall_v_high() && hsbvals[2] >= thresholdsState.getBall_v_low()
                && colour.getRed() <= thresholdsState.getBall_r_high() && colour.getRed() >= thresholdsState.getBall_r_low()
                && colour.getGreen() <= thresholdsState.getBall_g_high() && colour.getGreen() >= thresholdsState.getBall_g_low()
                && colour.getBlue() <= thresholdsState.getBall_b_high() && colour.getBlue() >= thresholdsState.getBall_b_low();
    }

    /**
     * Determines if a pixel is part of either grey circle, based on input RGB colours
     * and hsv values.
     *
     * @param color         The RGB colours for the pixel.
     * @param hsbvals       The HSV values for the pixel.
     *
     * @return              True if the RGB and HSV values are within the defined
     *                      thresholds (and thus the pixel is part of a grey circle),
     *                      false otherwise.
     */
    private boolean isGrey(Color colour, float[] hsbvals) {
        return hsbvals[0] <= thresholdsState.getGrey_h_high() && hsbvals[0] >= thresholdsState.getGrey_h_low()
                && hsbvals[1] <= thresholdsState.getGrey_s_high() && hsbvals[1] >= thresholdsState.getGrey_s_low()
                && hsbvals[2] <= thresholdsState.getGrey_v_high() && hsbvals[2] >= thresholdsState.getGrey_v_low()
                && colour.getRed() <= thresholdsState.getGrey_r_high() && colour.getRed() >= thresholdsState.getGrey_r_low()
                && colour.getGreen() <= thresholdsState.getGrey_g_high() && colour.getGreen() >= thresholdsState.getGrey_g_low()
                && colour.getBlue() <= thresholdsState.getGrey_b_high() && colour.getBlue() >= thresholdsState.getGrey_b_low();
    }

    /**
     * Determines if a pixel is part of either green plate, based on input RGB colours
     * and hsv values.
     *
     * @param color         The RGB colours for the pixel.
     * @param hsbvals       The HSV values for the pixel.
     *
     * @return              True if the RGB and HSV values are within the defined
     *                      thresholds (and thus the pixel is part of a green plate),
     *                      false otherwise.
     */
    private boolean isGreen(Color colour, float[] hsbvals) {
        return hsbvals[0] <= thresholdsState.getGreen_h_high() && hsbvals[0] >= thresholdsState.getGreen_h_low()
                && hsbvals[1] <= thresholdsState.getGreen_s_high() && hsbvals[1] >= thresholdsState.getGreen_s_low()
                && hsbvals[2] <= thresholdsState.getGreen_v_high() && hsbvals[2] >= thresholdsState.getGreen_v_low()
                && colour.getRed() <= thresholdsState.getGreen_r_high() && colour.getRed() >= thresholdsState.getGreen_r_low()
                && colour.getGreen() <= thresholdsState.getGreen_g_high() && colour.getGreen() >= thresholdsState.getGreen_g_low()
                && colour.getBlue() <= thresholdsState.getGreen_b_high() && colour.getBlue() >= thresholdsState.getGreen_b_low();
    }

    /**
     * Finds the orientation of a robot, given a list of the points contained within it's
     * T-shape (in terms of a list of x coordinates and y coordinates), the mean x and
     * y coordinates, and the image from which it was taken.
     *
     * @param xpoints           The x-coordinates of the points contained within the T-shape.
     * @param ypoints           The y-coordinates of the points contained within the T-shape.
     * @param meanX             The mean x-point of the T.
     * @param meanY             The mean y-point of the T.
     * @param image             The image from which the points were taken.
     * @param showImage         A boolean flag - if true a line will be drawn showing
     *                          the direction of orientation found.
     *
     * @return                  An orientation from -Pi to Pi degrees.
     * @throws NoAngleException
     */
    //Possibly needs to return something
    public void findGreyWithinGreen(ArrayList<Integer> xpoints, ArrayList<Integer> ypoints, int meanX, int meanY, BufferedImage image, boolean showImage) throws NoAngleException {
        assert (xpoints.size() == ypoints.size());
        if (xpoints.size() == 0) {
            throw new NoAngleException("No green pixels");
        }
    }

    // NOTE!  Changed from float to double
    public double findOrientation(ArrayList<Integer> xpoints, ArrayList<Integer> ypoints,
            int meanX, int meanY, BufferedImage image, boolean showImage) throws NoAngleException {
        assert (xpoints.size() == ypoints.size()) :
                "Error: Must be equal number of x and y points!";

        Graphics imageGraphics = image.getGraphics();
        if (xpoints.size() == 0) {
            throw new NoAngleException("No T pixels");
        }

        int stdev = 0;
        /* Standard deviation */
        for (int i = 0; i < xpoints.size(); i++) {
            int x = xpoints.get(i);
            int y = ypoints.get(i);

            stdev += Math.pow(Math.sqrt(Position.sqrdEuclidDist(x, y, meanX, meanY)), 2);
        }
        stdev = (int) Math.sqrt(stdev / xpoints.size());


        /* Find the position of the front of the T. */
        int frontX = 0;
        int frontY = 0;
        int frontCount = 0;
        for (int i = 0; i < xpoints.size(); i++) {
            if (stdev > 15) {
                if (Math.abs(xpoints.get(i) - meanX) < stdev && Math.abs(ypoints.get(i) - meanY) < stdev
                        && Position.sqrdEuclidDist(xpoints.get(i), ypoints.get(i), meanX, meanY) > Math.pow(15, 2)) {
                    frontCount++;
                    frontX += xpoints.get(i);
                    frontY += ypoints.get(i);
                }
            } else {
                if (Position.sqrdEuclidDist(xpoints.get(i), ypoints.get(i), meanX, meanY) > Math.pow(15, 2)) {
                    frontCount++;
                    frontX += xpoints.get(i);
                    frontY += ypoints.get(i);
                }
            }
        }

        /* If no points were found, throw exception. */
        if (frontCount == 0) {
            throw new NoAngleException("Front of T was not found");
        }

        /* Otherwise, get the frontX and Y. */
        frontX /= frontCount;
        frontY /= frontCount;

        imageGraphics.setColor(Color.white);
        imageGraphics.drawOval(frontX - 15, frontY - 15, 30, 30);
        imageGraphics.setColor(Color.blue);
        imageGraphics.drawOval(meanX - 15, meanY - 15, 30, 30);
        image.getGraphics().drawLine(frontX, frontY, meanX, meanY);

        /* In here, calculate the vector between meanX/frontX and
         * meanY/frontY, and then get the angle of that vector. */

        // Calculate the angle between the Mean(Middle) of T and the Front.
        // This should be the default angle for finding orientation.
        // Because the gray dot is often hard to identify.

        float length = (float) Math.sqrt(Math.pow(frontX - meanX, 2)
                + Math.pow(frontY - meanY, 2));
        float ax = (frontX - meanX) / length;
        float ay = (frontY - meanY) / length;

        // Get the angle in radians first.
        float radianAngleMF = (float) Math.acos(ax);

        // Now convert to degrees.
        double angleMF = Math.toDegrees((double) radianAngleMF);

        if (frontY < meanY) {
            angleMF = -angleMF;
        }

        if (angleMF < 0) {
            angleMF = Math.abs(angleMF);
        } else {
            angleMF = 360 - angleMF;
        }


        if (radianAngleMF == 0) {
            return (float) 0.001;
        }
        // # Handles the history of values ################
        /*
        lastFiveAngles.add(angleMF);
        
        while (lastFiveAngles.size() > 5) { 
        lastFiveAngles.remove(0);
        }
        
        if (lastFiveAngles.size() == 5) {
        angleMF = angleHistory.getMean(lastFiveAngles);
        }
         */
        // ################################################
        // DEBUG
        //System.out.println("Front-T angle = " + angleMF + ".  Remember Yellow is turned off for testing!");
        return angleMF;
    }
// getVectorAngle can either find the "longestLine" or the sum of vectors for attempting to get an angle.

    public double getVectorAngle(BufferedImage image, ArrayList<Integer> xPoints, ArrayList<Integer> yPoints, int meanX, int meanY) {

        int minX = meanX;
        int maxX = meanX;

        int minY = meanY;
        int maxY = meanY;

        int[] newXpoints = new int[4];
        int[] newYpoints = new int[4];

        int currentX;
        int currentY;

        Position helper = new Position(meanX, meanY);
        float range = 50;

        for (int i = 0; i < xPoints.size(); i++) {

            currentX = xPoints.get(i);
            currentY = yPoints.get(i);

            if ((helper.sqrdEuclidDist(meanX, meanY, currentX, currentY)) < range) {
                if (currentX < minX) {
                    minX = currentX;
                    newXpoints[0] = currentX;
                    newYpoints[0] = currentY;
                } else if (currentX > maxX) {
                    maxX = currentX;
                    newXpoints[1] = currentX;
                    newYpoints[1] = currentY;
                }

                if (currentY < minY) {
                    minY = currentY;
                    newYpoints[2] = currentY;
                    newXpoints[2] = currentX;
                } else if (currentY > maxY) {
                    maxY = currentY;
                    newYpoints[3] = currentY;
                    newXpoints[3] = currentX;
                }
            }

        }

        // #################
        // Vector Sum Method
        /*
        double vectorX = 0;
        double vectorY = 0;
        
        //for (int i = 0; i < 4; i++) {
        //    vectorX += newXpoints[i] - meanX;
        //    vectorY += meanY - newYpoints[i];
        //}
        
        vectorX = (minX - meanX) + (maxX - meanX);
        vectorY = (meanY - minY) + (meanY - maxY);
        
        if (vectorX == 0) {
        vectorX = 1;
        }
        
        if (vectorY == 0) {
        vectorY = 1;
        }
         */
        // End of Sum of Vectors Method
        // ################################
        // Squared Eclidean Distance Method

        double distance = 0;
        int pointX = 0;
        int pointY = 0;
        for (int i = 0; i < newXpoints.length; i++) {
            double tempDistance = Math.pow(meanX - newXpoints[i], 2) + Math.pow(meanY - newYpoints[i], 2);
            if (tempDistance > distance) {
                distance = tempDistance;
                pointX = newXpoints[i];
                pointY = newYpoints[i];
            }
            // DEBUG
            //System.out.println("newXpoint = " + newXpoints[i]);
        }

        // DEBUG - draws a small circle around where the point is detected.
        //image.getGraphics().drawOval(pointX-5, pointY-5, 10, 10);

        double vectorX = pointX - meanX;
        double vectorY = meanY - pointY;

        // End of Longest Line method
        // ##################################


        double divisor = vectorY / vectorX;
        double radianAngle = Math.atan(divisor);
        double degreeAngle = Math.toDegrees(radianAngle);
        degreeAngle = Math.abs(degreeAngle);

        double angle = 0.1;

        // DEBUG - Some prints used in testing.
        //System.out.println("MeanX is: " + meanX + ", MeanY is: " + meanY + ", PointX is: " + pointX + ", PointY is: " + pointY);
        //System.out.println("Angle = " + degreeAngle + ", VectorX = " + vectorX + ", VectorY = " + vectorY);
        //System.out.println("Using the vector method.  Remember Yellow is turned off for testing!");

        if (vectorX < 0) {
            if (vectorY > 0) {
                angle = degreeAngle;
            } else {
                angle = 360 - degreeAngle;
            }
        } else {
            if (vectorY > 0) {
                angle = 180 - degreeAngle;
            } else {
                angle = 180 + degreeAngle;
            }
        }

        // DEBUG - attempts to draw a line at the angle it's facing.
        image.getGraphics().drawLine(((int) (meanX - vectorX)), ((int) (meanY - vectorY)), meanX, meanY);

        System.out.println("VectorX = " + vectorX + ", VectorY = " + vectorY + ", Angle = " + angle);
        return angle;
    }

    public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    public boolean clusterChecker(int closeoneX, int closeoneY, int closetwoX, int closetwoY, int closethreeX, int closethreeY, int farX, int farY, String test) {


        double closefarone = distance(closeoneX, closeoneY, farX, farY);
        double closefartwo = distance(closetwoX, closetwoY, farX, farY);
        double closefarthree = distance(closethreeX, closethreeY, farX, farY);
        double distone = distance(closeoneX, closeoneY, closetwoX, closetwoY);
        double disttwo = distance(closeoneX, closeoneY, closethreeX, closethreeY);
        double distthree = distance(closetwoX, closetwoY, closethreeX, closethreeY);


        //Find the shortest distances from the potential cluster and the potential front point
        double min = Math.min(Math.min(closefarone, closefartwo), closefarthree);
        
        //If the close distances are all smaller than the smallest distance to the far one and...
        if (distone < min && disttwo < min && distthree < min) {
            //...if the close ones are near on an x axis, y axis, then it is a cluster.
            if (Math.abs(closeoneX - closetwoX) <= 6 && Math.abs(closeoneX - closethreeX) <= 6
                    && Math.abs(closetwoX - closethreeX) <= 6 && Math.abs(closeoneX - farX) > 10) {
                return true;
            }
            if (Math.abs(closeoneY - closetwoY) <= 6 && Math.abs(closeoneY - closethreeY) <= 6
                    && Math.abs(closetwoY - closethreeY) <= 6 && Math.abs(closeoneY - farY) > 10) {
                return true;
            }
        }
        return false;
    }

    public double getGeometricOrientation(ArrayList<Integer> xpoints, ArrayList<Integer> ypoints,
            int meanX, int meanY, BufferedImage image, boolean isBlue) throws NoAngleException {
        assert (xpoints.size() == ypoints.size()) :
                "Error: Must be equal number of x and y points!";
        Integer robotMaxX = 0;
        Integer robotMinX = 0;
        Integer robotMaxY = 0;
        Integer robotMinY = 0;
        try {
            robotMaxX = Collections.max(xpoints);
            robotMaxY = Collections.max(ypoints);
            robotMinX = Collections.min(xpoints);
            robotMinY = Collections.min(ypoints);
            Graphics imageGraphics = image.getGraphics();
            /*
             * These points represent the extrema of the colored T. The names are simply reflections
             * of visual debugging, nothing more. Blue is MinimumY, White is the MaximumX, Red is the 
             * MinimumX and Black is the MaximumY
             */


            int blueoneX = xpoints.get(ypoints.lastIndexOf(robotMinY));
            int blueoneY = robotMinY;
            int whiteoneX = robotMaxX;
            int whiteoneY = ypoints.get(xpoints.lastIndexOf(robotMaxX));
            int redoneX = robotMinX;
            int redoneY = ypoints.get(xpoints.lastIndexOf(robotMinX));
            int blackoneX = xpoints.get(ypoints.lastIndexOf(robotMaxY));
            int blackoneY = robotMaxY;
            int frontPointX = 0;
            int frontPointY = 0;
            int backPointX = 0;
            int backPointY = 0;
//            System.out.println("BlueX: " + blueoneX + " BlueY: " + blueoneY + " WhiteX: " + whiteoneX + " WhiteY: " + whiteoneY + " RedX: " + redoneX + " RedY: " + redoneY + " BlackX: " + blackoneX + " BlackY: " + blackoneY);
            boolean assigned = false;

            /*
             * 
             * Depending on how the robot is orientated, the distance along the top/bottom 
             * of the T will be between blue and white, or blue and red. The same is true on 
             * the other side, but this time between black and white or black and red. 
             * But, because we are dealing with Maximas and Minimas, we occasionally 
             * have a result where all the points are near one side. We don't know which points
             * to use, so we first check for clusters. 
             */
            double chooser = Math.min(distance(blueoneX, blueoneY, whiteoneX, whiteoneY), distance(blueoneX, blueoneY, redoneX, redoneY));
            //Check all the clusters
            if (clusterChecker(redoneX, redoneY, blackoneX, blackoneY, whiteoneX, whiteoneY, blueoneX, blueoneY, "blue")) {

                /*
                 * Here, red, black and white are clustered together, while blue is 
                 * far away. We say that blue is a good point, and then take the 
                 * average of the cluster for the other. If you want to see this,
                 * uncomment the imagegraphics lines
                 */
                frontPointX = blueoneX;
                frontPointY = blueoneY;
                backPointX = (redoneX + blackoneX + whiteoneX) / 3;
                backPointY = (redoneY + blackoneY + whiteoneY) / 3;

                /* 
                 *  imageGraphics.setColor(Color.black);
                 *  imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                 *  imageGraphics.setColor(Color.blue);
                 *  imageGraphics.drawOval(blueoneX - 5, blueoneY - 5, 10, 10);                 * 
                 */
            }
            if (clusterChecker(redoneX, redoneY, blackoneX, blackoneY, blueoneX, blueoneY, whiteoneX, whiteoneY, "white")) {
                frontPointX = whiteoneX;
                frontPointY = whiteoneY;
                backPointX = (redoneX + blackoneX + blueoneX) / 3;
                backPointY = (redoneY + blackoneY + blueoneY) / 3;

                /*             
                 * imageGraphics.setColor(Color.white);
                 * imageGraphics.drawOval(whiteoneX - 5, whiteoneY - 5, 10, 10); 
                 * imageGraphics.setColor(Color.black);
                 * imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                 */
            }
            if (clusterChecker(redoneX, redoneY, whiteoneX, whiteoneY, blueoneX, blueoneY, blackoneX, blackoneY, "black")) {
                frontPointX = blackoneX;
                frontPointY = blackoneY;
                backPointX = (redoneX + whiteoneX + blueoneX) / 3;
                backPointY = (redoneY + whiteoneY + blueoneY) / 3;

                /*
                 * imageGraphics.setColor(Color.black);
                 * imageGraphics.drawOval(blackoneX - 5, blackoneY - 5, 10, 10);
                 * imageGraphics.setColor(Color.white);
                 * imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                 */
            }
            if (clusterChecker(blackoneX, blackoneY, blueoneX, blueoneY, whiteoneX, whiteoneY, redoneX, redoneY, "red")) {
                frontPointX = redoneX;
                frontPointY = redoneY;
                backPointX = (whiteoneX + blackoneX + blueoneX) / 3;
                backPointY = (whiteoneY + blackoneY + blueoneY) / 3;

                /*
                 * imageGraphics.setColor(Color.red);
                 * imageGraphics.drawOval(redoneX - 5, redoneY - 5, 10, 10);
                 * imageGraphics.setColor(Color.black);
                 * imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                 */
            }

            //If none of the cluster principles hold, use the average points
            if (!clusterChecker(blackoneX, blackoneY, blueoneX, blueoneY, whiteoneX, whiteoneY, redoneX, redoneY, "")
                    && !clusterChecker(redoneX, redoneY, whiteoneX, whiteoneY, blueoneX, blueoneY, blackoneX, blackoneY, "")
                    && !clusterChecker(redoneX, redoneY, blackoneX, blackoneY, blueoneX, blueoneY, whiteoneX, whiteoneY, "")
                    && !clusterChecker(redoneX, redoneY, blackoneX, blackoneY, whiteoneX, whiteoneY, blueoneX, blueoneY, "")) {

                if (distance(blueoneX, blueoneY, whiteoneX, whiteoneY) == chooser) {
                    if (chooser < 7) {
                        frontPointX = (blueoneX + whiteoneX) / 2;
                        frontPointY = (blueoneY + whiteoneY) / 2;
                        assigned = true;
                    } else {
                        backPointX = (blueoneX + whiteoneX) / 2;
                        backPointY = (blueoneY + whiteoneY) / 2;
                    }

//                    imageGraphics.setColor(Color.black);
//                    imageGraphics.drawOval(frontPointX - 5, frontPointY - 5, 10, 10);
                } else {
                    if (chooser < 7) {
                        frontPointX = (blueoneX + redoneX) / 2;
                        frontPointY = (blueoneY + redoneY) / 2;
                        assigned = true;
                    } else {
                        backPointX = (blueoneX + redoneX) / 2;
                        backPointY = (blueoneY + redoneY) / 2;
                    }


                }

                double chooser2 = Math.min(distance(blackoneX, blackoneY, whiteoneX, whiteoneY), distance(blackoneX, blackoneY, redoneX, redoneY));
                if (distance(blackoneX, blackoneY, whiteoneX, whiteoneY) == chooser2) {
                    if (!assigned) {
                        frontPointX = (blackoneX + whiteoneX) / 2;
                        frontPointY = (blackoneY + whiteoneY) / 2;
                    } else {
                        backPointX = (blackoneX + whiteoneX) / 2;
                        backPointY = (blackoneY + whiteoneY) / 2;
                    }

                } else {
                    if (!assigned) {
                        frontPointX = (blackoneX + redoneX) / 2;
                        frontPointY = (blackoneY + redoneY) / 2;
                    } else {
                        backPointX = (blackoneX + redoneX) / 2;
                        backPointY = (blackoneY + redoneY) / 2;
                    }
//                    imageGraphics.setColor(Color.GRAY);
//                    imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                }




            }
            if (isBlue) {
                if (worldState.getBlueExtrema()) {
                    imageGraphics.setColor(Color.gray);
                    imageGraphics.drawOval(robotMaxX - 5, ypoints.get(xpoints.lastIndexOf(robotMaxX)) - 5, 10, 10);
                    imageGraphics.setColor(Color.red);
                    imageGraphics.drawOval(robotMinX - 5, ypoints.get(xpoints.lastIndexOf(robotMinX)) - 5, 10, 10);
                    imageGraphics.setColor(Color.cyan);
                    imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMinY)) - 5, robotMinY - 5, 10, 10);
                    imageGraphics.setColor(Color.black);
                    imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMaxY)) - 5, robotMaxY - 5, 10, 10);
                }
                if (worldState.getBlueTriple()) {
                    imageGraphics.setColor(Color.yellow);
                    imageGraphics.drawOval(meanX - 5, meanY - 5, 10, 10);
                    imageGraphics.setColor(Color.yellow);
                    imageGraphics.drawOval(frontPointX - 5, frontPointY - 5, 10, 10);
                    imageGraphics.setColor(Color.yellow);
                    imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                }
            } else {
                if (worldState.getYellowExtrema()) {
                    imageGraphics.setColor(Color.blue);
                    imageGraphics.drawOval(robotMaxX - 5, ypoints.get(xpoints.lastIndexOf(robotMaxX)) - 5, 10, 10);
                    imageGraphics.setColor(Color.gray);
                    imageGraphics.drawOval(robotMinX - 5, ypoints.get(xpoints.lastIndexOf(robotMinX)) - 5, 10, 10);
                    imageGraphics.setColor(Color.GREEN);
                    imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMinY)) - 5, robotMinY - 5, 10, 10);
                    imageGraphics.setColor(Color.cyan);
                    imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMaxY)) - 5, robotMaxY - 5, 10, 10);
                }
                if (worldState.getYellowTriple()) {
                    imageGraphics.setColor(Color.blue);
                    imageGraphics.drawOval(meanX - 5, meanY - 5, 10, 10);
                    imageGraphics.setColor(Color.blue);
                    imageGraphics.drawOval(frontPointX - 5, frontPointY - 5, 10, 10);
                    imageGraphics.setColor(Color.blue);
                    imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                }
            }




            /*
             * If we have reached here and frontPoint and backPoint are still zero, then we should run the alternate angle. 
             */
            if (frontPointX == 0 || backPointX == 0 || frontPointY == 0 || backPointY == 0) {
                return findOrientation(xpoints, ypoints, meanX, meanY, image, false);
            } else {

                /*
                 * Here, we take our two points and use them with the mean pixels to compute three angles. 
                 * The first uses the front and the mean.
                 */


                double angleMF = 0;
                if (frontPointY > backPointY) {
                    angleMF = (360 - Math.abs(Math.toDegrees(Math.atan2((frontPointY - meanY), (frontPointX - meanX)))));
                } else {
                    angleMF = Math.abs(Math.toDegrees(Math.atan2((frontPointY - meanY), (frontPointX - meanX))));
                }

                /*
                 * Next, we use the front and the back
                 */

                double angleFB = 0;

                if (frontPointY > backPointY) {
                    angleFB = (360 - Math.abs(Math.toDegrees(Math.atan2((frontPointY - backPointY), (frontPointX - backPointX)))));
                } else {
                    angleFB = Math.abs(Math.toDegrees(Math.atan2((frontPointY - backPointY), (frontPointX - backPointX))));
                }

                /*
                 * Finally, we use the mean and the back
                 */

                double angleMB = 0;
                if (frontPointY > backPointY) {
                    angleMB = (360 - Math.abs(Math.toDegrees(Math.atan2((meanY - backPointY), (meanX - backPointX)))));
                } else {
                    angleMB = Math.abs(Math.toDegrees(Math.atan2((meanY - backPointY), (meanX - backPointX))));
                }

                //FB seems to be the best
                if (isBlue) {
                    if (worldState.getBAngle()) {
                        System.out.println("Angle 1: " + angleMF + " Angle 2: " + angleFB + " Angle 3: " + angleMB);
                    }

                } else {
                    if (worldState.getYAngle()) {
                        System.out.println("Angle 1: " + angleMF + " Angle 2: " + angleFB + " Angle 3: " + angleMB);
                    }
                }




//                    if (radianAngleMF == 0) {
//                        return (float) 0.001;
//                    }

                return angleFB;
            }


        } catch (Exception e) {
            System.out.println("Not enough pixels");
            return findOrientation(xpoints, ypoints, meanX, meanY, image, false);
        }
    }
}
// FOLLOWING IS ALL CURRENTLY UNUSED CODE
// This was a part of the findOrientation method.

/* Calculate new angle using just the center of the T and the grey circle */
/*
length = (float) Math.sqrt(Math.pow(frontX - backX, 2)
+ Math.pow(frontY - backY, 2));
ax = (frontX - backX) / length;
ay = (frontY - backY) / length;
angle = (float) Math.acos(ax);
double angle3 = Math.toDegrees((double) Math.acos(ax));

if (frontY < meanY) {
angle3 = -angle3;
}

if(angle3<0) {
angle3 = Math.abs(angle3);
}
else {
angle3 = 360 - angle3;
}




length = (float) Math.sqrt(Math.pow(meanX - backX, 2)
+ Math.pow(meanY - backY, 2));
ax = (meanX - backX) / length;
ay = (meanY - backY) / length;
double angle4 = Math.toDegrees((double) Math.acos(ax));
angle = (float) Math.acos(ax);

if (frontY < meanY) {
angle4 = -angle4;
}

if(angle4<0) {
angle4 = Math.abs(angle4);
}
else {
angle4 = 360 - angle4;
}
 */
//Look in a cone in the opposite direction to try to find the grey circle
/*
ArrayList<Integer> greyXPoints = new ArrayList<Integer>();
ArrayList<Integer> greyYPoints = new ArrayList<Integer>();

for (int a=-20; a < 21; a++) {
ax = (float) Math.cos(radianAngleMF+((a*Math.PI)/180));
ay = (float) Math.sin(radianAngleMF+((a*Math.PI)/180));
for (int i = 15; i < 25; i++) {
int greyX = meanX - (int) (ax * i);
int greyY = meanY - (int) (ay * i);
try {
Color c = new Color(image.getRGB(greyX, greyY));
float hsbvals[] = new float[3];
Color.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbvals);
if (isGrey(c, hsbvals)) {
greyXPoints.add(greyX);
greyYPoints.add(greyY);
}
} catch (Exception e) {
//This happens if part of the search area goes outside the image
//This is okay, just ignore and continue
}
}
}
 */
/* No grey circle found
 * The angle found is probably wrong, skip this value and return 0 */
//        if (greyXPoints.size() < 30) {
//            throw new NoAngleException("No grey circle found");
//        }

/* Calculate center of grey circle points */
/*
int totalX = 0;
int totalY = 0;
for (int i = 0; i < greyXPoints.size(); i++) {
totalX += greyXPoints.get(i);
totalY += greyYPoints.get(i);
}
 */
/* Center of grey circle */
//float backX = totalX / greyXPoints.size();
//float backY = totalY / greyXPoints.size();
//imageGraphics.setColor(Color.red);
//imageGraphics.drawOval((int)backX-15, (int) backY-15, 30,30);

/* Check that the circle is surrounded by the green plate
 * Currently checks above and below the circle */
//int foundGreen = 0;
//int greenSides = 0;
/* Check if green points are above the grey circle */
/*
for (int x=(int) (backX-2); x < (int) (backX+3); x++) {
for (int y = (int) (backY-9); y < backY; y++) {
try {
Color c = new Color(image.getRGB(x, y));
float hsbvals[] = new float[3];
Color.RGBtoHSB(c.getRed(), c.getBlue(), c.getGreen(), hsbvals);
if (isGreen(c, hsbvals)) {
foundGreen++;
break;
}
} catch (Exception e) {
// Ignore.
}
}
}

if (foundGreen >= 3) {
greenSides++;
}

 * /
/* Check if green points are below the grey circle */
/*
foundGreen = 0;
for (int x=(int) (backX-2); x < (int) (backX+3); x++) {
for (int y = (int) (backY); y < backY+10; y++) {
try {
Color c = new Color(image.getRGB(x, y));
float hsbvals[] = new float[3];
Color.RGBtoHSB(c.getRed(), c.getBlue(), c.getGreen(), hsbvals);
if (isGreen(c, hsbvals)) {
foundGreen++;
break;
}
} catch (Exception e) {
// Ignore.
}
}
}

if (foundGreen >= 3) {
greenSides++;
}

 * /
/* Check if green points are left of the grey circle */
/*
foundGreen = 0;
for (int x=(int) (backX-9); x < backX; x++) {
for (int y = (int) (backY-2); y < backY+3; y++) {
try {
Color c = new Color(image.getRGB(x, y));
float hsbvals[] = new float[3];
Color.RGBtoHSB(c.getRed(), c.getBlue(), c.getGreen(), hsbvals);
if (isGreen(c, hsbvals)) {
foundGreen++;
break;
}
} catch (Exception e) {
// Ignore.
}
}
}

if (foundGreen >= 3) {
greenSides++;
}
 * /
/* Check if green points are right of the grey circle */
//foundGreen = 0;
        /*
for (int x=(int) (backX); x < (int) (backX+10); x++) {
for (int y = (int) (backY-2); y < backY+3; y++) {
try {
Color c = new Color(image.getRGB(x, y));
float hsbvals[] = new float[3];
Color.RGBtoHSB(c.getRed(), c.getBlue(), c.getGreen(), hsbvals);
if (isGreen(c, hsbvals)) {
foundGreen++;
break;
}
} catch (Exception e) {
// Ignore.
}
}
}

if (foundGreen >= 3) {
greenSides++;
}


if (greenSides < 3) {
throw new NoAngleException("Not enough green areas around the grey circle");
}
 * /

/*
 * At this point, the following is true:
 * Center of the T has been found
 * Front of the T has been found
 * Grey circle has been found
 * Grey circle is surrounded by green plate pixels on at least 3 sides
 * The grey circle, center of the T and front of the T line up roughly with the same angle
 */
/*
if (showImage) {
image.getGraphics().drawLine((int)backX, (int)backY, (int)(backX+ax*70), (int)(backY+ay*70));
image.getGraphics().drawOval((int) backX-4, (int) backY-4, 8, 8);
}
 */
//System.out.println("F/M: "+angleMF);
//System.out.println("F/M: "+angleMF+". F/B: "+angle3+". M/B: "+angle4);

