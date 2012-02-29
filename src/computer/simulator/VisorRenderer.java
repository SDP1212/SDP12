/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.simulator;

import java.awt.*;
import java.awt.geom.AffineTransform;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 *
 * @author Dimo Petroff
 */
public class VisorRenderer extends JPanel{
    
    private static final int RENDER_HEIGHT=300,RENDER_WIDTH=600;
    private static Dimension dimension=new Dimension(RENDER_WIDTH, RENDER_HEIGHT);
    private Shape[] robotinho,nemesis,ball,leftGoal,rightGoal;
    private Pitch pitch;
    private JFrame frame;
    
    public VisorRenderer(Pitch pitch){
        this.pitch=pitch;
        this.robotinho=pitch.robotinho.getVisualisation(RENDER_WIDTH, RENDER_HEIGHT);
        this.nemesis=pitch.nemesis.getVisualisation(RENDER_WIDTH, RENDER_HEIGHT);
        this.ball=pitch.ball.getVisualisation(RENDER_WIDTH, RENDER_HEIGHT);
        this.leftGoal=new Shape[] {new Rectangle((int)(pitch.getLeftGoal().getUpperPostCoordinates().getX()*RENDER_WIDTH/2),
                                                 (int)(RENDER_HEIGHT-pitch.getLeftGoal().getUpperPostCoordinates().getY()*RENDER_HEIGHT),
                                                 RENDER_WIDTH/50,
                                                 (int)((pitch.getLeftGoal().getUpperPostCoordinates().getY()-pitch.getLeftGoal().getLowerPostCoordinates().getY())*RENDER_HEIGHT))};
        this.rightGoal=new Shape[] {new Rectangle((int)(pitch.getRightGoal().getUpperPostCoordinates().getX()*RENDER_WIDTH/2)-RENDER_WIDTH/50,
                                                  (int)(RENDER_HEIGHT-pitch.getRightGoal().getUpperPostCoordinates().getY()*RENDER_HEIGHT),
                                                  RENDER_WIDTH/50,
                                                  (int)((pitch.getRightGoal().getUpperPostCoordinates().getY()-pitch.getRightGoal().getLowerPostCoordinates().getY())*RENDER_HEIGHT))};
        super.setMinimumSize(dimension);
        init();
    }
    
    private void init(){
        frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(this);
        frame.pack();
        frame.setVisible(true);
        dimension.height+=frame.getInsets().bottom+frame.getInsets().top;
        dimension.width+=frame.getInsets().left+frame.getInsets().right;
        frame.setMinimumSize(dimension);
    }
    
    @Override
    public void paint(Graphics graphics) {
        
        Graphics2D g=(Graphics2D)graphics;
        
//        g.drawLine(0, RENDER_HEIGHT/2, RENDER_WIDTH, RENDER_HEIGHT/2);
//        
//        g.drawLine(RENDER_WIDTH/2, 0, RENDER_WIDTH/2, RENDER_HEIGHT);
        
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, RENDER_WIDTH, RENDER_HEIGHT);
        
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(2f));
        if(pitch.getTargetGoal()==pitch.getLeftGoal())
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.BLACK);
        for(Shape s : leftGoal)
            g.draw(s);

        if(pitch.getTargetGoal()==pitch.getRightGoal())
            g.setColor(Color.BLUE);
        else
            g.setColor(Color.BLACK);
        for(Shape s : rightGoal)
            g.draw(s);

        g.setTransform(AffineTransform.getTranslateInstance(pitch.robotinho.position.getX()*RENDER_WIDTH/2, (1.0-pitch.robotinho.position.getY())*RENDER_HEIGHT));
        g.rotate(-pitch.robotinho.getOrientation().getDirectionRadians());
        g.setColor(Color.GREEN);
        for(Shape s : robotinho)
            g.draw(s);

        g.setTransform(AffineTransform.getTranslateInstance(pitch.nemesis.position.getX()*RENDER_WIDTH/2, (1.0-pitch.nemesis.position.getY())*RENDER_HEIGHT));
        g.rotate(-pitch.nemesis.getOrientation().getDirectionRadians());
        g.setColor(Color.RED);
        for(Shape s : nemesis)
            g.draw(s);

        g.setTransform(AffineTransform.getTranslateInstance(pitch.ball.position.getX()*RENDER_WIDTH/2, (1.0-pitch.ball.position.getY())*RENDER_HEIGHT));
        g.setColor(Color.RED);
        for(Shape s : ball)
            g.fill(s);

    }
    
    protected void kill(){
        frame.dispose();
    }
    
}
