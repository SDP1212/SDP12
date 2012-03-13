// 
// Dear SDP 2012 student,
// 
// Once you are done trying to 'optimize' this class,
// and have realized what a terrible mistake that was,
// please increment the following counter as a warning
// to the next guy:
// 
// total_hours_wasted_here = 29
// 

package computer.vision;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;
import java.awt.Color;
import java.awt.Point;
import java.awt.Transparency;
import java.awt.color.ColorSpace;
import java.awt.image.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

//import Planning.Main2;
//import Shared.ObjectInfo_Editable;

public class ImageProcessor {

	public static int DEBUG_LEVEL = 1;

	// ---Constants for Colour Accessing
	public static int RED = 0;
	public static int GREEN = 1;
	public static int BLUE = 2;

	// ---Reference Colours
	public static int[] redRef = new int[] {255,0,0};
	public static int[] yellRef = new int[] {255,255,0};
	public static int[] blueRef = new int[] {0,0,255};
    public static int[] grnRef = new int[] {0,255,0};

	protected static int height = 480;
	protected static int width = 640;

	// --- Barrel Distortion Correction
	private static double xFactor = 0;
    private static double yFactor = 0;

    public ImageProcessor(BufferedImage image) {
        this.image = image;
    }

	public static boolean useBarrelDistortion = false;
	public static boolean cameraView = true;
	public static boolean testmode = true;

	
	public static int xlowerlimit = 0;
	public static int xupperlimit = 630;
	public static int ylowerlimit = 85;
	public static int yupperlimit = 410;

	protected static double blueThreshold=87.5;
    protected static double blueRefThresh=12.5;
	protected static double yellThreshold=87.5;
    protected static double yellRefThresh=12.5;
    protected static double grnThreshold=12.5;

	protected static int searchdistance = 200;
	public static int rayOfLight = 35;


	// this will store object coordinates
	public static Point btPos = new Point(-1,-1);
	public static Point ytPos = new Point(-1,-1);
        public static Point nwPos = new Point(xlowerlimit+10,ylowerlimit+10);
        public static Point nePos = new Point(xupperlimit-10,ylowerlimit+10);
        public static Point sePos = new Point(xupperlimit-10,yupperlimit-10);
        public static Point swPos = new Point(xlowerlimit+10,yupperlimit-10);
	public static Point lastBallPos = new Point(-1,-1);

	LinkedList<Point> lines = new LinkedList<Point>();
	LinkedList<Integer> lineColor = new LinkedList<Integer>();
        Queue<Direction> bAngleChecker =new LinkedList<Direction>();
        Queue<Direction> yAngleChecker = new LinkedList<Direction>();



	BufferedImage image;
	protected static int mode = 5;
	private int[] blueAngles = new int[mode]; // method 1
	private int[] yellAngles = new int[mode]; // method 1
	private int lastYellPos = -1; // method 2
	private int lastBluePos = -1; // method 2
	static int method = 1; // method 1 for normalisation of 10 numbers, method 2
	// for last position checking.
        
    private static WorldState worldState;
    public static boolean ENABLE_NOISE_REDUCTION_FILTER=!true;


	// ~~~~~OUTPUT FOR OTHER TEAMS~~~~~~//
//	ObjectInfo_Editable objectInfos = new ObjectInfo_Editable();

