package computer.vision;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class WorldState implements computer.simulator.VisionInterface {
	private PixelCoordinates[] pitchCornerCoordinates;
	private PixelCoordinates yellowRobotCoordinates;
	private Direction yellowRobotOrientation;
	private PixelCoordinates blueRobotCoordinates;
	private Direction blueRobotOrientation;
	private PixelCoordinates[] leftGoalCoordinates;
	private PixelCoordinates[] rightGoalCoordinates;
	private PixelCoordinates ballCoordinates;

	/**
	 * @return the pitchCornerCoordinates
	 */
	public PixelCoordinates[] getPitchCornerCoordinates() {
        
        int leftBuffer = ImageProcessor.nwPos.x;
        int topBuffer = ImageProcessor.nePos.y;
        int rightBuffer = ImageProcessor.sePos.x;
        int bottomBuffer = ImageProcessor.swPos.y;
        
        boolean isThereBarrelCorrection = false;   // The 2 booleans needed in PixelCoordinates.
        boolean isOrientationCorrected = false;

        PixelCoordinates topLeftCorner = new PixelCoordinates(leftBuffer,topBuffer,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates topRightCorner = new PixelCoordinates(rightBuffer,topBuffer,isThereBarrelCorrection,isOrientationCorrected); 
        PixelCoordinates bottomLeftCorner = new PixelCoordinates(leftBuffer,bottomBuffer,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates bottomRightCorner = new PixelCoordinates(rightBuffer,bottomBuffer,isThereBarrelCorrection,isOrientationCorrected);
        
        PixelCoordinates[] results; 
        
        results = new PixelCoordinates [4]; 
        results[0] = topLeftCorner; 
        results[1] = topRightCorner;
        results[2] = bottomRightCorner;
        results[3] = bottomLeftCorner;
        
        System.out.println("DEBUG: Corners: "+results[0]+", "+results[1]+", "+results[2]+", "+results[3]);
        return results;
//        PixelCoordinates[] results; 
//        
//        results = new PixelCoordinates [4]; 
//        results[0] = new PixelCoordinates(0, 0, true, true); 
//        results[1] = new PixelCoordinates(640, 0, true, true);
//        results[2] = new PixelCoordinates(0, 480, true, true);
//        results[3] = new PixelCoordinates(640, 480, true, true);
//        
//        return results;
	}

	/**
	 * @param pitchCornerCoordinates the pitchCornerCoordinates to set
	 */
	public void setPitchCornerCoordinates(PixelCoordinates[] pitchCornerCoordinates) {
		this.pitchCornerCoordinates = pitchCornerCoordinates;
	}

	/**
	 * @return the yellowRobotCoordinates
	 */
	public PixelCoordinates getYellowRobotCoordinates() {
		if (yellowRobotCoordinates != null) {
			return yellowRobotCoordinates;
		} else {
//			System.err.println("NO YELLOW ROBOT COORDS");
			return new PixelCoordinates(0, 0, false, false);
		}
	}

	/**
	 * @param yellowRobotCoordinates the yellowRobotCoordinates to set
	 */
	public void setYellowRobotCoordinates(PixelCoordinates yellowRobotCoordinates) {
     //     PixelCoordinates  oldYellowRobotCoordinates = yellowRobotCoordinates;
//            System.out.print("x value " + oldYellowRobotCoordinates.getX() + " y value " + oldYellowRobotCoordinates.getY());
            int yellowX =yellowRobotCoordinates.getX(); 
            int yellowY = yellowRobotCoordinates.getY(); 
            boolean barrel = yellowRobotCoordinates.isBarrelCorrected();
            boolean orientation = yellowRobotCoordinates.isOrientationCorrected();



            double z = 250; 

            yellowX = (int) ((yellowX - 320)* ((z-20)/z)) + 320;
            yellowY = (int) ((yellowY - 240)* ((z-20)/z)) + 240;
            yellowRobotCoordinates.set(yellowX, yellowY,barrel, orientation);
            //System.err.println("(" + yellowRobotCoordinates.getX() + "," + yellowRobotCoordinates.getY()+")");

            this.yellowRobotCoordinates = yellowRobotCoordinates;
	}
    
	/**
	 * @return the yellowRobotOrientation
	 */
	public Direction getYellowRobotOrientation() {
		if (yellowRobotOrientation != null) {
			return yellowRobotOrientation;
		} else {
//			System.err.println("NO YELLOW ROBOT ORIENTATION");
			return new Direction(0);
		}
	}

	/**
	 * @param yellowRobotOrientation the yellowRobotOrientation to set
	 */
	public void setYellowRobotOrientation(Direction yellowRobotOrientation) {
		this.yellowRobotOrientation = yellowRobotOrientation;
	}

	/**
	 * @return the blueRobotCoordinates
	 */
	public PixelCoordinates getBlueRobotCoordinates() {
		if (blueRobotCoordinates != null) {
			return blueRobotCoordinates;
		} else {
//			System.err.println("NO BLUE ROBOT COORDS");
			return new PixelCoordinates(0, 0, false, false);
		}
	}

	/**
	 * @param blueRobotCoordinates the blueRobotCoordinates to set
	 */
	public void setBlueRobotCoordinates(PixelCoordinates blueRobotCoordinates) {
            int blueX =blueRobotCoordinates.getX(); 
            int blueY = blueRobotCoordinates.getY(); 
            boolean barrel = blueRobotCoordinates.isBarrelCorrected();
            boolean orientation = blueRobotCoordinates.isOrientationCorrected();



            double z = 250; 

            blueX = (int) ((blueX - 320)* ((z-20)/z)) + 320;
            blueY = (int) ((blueY - 240)* ((z-20)/z)) + 240;
            blueRobotCoordinates.set(blueX, blueY,barrel, orientation);
            this.blueRobotCoordinates = blueRobotCoordinates;
                
	}

	/**
	 * @return the blueRobotOrientation
	 */
	public Direction getBlueRobotOrientation() {
		if (blueRobotOrientation != null) {
			return blueRobotOrientation;
		} else {
//			System.err.println("NO BLUE ROBOT ORIENTATION");
			return new Direction(0);
		}
	}

	/**
	 * @param blueRobotOrientation the blueRobotOrientation to set
	 */
	public void setBlueRobotOrientation(Direction blueRobotOrientation) {
		this.blueRobotOrientation = blueRobotOrientation;
	}

	/**
	 * @return the leftGoalCoordinates
	 */
	public PixelCoordinates[] getLeftGoalCoordinates() {
		// First element is top coordinate, second is bottom.
        
        int leftBuffer = ImageProcessor.nwPos.x;
        int topBuffer = ImageProcessor.nePos.y;
        int rightBuffer = ImageProcessor.sePos.x;
        int bottomBuffer = ImageProcessor.swPos.y;
        
        boolean isThereBarrelCorrection = false;   // The 2 booleans needed in PixelCoordinates.
        boolean isOrientationCorrected = false; // If fisheye, etc, is implemented deal with these then.
        
        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = bottomBuffer - topBuffer;
        int topGoal = topBuffer + (goalWidth/4);
        int bottomGoal = bottomBuffer - (goalWidth/4);
        
        PixelCoordinates topLeftGoal = new PixelCoordinates(leftBuffer,topGoal,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates bottomLeftGoal = new PixelCoordinates(leftBuffer,bottomGoal,isThereBarrelCorrection,isOrientationCorrected); 
        
        PixelCoordinates[] leftGoalCoordinates; 
        
        leftGoalCoordinates = new PixelCoordinates [2]; 
        leftGoalCoordinates[0] = topLeftGoal; 
        leftGoalCoordinates[1] = bottomLeftGoal;
        
        return leftGoalCoordinates;
	}

	/**
	 * @param leftGoalCoordinates the leftGoalCoordinates to set
	 */
	public void setLeftGoalCoordinates(PixelCoordinates[] leftGoalCoordinates) {
		this.leftGoalCoordinates = leftGoalCoordinates;
	}

	/**
	 * @return the rightGoalCoordinates
	 */
	public PixelCoordinates[] getRightGoalCoordinates() {
		// First element is top coordinate, second is bottom.
        
        int leftBuffer = ImageProcessor.nwPos.x;
        int topBuffer = ImageProcessor.nePos.y;
        int rightBuffer = ImageProcessor.sePos.x;
        int bottomBuffer = ImageProcessor.swPos.y;
        
        boolean isThereBarrelCorrection = false;   // The 2 booleans needed in PixelCoordinates.
        boolean isOrientationCorrected = false; // If fisheye, etc, is implemented deal with these then.
        
        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = bottomBuffer - topBuffer;
        int topGoal = topBuffer + (goalWidth/4);
        int bottomGoal = bottomBuffer - (goalWidth/4);
        
        PixelCoordinates topRightGoal = new PixelCoordinates(rightBuffer,topGoal,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates bottomRightGoal = new PixelCoordinates(rightBuffer,bottomGoal,isThereBarrelCorrection,isOrientationCorrected); 
        
        PixelCoordinates[] rightGoalCoordinates; 
        
        rightGoalCoordinates = new PixelCoordinates [2]; 
        rightGoalCoordinates[0] = topRightGoal; 
        rightGoalCoordinates[1] = bottomRightGoal;
        
        return rightGoalCoordinates;
	}

	/**
	 * @param rightGoalCoordinates the rightGoalCoordinates to set
	 */
	public void setRightGoalCoordinates(PixelCoordinates[] rightGoalCoordinates) {
		this.rightGoalCoordinates = rightGoalCoordinates;
	}

	/**
	 * @return the ballCoordinates
	 */
	public PixelCoordinates getBallCoordinates() {
		if (ballCoordinates != null) {
			return ballCoordinates;
		} else {
			System.err.println("NO BALL COORDS");
			return new PixelCoordinates(0, 0, false, false);
		}
	}

	/**
	 * @param ballCoordinates the ballCoordinates to set
	 */
	public void setBallCoordinates(PixelCoordinates ballCoordinates) {
		this.ballCoordinates = ballCoordinates;
	}

}
