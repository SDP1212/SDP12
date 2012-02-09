/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.ai;

import computer.simulator.Pitch;
import computer.simulator.Robot;

/**
 * This AI does nothing. Nothing at all. It is intended as a placeholder
 * when a real nemesis exists on the pitch, so we can't know what it's thinking
 * or planning to do. All we can hope for in this case is to be able to compute
 * the nemesis' velocity and trajectory as events unfold in the real world.
 * 
 * @author Dimo Petroff
 */
public class DumbAI extends AI{
    
    public DumbAI(Pitch pitch, Robot self){
        super(pitch,self);
    }

    @Override
    public void run(){}
    
}
