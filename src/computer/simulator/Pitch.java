/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Pitch {
    
    //should be counted clockwise, starting at top-left
    public PixelCoordinates[] corners;
    
    public Robot us;
    public Robot nemesis;
    
    public Ball ball;
    
    public Goal left,right;
    
    /*protected void insertUs(PixelCoordinates coordinates,Direction orientation){
        us=new Robot();
        
        if(!coordinates.isBarrelCorrected()){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        else if(!coordinates.isOrientationCorrected()){
            throw new UnsupportedOperationException("Not yet implemented");
        }
        else us.setPosition((corners[1].getX()-corners[0].getX()),(corners[3].getX()-corners[0].getX()));
        
        us.setOrientation(orientation);
    }*/
    
}
