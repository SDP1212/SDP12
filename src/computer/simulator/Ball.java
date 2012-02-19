/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 *
 * @author Dimo Petroff
 */
public class Ball extends SimulatableObject{
    
    private static final double BALL_KICK_SPEED=1/(1000.0/Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS);//in units per simulator tick
    
    protected Ball(){
        this(1f,0.5f,false);
    }

    protected Ball(double x, double y, boolean real) {
        this.position=new Coordinates(x, y);
        this.real=real;
    }
    
    protected Ball(Coordinates coordinates, boolean real){
        this.position=coordinates;
        this.real=real;
    }
    
    public void kick(Coordinates kickerPosition, Direction kickerOrientation){
        if(getPosition().distance(kickerPosition)<0.2)//closer than ~25cm
            if(Math.abs(LineTools.angleBetweenLineAndDirection(new Line(kickerPosition,position), kickerOrientation))<(Math.PI/4)){//ball within a 90 degree cone in front of kicker
                double xComponent,yComponent;
                xComponent=BALL_KICK_SPEED*Math.cos(kickerOrientation.getDirectionRadians());
                yComponent=BALL_KICK_SPEED*Math.sin(kickerOrientation.getDirectionRadians());
                this.v.alter(xComponent, yComponent);
            }
    }

    protected void animate(long timeDeltaInMilliseconds) {
        setPosition(position.getX()+v.getXDistanceTravelled(timeDeltaInMilliseconds),
                    position.getY()+v.getYDistanceTravelled(timeDeltaInMilliseconds));
    }
}
