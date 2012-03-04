/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;
import computer.simulator.PixelCoordinates;
import java.util.ArrayList;

/**
 *
 * @author s0926369
 * @author Dimo Petroff
 */

public class BlobDetection {
    
    private static int[][] pointMap;
    private static int[][] labels;
    private static int currentLabel,xRange,yRange;
    private static ArrayList<Integer> mass;
    
    public static ArrayList<PixelCoordinates> getBlob(ArrayList<PixelCoordinates> points) {

        if(points.isEmpty())return points;
        // Some variable declarations.
        int[] MinMax = getMinMax(points);

        int xMin = MinMax[0];
        int xMax = MinMax[1];
        int yMin = MinMax[2];
        int yMax = MinMax[3];

        // Add 1 to the range or bad things will happen.
        xRange = xMax - xMin + 1;
        yRange = yMax - yMin + 1;

        // Represents the 2d array as a 1d array.
        pointMap = new int[xRange][yRange];
        //System.out.println("Debug: LoP size is " + listOfPoints.length); 
        //System.out.println("Debug: xRange is " + xRange + ", yRange is " + yRange);
        
        // Loop through DETECTIONS.  Make the value of detections 1.  (Undetected is 0 by default properties of int)
        for (int i = 0; i < points.size(); i++)
                pointMap[points.get(i).getX()-xMin][points.get(i).getY()-yMin] = 1;

        // We can use the size of the mass array to list the number of blobs.
        // Each element in "mass" represents how many pixels are classified as a single blob.
        mass = new ArrayList<Integer>();
        
        // Gives index 0 (non-objects) a mass of -1, ensuring it's never the largest object
        // as long as another object (of >= 2 pixels) exists.
        mass.add(-1);

        // LabelTable stores all labels of all pixels.
        labels = new int[xRange][yRange];
        currentLabel = 0;

        for (int y = 0; y < yRange; y++) {
            for (int x = 0; x < xRange; x++) {
                // If the pixel is an object...
                if(pointMap[x][y]==1){
                    mark(x,y);
                    currentLabel++;
                    mass.add(0);
                }// else {
                    //System.out.println("Debug: Not an object pixel!");
                //}
            }
        }

        int largestBlob = getMaxBlob(mass);
        
        //for (int i = 0; i < massArray.size(); i++) {
        //    System.out.println("Index i = " + i + " has mass " + massArray.get(i));
        //}
        
//        System.out.println("#Blobs = " + massArray.size() + ".   Mass = " + massArray.get(largestBlob));

        ArrayList<PixelCoordinates> out=new ArrayList<PixelCoordinates>();
        for(PixelCoordinates pixel : points)
            if(labels[pixel.getX()-xMin][pixel.getY()-yMin] == largestBlob)
                out.add(pixel);
        return out;
    }
    
    private static void mark(int x, int y) {
        if (pointMap[x][y] != 1 || mass.get(currentLabel)>Math.pow(ImageProcessor.rayOfLight, 2)) 
            return;
        pointMap[x][y]=0;
        labels[x][y]=currentLabel;
        mass.set(currentLabel, mass.get(currentLabel)+1);
        if(x>0)
            mark(x-1, y);
        if(x>0 && y>0)
            mark(x-1, y-1);
        if(y>0)
            mark(x,   y-1);
        if(x<xRange-1 && y>0)
            mark(x+1, y-1);
        if(x<xRange-1)
            mark(x+1, y);
        if(x<xRange-1 && y<yRange-1)
            mark(x+1, y+1);
        if(y<yRange-1)
            mark(x,   y+1);
        if(x>0 && y<yRange-1)
            mark(x-1, y+1);
    }
    