	/**
	 * @param image
	 * @return image
	 */
	public BufferedImage process(BufferedImage image) {

        
        //reinit barrel correction
        if(useBarrelDistortion){
            xFactor=Math.atan(0.5)/((nePos.x-nwPos.x)/2.0);
            yFactor=Math.atan(((nePos.y-sePos.y)/2.0)/(nePos.x-nwPos.x))/((nePos.y-sePos.y)/2.0);
        }
//        System.err.print(blueThreshold+"\n"+blueRefThresh+"\n"+yellThreshold+"\n"+yellRefThresh+"\n");
        //		 create raster from given image
		Raster data;
		try {
			data = image.getData();
		} catch (NullPointerException e) {
			System.out.println(e.toString());
			return null;
		}

		Point ballPos = new Point(-1, -1);

		// create a new blank raster of the same size
		ColorSpace cs = ColorSpace.getInstance(ColorSpace.CS_sRGB);
		ColorModel cm = new ComponentColorModel(cs, false, false,
				Transparency.OPAQUE, DataBuffer.TYPE_BYTE);
		WritableRaster wraster = data.createCompatibleWritableRaster();

		// this will be used to find red pixel (one!)
		double closestRedFound = Double.POSITIVE_INFINITY;

		// this will be used to find blue robot centroid
		int btCentroidCount = 0;
		Point btCentroid = new Point(-1, -1);

		// this for yellow
		int ytCentroidCount = 0;
		Point ytCentroid = new Point(-1, -1);
		// ray of light processing
//               try {
//               System.err.print("x value " + worldState.getYellowRobotCoordinates().getX() + " y value " + worldState.getYellowRobotCoordinates().getY());
//
//               } 
//               catch (Exception e) {
               
//               }
        
		for (int i = xlowerlimit; i < xupperlimit; i = i + 1) { // for every
			for (int j = ylowerlimit; j < yupperlimit; j = j + 1) {
				if (((i % 2 == 0) || (j % 2 == 0)))
					continue;

				// get RGB values for a pixel
				int[] pixelColour = new int[3];
				getPixel(data, i, j, pixelColour);

				// finds how red is a pixel
				double ballDifference = getColourDifference(redRef, pixelColour);

				// barrel distortion
				Point out = new Point(i, j);

				// if converted pixel is still within pict then set it on the
				// new raster
				if (DEBUG_LEVEL < 4)
					drawPixel(wraster, out, pixelColour);

				// this will try to find the "reddest" point. this works better
				// for the ball than centroid
				if (ballDifference < closestRedFound) {
					closestRedFound = ballDifference;
					ballPos = out;
				}

				if (isGreen(pixelColour)) {
					if (DEBUG_LEVEL > 1)
						drawPixel(wraster, out, new int[] { 0, 255, 0 });
				}
			}
		}
        
        ArrayList<PixelCoordinates> yellowBlob=new ArrayList<PixelCoordinates>();
        ArrayList<PixelCoordinates> blueBlob=new ArrayList<PixelCoordinates>();
//        ArrayList<PixelCoordinates> redBlob=new ArrayList<PixelCoordinates>();
        
        for(int x=Math.max(btPos.x-rayOfLight,xlowerlimit);x<Math.min(btPos.x+rayOfLight,xupperlimit);x++)
            for(int y=Math.max(btPos.y-rayOfLight,ylowerlimit);y<Math.min(btPos.y+rayOfLight,yupperlimit);y++){
                int[] pixelColour = new int[3];
                getPixel(data, x, y, pixelColour);
                if(isBlue(pixelColour)){
                    blueBlob.add(new PixelCoordinates(x, y, useBarrelDistortion, false));
                    if (DEBUG_LEVEL > 1)
                        drawPixel(wraster,new Point(x, y),new int[] {0,0,127});
                }
            }

        for(int x=Math.max(ytPos.x-rayOfLight,xlowerlimit);x<Math.min(ytPos.x+rayOfLight,xupperlimit);x++)
            for(int y=Math.max(ytPos.y-rayOfLight,ylowerlimit);y<Math.min(ytPos.y+rayOfLight,yupperlimit);y++){
                int[] pixelColour = new int[3];
                getPixel(data, x, y, pixelColour);
                if(isYellow(pixelColour)){
                    yellowBlob.add(new PixelCoordinates(x, y, useBarrelDistortion, false));
                    if (DEBUG_LEVEL > 1)
                        drawPixel(wraster,new Point(x, y),new int[] {127,127,0});
                }
//                else if(isRed(pixelColour))redBlob.add(new PixelCoordinates(x, y, useBarrelDistortion, false));
            }
        
        yellowBlob=BlobDetection.getBlob(yellowBlob);
        blueBlob=BlobDetection.getBlob(blueBlob);
//        redBlob=BlobDetection.getBlob(redBlob);
        
        // Calculate Centroid of blue Co-ordinates
        for(PixelCoordinates pixel : blueBlob){
            // barrel distortion
            Point out = new Point(pixel.getX(), pixel.getY());
            if (DEBUG_LEVEL > 1)
                drawPixel(wraster, out, new int[] {0,0,255});
            btCentroidCount++;
            btCentroid.x += out.x;
            btCentroid.y += out.y;
            

        }
        
        // Calculate Centroid of yellow Co-ordinates
        for(PixelCoordinates pixel : yellowBlob){
            Point out = new Point(pixel.getX(), pixel.getY());
            if (DEBUG_LEVEL > 1)
                drawPixel(wraster, out, new int[] {255,255,0});
            ytCentroidCount++;
            ytCentroid.x += out.x;
            ytCentroid.y += out.y;
        }

		// where 5 is just some minimal number of pixels found
            if (btCentroidCount > 5) {
                btPos = new Point(btCentroid.x / btCentroidCount, btCentroid.y
                        / btCentroidCount);
                int btAngle = findAngle(data, wraster, btPos, blueRef);
//			System.out.println("Blue: (" + btPos.x + ", " + btPos.y +")");
                if (btPos.x >= 0 && btPos.y >= 0) {
                    Viewer.getWorldState().setBlueRobotCoordinates(new PixelCoordinates(btPos.x, btPos.y, useBarrelDistortion, false));
                }

                if (bAngleChecker.size() < 7) {
                    Viewer.getWorldState().setBlueRobotOrientation(new Direction(Math.toRadians(btAngle)));
                    bAngleChecker.add(new Direction(Math.toRadians(btAngle)));
                } else {
                    Viewer.getWorldState().setBlueRobotOrientation(new Direction(angleChecker(bAngleChecker, btAngle)));
                }

            }

            if (ytCentroidCount > 5) {
                ytPos = new Point(ytCentroid.x / ytCentroidCount, ytCentroid.y
                        / ytCentroidCount);
                int ytAngle = findAngle(data, wraster, ytPos, yellRef);
                if (ytPos.x >= 0 && ytPos.y >= 0) {
                    Viewer.getWorldState().setYellowRobotCoordinates(new PixelCoordinates(ytPos.x, ytPos.y, useBarrelDistortion, false));
                }
                
                if (yAngleChecker.size() < 7) {
                    Viewer.getWorldState().setYellowRobotOrientation(new Direction(Math.toRadians(ytAngle)));
                    yAngleChecker.add(new Direction(Math.toRadians(ytAngle)));
                } else {
                    Viewer.getWorldState().setYellowRobotOrientation(new Direction(angleChecker(yAngleChecker, ytAngle)));
                }

            }
		         
            findBall(wraster, ballPos);
            
            if (ballPos.x >= 0 && ballPos.y >= 0) {
                Viewer.getWorldState().setBallCoordinates(new PixelCoordinates(lastBallPos.x, lastBallPos.y, useBarrelDistortion, false));
            }
			
		int lineColors = 0;
		if (DEBUG_LEVEL > 0)
			for (int i = 0; i < lines.size(); i += 2) {
				try {
					Color c = new Color(lineColor.get(lineColors));
					lineColors++;
					int color[] = new int[] { c.getRed(), c.getGreen(),
							c.getBlue() };
					try {
						drawLine(wraster, (Point) lines.get(i), (Point) lines
								.get(i + 1), color);
					} catch (NullPointerException e) {

					}
				} catch (IndexOutOfBoundsException e) {
					//
				}
			}
        
        if(DEBUG_LEVEL>0){
            drawCross(wraster, btPos, blueRef);
            drawCross(wraster, ytPos, yellRef);
            drawLine(wraster, nwPos, nePos, new int[] {255,127,0});
            drawLine(wraster, nePos, sePos, new int[] {255,127,0});
            drawLine(wraster, sePos, swPos, new int[] {255,127,0});
            drawLine(wraster, swPos, nwPos, new int[] {255,127,0});
        }
        
        if(!(btPos.x==btPos.y && btPos.y==-1 || ytPos.x==ytPos.y && ytPos.y==-1)){
            int[] temp=new int[3];
			getPixel(data, btPos.x,btPos.y,temp);
            if(isRef(temp,blueRef,blueRefThresh)){
                blueRef[0]=temp[0];
                blueRef[1]=temp[1];
                blueRef[2]=temp[2];
            }
			temp=new int[3];
            getPixel(data, ytPos.x,ytPos.y,temp);
            if(isRef(temp,yellRef,yellRefThresh)){
                yellRef[0]=temp[0];
                yellRef[1]=temp[1];
                yellRef[2]=temp[2];
            }
        }

		BufferedImage img = new BufferedImage(cm, wraster, false, null);
		return img;
	}

