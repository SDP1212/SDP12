/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import computer.control.ControlInterface;

/**
 * Implements the entire simulator logic.
 * 
 * @author Dimo Petroff
 */
public class Engine implements Runnable{
    
    public static final short SIMULATOR_TICK_LENGTH_IN_MILLISECONDS=40;
    private boolean barrelEffectPresent=false,tableMisalignmentPresent=false;
    private static Pitch pitch=null;
    private VisionInterface vision;
    
    /**
     * Allocates a simulator Engine object and initializes it with
     * the appropriate settings.
     * 
     * @param vision a VisionInterface implementation, through which we get the real-world data
     * @param control a ControlInterface through which to send commands to the brick
     * @param simulateRobot true if the simulator should simulate Robotinho
     * @param simulateNemesis true if the adversary is virtual and needs to be simulated
     * @param simulateBall true if the ball is virtual and should be simulated
     * @param targetGoal indicates which goal is the target. Should be either Pitch.TARGET_LEFT_GOAL or Pitch.TARGET_RIGHT_GOAL
     * @param nemesisColour defines the color of nemesis' plate. Should be either Robot.BLUE_PLATE or Robot.YELLOW_PLATE
     * @param ourAI the class of AI that should be used for Robotinho's operation
     * @param nemesisAI the class of AI that should be used for nemesis' operation
     */
    public Engine(VisionInterface vision, ControlInterface control, boolean simulateRobot, boolean simulateNemesis, boolean simulateBall, short targetGoal, short nemesisColour, Class ourAI, Class nemesisAI){
        
        PixelCoordinates[] tempCoordinatesArray;
        PixelCoordinates tempCoordinates=null;
        Direction tempOrientation=null;
        
        // Obtain and check coordinates, and initialise pitch.
        tempCoordinatesArray=vision.getPitchCornerCoordinates();
        if(tempCoordinatesArray.length!=4)System.err.println("WARNING: Simulation has obtained suspect Pitch Corner coordinates from VisionInterface. Hopefully this can be ignored.");
        for(PixelCoordinates coordinates : tempCoordinatesArray)
            testPixelCoordinates(coordinates);
        this.pitch=new Pitch(tempCoordinatesArray);
        
        // Initialise Robotinho
        if(!simulateRobot){
            short ourColour;
            switch(nemesisColour){
                // If Nemesis is blue, then we're yellow.
                case Robot.BLUE_PLATE:{
                    tempCoordinates=vision.getYellowRobotCoordinates();
                    tempOrientation=vision.getYellowRobotOrientation();
                    ourColour=Robot.YELLOW_PLATE;
                    break;
                }
                // And vice versa.
                case Robot.YELLOW_PLATE:{
                    tempCoordinates=vision.getBlueRobotCoordinates();
                    tempOrientation=vision.getBlueRobotOrientation();
                    ourColour=Robot.BLUE_PLATE;
                    break;
                }
                default:throw new Error("SIMULATOR ERROR: "+nemesisColour+" is not a valid robot colour. The valid robot colours are 'Robot.BLUE_PLATE' and 'Robot.YELLOW_PLATE'!");
            }
            testPixelCoordinates(tempCoordinates);
            this.pitch.insertRobotinho(tempCoordinates, tempOrientation, ourColour, ourAI, control);
            
        }else{
            Coordinates tempPos;
            short ourColour;
            switch(nemesisColour){
                // If Nemesis is blue, then we're yellow.
                case Robot.BLUE_PLATE:{
                    ourColour=Robot.YELLOW_PLATE;
                    break;
                }
                // And vice versa
                case Robot.YELLOW_PLATE:{
                    ourColour=Robot.BLUE_PLATE;
                    break;
                }
                default:throw new Error("SIMULATOR ERROR: "+nemesisColour+" is not a valid robot colour. The valid robot colours are 'Robot.BLUE_PLATE' and 'Robot.YELLOW_PLATE'!");
            }
            switch(targetGoal){
                // If target is on the left, we must start on the right.
                case Pitch.TARGET_LEFT_GOAL:{
                    tempPos=new Coordinates(1.5f, 0.5f);
                    tempOrientation=new Direction(Math.PI);
                    break;
                }
                // And vice versa.
                case Pitch.TARGET_RIGHT_GOAL:{
                    tempPos=new Coordinates(0.5f, 0.5f);
                    tempOrientation=new Direction(0f);
                    break;
                }
                default:throw new Error("SIMULATOR ERROR: "+targetGoal+" is not a valid target identification. The valid targets are 'Pitch.TARGET_LEFT_GOAL' and 'Pitch.TARGET_RIGHT_GOAL'!");
            }
            this.pitch.insertRobotinhoInternal(tempPos, tempOrientation, ourColour, ourAI, control);
        }
        
        // Initialise nemesis
        if(!simulateNemesis){
            switch(nemesisColour){
                case Robot.BLUE_PLATE:{
                    tempCoordinates=vision.getBlueRobotCoordinates();
                    tempOrientation=vision.getBlueRobotOrientation();
                    break;
                }
                case Robot.YELLOW_PLATE:{
                    tempCoordinates=vision.getYellowRobotCoordinates();
                    tempOrientation=vision.getYellowRobotOrientation();
                    break;
                }
                default:throw new Error("SIMULATOR ERROR: "+nemesisColour+" is not a valid robot colour. The valid robot colours are 'Robot.BLUE_PLATE' and 'Robot.YELLOW_PLATE'!");
            }
            testPixelCoordinates(tempCoordinates);
                this.pitch.insertNemesis(tempCoordinates, tempOrientation, nemesisColour);            
        }else{
            Coordinates tempPos;
            switch(targetGoal){
                // If target is on the left, nemesis must start on the left.
                case Pitch.TARGET_LEFT_GOAL:{
                    tempPos=new Coordinates(0.5f, 0.5f);
                    tempOrientation=new Direction(0f);
                    break;
                }
                // Analogous.
                case Pitch.TARGET_RIGHT_GOAL:{
                    tempPos=new Coordinates(1.5f, 0.5f);
                    tempOrientation=new Direction(Math.PI);
                    break;
                }
                default:throw new Error("SIMULATOR ERROR: "+targetGoal+" is not a valid target identification. The valid targets are 'Pitch.TARGET_LEFT_GOAL' and 'Pitch.TARGET_RIGHT_GOAL'!");
            }
            this.pitch.insertNemesisInternal(tempPos, tempOrientation, nemesisColour, nemesisAI);
        }
        
        // Initialise ball
        if(!simulateBall){
            tempCoordinates=vision.getBallCoordinates();
            testPixelCoordinates(tempCoordinates);
            this.pitch.insertBall(tempCoordinates);
        }else{
            this.pitch.insertBallInternal(new Coordinates(1f, 0.5f));
        }
        
        // Initialise goals
        tempCoordinatesArray=new PixelCoordinates[4];
        System.arraycopy(vision.getLeftGoalCoordinates(), 0, tempCoordinatesArray, 0, 2);
        System.arraycopy(vision.getRightGoalCoordinates(), 0, tempCoordinatesArray, 2, 2);
        for(PixelCoordinates coordinates : tempCoordinatesArray)
            testPixelCoordinates(coordinates);
        this.pitch.insertGoals(new PixelCoordinates[]{tempCoordinatesArray[0],tempCoordinatesArray[1]},
                               new PixelCoordinates[]{tempCoordinatesArray[2],tempCoordinatesArray[3]},
                               targetGoal);
        
        // Remember VisionInterface
        this.vision=vision;
    }
    
