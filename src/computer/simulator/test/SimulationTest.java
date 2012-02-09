/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator.test;

import computer.ApplicationController;
import computer.Communication;
import computer.ai.DumbAI;
import computer.ai.GoToBall;
import computer.simulator.Engine;
import computer.simulator.Pitch;
import computer.simulator.Robot;

/**
 * An example of how the simulator is intended to work. Not the only possible
 * way, but my favorite.
 * 
 * @author Dimo Petroff
 */
public class SimulationTest {
    
    public static void main(String[] args) throws InterruptedException{
        
        // Initialise vision, settings, communication, whatever else.
        Communication comm = new Communication();
        comm.connect();
        
        // Initialise simulator engine, providing the required settings, and start it in its own thread.
        Engine eng=new Engine(new FakeVision(), comm, false, true, true, Pitch.TARGET_LEFT_GOAL, Robot.BLUE_PLATE, GoToBall.class, DumbAI.class);
        Thread enginethread=new Thread(eng);
        enginethread.start();
        
        // Do stuff or wait for something.
        Thread.sleep(5000);
        
        // Stop simulator. Not sure if this is necessary when next action is to quit.
        enginethread.interrupt();
        
        // Quit or do some other stuff.
    }
    
}
