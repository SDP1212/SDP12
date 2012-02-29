/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;
import java.util.ArrayList;

/**
 *
 * @author s0926369
 */

public class BlobDetection {
    
    public BlobDetection() {
        // Default constructor
    }
    
    public BlobObject getCoordinatesOfBlob(ArrayList<Integer> xPoints, ArrayList<Integer> yPoints) {

        
        // Some variable declarations.
        int[] xMinMax = getMinMax(xPoints);
        int[] yMinMax = getMinMax(yPoints);

        int xMin = xMinMax[0];
        int xMax = xMinMax[1];
        int yMin = yMinMax[0];
        int yMax = yMinMax[1];

        // Add 1 to the range or bad things will happen.
        int xRange = xMax - xMin + 1;
        int yRange = yMax - yMin + 1;

        // Represents the 2d array as a 1d array.
        int[] listOfPoints = new int[xRange*yRange];
        //System.out.println("Debug: LoP size is " + listOfPoints.length); 
        //System.out.println("Debug: xRange is " + xRange + ", yRange is " + yRange);
        
        // Loop through DETECTIONS.  Make the value of detections 1.  (Undetected is 0 by default properties of int)
        for (int i = 0; i < xPoints.size(); i++) {
            try {
                int index = convert2Dto1D(xPoints.get(i),yPoints.get(i),xMin,yMin,xRange);
                //System.out.println("Debug: Index is " + index);
                listOfPoints[index] = 1;
            } catch (Exception e) {
                //
            }
        }

        // We can use the size of the mass array to list the number of blobs.
        // Each element in "mass" represents how many pixels are classified as a single blob.
        ArrayList<Integer> massArray = new ArrayList<Integer>();
        
        // Gives index 0 (non-objects) a mass of -1, ensuring it's never the largest object
        // as long as another object (of >= 2 pixels) exists.
        massArray.add(-1);

        // LabelTable stores all labels of all pixels.
	int[] labelTable = new int[xRange*yRange];
        int maxLabel = 1;

        for (int y = 0; y < yRange; y++) {
            for (int x = 0; x < xRange; x++) {
                int index = convert2Dto1D(x,y,0,0,xRange);
                //System.out.println("Debug: Index is " + index);
                // If the pixel is an object...
                if (listOfPoints[index] == 1) {
                    // If the pixel to left is an object and not at side of image...
                    if ((x != 0) && (listOfPoints[index-1] == 1)) {

                        labelTable[index] = labelTable[index-1];
                        massArray.set(labelTable[index], (massArray.get(labelTable[index-1])+1));
                        //System.out.println("Debug: Found pixel to left!" + labelTable[index]);

                    // Top Left
                    } else if ( ((y != 0) && (x!= 0)) && (listOfPoints[(index-xRange)-1] == 1)) {
                        
                        labelTable[index] = labelTable[(index-xRange)-1];
                        massArray.set(labelTable[index], (massArray.get(labelTable[(index-xRange)-1])+1));
                        //System.out.println("Debug: Found pixel above-left!" + labelTable[index]);
                        
                    // Above    
                    } else if ((y != 0) && (listOfPoints[index-xRange] == 1)) {
                        
                        labelTable[index] = labelTable[index-xRange];
                        massArray.set(labelTable[index], (massArray.get(labelTable[index-xRange])+1));
                        //System.out.println("Debug: Found pixel above!" + labelTable[index]);
                        
                    // Top Right
                    } else if ( ((y != 0) && (x!= xRange-1)) && (listOfPoints[(index-xRange)+1] == 1)) {    
                    
                        labelTable[index] = labelTable[(index-xRange)+1];
                        massArray.set(labelTable[index], (massArray.get(labelTable[(index-xRange)+1])+1));
                        //System.out.println("Debug: Found pixel above-right!" + labelTable[index]);
                        
                    // If there is at least one other point connected to object...
                    } else if ( ((x != (xRange-1)) && (listOfPoints[index+1] == 1)) ||
                                ((y != (yRange-1)) && (listOfPoints[index+xRange] == 1))  ||
                                (((x != 0) && (y != yRange-1)) && (listOfPoints[(index+xRange)-1] == 1)) ||
                                (((x != xRange-1) && (y != yRange-1)) && (listOfPoints[(index+xRange)+1] == 1)) ) {
                        
                        
                        labelTable[index] = maxLabel;
                        //System.out.println("Debug: Found new pixel!" + labelTable[index]);
                        maxLabel += 1;
                        massArray.add(1);
                    }
                } else {
                    //System.out.println("Debug: Not an object pixel!");
                }
            }
        }

        int largestBlob = getMaxBlob(massArray);
        
        //for (int i = 0; i < massArray.size(); i++) {
        //    System.out.println("Index i = " + i + " has mass " + massArray.get(i));
        //}
        
//        System.out.println("#Blobs = " + massArray.size() + ".   Mass = " + massArray.get(largestBlob));
        
        ArrayList<Integer> xBlobPoints = new ArrayList<Integer>();
        ArrayList<Integer> yBlobPoints = new ArrayList<Integer>();
        
        if (largestBlob >= 1) {
            for (int i = 0; i < labelTable.length; i++) {
                if (labelTable[i] == largestBlob) {
                    xBlobPoints.add((i % xRange) + xMin);
                    yBlobPoints.add((int) ((i / xRange) + yMin));
                }
            }
        // Adds a point if no object detected.
        } else {
            xBlobPoints.add(320);
            yBlobPoints.add(240);
        }
        
        BlobObject returner = new BlobObject(xBlobPoints, yBlobPoints);
        // Note! Order is: {x1,y1,x2,y2,..,xn,yn}
        //for (int i = 0; i < labelTable.length ; i++) { 
            
        //}
        return returner;
        
    }

    // Returns the minimum and maximum xy coordinates.
    public int[] getMinMax(ArrayList<Integer> array) {
        int minimum = 9001;
        int maximum = -1;
        for (int i = 0; i < array.size(); i++) {
            int currentValue = array.get(i);
            if (currentValue < minimum) {
                minimum = currentValue;
            } else if (currentValue > maximum) {
                maximum = currentValue;
            }
        }
        int[] minMax = new int[2];
        minMax[0] = minimum;
        minMax[1] = maximum;
        return minMax;
    }
    
    // Note: The offsets are xMin,yMin at one part of code and 0,0 at another.
    public int convert2Dto1D(int x, int y, int xOffset, int yOffset, int xRange) {
        return ((x-xOffset) + ((y-yOffset)*xRange));
    }
    
    // Finds the index of the largest blob.
    public int getMaxBlob(ArrayList<Integer> massArray) {
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