    private void testPixelCoordinates(PixelCoordinates coordinates){
        if(!barrelEffectPresent && !coordinates.isBarrelCorrected()){
            System.err.println("WARNING: Coordinates from vision have barrel distortion. Calculations may not be accurate.");
//            switch(javax.swing.JOptionPane.showConfirmDialog(null, "Barrel-distorted coordinates may induce undesirable side-effects such as itching, sweating, irritation, and/or death.\nTo put it more clearly, this makes it impossible to predic objects' motion so no attempt will be made. The robot will only know where things are (approximately), the direction they're facing and the current speed. Considering motion would not be in a straight line, the latter two probably won't be too helpful but it's better than nothing.\n\nWould you still like to continue? Click \"Yes,\" I dare you. No, I double-dare you.", "Don't panic!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE)){
//
//                case javax.swing.JOptionPane.NO_OPTION:{
//                    throw new Error("The operator has chickened out of using barrel-distorted coordinates. But this is a good thing since those are NOT optimally supported by the (simulator) system!");
//                }
//                    
//                case javax.swing.JOptionPane.YES_OPTION:{
                    barrelEffectPresent=true;
//                    break;
//                }
//                    
//            }
        }
        if(!tableMisalignmentPresent && !coordinates.isOrientationCorrected()){
//            switch(javax.swing.JOptionPane.showConfirmDialog(null, "The provided coordinates may include a slight rotational misalignment of the table. This should not reduce performance in any significant way. You should carry on, ignoring this message.\n\nWould you like to continue?", "Do NOT panic!", javax.swing.JOptionPane.YES_NO_OPTION, javax.swing.JOptionPane.WARNING_MESSAGE)){
//                
//                case javax.swing.JOptionPane.NO_OPTION:{
//                    throw new Error("The operator has chickened out of using a misaligned table... What a shame.");
//                }
//                    
//                case javax.swing.JOptionPane.YES_OPTION:{
                    tableMisalignmentPresent=true;
//                    break;
//                }
//            }
        }
    }
    
    
    /**
     * This method implements the entire simulator logic. For an example of
     * intended use, refer to simulator.test.SimulationTest
     */
    @Override
    public void run(){
        long lastRun=0,thisRun;
        // TODO: Remove next line for production version:
        int i=0;
        while(true){
            
            // stop running if interrupted
            if(Thread.interrupted())return;
            
            // Make sure this only runs at most once every 40 milliseconds - the camera framerate is 25Hz, so there will be no new information to process more frequently.
            if((thisRun=System.currentTimeMillis())<=lastRun+Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS)try{
                Thread.sleep(Engine.SIMULATOR_TICK_LENGTH_IN_MILLISECONDS-(thisRun-lastRun));
            }catch(InterruptedException e) {return;} // stop if interrupted
            lastRun=System.currentTimeMillis();
            
            // Process Robotinho
            pitch.robotinho.brain.run();
            if(pitch.robotinho.isReal()){
                PixelCoordinates newCoordinates;
                Direction newDirection;
                switch(pitch.robotinho.getColour()){
                    case Robot.BLUE_PLATE:{
                        newCoordinates=vision.getBlueRobotCoordinates();
                        newDirection=vision.getBlueRobotOrientation();
                        break;
                    }
                    case Robot.YELLOW_PLATE:{
                        newCoordinates=vision.getYellowRobotCoordinates();
                        newDirection=vision.getYellowRobotOrientation();
                        break;
                    }
                    default:throw new Error("FATAL ERROR: Robotinho has invalid colour plate.");
                }
                pitch.updateRobotinho(newCoordinates, newDirection, thisRun-lastRun);
                //TODO: recalculate trajectory
            }else{
                pitch.robotinho.updateVelocity(thisRun-lastRun);
                pitch.robotinho.animate(thisRun-lastRun);
                //TODO: recalculate trajectory
            }
            
            // Process Nemesis
            if(pitch.nemesis.isReal()){
                PixelCoordinates newCoordinates;
                Direction newDirection;
                switch(pitch.nemesis.getColour()){
                    case Robot.BLUE_PLATE:{
                        newCoordinates=vision.getBlueRobotCoordinates();
                        newDirection=vision.getBlueRobotOrientation();
                        break;
                    }
                    case Robot.YELLOW_PLATE:{
                        newCoordinates=vision.getYellowRobotCoordinates();
                        newDirection=vision.getYellowRobotOrientation();
                        break;
                    }
                    default:throw new Error("FATAL ERROR: Nemesis has invalid colour plate.");
                }
                pitch.updateNemesis(newCoordinates, newDirection, thisRun-lastRun);
                //TODO: recalculate trajectory
            }else{
                pitch.nemesis.brain.run();
                pitch.nemesis.updateVelocity(thisRun-lastRun);
                pitch.nemesis.animate(thisRun-lastRun);
                //TODO: recalculate trajectory
            }
            
            // Process ball
            if(pitch.ball.isReal()){
                pitch.updateBall(vision.getBallCoordinates(),thisRun-lastRun);
                // TODO: recalculate intercept and trajectory
            }else{
                // ball-robot collisions
                Double collisionAngle=null;
                if(pitch.ball.getPosition().distance(pitch.robotinho.getPosition())<0.17)//about 21 cm
                    collisionAngle=LineTools.angleBetweenLineAndDirection(new Line(pitch.robotinho.getPosition(),pitch.ball.getPosition()), new Direction(0));
                if(pitch.ball.getPosition().distance(pitch.nemesis.getPosition())<0.17)//about 21 cm
                    collisionAngle=LineTools.angleBetweenLineAndDirection(new Line(pitch.nemesis.getPosition(),pitch.ball.getPosition()), new Direction(0));
                if(collisionAngle!=null)
                    pitch.ball.getV().set(0.04*Math.cos(collisionAngle), 0.04*Math.sin(collisionAngle));
                
                // ball-wall collisions
                double ballX=pitch.ball.getPosition().getX(),ballY=pitch.ball.getPosition().getY();
                Velocity ballV=pitch.ball.getV();
                if(ballX<0){
                    pitch.ball.setPosition(-ballX, ballY);
                    ballV.set(-ballV.getXcomponent(), ballV.getYcomonent());
                }
                if(ballX>2){
                    pitch.ball.setPosition(2-(ballX-2), ballY);
                    ballV.set(-ballV.getXcomponent(), ballV.getYcomonent());
                }
                if(ballY<0){
                    pitch.ball.setPosition(ballX, -ballY);
                    ballV.set(ballV.getXcomponent(), -ballV.getYcomonent());
                }
                if(ballY>1){
                    pitch.ball.setPosition(ballX, 1-(ballY-1));
                    ballV.set(ballV.getXcomponent(), -ballV.getYcomonent());
                }
                pitch.ball.animate(thisRun-lastRun);
                
                // TODO: simulate ball behaviour. and uncomment below.
                //pitch.ball.updateVelocity();
            }
            
            // TODO: Remove next two lines for production version:
            i++;
//            System.out.println("Simulation iteration "+i+" complete.");
        }
    }
    
}
