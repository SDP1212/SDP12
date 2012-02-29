/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author s0926369
 */
public class Preprocessing {

    private PitchConstants pitchConstants;
    private ThresholdsState thresholdsState;

    public Preprocessing(PitchConstants pitchConstants, ThresholdsState thresholdstate) {
        this.pitchConstants = pitchConstants;
        this.thresholdsState = thresholdsState;
    }

    public BufferedImage fullNormalise(BufferedImage image) {

        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        int leftBuffer = pitchConstants.leftBuffer;
        int rightBuffer = pitchConstants.rightBuffer;

        ArrayList<Integer> yellowXPoints = new ArrayList<Integer>();
        ArrayList<Integer> yellowYPoints = new ArrayList<Integer>();

        ArrayList<Integer> blueXPoints = new ArrayList<Integer>();
        ArrayList<Integer> blueYPoints = new ArrayList<Integer>();

        ArrayList<Integer> redXPoints = new ArrayList<Integer>();
        ArrayList<Integer> redYPoints = new ArrayList<Integer>();

        ArrayList<Integer> greenXPoints = new ArrayList<Integer>();
        ArrayList<Integer> greenYPoints = new ArrayList<Integer>();

        double redModifier = 1.00;
        double greenModifier = 1.00;
        double blueModifier = 1.00;

        for (int x = leftBuffer; x < (640 - rightBuffer); x++) {
            for (int y = topBuffer; y < (480 - bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x, y));

                boolean isItYellow = isYellow(c);
                if (isItYellow) {
                    yellowXPoints.add(x);
                    yellowYPoints.add(y);
                }

                // The RGB values of a pixel.
                int pRed = (int) (redModifier * c.getRed());
                int pGreen = (int) (greenModifier * c.getGreen());
                int pBlue = (int) (blueModifier * c.getBlue());

                if (pRed > 255) {
                    pRed = 255;
                }

                if (pGreen > 255) {
                    pGreen = 255;
                }

                if (pBlue > 255) {
                    pBlue = 255;
                }

                int pSum = pRed + pGreen + pBlue;
                if (pSum == 0) {
                    pSum = 1;
                }
                int normRed = (int) ((255 * pRed) / pSum);
                int normGreen = (int) ((255 * pGreen) / pSum);
                int normBlue = (int) ((255 * pBlue) / pSum);

                Color temp = new Color(normRed, normGreen, normBlue);

                image.setRGB(x, y, 0xFFFFFFFF);

                if (isRed(temp)) {
                    image.setRGB(x, y, 0x00FF0000);
                    redXPoints.add(x);
                    redYPoints.add(y);
                }

                if (isBlue(temp)) {
                    image.setRGB(x, y, 0x000000FF);
                    blueXPoints.add(x);
                    blueYPoints.add(y);
                }

                if (isItYellow) {
                    image.setRGB(x, y, 0x00000000);
                }
            }
        }

        BlobDetection bd = new BlobDetection();

        // #####################################################################
        // Blobmethod Blue
        // #####################################################################
        BlobObject bo = bd.getCoordinatesOfBlob(blueXPoints, blueYPoints);
        ArrayList<Integer> newBlueXPoints = bo.xpoints;
        ArrayList<Integer> newBlueYPoints = bo.ypoints;
        int[] blueCentroid = getCentroid(newBlueXPoints, newBlueYPoints);

        try {
            double angle = getGeometricOrientation(newBlueXPoints, newBlueYPoints, blueCentroid[0], blueCentroid[1], image);
            //System.out.println("Blue Angle = " + angle);
        } catch (NoAngleException e) {
            // Comment.
        }

        // #####################################################################
        // Blobmethod Yellow
        // #####################################################################

        bo = bd.getCoordinatesOfBlob(yellowXPoints, yellowYPoints);
        ArrayList<Integer> newYellowXPoints = bo.xpoints;
        ArrayList<Integer> newYellowYPoints = bo.ypoints;
        int[] yellowCentroid = getCentroid(newYellowXPoints, newYellowYPoints);

        try {
            double angle = findOrientation(newYellowXPoints, newYellowYPoints, yellowCentroid[0], yellowCentroid[1], image, true);
            // System.out.println("Yellow Angle = " + angle);
        } catch (NoAngleException e) {
            // Comment.
        }

        // #####################################################################
        // Normal methods
        // #####################################################################

        //int[] blueCentroid = getCentroid(blueXPoints,blueYPoints);
        //int[] yellowCentroid = getCentroid(yellowXPoints,yellowYPoints);