	/**
	 * 
	 * @param raster
	 * @param ballPos
	 */
	private void findBall(WritableRaster raster, Point ballPos) {
		if (ballPos != null) { // if we can see the ball, initiate lastpos value
			if (lastBallPos.x == -1) {
				lastBallPos = ballPos;

			}
			// ignores the movement when it is higher than expected or very
			// small
			double ballmovement = Tools.getDistanceBetweenPoint(ballPos,
					lastBallPos);
			if (ballmovement < searchdistance && ballmovement > 3) {
				lastBallPos = ballPos;
			} else {
				ballPos = lastBallPos;
			}
			if (DEBUG_LEVEL > 0)
				drawCross(raster, ballPos, new int[] {255,0,0});
		} else {
			// if we can't see the ball
			if (lastBallPos.x != -1) {
				// try last values
				ballPos = lastBallPos;
				if (DEBUG_LEVEL > 0)
					drawCross(raster, lastBallPos, new int[] {255,0,0});
			}
			// and if we can't see the ball AND there are no previous values, do
			// nothing.
		}
	}

	/**
	 * This function attempts to find current robot rotation angle.
	 * 
	 * @param image
	 *            original image
	 * @param raster
	 *            writable raster
	 * @param p1
	 *            centre of the robot
	 * @param colour
	 *            of the robot
	 * @return angle in degrees
	 */
	private int findAngle(Raster image, WritableRaster raster, Point p1,
			int[] colour) {
		// x and y values of the centre of the robot
		int x = p1.x;
		int y = p1.y;
		// this defines x and y values of checkpoints around the centre of the
		// robot
		// they will be used to check if they are of an expected value,
		// specified in Ts (types)
		// 0 stands for 'T' sign colour (blue or yellow)
		// 1 stands for the dark spot
		// 2 stands for bright green
		int[] Xs = new int[] { x, x, x + 2, x - 2, x - 8, x + 8, x + 8, x - 8,
				x + 12, x - 12, x + 2, x - 2, x + 2, x - 2, x + 12, x + 12,
				x - 12, x - 12, x + 8, x - 8 };
		int[] Ys = new int[] { y, y - 8, y + 14, y + 14, y + 14, y + 14, y - 8,
				y - 8, y + 8, y + 8, y - 24, y - 24, y - 20, y - 20, y - 20,
				y + 18, y - 20, y + 18, y + 2, y + 2 };
		int[] Ts = new int[] { 0, 0, 0, 0, 2, 2, 0, 0, 2, 2, 1, 1, 1, 1, 2, 2,
				2, 2, 2, 2 };

		int maxscore = 0;
		int bestangle = -1;
		int len = Xs.length;
		int step3 = 1;

		// iterate over all 360 degrees, jumping by every 'step' degrees.
		// step=1 might be tempting, but this is not that accurate anyway
		for (int angle = 0; angle < 360; angle += step3) {
			int score = 0; // a score is given to each tested angle, later we
			// check which was best
			int dark = 0;
			for (int i = 0; i < len; i++) // iterate over all checkpoints
			{
				// create a new, rotated, temporary point
				Point tmp = Tools.rotatePoint(new Point(x, y), new Point(Xs[i],
						Ys[i]), angle);
				int[] pointcolour = new int[3];
				// try to read the colour of the pixel under our rotated
				// checkpoint
				try {
					getPixel(image, tmp.x, tmp.y, pointcolour);
				} catch (ArrayIndexOutOfBoundsException e) {
					continue; // just go to next
				}
				// see which type of pixel should it be
				switch (Ts[i]) {
				case 0: // T sign: blue/yell
					if (Arrays.equals(colour, blueRef)) {
						if (isBlue(pointcolour)) {
							score += 2;
						}
					} else {
						if (isYellow(pointcolour)) {
							score += 2;
						}
					}
					break;
				case 1: // dark dot
					int brightness = pointcolour[0] + pointcolour[1]
							+ pointcolour[2];
					if (brightness < 240) {
						score += 1;
						dark++;
					}
					break;
				case 2: // plate: bright green
					if (isGreen(pointcolour)) {
						score++;
					}
					break;
				default:
					break;
				}
			}
			// if checkpoints at this angle scored higher than current max,
			// update.
			if (score > maxscore) {
				maxscore = score;
				bestangle = angle;
			}
		}

		int ignoreStep = 8;
		if (Arrays.equals(colour, blueRef) && method == 1) {
			blueAngles = Tools.push(blueAngles, bestangle);
			bestangle = Tools.goodAvg(blueAngles);
		} else if (Arrays.equals(colour, yellRef) && method == 1) {
			yellAngles = Tools.push(yellAngles, bestangle);
			bestangle = Tools.goodAvg(yellAngles);
		} else if (Arrays.equals(colour, blueRef) && method == 2) {
			if (lastBluePos == -1) {
				lastBluePos = bestangle;
			} else if (lastBluePos + ignoreStep < bestangle
					|| lastBluePos - ignoreStep > bestangle) {
				lastBluePos = bestangle;
			} else {
				bestangle = lastBluePos;
			}
		} else if (Arrays.equals(colour, yellRef) && method == 2) {
			if (lastYellPos == -1) {
				lastYellPos = bestangle;
			} else if (lastYellPos + ignoreStep < bestangle
					|| lastYellPos - ignoreStep > bestangle) {
				lastYellPos = bestangle;
			} else {
				bestangle = lastYellPos;
			}
		}

		if (DEBUG_LEVEL > 2) {
			// this will mark the checkpoints
			for (int i = 0; i < len; i++) {
				Point tmp = Tools.rotatePoint(new Point(x, y), new Point(Xs[i],
						Ys[i]), bestangle);
				drawLittleCross(raster, tmp, new int[] {255,0,0});
			}
		}
		if (DEBUG_LEVEL > 0) {
			// this will dr95aw a box over the robot
			Point corner1 = Tools.rotatePoint(new Point(x, y), new Point(
					Xs[14], Ys[14] - 6), bestangle);
			Point corner2 = Tools.rotatePoint(new Point(x, y), new Point(
					Xs[15], Ys[15]), bestangle);
			Point corner3 = Tools.rotatePoint(new Point(x, y), new Point(
					Xs[16], Ys[16] - 6), bestangle);
			Point corner4 = Tools.rotatePoint(new Point(x, y), new Point(
					Xs[17], Ys[17]), bestangle);

			drawLine(raster, corner1, corner2, new int[] {255,0,0});
			drawLine(raster, corner2, corner4, new int[] {255,0,0});
			drawLine(raster, corner4, corner3, new int[] {255,0,0});
			drawLine(raster, corner3, corner1, new int[] {255,0,0});

			corner1 = Tools.rotatePoint(new Point(x + 1, y + 1), new Point(
					Xs[14], Ys[14] - 6), bestangle);
			corner2 = Tools.rotatePoint(new Point(x + 1, y + 1), new Point(
					Xs[15], Ys[15]), bestangle);
			corner3 = Tools.rotatePoint(new Point(x + 1, y + 1), new Point(
					Xs[16], Ys[16] - 6), bestangle);
			corner4 = Tools.rotatePoint(new Point(x + 1, y + 1), new Point(
					Xs[17], Ys[17]), bestangle);

			drawLine(raster, corner1, corner2, new int[] {255,0,0});
			drawLine(raster, corner2, corner4, new int[] {255,0,0});
			drawLine(raster, corner4, corner3, new int[] {255,0,0});
			drawLine(raster, corner3, corner1, new int[] {255,0,0});

			corner1 = Tools.rotatePoint(new Point(x - 1, y - 1), new Point(
					Xs[14], Ys[14] - 6), bestangle);
			corner2 = Tools.rotatePoint(new Point(x - 1, y - 1), new Point(
					Xs[15], Ys[15]), bestangle);
			corner3 = Tools.rotatePoint(new Point(x - 1, y - 1), new Point(
					Xs[16], Ys[16] - 6), bestangle);
			corner4 = Tools.rotatePoint(new Point(x - 1, y - 1), new Point(
					Xs[17], Ys[17]), bestangle);

			drawLine(raster, corner1, corner2, new int[] {255,0,0});
			drawLine(raster, corner2, corner4, new int[] {255,0,0});
			drawLine(raster, corner4, corner3, new int[] {255,0,0});
			drawLine(raster, corner3, corner1, new int[] {255,0,0});

			corner1 = Tools.rotatePoint(new Point(x + 1, y - 1), new Point(
					Xs[14], Ys[14] - 6), bestangle);
			corner2 = Tools.rotatePoint(new Point(x + 1, y - 1), new Point(
					Xs[15], Ys[15]), bestangle);
			corner3 = Tools.rotatePoint(new Point(x + 1, y - 1), new Point(
					Xs[16], Ys[16] - 6), bestangle);
			corner4 = Tools.rotatePoint(new Point(x + 1, y - 1), new Point(
					Xs[17], Ys[17]), bestangle);

			drawLine(raster, corner1, corner2, new int[] {255,0,0});
			drawLine(raster, corner2, corner4, new int[] {255,0,0});
			drawLine(raster, corner4, corner3, new int[] {255,0,0});
			drawLine(raster, corner3, corner1, new int[] {255,0,0});

			corner1 = Tools.rotatePoint(new Point(x - 1, y + 1), new Point(
					Xs[14], Ys[14] - 6), bestangle);
			corner2 = Tools.rotatePoint(new Point(x - 1, y + 1), new Point(
					Xs[15], Ys[15]), bestangle);
			corner3 = Tools.rotatePoint(new Point(x - 1, y + 1), new Point(
					Xs[16], Ys[16] - 6), bestangle);
			corner4 = Tools.rotatePoint(new Point(x - 1, y + 1), new Point(
					Xs[17], Ys[17]), bestangle);

			drawLine(raster, corner1, corner2, new int[] {255,0,0});
			drawLine(raster, corner2, corner4, new int[] {255,0,0});
			drawLine(raster, corner4, corner3, new int[] {255,0,0});
			drawLine(raster, corner3, corner1, new int[] {255,0,0});
		}
		
		return (360 + 270 - bestangle) % 360;

	}

