/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import vision.Position;

/**
 *
 * @author Dimo Petroff
 */
public final class PixelCoordinates {
    
    private Integer x,y;
    private boolean barrelCorrected, orientationCorrected;
    
    public PixelCoordinates(){
        this(null,null,false,false);
    }
    
    public PixelCoordinates(Integer x, Integer y, boolean barrelCorrected, boolean orientationCorrected){
        this.set(x, y, barrelCorrected, orientationCorrected);
    }
    
    /**
     * Sets the x and y coordinates in pixel-space. Must specify if these are corrected for barrel/fish-eye distortion effect and table orientation.
     * 
     * @param x
     * @param y
     * @param barrelCorrected true specifies that the given coordinates have been corrected for barrel/fish-eye distortion
     * @param orientationCorrected true specifies that the given coordinates have been corrected so that the table is perfectly aligned in the x and y directions
     */
    public void set(int x, int y, boolean barrelCorrected, boolean orientationCorrected){
        this.x=x;
        this.y=y;
        this.barrelCorrected=barrelCorrected;
        this.orientationCorrected=orientationCorrected;
    }
    
    public int getX(){
        return this.x;
    }

    public int getY(){
        return this.y;
    }
    
    public boolean isBarrelCorrected(){
        return barrelCorrected;
    }
    
    public boolean isOrientationCorrected(){
        return orientationCorrected;
    }
    
    public static double euclideanDistance(Coordinates p1, Coordinates p2) {
        return Math.sqrt(Math.pow(p1.getX() - p2.getX(), 2) + Math.pow(p1.getY() - p2.getY(), 2));
    }
    
}
