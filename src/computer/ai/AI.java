/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import computer.simulator.Coordinates;
import computer.simulator.Pitch;
import computer.simulator.Robot;
import java.util.ArrayList;

/**
 * The Robot's decision-making should extend this so that it can properly interface with simulation.
 *
 * @author Dimo Petroff
 */
public abstract class AI implements Runnable{

    protected Pitch pitch=null;
    protected Robot self=null;
    protected ArrayList<Coordinates> actionPlan;

    /**
     * This should be the only constructor. Subclasses should call this, so that the simulator can provide a reference to the pitch for easy access to the current state of the simulation/world/whatever.
     * 
     * @param pitch a reference to the pitch
     * @param self a reference to the robot being controlled
     */
    public AI(Pitch pitch, Robot self){
        this.pitch=pitch;
        this.self=self;
    }
    
    /**
     * This method should implement the decision-making process. It is called by
     * the simulator once the real world has been processed into the internal
     * coordinates format and any necessary trajectories have been computed.
     * 
     * This should prepare an "action plan" of opcodes which can be executed by
     * the simulator or the brick.
     * N.B. The simulator implements and interprets the same opcodes as
     * the brick for use in simulated robots.
     */
    public abstract void run();
    
    public ArrayList<Coordinates> getActionPlan(){
        return actionPlan;
    }
}