	/*
	 * Basic Functions
	 */
	/**
	 * The following three functions check if a given colour is classified as
	 * bright green/yellow/blue respectively.
	 */
	boolean isGreen(int[] colour) {
		return (getColourDifference(grnRef, colour)<grnThreshold);
	}

	boolean isYellow(int[] colour) {
		return (getColourDifference(yellRef, colour)<yellThreshold);
	}

	boolean isBlue(int[] colour) {
		return (getColourDifference(blueRef, colour)<blueThreshold);
	}
    
    boolean isRef(int[] c1, int[] c2, double thresh){
        return (getColourDifference(c1, c2)<thresh);
    }

	/**
	 * Barrel Distortion Correction 'straightens' the distorted image.
	 * But doesn't quite work properly. But it is not really needed anyway.
	 * 
	 * @param p
	 *            point coordinates
	 * @return if useBarrelDistorion is false returns p1, otherwise, returns
	 *         adjusted coordinates
	 */
	public static Point debarrel(Point p) {
		if (!useBarrelDistortion)
			return p;
        
        double distance=Math.sqrt(Math.pow(p.x-320,2)+Math.pow(p.y-240,2));
        
        double xRatio=distance/((nePos.x-nwPos.x)*Math.tan(distance*xFactor));
        double yRatio=distance/((nePos.x-nwPos.x)*Math.tan(distance*yFactor));
        
        double x=(p.x-320)*xRatio;
        double y=(p.y-240)*yRatio;

		return new Point((int)(x+320), (int)(y+240));
	}

