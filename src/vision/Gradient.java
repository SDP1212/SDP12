package vision;

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
    
    public Gradient(int x,int y){
        this.magnitude=new int[x][y];
        this.direction=new int[x][y];
    }
    
}
