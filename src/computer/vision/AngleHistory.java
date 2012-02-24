package computer.vision;
import java.util.ArrayList;

/**
 * 
 * @author s0926369
 */

public class AngleHistory {
    
    //private ArrayList<Double> angleList;
    int listSize;
    
    public AngleHistory() {
        
        // Could modify the code to take arrays of size other than 5.
        //listSize = angleList.size();
        //if (listSize % 2 != 1) {
          //  angleList.remove(0);
         //   listSize = angleList.size();
        //}
        //this.angleList = angleList;
    }
    
    // Uses an ArrayList of the last 5 angles to find the average of these.
    public double getMean(ArrayList<Double> angleList) {
        
        double[] minMax = getMinMax(angleList);
        double minimum = minMax[0];
        double maximum = minMax[1];
        
        // If the difference is low we can get the mean easily.
        // If it's high then we need to do a lot more work!
        boolean bigDifference = false;
        if ((maximum - minimum) >= 180) {
            bigDifference = true;
        }
        
        double angleSum = 0;
        double angleMean = 0;
        
        // The mean here is for examples such as 10,30,..,50 or 300,310,..,340.
        if (bigDifference == false) {
            for (int i = 0; i < 5; i++) {
                angleSum += angleList.get(i);
            }
            angleMean = angleSum / 5;
        
        // This else is for examples such as 330,350,10,30,50.
        } else {
            // We need to see how many values are high and how many are low.
            int count = 0;
            for (int i = 0; i < 5; i++) {
                double currentValue = angleList.get(i);
                if (angleList.get(i) >= 180) {
                    count += 1;
                } else {
                    // We cheat by making values such as 10 into 370.
                    currentValue += 360;
                }
                angleSum += currentValue;
            }
            
            // We now get the mean dependent on how many values were low.
            if (count == 1 || count == 2) {
                // Example: 330,350,10,30,50.
                angleMean = (angleSum / 5) - 360;
            } else {
                // Example: 310,330,350,10,30.
                angleMean = angleSum / 5;
            }
            
            if (angleMean < 0) {
                angleMean += 360;
            }
            
            if (angleMean >= 360) {
                angleMean += -360;
            }
        }
        
        return angleMean;
    }
    
    // Just a method to return the minimum and maximum values from an array.
    public double[] getMinMax(ArrayList<Double> angleList) {
        
        double minimum = 361;
        double maximum = -1;
        
        for (int i = 0; i < 5; i++) {
            double currentValue = angleList.get(i);
            if (currentValue > maximum) {
                maximum = currentValue;
            } else if (currentValue < minimum) {
                minimum = currentValue;
            }
        }
        
        double[] minMax = {minimum, maximum};
        return minMax;
    }
}