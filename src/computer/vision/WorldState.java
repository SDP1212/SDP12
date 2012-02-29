package computer.vision;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;
import java.util.ArrayList;

public class WorldState implements computer.simulator.VisionInterface {

    private int direction; // 0 = right, 1 = left.
    private int colour; // 0 = yellow, 1 = blue
    private int pitch; // 0 = main, 1 = side room
    private int blueX;
    private int blueY;
    private int yellowX;
    private int yellowY;
    private int ballX;
    private int ballY;
    private int greenX;
    private int greenY;
    private int greenX2;
    private int greenY2;
    private float blueOrientation;
    private float yellowOrientation;
    private long counter;
    private boolean showBlueExtrema;
    private boolean showYellowExtrema;
    private boolean showYAngle;
    private boolean showBAngle;
    private boolean showBlueTriple;
    private boolean showYellowTriple;
    boolean isDistortionCorrection = false;
    boolean isOrientationCorrected = false;
    // Holds the history of the blue/yellow angles respectively.
    ArrayList<Double> blueFiveAngles = new ArrayList<Double>();
    ArrayList<Double> yellowFiveAngles = new ArrayList<Double>();

    // Adds an angle to the blue history and keeps size of 5.
    public void addBlueAngle(double angle) {
        while (blueFiveAngles.size() < 6) {
            blueFiveAngles.add(angle);
        }
        while (blueFiveAngles.size() > 5) {
            blueFiveAngles.remove(0);
        }
    }

    // Returns the blue history.
    public ArrayList<Double> getBlueAngle() {
        return blueFiveAngles;
    }

    // An alternative to getBlueOrientation.
    public double getBlueOrientationFromHistory() {
        AngleHistory angleHistory = new AngleHistory();
        return angleHistory.getMean(blueFiveAngles);
    }

    // Adds an angle to the yellow history and keeps size of 5.
    public void addYellowAngle(double angle) {
        while (yellowFiveAngles.size() < 6) {
            yellowFiveAngles.add(angle);
        }
        while (yellowFiveAngles.size() > 5) {
            yellowFiveAngles.remove(0);
        }
    }

    // Returns the yellow history.
    public ArrayList<Double> getYellowAngle() {
        return yellowFiveAngles;
    }

    // An alternative to getYellowOrientation.
    public double getYellowOrientationFromHistory() {
        AngleHistory angleHistory = new AngleHistory();
        return angleHistory.getMean(yellowFiveAngles);
    }

    public WorldState() {

        /* control properties */
        this.direction = 0;
        this.colour = 0;
        this.pitch = 0;

        /* object properties */
        this.blueX = 0;
        this.blueY = 0;
        this.yellowX = 0;
        this.yellowY = 0;
        this.ballX = 0;
        this.ballY = 0;
        this.greenX = 0;
        this.greenY = 0;
        this.greenX2 = 0;
        this.greenY2 = 0;
        this.blueOrientation = 0;
        this.yellowOrientation = 0;
        this.showBAngle = false;
        this.showBlueExtrema = false;
        this.showBlueTriple = false;
        this.showYAngle = false;
        this.showYellowExtrema = false;
        this.showYellowTriple = false;

    }

    public int getBlueX() {
        return blueX;
    }

    public void setBlueX(int blueX) {
        this.blueX = blueX;
    }

    public int getBlueY() {
        return blueY;
    }

    public void setBlueY(int blueY) {
        this.blueY = blueY;
    }

    public int getYellowX() {
        return yellowX;
    }

    public void setYellowX(int yellowX) {
        this.yellowX = yellowX;
    }

    public int getYellowY() {
        return yellowY;
    }

    public void setYellowY(int yellowY) {
        this.yellowY = yellowY;
    }

    public int getBallX() {
        return ballX;
    }

    public void setBallX(int ballX) {
        this.ballX = ballX;
    }

    public int getBallY() {
        return ballY;
    }

    public void setBallY(int ballY) {
        this.ballY = ballY;
    }

    public int getGreenX() {
        return greenX;
    }

    public void setGreenX(int greenX) {
        this.greenX = greenX;
    }

    public int getGreenY() {
        return greenY;
    }

    public void setGreenY(int greenY) {
        this.greenY = greenY;
    }

    public int getGreenX2() {
        return greenX2;
    }

    public void setGreenX2(int greenX) {
        this.greenX2 = greenX;
    }

    public int getGreenY2() {
        return greenY2;
    }

    public void setGreenY2(int greenY) {
        this.greenY2 = greenY;
    }

    public void setBlueExtrema(boolean show) {
        this.showBlueExtrema = show;
    }

    public boolean getBlueExtrema() {
        return showBlueExtrema;
    }

    public void setBlueTriple(boolean show) {
        this.showBlueTriple = show;
    }

    public boolean getBlueTriple() {
        return showBlueTriple;
    }

    public void setBAngle(boolean show) {
        this.showBAngle = show;
    }

    public boolean getBAngle() {
        return showBAngle;
    }

    public void setYellowExtrema(boolean show) {
        this.showYellowExtrema = show;
    }

    public boolean getYellowExtrema() {
        return showYellowExtrema;
    }

    public void setYellowTriple(boolean show) {
        this.showYellowTriple = show;
    }

    public boolean getYellowTriple() {
        return showYellowTriple;
    }

    public void setYAngle(boolean show) {
        this.showYAngle = show;
    }

    public boolean getYAngle() {
        return showYAngle;
    }

