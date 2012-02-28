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
    
    private double vX,vY;
    private double vXold,vYold;
    private double vXmax,vYmax;
    
    public Velocity(Coordinates previousPosition, Coordinates currentPosition, long timeDeltaInMilliseconds){
        double TIME_FACTOR=(double)timeDeltaInMilliseconds/1000.0;
        this.set((currentPosition.getX()-previousPosition.getX())/TIME_FACTOR,
                 (currentPosition.getY()-previousPosition.getY())/TIME_FACTOR);
    }
    
    public void recalculate(Coordinates previousPosition, Coordinates currentPosition, long timeDeltaInMilliseconds){
        double TIME_FACTOR=(double)timeDeltaInMilliseconds/1000.0;
        this.vXold=this.vX;
        this.vYold=this.vY;
        this.vX=(currentPosition.getX()-previousPosition.getX())/TIME_FACTOR;
        this.vY=(currentPosition.getY()-previousPosition.getY())/TIME_FACTOR;
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
    
    public double getYcomponent(){
        return this.vY;
    }
    
    public double getSpeed() {
        return Math.sqrt(Math.pow(getXcomponent(),2)+Math.pow(getYcomponent(), 2));
    }
    
    public double getXDistanceTravelled(long timeDeltaInMilliseconds){
        return this.getXcomponent()*((double)timeDeltaInMilliseconds/1000.0);
    }
    
    public double getYDistanceTravelled(long timeDeltaInMilliseconds){
        return this.getYcomponent()*((double)timeDeltaInMilliseconds/1000.0);
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
