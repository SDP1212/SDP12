/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Dimo Petroff
 */
public class Ball extends SimulatableObject{
    
    private static final double BALL_KICK_SPEED=1/(1000.0/Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS);//in units per simulator tick
    
    protected Ball(double x, double y, boolean real) {
        this.setPosition(x, y);
        this.updateVelocity(1);
        this.real=real;
    }
    
    protected Ball(Coordinates coordinates, boolean real){
        this.setPosition(coordinates.getX(), coordinates.getY());
        this.updateVelocity(40);
        this.real=real;
    }
    
    public void kick(Coordinates kickerPosition, Direction kickerOrientation){
        if(getPosition().distance(kickerPosition)<Coordinates.distanceFromCentimetres(25)/2)//closer than ~25cm
            if(Math.abs(LineTools.angleBetweenLineAndDirection(new Line(kickerPosition,position), kickerOrientation))<(Math.PI/4)){//ball within a 90 degree cone in front of kicker
                double xComponent,yComponent;
                xComponent=BALL_KICK_SPEED*Math.cos(kickerOrientation.getDirectionRadians());
                yComponent=BALL_KICK_SPEED*Math.sin(kickerOrientation.getDirectionRadians());
                this.v.alter(xComponent, yComponent);
            }
    }

    @Override
    protected void animate(long timeDeltaInMilliseconds) {
        setPosition(position.getX()+v.getXDistanceTravelled(timeDeltaInMilliseconds),
                    position.getY()+v.getYDistanceTravelled(timeDeltaInMilliseconds));
    }

    @Override
    protected Shape[] getVisualisation(int width, int height) {
        double centerX=(Coordinates.distanceFromCentimetres(2))*width/2;
        double centerY=Coordinates.distanceFromCentimetres(2)*height;
        return new Shape[]{new Ellipse2D.Double(-centerX, -centerY, 2*centerX, 2*centerY)};
    }
}