    public float getBlueOrientation() {
        //System.out.println("Blue orientation: " + blueOrientation);
        // return getBlueOrientationFromHistory();
        return blueOrientation;
    }

    public void setBlueOrientation(float blueOrientation) {
        //System.out.println("Blue orientation: " + blueOrientation);
        this.blueOrientation = blueOrientation;
    }

    public float getYellowOrientation() {
        // return getYellowOrientationFromHistory();
        return yellowOrientation;
    }

    public void setYellowOrientation(float yellowOrientation) {
        this.yellowOrientation = yellowOrientation;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public int getColour() {
        return colour;
    }

    public void setColour(int colour) {
        this.colour = colour;
    }

    public int getPitch() {
        return pitch;
    }

    public void setPitch(int pitch) {
        this.pitch = pitch;

    }

    public void updateCounter() {
        this.counter++;
    }

    public long getCounter() {
        return this.counter;
    }

    // Following methods are for the interface.
    public PixelCoordinates[] getPitchCornerCoordinates() {

        PitchConstants pitchConstants = new PitchConstants(pitch);

        int leftBuffer = pitchConstants.leftBuffer;
        int topBuffer = pitchConstants.topBuffer;
        int rightBuffer = 640 - pitchConstants.rightBuffer;
        int bottomBuffer = 480 - pitchConstants.bottomBuffer;

        System.out.println("DEBUG: Corners: " + leftBuffer + ", " + rightBuffer + ", " + topBuffer + ", " + bottomBuffer);

        PixelCoordinates topLeftCorner = new PixelCoordinates(leftBuffer, topBuffer, isDistortionCorrection, isOrientationCorrected);
        PixelCoordinates topRightCorner = new PixelCoordinates(rightBuffer, topBuffer, isDistortionCorrection, isOrientationCorrected);
        PixelCoordinates bottomLeftCorner = new PixelCoordinates(leftBuffer, bottomBuffer, isDistortionCorrection, isOrientationCorrected);
        PixelCoordinates bottomRightCorner = new PixelCoordinates(rightBuffer, bottomBuffer, isDistortionCorrection, isOrientationCorrected);

        PixelCoordinates[] results;

        results = new PixelCoordinates[4];
        results[0] = topLeftCorner;
        results[1] = topRightCorner;
        results[2] = bottomRightCorner;
        results[3] = bottomLeftCorner;

        return results;
    }

    public PixelCoordinates getYellowRobotCoordinates() {
        return new PixelCoordinates(getYellowX(), getYellowY(), isDistortionCorrection, isOrientationCorrected);
    }

    public Direction getYellowRobotOrientation() {
        return new Direction(Math.toRadians(getYellowOrientation()));
    }

    public PixelCoordinates getBlueRobotCoordinates() {
        return new PixelCoordinates(getBlueX(), getBlueY(), isDistortionCorrection, isOrientationCorrected);
    }

    public Direction getBlueRobotOrientation() {
        //this is in radians
//        System.out.println("blue or " + getBlueOrientation() );
        return new Direction(Math.toRadians(getBlueOrientation()));
    }

    public PixelCoordinates[] getLeftGoalCoordinates() {
        // First element is top coordinate, second is bottom.

        PitchConstants pitchConstants = new PitchConstants(pitch);

        int leftBuffer = pitchConstants.leftBuffer;
        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = 480 - pitchConstants.bottomBuffer;

        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = bottomBuffer - topBuffer;
        int topGoal = topBuffer + (goalWidth / 4);
        int bottomGoal = bottomBuffer - (goalWidth / 4);

        PixelCoordinates topLeftGoal = new PixelCoordinates(leftBuffer, topGoal, isDistortionCorrection, isOrientationCorrected);
        PixelCoordinates bottomLeftGoal = new PixelCoordinates(leftBuffer, bottomGoal, isDistortionCorrection, isOrientationCorrected);

        PixelCoordinates[] leftGoalCoordinates;

        leftGoalCoordinates = new PixelCoordinates[2];
        leftGoalCoordinates[0] = topLeftGoal;
        leftGoalCoordinates[1] = bottomLeftGoal;

        return leftGoalCoordinates;

    }

    public PixelCoordinates[] getRightGoalCoordinates() {
        // First element is top coordinate, second is bottom.

        PitchConstants pitchConstants = new PitchConstants(pitch);

        int topBuffer = pitchConstants.topBuffer;
        int rightBuffer = 640 - pitchConstants.rightBuffer;
        int bottomBuffer = 480 - pitchConstants.bottomBuffer;

        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = bottomBuffer - topBuffer;
        int topGoal = topBuffer + (goalWidth / 4);
        int bottomGoal = bottomBuffer - (goalWidth / 4);

        PixelCoordinates topRightGoal = new PixelCoordinates(rightBuffer, topGoal, isDistortionCorrection, isOrientationCorrected);
        PixelCoordinates bottomRightGoal = new PixelCoordinates(rightBuffer, bottomGoal, isDistortionCorrection, isOrientationCorrected);

        PixelCoordinates[] rightGoalCoordinates;

        rightGoalCoordinates = new PixelCoordinates[2];
        rightGoalCoordinates[0] = topRightGoal;
        rightGoalCoordinates[1] = bottomRightGoal;

        return rightGoalCoordinates;
    }

    public PixelCoordinates getBallCoordinates() {
        return new PixelCoordinates(getBallX(), getBallY(), isDistortionCorrection, isOrientationCorrected);
    }
}