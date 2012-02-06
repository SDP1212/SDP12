/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.ai.AI;

/**
 *
 * @author Dimo Petroff
 */
public class Robot extends SimulatableObject{
    
    public static final short YELLOW_PLATE=0,BLUE_PLATE=1;
    private Direction orientation;
    private short colour;
    protected AI brain;
    
    protected Robot(AI brain, Boolean real, short colour){
        this.brain=brain;
        this.real=real;
        this.colour=colour;
    }
    
    protected void setOrientation(Direction orientation) {
        this.orientation=orientation;
    }

    public short getColour() {
        return this.colour;
    }
    
}
