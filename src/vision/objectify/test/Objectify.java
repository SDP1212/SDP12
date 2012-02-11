
import canny.CannyEdge;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dimo Petroff
 */
public class Objectify extends Component {

    //displays the image to the jFrame surface
    private static void reload(int[][] output) {
	Objectify obj=((Objectify)((JPanel)((JLayeredPane)((JRootPane)f.getComponents()[0]).getComponents()[1]).getComponents()[0]).getComponents()[0]);
	for(int x=0;x<output.length;x++)
	    for(int y=0;y<output[0].length;y++)
		obj.left.setRGB(x, y, (output[x][y]<<16)+(output[x][y]<<8)+(output[x][y]));
	obj.repaint();
    }
      
    BufferedImage left=null,right=null;

    @Override
    public void paint(Graphics g) {
        g.drawImage(left, 0, 0, null);
    }

    public Objectify() {
       try {
	    left = ImageIO.read(new File("left.jpg"));
//	    right = ImageIO.read(new File("right.jpg"));
       } catch (IOException e) {
       }

    }

    @Override
    public Dimension getPreferredSize() {
        if (left==null)// || right==null)
            return new Dimension(100,100);
        else 
            return new Dimension(left.getWidth(null), left.getHeight(null));
    }

    public static JFrame f = new JFrame("Load Image Sample");
    

    public static void main(String[] args) {
            
        f.addWindowListener(new WindowAdapter(){
	    @Override
                public void windowClosing(WindowEvent e) {
                    System.exit(0);
                }
            });
        f.add(new Objectify());
        f.pack();
        f.setVisible(true);
        
        try{
            BufferedImage img=ImageIO.read(new File("left.jpg"));
            long start=System.currentTimeMillis();
            int[][] temp=CannyEdge.detect(img, CannyEdge.NOISE_REDUCTION_NORMAL, CannyEdge.SOBEL_SCHARR,2);
            long finish=System.currentTimeMillis();
            System.out.println("Processed "+(((float)(img.getWidth()*img.getHeight()))/1000000)+" MPixels in "+(finish-start)+" milliseconds.");
            reload(temp);
        }catch (IOException e){}
    }
}