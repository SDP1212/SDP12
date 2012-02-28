/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator.test;

import computer.simulator.Direction;
import computer.simulator.PixelCoordinates;
import computer.simulator.VisionInterface;

/**
 * Just faking a vision interface for testing purposes.
 *
 * @author Dimo Petroff
 */
public class FakeVision implements VisionInterface{

    int i=0;
    
    @Override
    public PixelCoordinates[] getPitchCornerCoordinates() {
        return new PixelCoordinates[] {new PixelCoordinates(0,50,true,true),new PixelCoordinates(100,50,true,true),new PixelCoordinates(100,0,true,true),new PixelCoordinates(0,0,true,true)};
    }

    @Override
    public PixelCoordinates getYellowRobotCoordinates() {
        return new PixelCoordinates(25+i++,25+(int)(0.1*i), true, true);
    }

    @Override
    public Direction getYellowRobotOrientation() {
        return new Direction(0-0.001*i);
    }

    @Override
    public PixelCoordinates getBlueRobotCoordinates() {
        return new PixelCoordinates(75,25, true, true);
    }

    @Override
    public Direction getBlueRobotOrientation() {
        return new Direction((float)Math.PI);
    }

    @Override
    public PixelCoordinates[] getLeftGoalCoordinates() {
        return new PixelCoordinates[] {new PixelCoordinates(0,37,true,true),new PixelCoordinates(0,12,true,true)};
    }

    @Override
    public PixelCoordinates[] getRightGoalCoordinates() {
        return new PixelCoordinates[] {new PixelCoordinates(100,37,true,true),new PixelCoordinates(100,12,true,true)};
    }

    @Override
    public PixelCoordinates getBallCoordinates() {
        return new PixelCoordinates(50, 25, true, true);
    }
    
}
