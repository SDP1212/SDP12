package brick;

import java.io.*;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class Logger {
	
	private FileOutputStream outLog = null;
	private Logger() {
		File file = new File("log.dat");
        try {
            if (!file.exists()) {
                    file.createNewFile();
            } else {
                file.delete();
                file.createNewFile();
            }
            } catch (IOException ex) {
        }
        try {
            outLog = new FileOutputStream(file, true);
        } catch (FileNotFoundException ex) {
            System.err.println("Failed to create output stream");
            System.exit(1);
        }
	}
	
	public static Logger getInstance() {
		return LoggerHolder.INSTANCE;
	}
	
	private static class LoggerHolder {
		private static final Logger INSTANCE = new Logger();
	}
	
	/** 
     * Write a message to the log file. This will be prepended with the current time.
     * @param outLog The log file to write to.
     * @param message The message to write.
     */
    public synchronized static void logToFile(String message) {
//        DataOutputStream dataOut = new DataOutputStream(outLog);
//        try { 
//            // write
//            dataOut.writeChars(System.currentTimeMillis() + " " + message+"\n");
//
//            outLog.flush(); // flush the buffer and write the file
//			dataOut.close();
//        } catch (IOException e) {
//            System.err.println("Failed to write to output stream");
//        }
		
    }
	
	
	public void close() {
		try {
			outLog.close();
		} catch (IOException ex) {
			System.err.println("File error: " + ex.toString());
		}
	}
}