	/**
	 * Calculate difference between two colours
	 * 
	 * @param c1
	 * @param c2
	 * @return distance
	 */
	public double getColourDifference(int[] c1, int[] c2) {
		return Math.sqrt(
                Math.pow(c1[RED]-c2[RED],2)+
                Math.pow(c1[GREEN]-c2[GREEN],2)+
                Math.pow(c1[BLUE]-c2[BLUE],2));
	}

	/**
	 * Draw line from p1 to p2 of specified colour on given raster.
	 * 
	 * @param raster
	 *            draw on writable raster
	 * @param p1
	 *            start point
	 * @param p2
	 *            end point
	 * @param colour
	 *            - colour as three integers
	 */
	public void drawLine(WritableRaster raster, Point p1, Point p2, int[] colour) {
		int x0 = p1.x;
		int y0 = p1.y;
		int x1 = p2.x;
		int y1 = p2.y;
		int dx = x1 - x0;
		int dy = y1 - y0;
		drawPixel(raster, new Point(x0, y0), colour);
		if (dx != 0) {
			float m = (float) dy / (float) dx;
			float b = y0 - m * x0;
			dx = (x1 > x0) ? 1 : -1;
			while (x0 != x1) {
				x0 += dx;
				y0 = Math.round(m * x0 + b);
				drawPixel(raster, new Point(x0, y0), colour);
			}
		}
        else for(int i=Math.min(p1.y, p2.y);i<Math.max(p1.y, p2.y);i++)
            drawPixel(raster, new Point(p1.x,i), colour);
	}

