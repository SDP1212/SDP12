/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 *
 * @author s0926369, s0Lavanya
 */
public class ShadowProcessing {
    
    private PitchConstants pitchConstants;
    
    public ShadowProcessing (PitchConstants pitchc) { 
        this.pitchConstants = pitchc;
    } 
    
    public BufferedImage sideNormalise(BufferedImage image) {

        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        int leftBuffer = pitchConstants.leftBuffer;
        int rightBuffer = pitchConstants.rightBuffer;
        
        double redModifier = 1.10;
        double greenModifier = 1.00;
        double blueModifier = 1.40;
        
	int widthRange = 8;
	int heightRange = 32;

        for (int x = leftBuffer; x < (640-rightBuffer); x++) {
            for (int y = topBuffer; y < (480-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x,y));

		if ( (x < leftBuffer+widthRange || x > 640-(rightBuffer+widthRange)) ||
		    ( y < topBuffer+heightRange || y > 480-(bottomBuffer+heightRange)) ) {

		    // The RGB values of a pixel.
                    int pRed = (int) (redModifier * c.getRed());
                    int pGreen = (int) (greenModifier * c.getGreen());
                    int pBlue = (int) (blueModifier * c.getBlue());

                    if (pRed > 255) {
                        pRed = 255;
                    }

                    if (pGreen > 255) {
                        pGreen = 255;
                    }

                    if (pBlue > 255) {
                        pBlue = 255;
                    }

                    int pSum = pRed + pGreen + pBlue;

                    int normRed = (int) ((255*pRed)/pSum);
                    int normGreen = (int) ((255*pGreen)/pSum);
                    int normBlue = (int) ((255*pBlue)/pSum);

                    Color temp = new Color(normRed,normGreen,normBlue);
                    int normColour = temp.getRGB();
                    image.setRGB(x,y,normColour);
                }
            }
        }
        return image;
    }
    
    public BufferedImage fullNormalise(BufferedImage image) {
        
        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        int leftBuffer = pitchConstants.leftBuffer;
        int rightBuffer = pitchConstants.rightBuffer;
        
        double redModifier = 1.00;
        double greenModifier = 1.00;
        double blueModifier = 1.00;
        
        for (int x = leftBuffer; x < (640-rightBuffer); x++) {
            for (int y = topBuffer; y < (480-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x,y));

                // The RGB values of a pixel.

                int pRed = (int) (redModifier * c.getRed());
                int pGreen = (int) (greenModifier * c.getGreen());
                int pBlue = (int) (blueModifier * c.getBlue());

                if (pRed > 255) {
                    pRed = 255;
                }

                if (pGreen > 255) {
                    pGreen = 255;
                }

                if (pBlue > 255) {
                    pBlue = 255;
                }

                int pSum = pRed + pGreen + pBlue;

                int normRed = (int) ((255*pRed)/pSum);
                int normGreen = (int) ((255*pGreen)/pSum);
                int normBlue = (int) ((255*pBlue)/pSum);

                Color temp = new Color(normRed,normGreen,normBlue);
                int normColour = temp.getRGB();
                image.setRGB(x,y,normColour);
            }
        }
    return image;
    }
    
    public BufferedImage shadowRemoval(BufferedImage image, int[] rgbArray) {
        
        int topBuffer = pitchConstants.topBuffer;
        int bottomBuffer = pitchConstants.bottomBuffer;
        int leftBuffer = pitchConstants.leftBuffer;
        int rightBuffer = pitchConstants.rightBuffer;

        int rgb_redMean = rgbArray[0];
        int rgb_greenMean = rgbArray[1];
        int rgb_blueMean = rgbArray[2];
        
        for (int x = leftBuffer; x < (640-rightBuffer); x++) {
            for (int y = topBuffer; y < (topBuffer+30); y++) {
                Color c = new Color(image.getRGB(x,y));
                int pixelRed = c.getRed();
                int pixelGreen = c.getGreen();
                int pixelBlue = c.getBlue();

                int differenceRed = pixelRed - rgb_redMean; 
                int differenceGreen = pixelGreen - rgb_greenMean; 
                int differenceBlue = pixelBlue - rgb_blueMean; 
                
                if ((Math.abs(differenceRed) > 20) && (Math.abs(differenceRed) < 40)){
                    pixelRed = pixelRed - differenceRed;
                }
                
                if ((Math.abs(differenceGreen) > 20) && (Math.abs(differenceGreen) < 40)) {
                    pixelGreen = pixelGreen - differenceGreen;
                }
                
                if ((Math.abs(differenceBlue) > 20) && (Math.abs(differenceBlue) < 40)) {
                    pixelBlue = pixelBlue - differenceBlue;
                }

                Color temp = new Color(pixelRed,pixelGreen,pixelBlue);
                int meanColour = temp.getRGB();
                image.setRGB(x,y,meanColour);
            }
        }
        
        for (int x = leftBuffer; x < (640-rightBuffer); x++) {
            for (int y = (480-bottomBuffer-30); y < (480-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x, y));
                int pixelRed = c.getRed();
                int pixelGreen = c.getGreen();
                int pixelBlue = c.getBlue();

                int differenceRed = pixelRed - rgb_redMean; 
                int differenceGreen = pixelGreen - rgb_greenMean; 
                int differenceBlue = pixelBlue - rgb_blueMean; 
                
                if ((Math.abs(differenceRed) > 20) && (Math.abs(differenceRed) < 40)){
                    pixelRed = pixelRed - differenceRed;
                }
                
                if ((Math.abs(differenceGreen) > 20) && (Math.abs(differenceGreen) < 40)) {
                    pixelGreen = pixelGreen - differenceGreen;
                }
                
                if ((Math.abs(differenceBlue) > 20) && (Math.abs(differenceBlue) < 40)) {
                    pixelBlue = pixelBlue - differenceBlue;
                }

                Color temp = new Color(pixelRed,pixelGreen,pixelBlue);
                int meanColour = temp.getRGB();
                image.setRGB(x,y,meanColour);
            }
        }
        
        for (int x = leftBuffer; x < (leftBuffer + 30); x++) {
            for (int y = bottomBuffer; y < (480-topBuffer-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x, y));
                int pixelRed = c.getRed();
                int pixelGreen = c.getGreen();
                int pixelBlue = c.getBlue();

                int differenceRed = pixelRed - rgb_redMean; 
                int differenceGreen = pixelGreen - rgb_greenMean; 
                int differenceBlue = pixelBlue - rgb_blueMean; 
                
                if ((Math.abs(differenceRed) > 20) && (Math.abs(differenceRed) < 40)){
                    pixelRed = pixelRed - differenceRed;
                }
                
                if ((Math.abs(differenceGreen) > 20) && (Math.abs(differenceGreen) < 40)) {
                    pixelGreen = pixelGreen - differenceGreen;
                }
                
                if ((Math.abs(differenceBlue) > 20) && (Math.abs(differenceBlue) < 40)) {
                    pixelBlue = pixelBlue - differenceBlue;
                }

                Color temp = new Color(pixelRed,pixelGreen,pixelBlue);
                int meanColour = temp.getRGB();
                image.setRGB(x,y,meanColour);
            }
        }
        
        for (int x = (640-rightBuffer-30); x < (640-rightBuffer); x++) {
            for (int y = bottomBuffer; y < (480-topBuffer-bottomBuffer); y++) {
                Color c = new Color(image.getRGB(x, y));
                int pixelRed = c.getRed();
                int pixelGreen = c.getGreen();
                int pixelBlue = c.getBlue();

                int differenceRed = pixelRed - rgb_redMean; 
                int differenceGreen = pixelGreen - rgb_greenMean; 
                int differenceBlue = pixelBlue - rgb_blueMean; 
                
                if ((Math.abs(differenceRed) > 20) && (Math.abs(differenceRed) < 40)){
                    pixelRed = pixelRed - differenceRed;
                }
                
                if ((Math.abs(differenceGreen) > 20) && (Math.abs(differenceGreen) < 40)) {
                    pixelGreen = pixelGreen - differenceGreen;
                }
                
                if ((Math.abs(differenceBlue) > 20) && (Math.abs(differenceBlue) < 40)) {
                    pixelBlue = pixelBlue - differenceBlue;
                }

                Color temp = new Color(pixelRed,pixelGreen,pixelBlue);
                int meanColour = temp.getRGB();
                image.setRGB(x,y,meanColour);
            }
        }
        return image;
    }
    
    public int[] calculateMean(BufferedImage image) {

        int rgb_redSum = 0;
        int rgb_greenSum = 0;
        int rgb_blueSum = 0;

        for (int x = 220; x < 240; x++) {
            for (int y = 300; y < 320; y++) {
                Color c = new Color(image.getRGB(y, x));
                rgb_redSum = rgb_redSum + c.getRed();
                rgb_greenSum = rgb_greenSum + c.getGreen();
                rgb_blueSum = rgb_blueSum + c.getBlue();
            }
        }

        int rgb_redMean = (int) (rgb_redSum / 400);
        int rgb_greenMean = (int) (rgb_greenSum / 400);
        int rgb_blueMean = (int) (rgb_blueSum / 400);

        int[] rgbArray = {rgb_redMean, rgb_greenMean, rgb_blueMean}; 

        return rgbArray;
    }
}