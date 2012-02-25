/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package computer.vision;


import java.awt.image.BufferedImage;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Dimo Petroff
 */
public class CannyEdge {
    
    public static final short SOBEL_SIMPLE=2,SOBEL_PURE=1,SOBEL_SCHARR=0;
    public static final short NOISE_REDUCTION_NONE=0,NOISE_REDUCTION_LOW=1,NOISE_REDUCTION_NORMAL=2;
    
   /**
     * Converts a BufferedImage into a 2D integer array. Also, the color is converted to grey-scale.
     * 
     * @param input - the BufferedImage to process
     * @return - a 2D integer gray-scale representation of the image
     */
    public static int[][] getGrayscale(BufferedImage input, int startX, int startY, int width, int height){
	int[][] output=new int[width][height];
	for(int x=0;x<output.length;x++)
	    for(int y=0;y<output[0].length;y++){
                try{
                int pixel=input.getRGB(startX+x,startY+y);
		output[x][y]=(((pixel>>>16)&0xFF)+((pixel>>>8)&0xFF)+(pixel&0xFF))/3;
                }
                catch(Exception e) {
                    System.out.println("StartX: "+startX+" StartY: "+startY+" X: "+x+" Y: "+y+" Height: "+height+" Width: "+width);
                }
            }
	return output;
    }
    
    /**
     * Convolves the input and kernel.
     * Any 2D matrices would be meaningful but the kernel MUST have odd dimensions.
     * 
     * @param kernel      - a 2D array representing the kernel. 
     * @param input       - a 2D array representing the input
     * @return - the result of the convolution.
     */
    public static int[][] filter(int[][] kernel, int[][] input){
	return filter(kernel,0,input);
    }
    
    /**
     * Convolves the input and kernel, dividing the result with the denominator.
     * Any 2D matrices would be meaningful but the kernel MUST have odd dimensions.
     * 
     * @param kernel      - a 2D array representing the kernel. 
     * @param denominator - the result will be divided by this. Ignored if zero.
     * @param input       - a 2D array representing the input
     * @return - the result of the convolution after division.
     */
    public static int[][] filter(int[][] kernel, int denominator, int[][] input){
	if(kernel.length%2!=1 || kernel[0].length%2!=1) throw new Error("FILTER ERROR: The provided kernel MUST have odd dimentions.");
	int[][] output=new int[input.length][input[0].length];
	for(int x=kernel.length/2;x<input.length-kernel.length/2;x++)
	    for(int y=kernel[0].length/2;y<input[0].length-kernel[0].length/2;y++) {
		output[x][y]=0;
		for(int i=-kernel.length/2;i<=kernel.length/2;i++)
		    for(int j=-kernel[0].length/2;j<=kernel[0].length/2;j++)
			output[x][y]+=kernel[i+kernel.length/2][j+kernel[0].length/2]*input[x+i][y+j];
                if(denominator!=0)output[x][y]/=denominator;
	    }
	return output;
    }
    
    /**
     * Reduces noise by blurring the input
     * @param input - the grey-scale image as a 2D integer array
     * @param noiseReductionLevel - controls the amount of blur: 0 for none, 1 for low, anything else for normal
     * @return the processed image as a 2D integer array
     */
    public static int[][] noiseReduction(int[][] input, short noiseReductionLevel){
        
        switch(noiseReductionLevel){
            
            //No Noise Reduction
            case 0:{
                return input;
            }

            //Some noise reduction - note, numbers for kernel were pulled out of a hat.
            case 1:{
                return filter(new int[][] {{ 3, 5, 3},
                                             { 5,10, 5},
                                             { 3, 5, 3}},42,input);
            }

            //Normal noise reduction - proper gaussian blur, numbers taken from wikipedia article on canny
            default :{
                return filter(new int[][] {{ 2, 4, 5, 4, 2},
                                             { 4, 9,12, 9, 4},
                                             { 5,12,15,12, 5},
                                             { 4, 9,12, 9, 4},
                                             { 2, 4, 5, 4, 2}},159,input);
            }
        }
    }
    
    /**
     * Computes a gradient from the input using the selected method.
     * 
     * @param input      - gray-scale input image as a 2D integer array
     * @param kernelType - 1 selects the pure Sobel kernels, 0 selects a minimal solution and anything else selects Scharr's kernels optimised for rotational symmentry
     * @return - the computed gradient
     */
    public static Gradient sobel(int[][] input, short kernelType){
        int[][] gradientX,gradientY;

        switch(kernelType){
            case 0:{
                gradientX  =filter(new int[][] {{ 1},
                                              { 0},
                                              {-1}},input);

                gradientY=filter(new int[][] {{ 1, 0,-1}},input);
                break;
            }
            case 1:{
            gradientX=filter(new int[][] {{ 1, 2, 1},
                                            { 0, 0, 0},
                                            {-1,-2,-1}},input);

            gradientY=filter(new int[][] {{ 1, 0,-1},
                                            { 2, 0,-2},
                                            { 1, 0,-1}},input);
            break;
            }
            default:{
                gradientX=filter(new int[][] {{  3, 10,  3},
                                              {  0,  0,  0},
                                              { -3,-10, -3}},input);

                gradientY=filter(new int[][] {{  3,  0, -3},
                                              { 10,  0,-10},
                                              {  3,  0, -3}},input);
                break;
            }
        }

        Gradient gradient=new Gradient(input.length,input[0].length);
        double temp = 0;
        int count = 0;
        for(int x=0;x<input.length;x++)
            for(int y=0;y<input[0].length;y++){
                gradient.magnitude[x][y]=(int)(Math.sqrt(Math.pow((double)gradientX[x][y],2)+
                                                    Math.pow((double)gradientY[x][y],2)));
                gradient.direction[x][y]=(int)(1000*Math.atan2(gradientX[x][y],gradientY[x][y]));
                gradient.direction2[x][y]=(double)(Math.atan2(gradientY[x][y],gradientX[x][y]));
                //System.out.print((Math.sqrt(Math.pow((double)gradientX[x][y],2)+Math.pow((double)gradientY[x][y],2)))+" ");

            }
       // System.out.println("Average angle: "+temp/count);
        return gradient;
    }
    