	public void addLineToBeDrawn(Point p1, Point p2, int color) {
		lines.add(p1);
		lines.add(p2);
		lineColor.add(color);
	}

	/**
	 * A safe way to draw a pixel
	 * 
	 * @param raster
	 *            draw on writable raster
	 * @param p1
	 *            point coordinates
	 * @param colour
	 *            colour
	 */
	private void drawPixel(WritableRaster raster, Point p1, int[] colour) {
		if (p1.x >= 0 && p1.x < width && p1.y >= 0 && p1.y < height)
			raster.setPixel(p1.x, p1.y, colour);
	}

	/**
	 * Draws 2 perpendicular lines intersecting at point p1
	 * 
	 * @param raster
	 *            draw on writable raster
	 * @param p1
	 *            point coordinates
	 * @param colour
	 *            colour
	 */
	private void drawCross(WritableRaster raster, Point p1, int[] colour) {
		for (int i = 0; i < width; i++) {
			drawPixel(raster, new Point(i, p1.y), colour);
		}
		for (int i = 0; i < height; i++) {
			drawPixel(raster, new Point(p1.x, i), colour);
		}
	}

	/**
	 * The drawLittleCross function is for drawing a little cross.
	 * 
	 * @param raster
	 *            draw on writable raster
	 * @param p
	 *            point coordinates
	 * @param colour
	 *            colour
	 */
	private void drawLittleCross(WritableRaster raster, Point p, int[] colour) {
		drawPixel(raster, p, colour);
		drawPixel(raster, new Point(p.x + 1, p.y), colour);
		drawPixel(raster, new Point(p.x - 1, p.y), colour);
		drawPixel(raster, new Point(p.x, p.y + 1), colour);
		drawPixel(raster, new Point(p.x, p.y - 1), colour);
	}
    