        /*try {
        double angle = findOrientation(blueXPoints, blueYPoints, blueCentroid[0], blueCentroid[1], image, true);
        //System.out.println("Angle is " + angle);
        } catch (NoAngleException e) {
        // Comment.
        }*/

        /*try {
        double angle = findOrientation(yellowXPoints, yellowYPoints, yellowCentroid[0], yellowCentroid[1], image, true);
        System.out.println("Angle is " + angle);
        } catch (NoAngleException e) {
        // Comment.
        }*/

        // #####################################################################

        return image;
    }

    public boolean isRed(Color c) {

        int lowRed = 128;
        int highRed = 255;
        int lowGreen = 0;
        int highGreen = 70;
        int lowBlue = 0;
        int highBlue = 63;

        boolean isItRed = (c.getRed() >= lowRed && c.getRed() <= highRed)
                && (c.getGreen() >= lowGreen && c.getGreen() <= highGreen)
                && (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
        return isItRed;
    }

    public boolean isBlue(Color c) {
        /*
        float[] hsbvals = new float[3];
        c.RGBtoHSB(c.getRed(), c.getGreen(), c.getBlue(), hsbvals);
        
        return hsbvals[0] <= thresholdsState.getBlue_h_high() && hsbvals[0] >= thresholdsState.getBlue_h_low()
        && hsbvals[1] <= thresholdsState.getBlue_s_high() && hsbvals[1] >= thresholdsState.getBlue_s_low()
        && hsbvals[2] <= thresholdsState.getBlue_v_high() && hsbvals[2] >= thresholdsState.getBlue_v_low()
        && c.getRed() <= thresholdsState.getBlue_r_high() && c.getRed() >= thresholdsState.getBlue_r_low()
        && c.getGreen() <= thresholdsState.getBlue_g_high() && c.getGreen() >= thresholdsState.getBlue_g_low()
        && c.getBlue() <= thresholdsState.getBlue_b_high() && c.getBlue() >= thresholdsState.getBlue_b_low();
         */

        int lowRed = 32;
        int highRed = 92;
        int lowGreen = 64;
        int highGreen = 128;
        int lowBlue = 92;
        int highBlue = 255;

        boolean isItBlue = (c.getRed() >= lowRed && c.getRed() <= highRed)
                && (c.getGreen() >= lowGreen && c.getGreen() <= highGreen)
                && (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
        return isItBlue;

    }

    public boolean isYellow(Color c) {

        int lowRed = 200;
        int highRed = 255;
        int lowGreen = 200;
        int highGreen = 255;
        int lowBlue = 80;
        int highBlue = 160;

        /*int lowRed = 5;
        int highRed = 100;
        int lowGreen = 34;
        int highGreen = 102;
        int lowBlue = 0;
        int highBlue = 100;*/

        boolean isItYellow = (c.getRed() >= lowRed && c.getRed() <= highRed)
                && (c.getGreen() >= lowGreen && c.getGreen() <= highGreen)
                && (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
        return isItYellow;
    }

    public int[] getCentroid(ArrayList<Integer> xPoints, ArrayList<Integer> yPoints) {

        int xSum = 0;
        int ySum = 0;

        for (int i = 0; i < xPoints.size(); i++) {
            xSum += xPoints.get(i);
            ySum += yPoints.get(i);
        }

        int centroid[] = new int[2];

        try {
            centroid[0] = (int) (xSum / xPoints.size());
            centroid[1] = (int) (ySum / yPoints.size());
        } catch (Exception e) {
            centroid[0] = -1;
            centroid[1] = -1;
        }
        //System.out.println("DEBUG: X = " + centroid[0] + ", Y = " + centroid[1]);
        return centroid;
    }

    public double findOrientation(ArrayList<Integer> xPoints, ArrayList<Integer> yPoints,
            int meanX, int meanY, BufferedImage image, boolean showImage) throws NoAngleException {
        assert (xPoints.size() == yPoints.size()) :
                "Error: Must be equal number of x and y points!";

        Graphics imageGraphics = image.getGraphics();
        if (xPoints.size() == 0) {
            throw new NoAngleException("No T pixels");
        }

        int stdev = 0;
        /* Standard deviation */
        for (int i = 0; i < xPoints.size(); i++) {
            int x = xPoints.get(i);
            int y = yPoints.get(i);

            stdev += Math.pow(Math.sqrt(Position.sqrdEuclidDist(x, y, meanX, meanY)), 2);

        }
        stdev = (int) Math.sqrt(stdev / xPoints.size());
        System.out.println(stdev);

        /* Find the position of the front of the T. */
        int frontX = 0;
        int frontY = 0;
        int frontCount = 0;
        for (int i = 0; i < xPoints.size(); i++) {
            if (stdev > 15) { // Why "> 15" ?
                if (Math.abs(xPoints.get(i) - meanX) < stdev
                        && Math.abs(yPoints.get(i) - meanY) < stdev
                        && Position.sqrdEuclidDist(xPoints.get(i), yPoints.get(i), meanX, meanY) > Math.pow(15, 2)) {

                    frontCount++;
                    frontX += xPoints.get(i);
                    frontY += yPoints.get(i);
                }
            } else {
                if (Position.sqrdEuclidDist(xPoints.get(i), yPoints.get(i), meanX, meanY) > Math.pow(15, 2)) {
                    frontCount++;
                    frontX += xPoints.get(i);
                    frontY += yPoints.get(i);
                }
            }
        }

        if (frontCount == 0) {
            throw new NoAngleException("Front of T was not found");
        }

        frontX /= frontCount;
        frontY /= frontCount;

        imageGraphics.setColor(Color.black);
        imageGraphics.drawOval(frontX - 15, frontY - 15, 30, 30);
        imageGraphics.setColor(Color.blue);
        imageGraphics.drawOval(meanX - 15, meanY - 15, 30, 30);
        image.getGraphics().drawLine(frontX, frontY, meanX, meanY);

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

        return angleMF;
    }

    public boolean clusterChecker(int closeoneX, int closeoneY, int closetwoX, int closetwoY, int closethreeX, int closethreeY, int farX, int farY, String test) {

        //Check if the close ones are near on an x axis, y axis 
        if (Math.abs(closeoneX - closetwoX) <= 6 && Math.abs(closeoneX - closethreeX) <= 6 && Math.abs(closetwoX - closethreeX) <= 6 && Math.abs(closeoneX - farX) > 10) {

            if (distance(closeoneX, closeoneY, closetwoX, closetwoY) < distance(closeoneX, closeoneY, farX, farY)
                    && distance(closeoneX, closeoneY, closethreeX, closethreeY) < distance(closethreeX, closethreeY, farX, farY)
                    && distance(closetwoX, closetwoY, closethreeX, closethreeY) < distance(closetwoX, closetwoY, farX, farY)) {
//                System.out.println(test + " Cluster");
                return true;
            }
        }
        if (Math.abs(closeoneY - closetwoY) <= 6 && Math.abs(closeoneY - closethreeY) <= 6 && Math.abs(closetwoY - closethreeY) <= 6 && Math.abs(closeoneY - farY) > 10) {
            if (distance(closeoneX, closeoneY, closetwoX, closetwoY) < distance(closeoneX, closeoneY, farX, farY)
                    && distance(closeoneX, closeoneY, closethreeX, closethreeY) < distance(closethreeX, closethreeY, farX, farY)
                    && distance(closetwoX, closetwoY, closethreeX, closethreeY) < distance(closetwoX, closetwoY, farX, farY)) {
//                System.out.println(test + " Cluster");
                return true;
            }
        }
        return false;
    }

    public double getGeometricOrientation(ArrayList<Integer> xpoints, ArrayList<Integer> ypoints,
            int meanX, int meanY, BufferedImage image) throws NoAngleException {
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
                /*
                imageGraphics.setColor(Color.blue);
                imageGraphics.drawOval(meanX - 5, meanY - 5, 10, 10);
                imageGraphics.setColor(Color.yellow);
                imageGraphics.drawOval(frontPointX - 5, frontPointY - 5, 10, 10);
                imageGraphics.setColor(Color.red);
                imageGraphics.drawOval(backPointX - 5, backPointY - 5, 10, 10);
                 */
            }

            imageGraphics.drawOval(robotMaxX - 5, ypoints.get(xpoints.lastIndexOf(robotMaxX)) - 5, 10, 10);
            imageGraphics.setColor(Color.red);
            imageGraphics.drawOval(robotMinX - 5, ypoints.get(xpoints.lastIndexOf(robotMinX)) - 5, 10, 10);
            imageGraphics.setColor(Color.blue);
            imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMinY)) - 5, robotMinY - 5, 10, 10);
            imageGraphics.setColor(Color.black);
            imageGraphics.drawOval(xpoints.get(ypoints.lastIndexOf(robotMaxY)) - 5, robotMaxY - 5, 10, 10);


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
                System.out.println("Angle 1: " + angleMF + " Angle 2: " + angleFB + " Angle 3: " + angleMB);


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

    public double distance(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }
}
