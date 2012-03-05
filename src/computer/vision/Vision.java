package computer.vision;
import java.util.List;

import au.edu.jcu.v4l4j.Control;
import au.edu.jcu.v4l4j.FrameGrabber;
import au.edu.jcu.v4l4j.VideoDevice;
import au.edu.jcu.v4l4j.exceptions.V4L4JException;

public abstract class Vision extends Thread {
	// Video capture variables
	protected VideoDevice videoDevice;
	protected FrameGrabber frameGrabber;
	protected Thread captureThread;
	public static int SATURATION = 127;
	public static int BRIGHTNESS = 127;
	public static int CONTRAST = 127;
	private static final int HUE = 0;
	public static final int FULL_LUMA_RANGE = 1;
	public static final int UV_RATIO = 49;
	private List<Control> controls;	

	public Vision() {
		super();
	}

	/**
	 * Initialises the FrameGrabber object with the given parameters
	     * @param dev the video device file to capture from
	     * @param w the desired capture width
	     * @param h the desired capture height
	     * @param std the capture standard
	     * @param channel the capture channel
	     * @param qty the JPEG compression quality
	     * @throws V4L4JException if any parameter if invalid
	 */
	protected void initFrameGrabber(String dev, int w, int h,
			int std, int channel, int qty) throws V4L4JException {
			    videoDevice = new VideoDevice(dev);
//			    try {
					controls =  videoDevice.getControlList().getList();
					for(Control c: controls) { 
						if(c.getName().equals("Saturation"))
							if(c.getName().equals("Contrast"))
								c.setValue(CONTRAST);
							if(c.getName().equals("Brightness"))
								c.setValue(BRIGHTNESS);
							if(c.getName().equals("full luma range"))
								c.setValue(FULL_LUMA_RANGE);
							if(c.getName().equals("Hue"))
								c.setValue(HUE);
							if(c.getName().equals("Saturation"))
								c.setValue(SATURATION);
							if(c.getName().equals("uv ratio"))
								c.setValue(UV_RATIO);
							}
//			    }
//			    catch(V4L4JException e3) { 
//			    	System.out.println("Cannot set video device settings!"); 
//			    }

                              
			    frameGrabber = videoDevice.getJPEGFrameGrabber(w, h, channel, std, qty);
			    frameGrabber.startCapture();
			    System.out.println("Starting capture at "+frameGrabber.getWidth()+"x"+frameGrabber.getHeight());            
			}
	public void setContrast(int contrast) {
		if (controls != null) {
			for(Control c: controls) { 
				if (c.getName().equals("Contrast")) {
				try{
					c.setValue(Math.min(contrast, 127));
                    CONTRAST=contrast;
				} catch(V4L4JException e3) { 
						System.err.println("Cannot set video device settings! Contrast="+contrast); 
			    }
				}
			}
		}
	}
	
	public void setSaturation(int saturation) {
		if (controls != null) {
			for (Control c : controls) {
				if (c.getName().equals("Saturation")) {
					try {
						c.setValue(Math.min(saturation, 127));
                        SATURATION=saturation;
					} catch (V4L4JException e3) {
						System.err.println("Cannot set video device settings! Saturation="+saturation);
					}
				}
			}
		}
	}
	
	public void setBrightness(int brightness) {
		if (controls != null) {
			for (Control c : controls) {
				if (c.getName().equals("Brightness")) {
					try {
						c.setValue(Math.min(brightness, 255));
                        BRIGHTNESS=brightness;
					} catch (V4L4JException e3) {
						System.err.println("Cannot set video device settings! Brightness="+brightness);
					}
				}
			}
		}
	}
}