        /*
         * Function for eliminating statistically unliking angles
         * Uses Chauvinets Criterium for detecting outliers
         * And the previous seven angles, stored in a Queue.
         * This method also takes into account the circularity of degrees
         * 
         *  @param angleChecker
         *              queue of previous seven angles
         * 
         *  @param angle
         *              the latest angle from vision
         */
    private double angleChecker(Queue<Direction> angleChecker, double angle) {
        
            angleChecker.poll();
            angleChecker.add(new Direction(Math.toRadians(angle)));
           
            boolean trigcheckone = false;
            boolean trigchecktwo = false;
            double sintotal = 0;
            double costotal = 0;
            for (Direction c : angleChecker) {
                //Check if we need to use sin/cos to figure out the standard deviation
                if (c.getDirectionDegrees() > 0 && c.getDirectionDegrees() < 90) {
                    trigcheckone = true;
                }
                if (c.getDirectionDegrees() < 360 && c.getDirectionDegrees() > 270) {
                    trigchecktwo = true;
                }
                sintotal += Math.sin(c.getDirectionRadians());
                costotal += Math.cos(c.getDirectionRadians());
            }

            sintotal /= angleChecker.size();
            costotal /= angleChecker.size();
            double average = Math.atan2(sintotal, costotal);
            if (average < 0) {
                average += 2 * Math.PI;
            }

            //Calculate the standard deviation one of two ways
            double stdevsin = 0;
            double stdevcos = 0;
            double stdev = 0;

            if (trigcheckone && trigchecktwo) {
                for (Direction c : angleChecker) {
                    //Calculate the standard deviation with sins and cosines
                    stdevsin += Math.pow(Math.sin(c.getDirectionRadians() - (average)), 2);
                    stdevcos += Math.pow(Math.cos(c.getDirectionRadians() - (average)), 2);
                }
                stdevsin /= (angleChecker.size() - 1);
                stdevcos /= (angleChecker.size() - 1);
                stdev = Math.toDegrees(Math.atan2(Math.sqrt(stdevsin), Math.sqrt(stdevcos)));
            } else {
                for (Direction c : angleChecker) {
                    //Calculate the standard deviation the normal way
                    stdev += Math.pow(c.getDirectionDegrees() - Math.toDegrees(average), 2);
                }
                stdev /= (angleChecker.size() - 1);
                stdev = Math.sqrt(stdev);
            }


            double probability = 0;
            double total = 0;
            sintotal = 0;
            costotal = 0;

            for (Direction c : angleChecker) {
                //Use Chauvenet's criterion
                if (Math.abs(c.getDirectionDegrees() - Math.toDegrees(average)) / stdev > 3) {
                    probability = 1 - .997;
                } else if (Math.abs(c.getDirectionDegrees() - Math.toDegrees(average)) / stdev > 2) {
                    probability = 1 - .95;
                } else if (Math.abs(c.getDirectionDegrees() - Math.toDegrees(average)) / stdev > 1) {
                    probability = 1 - .68;
                } else {
                    probability = .5;
                }
                if (probability * angleChecker.size() > .5) {
                    sintotal += Math.sin(c.getDirectionRadians());
                    costotal += Math.cos(c.getDirectionRadians());
                    total++;
                }
            }

            sintotal /= angleChecker.size();
            costotal /= angleChecker.size();
            average = Math.atan2(sintotal, costotal);
            if (average < 0) {
                average += 2 * Math.PI;
            }
            
        return average;
    }
    static final int[][] kernel=new int[][] {{ 2, 4, 5, 4, 2},
                                    { 4, 9,12, 9, 4},
                                    { 5,12,15,12, 5},
                                    { 4, 9,12, 9, 4},
                                    { 2, 4, 5, 4, 2}};
    private void getPixel(Raster data, int pixelX, int pixelY, int[] destination){
        if(destination.length<3)
            return;

        Point p=debarrel(new Point(pixelX, pixelY));
        pixelX=Math.max(xlowerlimit,Math.min(p.x,xupperlimit));
        pixelY=Math.max(ylowerlimit,Math.min(p.y,yupperlimit));
        if(!ENABLE_NOISE_REDUCTION_FILTER || 
                !((btPos.x-rayOfLight<pixelX && pixelX<btPos.x+rayOfLight &&
                   btPos.y-rayOfLight<pixelY && pixelY<btPos.y+rayOfLight) ||
                  (ytPos.x-rayOfLight<pixelX && pixelX<ytPos.x+rayOfLight &&
                   ytPos.y-rayOfLight<pixelY && pixelY<ytPos.y+rayOfLight))){
            

            data.getPixel(pixelX, pixelY, destination);
            return;
        }
        
        for(int i=0;i<destination.length;i++)
            destination[i]=0;
        int normalizer=0;
        int[] sample=new int[3];
        for(int x=Math.max(xlowerlimit,pixelX-kernel.length/2);x<=(pixelX+(kernel.length/2)) && x<xupperlimit;x++)
            for(int y=Math.max(ylowerlimit,pixelY-kernel[0].length/2);y<=(pixelY+(kernel[0].length/2)) && y<yupperlimit;y++){
                data.getPixel(x, y, sample);
                destination[RED]+=sample[RED]*kernel[x-(pixelX-kernel.length/2)][y-(pixelY-kernel[0].length/2)];
                destination[GREEN]+=sample[GREEN]*kernel[x-(pixelX-kernel.length/2)][y-(pixelY-kernel[0].length/2)];
                destination[BLUE]+=sample[BLUE]*kernel[x-(pixelX-kernel.length/2)][y-(pixelY-kernel[0].length/2)];
                normalizer+=kernel[x-(pixelX-kernel.length/2)][y-(pixelY-kernel[0].length/2)];
            }
        if(normalizer>0)
            for(int i=0;i<3;i++)
                destination[i]/=normalizer;
        
    }

	public ImageProcessor() {	}

}
