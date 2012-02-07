/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Goal {
    
    private Coordinates upperPost,lowerPost;
    private Boolean isTarget=null;
    
    public Goal(float xUpper, float yUpper, float xLower, float yLower){
        this.upperPost=new Coordinates(xUpper, yUpper);
        this.lowerPost=new Coordinates(xLower, yLower);
    }

    protected void setTarget() {
        this.isTarget=Boolean.TRUE;
    }
    
    public boolean isTarget(){
        return this.isTarget==null ? false : this.isTarget;
    }
    
    public float getUpperPostCoordinates(){
        return this.upperPost.getY();
    }
    
    public float getLowerPostCoordinates(){
        return this.lowerPost.getY();
    }
}
