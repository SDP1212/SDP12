/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.control;

/**
 * Communication and Robot should implement this to ensure that robot behavior
 * can be simulated and executed consistently.
 * 
 * @author Dimo Petroff
 */
public interface ControlInterface {
    public static int DISCONNECTED = 0;
    public static int READY = 1;
    public static int WAITING = 2;
    public static int ERROR = 3;
    
    public void forward(int speed);
    
    public void backward(int speed);
    
    public void stop();
    
    public void kick();
    
    public void rotate (double angle);
    
    public int getCommState();
    
}
