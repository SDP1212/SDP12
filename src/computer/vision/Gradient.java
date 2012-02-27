/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dimo Petroff
 */
public class Gradient {
    
    public int[][] magnitude=null;
    public int[][] direction=null;
    public double[][] direction2=null;
    
    public Gradient(int x,int y){
        this.magnitude=new int[x][y];
        this.direction=new int[x][y];
        this.direction2=new double[x][y];
    }
    
}