    public static ArrayList<PixelCoordinates> getBlobOld(ArrayList<PixelCoordinates> points) {

        if(points.isEmpty())return points;
        // Some variable declarations.
        int[] MinMax = getMinMax(points);

        int xMin = MinMax[0];
        int xMax = MinMax[1];
        int yMin = MinMax[2];
        int yMax = MinMax[3];

        // Add 1 to the range or bad things will happen.
        int xRange = xMax - xMin + 1;
        int yRange = yMax - yMin + 1;

        // Represents the 2d array as a 1d array.
        int[][] pointMap = new int[xRange][yRange];
        //System.out.println("Debug: LoP size is " + listOfPoints.length); 
        //System.out.println("Debug: xRange is " + xRange + ", yRange is " + yRange);
        
        // Loop through DETECTIONS.  Make the value of detections 1.  (Undetected is 0 by default properties of int)
        for (int i = 0; i < points.size(); i++)
                pointMap[points.get(i).getX()-xMin][points.get(i).getY()-yMin] = 1;

        // We can use the size of the mass array to list the number of blobs.
        // Each element in "mass" represents how many pixels are classified as a single blob.
        ArrayList<Integer> massArray = new ArrayList<Integer>();
        
        // Gives index 0 (non-objects) a mass of -1, ensuring it's never the largest object
        // as long as another object (of >= 2 pixels) exists.
        massArray.add(-1);

        // LabelTable stores all labels of all pixels.
	int[][] labelTable = new int[xRange][yRange];
        int maxLabel = 1;

        for (int y = 0; y < yRange; y++) {
            for (int x = 0; x < xRange; x++) {
                // If the pixel is an object...
                if (pointMap[x][y] == 1) {
                    // If the pixel to left is an object and not at side of image...
                    if ((x != 0) && (pointMap[x-1][y] == 1)) {

                        labelTable[x][y] = labelTable[x-1][y];
                        massArray.set(labelTable[x][y], (massArray.get(labelTable[x-1][y])+1));
                        //System.out.println("Debug: Found pixel to left!" + labelTable[index]);

                    // Top Left
                    } else if ( ((y != 0) && (x!= 0)) && (pointMap[x-1][y-1] == 1)) {
                        
                        labelTable[x][y] = labelTable[x-1][y-1];
                        massArray.set(labelTable[x][y], (massArray.get(labelTable[x-1][y-1])+1));
                        //System.out.println("Debug: Found pixel above-left!" + labelTable[index]);
                        
                    // Above    
                    } else if ((y != 0) && (pointMap[x][y-1] == 1)) {
                        
                        labelTable[x][y] = labelTable[x][y-1];
                        massArray.set(labelTable[x][y], (massArray.get(labelTable[x][y-1])+1));
                        //System.out.println("Debug: Found pixel above!" + labelTable[index]);
                        
                    // Top Right
                    } else if ( ((y != 0) && (x!= xRange-1)) && (pointMap[x+1][y-1] == 1)) {    
                    
                        labelTable[x][y] = labelTable[x+1][y-1];
                        massArray.set(labelTable[x][y], (massArray.get(labelTable[x+1][y-1])+1));
                        //System.out.println("Debug: Found pixel above-right!" + labelTable[index]);
                        
                    // If there is at least one other point connected to object...
                    } else if ( ((x != (xRange-1)) && (pointMap[x+1][y] == 1)) ||
                                ((y != (yRange-1)) && (pointMap[x][y+1] == 1)) ||
                                ((x != 0) && (y != yRange-1) && (pointMap[x-1][y+1] == 1)) ||
                                ((x != xRange-1) && (y != yRange-1) && (pointMap[x+1][y+1] == 1))) {
                        labelTable[x][y] = maxLabel;
                        //System.out.println("Debug: Found new pixel!" + labelTable[index]);
                        maxLabel += 1;
                        massArray.add(1);
                    }
                }// else {
                    //System.out.println("Debug: Not an object pixel!");
                //}
            }
        }

        int largestBlob = getMaxBlob(massArray);
        
        //for (int i = 0; i < massArray.size(); i++) {
        //    System.out.println("Index i = " + i + " has mass " + massArray.get(i));
        //}
        
//        System.out.println("#Blobs = " + massArray.size() + ".   Mass = " + massArray.get(largestBlob));

        ArrayList<PixelCoordinates> out=new ArrayList<PixelCoordinates>();
        if(largestBlob >= 1)
            for(PixelCoordinates pixel : points)
                if(labelTable[pixel.getX()-xMin][pixel.getY()-yMin] == largestBlob)
                    out.add(pixel);
        return out;
    }

    // Returns the minimum and maximum xy coordinates.
    public static int[] getMinMax(ArrayList<PixelCoordinates> array) {
        int minX = 640;
        int maxX = 0;
        int minY = 480;
        int maxY = 0;
        for (PixelCoordinates current : array) {
            if(minX>current.getX())
                minX=current.getX();
            if(minY>current.getY())
                minY=current.getY();
            if(maxX<current.getX())
                maxX=current.getX();
            if(maxY<current.getY())
                maxY=current.getY();
        }
        return new int[] {minX,maxX,minY,maxY};
    }
    
    // Note: The offsets are xMin,yMin at one part of code and 0,0 at another.
    public int convert2Dto1D(int x, int y, int xOffset, int yOffset, int xRange) {
        return ((x-xOffset) + ((y-yOffset)*xRange));
    }
    
    // Finds the index of the largest blob.
    public static int getMaxBlob(ArrayList<Integer> massArray) {
        int maxValue = 0;
        int returnIndex = 0;
        for (int i = 0; i < massArray.size(); i++) {
            if (massArray.get(i) > maxValue) {
                maxValue = massArray.get(i);
                returnIndex = i;
            }
        }
        return returnIndex;
    }
    
    // Pass a value of 1 into this to get Ys, 0 to get Xs.
    public ArrayList<Integer> extractXorY(ArrayList<Integer> array, int x) {
        
        int realSize = array.size() / 2;
        ArrayList<Integer> returnArray = new ArrayList<Integer>();
        for (int i = 0; i < realSize; i++) {
            returnArray.add(array.get((2*i)+x));
        }
        return returnArray;
    }
}
