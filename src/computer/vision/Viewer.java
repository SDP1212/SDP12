package computer.vision;

import au.edu.jcu.v4l4j.V4L4JConstants;
import au.edu.jcu.v4l4j.VideoFrame;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;
import java.awt.Point;
import java.awt.image.BufferedImage;
import computer.vision.WorldState;

public class Viewer extends Vision implements Runnable {

	// Window Variables

	private boolean stopCapturingVideo;

	// Frames per second Variables - Max 25fps
	long prevsec = 0; // this will store previous timestamp
	int framesThisSecond = 0; // this is fps counter
	GUI gui;
	int imgwidth;
	int imgheight;
	public static ImageProcessor imageProcessor;
	private Integer[] refColorPointer=null;
	private Point refNewPixel=null;
	private static WorldState worldState;

	/**
	 * Builds a WebcamViewer object
	 * 
	 * @param dev
	 *            the video device file to capture from
	 * @param w
	 *            the desired capture width
	 * @param h
	 *            the desired capture height
	 * @param std
	 *            the capture standard
	 * @param channel
	 *            the capture channel
	 * @param qty
	 *            the JPEG compression quality
	 * @throws V4L4JException
	 *             if any parameter if invalid
	 */
	public Viewer(String dev, int w, int h, int std, int channel, int qty) {
		try {
			initFrameGrabber(dev, w, h, std, channel, qty);
		} catch (V4L4JException e) {
			System.out.println(e.toString());
			e.printStackTrace();
		}
		imageProcessor = new ImageProcessor();
		gui = new GUI();
		gui.setViewer(this);
		gui.setVisible(true);
		stopCapturingVideo = false;
		captureThread = new Thread(this, "Capture Thread");
		captureThread.start();
		worldState = new WorldState();
	}

	private void calculateFPS() {
		int forwsec = (int) System.currentTimeMillis() / 1000;
		if (prevsec != forwsec) {
//			if (framesThisSecond > 24)
//				System.out.println("FPS:" + framesThisSecond
//						+ "!!! WOOHOO im super fast!");
//			else
//				System.out.println("FPS:" + framesThisSecond);
			framesThisSecond = 0;
			prevsec = Integer.valueOf(forwsec);
		} else {
			framesThisSecond++;
		}
	}

	/**
	 * Implements the capture thread: get a frame from the FrameGrabber, and
	 * display it
	 */
	@Override
	public void run() {

		prevsec = (int) System.currentTimeMillis() / 1000;

		VideoFrame frame;

		try {
			BufferedImage image;
			image = frameGrabber.getVideoFrame().getBufferedImage();
			imgwidth = image.getWidth();
			imgheight = image.getHeight();
		} catch (V4L4JException e1) {
			e1.printStackTrace();
		}

		try {
			while (!stopCapturingVideo) {
				frame = frameGrabber.getVideoFrame();
				try {
					BufferedImage image=frame.getBufferedImage();
					if(refColorPointer!=null && refNewPixel!=null){
//						int[] sample=new int[9];
//                        image.getRGB(refNewPixel.x-1, refNewPixel.y-1, 3, 3, sample, 0, 1);
						int newRef=image.getRGB(refNewPixel.x, refNewPixel.y);
//						for(int s : sample)
//							newRef+=s;
//						newRef/=9;
						refColorPointer[0]=(newRef>>>16)&0xFF;
						refColorPointer[1]=(newRef>>>8)&0xFF;
						refColorPointer[2]=newRef&0xFF;
						gui.setRefChanger(refColorPointer);
						refColorPointer=null;
						refNewPixel=null;
					}
					BufferedImage iq = imageProcessor.process(frame
							.getBufferedImage());
					gui.setImage(iq);
					ImageProcessor.displX = gui.getLocationOnScreen().x;
					ImageProcessor.displY = gui.getLocationOnScreen().y - 25;
				} catch (NullPointerException e) {
					e.printStackTrace();
					System.out.println("Shutting down...");
					stopCapturingVideo = true;
					frameGrabber.stopCapture();
					videoDevice.releaseFrameGrabber();
				}
				frame.recycle();
				calculateFPS();
			}
		} catch (V4L4JException e) {
			e.printStackTrace();
			System.out.println("Failed to capture image");
		}
                
	}

	/**
	 * Add a line to the printed image for debugging
	 * 
	 * @param p1
	 *            Point from
	 * @param p2
	 *            Point to
	 */
	public void addLine(Point p1, Point p2, int color) {
		imageProcessor.addLineToBeDrawn(p1, p2, color);
	}

	public void dropLine() {
		if (!imageProcessor.lines.isEmpty()) {
			imageProcessor.lines.pop();
			imageProcessor.lines.pop();
			imageProcessor.lineColor.pop();
		}
	}

	public void dropAllLines() {
		while (!imageProcessor.lines.isEmpty()) {
			imageProcessor.lines.pop();
		}
		while (!imageProcessor.lineColor.isEmpty()) {
			imageProcessor.lineColor.pop();
		}
	}

	public static void main(String[] args) {
		String dev = "/dev/video0";
		int w = 640, h = 480, std = V4L4JConstants.STANDARD_PAL, channel = 0, qty = 60;
		new Viewer(dev, w, h, std, channel, qty);
	}

	public static Viewer startVision() {
		String caller = new Throwable().fillInStackTrace().getStackTrace()[1]
				.getClassName();
		GUI.className = caller;
		String dev = "/dev/video0";
		int w = 640, h = 480, std = V4L4JConstants.STANDARD_PAL, channel = 0, qty = 60;
		return new Viewer(dev, w, h, std, channel, qty);
	}
	
	public void setNewReference(Integer[] reference, int x, int y){
		this.refColorPointer=reference;
		this.refNewPixel=new Point(x, y);
	}

	public static WorldState getWorldState() {
		return worldState;
	}

}