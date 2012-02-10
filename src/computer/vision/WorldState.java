package computer.vision;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;

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
	private float blueOrientation;
	private float yellowOrientation;
	private long counter;
  
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
		this.blueOrientation = 0;
		this.yellowOrientation = 0;
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

	public float getBlueOrientation() {
                //System.out.println("Blue orientation: " + blueOrientation);
		return blueOrientation;
	}

	public void setBlueOrientation(float blueOrientation) {
            //System.out.println("Blue orientation: " + blueOrientation);
		this.blueOrientation = blueOrientation;
	}

	public float getYellowOrientation() {
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
        int rightBuffer = pitchConstants.rightBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        
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
        results[2] = bottomLeftCorner;
        results[3] = bottomRightCorner;
        
        return results;
    }

    public PixelCoordinates getYellowRobotCoordinates() {
        return new PixelCoordinates(getYellowX(), getYellowY(), false, false);
    }

    public Direction getYellowRobotOrientation() {
        return new Direction(getYellowOrientation());
    }

    public PixelCoordinates getBlueRobotCoordinates() {
        return new PixelCoordinates(getBlueX(), getBlueY(), false, false);
    }

    public Direction getBlueRobotOrientation() {
        //this is in radians
        //System.out.println("blue or " + getBlueOrientation() );
        return new Direction(getBlueOrientation());
    }

    public PixelCoordinates[] getLeftGoalCoordinates() {
        // First element is top coordinate, second is bottom.
        
        PitchConstants pitchConstants = new PitchConstants(pitch);
        
        int leftBuffer = pitchConstants.leftBuffer;
        int topBuffer = pitchConstants.topBuffer;
        int rightBuffer = pitchConstants.rightBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        
        boolean isThereBarrelCorrection = false;   // The 2 booleans needed in PixelCoordinates.
        boolean isOrientationCorrected = false; // If fisheye, etc, is implemented deal with these then.
        
        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = topBuffer - bottomBuffer;
        int topGoal = Math.abs(topBuffer - (goalWidth/4));
        int bottomGoal = Math.abs(bottomBuffer + (goalWidth/4));
        
        PixelCoordinates topLeftGoal = new PixelCoordinates(leftBuffer,topGoal,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates bottomLeftGoal = new PixelCoordinates(leftBuffer,bottomGoal,isThereBarrelCorrection,isOrientationCorrected); 
        
        PixelCoordinates[] leftGoalCoordinates; 
        
        leftGoalCoordinates = new PixelCoordinates [2]; 
        leftGoalCoordinates[0] = topLeftGoal; 
        leftGoalCoordinates[1] = bottomLeftGoal;
        
        return leftGoalCoordinates;
        
    }

    public PixelCoordinates[] getRightGoalCoordinates() {
        // First element is top coordinate, second is bottom.
        
        PitchConstants pitchConstants = new PitchConstants(pitch);
        
        int leftBuffer = pitchConstants.leftBuffer;
        int topBuffer = pitchConstants.topBuffer;
        int rightBuffer = pitchConstants.rightBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        
        boolean isThereBarrelCorrection = false;   // The 2 booleans needed in PixelCoordinates.
        boolean isOrientationCorrected = false; // If fisheye, etc, is implemented deal with these then.
        
        // The goal sides are 4ft (120cm) - goal width 2ft(60cm): ration is 1:2:1 (4).
        // Thus we divide the width by 4 and add the ratio to the bottom.
        // And subtract the ratio from the top.
        int goalWidth = topBuffer - bottomBuffer;
        int topGoal = Math.abs(topBuffer - (goalWidth/4));
        int bottomGoal = Math.abs(bottomBuffer + (goalWidth/4));
        
        PixelCoordinates topRightGoal = new PixelCoordinates(rightBuffer,topGoal,isThereBarrelCorrection,isOrientationCorrected);
        PixelCoordinates bottomRightGoal = new PixelCoordinates(rightBuffer,bottomGoal,isThereBarrelCorrection,isOrientationCorrected); 
        
        PixelCoordinates[] rightGoalCoordinates; 
        
        rightGoalCoordinates = new PixelCoordinates [2]; 
        rightGoalCoordinates[0] = topRightGoal; 
        rightGoalCoordinates[1] = bottomRightGoal;
        
        return rightGoalCoordinates;
    }

    public PixelCoordinates getBallCoordinates() {
        return new PixelCoordinates(getBallX(), getBallY(), false, false);
    }
	
}