    /**
     * Normalizes the contents of the input to the specified range
     * 
     * @param input  - the input. any 2D array would be meaningful
     * @param newmin - the desired minimum value
     * @param newmax - the desired maximum value
     */
    public static void normalize(int[][] input, int newmin, int newmax){
	int oldmax=input[0][0], oldmin=input[0][0];
	for(int x=1;x<input.length-1;x++)
	    for(int y=1;y<input[0].length-1;y++) {
		if(oldmin>input[x][y])oldmin=input[x][y];
		if(oldmax<input[x][y])oldmax=input[x][y];
	    }
	for(int x=1;x<input.length-1;x++) {
	    for(int y=1;y<input[0].length-1;y++) {
                if ((oldmax - oldmin) == 0) {
                    oldmax += 1;
                }
		input[x][y]=((input[x][y]-oldmin)*(newmax-newmin)/(oldmax-oldmin))+newmin;
            }
        }
    }
    
    /**
     * Performs Non-Maximum Suppression:
     * Simple thresholding results in thick edges. True edges occur at local maxima only.
     * 
     * @param threshold - the percentage of the maximum magnitude, above which a pixel is considered as potentially part of an edge
     * @param gradient  - the precomputed gradient
     */
    public static int[][] nonMaximaSuppression(float threshold, Gradient gradient){
        normalize(gradient.magnitude, 0, 1000);
        int[][] output=new int[gradient.direction.length][gradient.direction[0].length];
	for(int x=1;x<gradient.direction.length-1;x++)
	    for(int y=1;y<gradient.direction[0].length-1;y++){
                output[x][y]=0;
                if(gradient.magnitude[x][y]>=(threshold*10)){
                    if(((gradient.direction[x][y]>=-393 && gradient.direction[x][y]<393)||(gradient.direction[x][y]<-2749 || gradient.direction[x][y]>=2749))&& //edge gradient.direction is east
                        (gradient.magnitude[x][y-1]<gradient.magnitude[x][y] && gradient.magnitude[x][y+1]<gradient.magnitude[x][y]))output[x][y]=255;
                    if(((gradient.direction[x][y]>=393 && gradient.direction[x][y]<1178)||(gradient.direction[x][y]>=-2749 && gradient.direction[x][y]<-1963))&& //edge gradient.direction is north-east
                        (gradient.magnitude[x-1][y-1]<gradient.magnitude[x][y] && gradient.magnitude[x+1][y+1]<gradient.magnitude[x][y]))output[x][y]=255;
                    if(((gradient.direction[x][y]>=1178 && gradient.direction[x][y]<1963)||(gradient.direction[x][y]>=-1963 && gradient.direction[x][y]<-1178))&& //edge gradient.direction is north
                        (gradient.magnitude[x-1][y]<gradient.magnitude[x][y] && gradient.magnitude[x+1][y]<gradient.magnitude[x][y]))output[x][y]=255;
                    if(((gradient.direction[x][y]>=1963 && gradient.direction[x][y]<2749)||(gradient.direction[x][y]>=-1178 && gradient.direction[x][y]<-393))&& //edge gradient.direction is north-west
                        (gradient.magnitude[x+1][y-1]<gradient.magnitude[x][y] && gradient.magnitude[x-1][y+1]<gradient.magnitude[x][y]))output[x][y]=255;
                }
	    }
        return output;
    }
    
    /**
     * Produces an edge map from the input image.
     * 
     * @param input       - BufferedImage to process
     * @param noiselevel  - option for noise reduction
     * @param sobelmethod - setting for Sobel operator
     * @param threshold   - defines the percentage of the maximum magnitude at which a pixel is considered potentially part of an edge. ~2 gives decent results
     * @return - an edge map as a 2D integer array. 0 means no edge, 255 means edge.
     */
    public static int[][] detect(BufferedImage input, int startX, int startY, int width, int height, short noiselevel, short sobelmethod, float threshold){
        Gradient grad=sobel(noiseReduction(getGrayscale(input, startX, startY, width, height),noiselevel),sobelmethod);
        return nonMaximaSuppression(threshold,grad);
    }
}
