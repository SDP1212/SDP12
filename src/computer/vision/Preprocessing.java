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

import javax.swing.JFrame;
import javax.swing.JLabel;
/**
 *
 * @author s0926369
 */
public class Preprocessing {
    
    private PitchConstants pitchConstants;
    
    public Preprocessing(PitchConstants pitchConstants) { 
        this.pitchConstants = pitchConstants;
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
        
        for (int x = leftBuffer; x < (640-rightBuffer); x++) {
            for (int y = topBuffer; y < (480-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x,y));

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
                int normRed = (int) ((255*pRed)/pSum);
                int normGreen = (int) ((255*pGreen)/pSum);
                int normBlue = (int) ((255*pBlue)/pSum);
                
                Color temp = new Color(normRed,normGreen,normBlue);
                
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
        
        ArrayList<Integer> newBluePoints = bd.getCoordinatesOfBlob(blueXPoints, blueYPoints);
        ArrayList<Integer> newBlueXPoints = bd.extractXorY(newBluePoints,0);
        ArrayList<Integer> newBlueYPoints = bd.extractXorY(newBluePoints,1);
        int[] blueCentroid = getCentroid(newBlueXPoints,newBlueYPoints);
        
        try {
            double angle = findOrientation(newBlueXPoints, newBlueYPoints, blueCentroid[0], blueCentroid[1], image, true);
            //System.out.println("Blue Angle = " + angle);
        } catch (NoAngleException e) {
            // Comment.
        }
        
        // #####################################################################
        // Blobmethod Yellow
        // #####################################################################
        
        ArrayList<Integer> newYellowPoints = bd.getCoordinatesOfBlob(yellowXPoints,yellowYPoints);
        ArrayList<Integer> newYellowXPoints = bd.extractXorY(newYellowPoints,0);
        ArrayList<Integer> newYellowYPoints = bd.extractXorY(newYellowPoints,1);
        int[] yellowCentroid = getCentroid(newYellowXPoints,newYellowYPoints);
        
        try {
            double angle = findOrientation(newYellowXPoints, newYellowYPoints, yellowCentroid[0], yellowCentroid[1], image, true);
            System.out.println("Yellow Angle = " + angle);
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
        
        boolean isItRed = (c.getRed() >= lowRed && c.getRed() <= highRed) && 
                          (c.getGreen() >= lowGreen && c.getGreen() <= highGreen) && 
                          (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
        return isItRed;
    }
    
    public boolean isBlue(Color c) {
        
        int lowRed = 32;
        int highRed = 92;
        int lowGreen = 64;
        int highGreen = 128;
        int lowBlue = 92;
        int highBlue = 255;
        
        boolean isItBlue = (c.getRed() >= lowRed && c.getRed() <= highRed) && 
                          (c.getGreen() >= lowGreen && c.getGreen() <= highGreen) && 
                          (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
        return isItBlue;
    }
    
    public boolean isYellow(Color c) {
        
        int lowRed = 110;
        int highRed = 220;
        int lowGreen = 100;
        int highGreen = 190;
        int lowBlue = 30;
        int highBlue = 150;
        
        /*int lowRed = 5;
        int highRed = 100;
        int lowGreen = 34;
        int highGreen = 102;
        int lowBlue = 0;
        int highBlue = 100;*/
        
        boolean isItYellow = (c.getRed() >= lowRed && c.getRed() <= highRed) && 
                          (c.getGreen() >= lowGreen && c.getGreen() <= highGreen) && 
                          (c.getBlue() >= lowBlue && c.getBlue() <= highBlue);
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


        /* Find the position of the front of the T. */
        int frontX = 0;
        int frontY = 0;
        int frontCount = 0;
        for (int i = 0; i < xPoints.size(); i++) {
            if (stdev > 17 && stdev < 19  ) { // Why "> 15" ?
                if (Math.abs(xPoints.get(i) - meanX) < stdev &&
                    Math.abs(yPoints.get(i) - meanY) < stdev &&
                    Position.sqrdEuclidDist(xPoints.get(i), yPoints.get(i), meanX, meanY) > Math.pow(15, 2)) {
                    
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
}
