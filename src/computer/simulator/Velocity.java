/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

/**
 * Used to represent velocity as an X component and Y component.
 * Measured in units per simulator tick.
 * 
 * @author Dimo Petroff
 */
public final class Velocity {
    
    public static final short SIMULATOR_TICK_LENGTH_IN_MILLISECONDS=Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS;    
    private double vX,vY;
    private double vXold,vYold;
    private double vXmax,vYmax;
    
    public Velocity(Coordinates previousPosition, Coordinates currentPosition, long timeDeltaInMilliseconds){
        double TIME_FACTOR=SIMULATOR_TICK_LENGTH_IN_MILLISECONDS/(double)timeDeltaInMilliseconds;
        this.set((currentPosition.getX()-previousPosition.getX())*TIME_FACTOR,
                 (currentPosition.getY()-previousPosition.getY())*TIME_FACTOR);
    }
    
    public void recalculate(Coordinates previousPosition, Coordinates currentPosition, long timeDeltaInMilliseconds){
        double TIME_FACTOR=SIMULATOR_TICK_LENGTH_IN_MILLISECONDS/(double)timeDeltaInMilliseconds;
        this.vXold=this.vX;
        this.vYold=this.vY;
        this.vX=(currentPosition.getX()-previousPosition.getX())*TIME_FACTOR;
        this.vY=(currentPosition.getY()-previousPosition.getY())*TIME_FACTOR;
        if(this.vX>this.vXmax)this.vXmax=this.vX;
        if(this.vY>this.vYmax)this.vYmax=this.vY;
    }
    
    public void set(double xComponent, double yComponent){
        this.vXmax=this.vXold=this.vX=xComponent;
        this.vYmax=this.vYold=this.vY=yComponent;
    }
    
    public void alter(double xComponent, double yComponent){
        this.vXold=this.vX;
        this.vYold=this.vY;
        this.vX+=xComponent;
        this.vY+=yComponent;
        if(this.vX>this.vXmax)this.vXmax=this.vX;
        if(this.vY>this.vYmax)this.vYmax=this.vY;
    }
    
    public double getXcomponent(){
        return this.vX;
    }
    
    public double getYcomonent(){
        return this.vY;
    }
    
    public double getXDistanceTravelled(long timeDeltaInMilliseconds){
        return this.getXcomponent()*((double)timeDeltaInMilliseconds/SIMULATOR_TICK_LENGTH_IN_MILLISECONDS);
    }
    
    public double getYDistanceTravelled(long timeDeltaInMilliseconds){
        return this.getYcomonent()*((double)timeDeltaInMilliseconds/SIMULATOR_TICK_LENGTH_IN_MILLISECONDS);
    }
    
    public double getPreviousXcomponent(){
        return this.vXold;
    }
    
    public double getPreviousYcomonent(){
        return this.vYold;
    }
    
    public double getMaximumObservedXcomponent(){
        return this.vXmax;
    }
    
    public double getMaximumObservedYcomponent(){
        return this.vYmax;
    }
    
}
