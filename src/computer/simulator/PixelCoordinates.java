/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent a set of coordinates provided by the vision interface, and
 * their associated properties.
 * 
 * By convention, the top-left corner of the image is considered the origin,
 * X is the horizontal direction, growing right, and Y is the vertical
 * direction, growing down.
 * 
 * @author Dimo Petroff
 */
public final class PixelCoordinates {
    
    private int x,y;
    private boolean barrelCorrected, orientationCorrected;
    
    /**
     * Allocates a PixelCoordinates object, setting the x and y coordinates in
     * pixel-space. Must specify if these are corrected for barrel/fish-eye
     * distortion effect and table orientation.
     * 
     * @param x
     * @param y
     * @param barrelCorrected true specifies that the given coordinates have been corrected for barrel/fish-eye distortion
     * @param orientationCorrected true specifies that the given coordinates have been corrected so that the table is perfectly aligned in the x and y directions
     */
    public PixelCoordinates(int x, int y, boolean barrelCorrected, boolean orientationCorrected){
        this.set(x, y, barrelCorrected, orientationCorrected);
    }
    
    /**
     * Sets the x and y coordinates in pixel-space. Must specify if these are
     * corrected for barrel/fish-eye distortion effect and table orientation.
     * 
     * @param x
     * @param y
     * @param barrelCorrected true specifies that the given coordinates have been corrected for barrel/fish-eye distortion
     * @param orientationCorrected true specifies that the given coordinates have been corrected so that the table is perfectly aligned in the x and y directions
     */
    public void set(int x, int y, boolean barrelCorrected, boolean orientationCorrected){
        if(x<0 || y<0)
            throw new Error("FATAL ERROR: The given PixelCoordinates ("+x+","+y+") are invalid.");
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
    
}
