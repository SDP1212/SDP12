/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * This represents a goal as the coordinates of the two posts. Also defines
 * if this is the target goal or not.
 * 
 * @author Dimo Petroff
 */
public class Goal {
    
    private Coordinates upperPost,lowerPost;
    private Boolean isTarget=null;
    
    public Goal(double xUpper, double yUpper, double xLower, double yLower){
        this.upperPost=new Coordinates(xUpper, yUpper);
        this.lowerPost=new Coordinates(xLower, yLower);
    }

    protected void setTarget() {
        this.isTarget=Boolean.TRUE;
    }
    
    public boolean isTarget(){
        return this.isTarget==null ? false : this.isTarget;
    }
    
    public Coordinates getUpperPostCoordinates(){
        return this.upperPost;
    }
    
    public Coordinates getLowerPostCoordinates(){
        return this.lowerPost;
    }
	
	public Coordinates getCentre() {
		return new Coordinates(this.lowerPost.getX(), (this.upperPost.getY() - this.lowerPost.getY()) / 2);
	}

	@Override
	public String toString() {
		return upperPost.toString() + " " + lowerPost.toString();
	}
}
