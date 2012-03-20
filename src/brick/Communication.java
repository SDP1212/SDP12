package brick;

import java.io.*;
import lejos.nxt.*;
import lejos.nxt.comm.*;

/**
 *
 * @author Matt Jeffryes <m.j.jeffryes@sms.ed.ac.uk>
 */
public class Communication implements Runnable {

	private NXTConnection connection = null;
	private InputStream in;
	private OutputStream out;
	private boolean connected = false;
	private final Object connectionLock = new Object();

	private Communication() {
	}

	public static Communication getInstance() {
		return CommunicationHolder.INSTANCE;
	}

	private static class CommunicationHolder {

		private static final Communication INSTANCE = new Communication();
	}

	/**
	 * Wait for a connection, and give feedback on status.
	 */
	public void waitForConnection() {
		LCD.drawString("Waiting for connection", 0, 0);
		Sound.playTone(2000, 1000);
		synchronized (connectionLock) {
			connection = Bluetooth.waitForConnection();
			if (getConnection().getClass() == BTConnection.class) {
				LCD.clear();
				in = getConnection().openInputStream();
				out = getConnection().openOutputStream();
				setConnected(true);
				LCD.drawString("Connected", 0, 0);
				Logger.logToFile("Connected");
			}
		}
	}

	/**
	 * Close the connection, and give feedback on status.
	 */
	public void closeConnection() {
		LCD.clear();
		LCD.drawString("Quitting", 0, 0);
		synchronized (connectionLock) {
			try {
				in.close();
				out.close();
				connection.close();
				LCD.clear();
			} catch (IOException e) {
				Logger.logToFile(e.toString());
			}
			setConnected(false);
		}
	}

	private NXTConnection getConnection() {
		synchronized (connectionLock) {
			return connection;
		}
	}

	/**
	 * The status of the connection.
	 * 
	 * @return True when connected, false otherwise.
	 */
	public boolean isConnected() {
		return connected;
	}

	/**
	 * Update the status of the connection.
	 * 
	 * @param newConnected True if connected, false otherwise. 
	 */
	private void setConnected(boolean newConnected) {
		connected = newConnected;
	}

	/**
	 * Send an opcode message to the computer.
	 * 
	 * @param message An opcode.
	 */
	public void sendMessage(int message) {
		if (isConnected()) {
			try {
				synchronized (connectionLock) {
					out.write(intToByteArray(message));
					out.flush();
				}
			} catch (IOException e) {
				Logger.logToFile(e.toString());
			}
		}
	}

	/**
	 * Convert a byte array to an integer.
	 * 
	 * @param b An array of bytes.
	 * @return An integer.
	 */
	public static int byteArrayToInt(byte[] b) {
		int value = 0;
		// For each position in the array
		for (int i = 0; i < 4; i++) {
			// Shift by an amount corresponding to our current position the byte at that position
			int shift = (4 - 1 - i) * 8;
			value += ((b[i] & 0xFF) << shift);
		}
		return value;
	}

	/**
	 * Convert an integer to a byte array.
	 * 
	 * @param in An integer that can be stored within 4 bytes.
	 * @return A byte array.
	 */
	public static byte[] intToByteArray(int in) {
		byte[] b = new byte[4];
		// For each position in the array
		for (int i = 0; i < 4; i++) {
			// Shift the integer by out current position in the array
			int shift = (3 - i) * 8;
			b[i] = (byte) ((in & 0xFF) >> shift);
		}
		return b;
	}

	public void run() {
		waitForConnection();
		if (!connected) {
			return;
		}
		int n = Brick.DO_NOTHING;
		Movement movement = Movement.getInstance();
		while (n != Brick.QUIT && !Thread.interrupted() && connected) {
			try {
				// Read in 4 bytes from the bluetooth stream
				byte[] byteBuffer = new byte[4];
				in.read(byteBuffer);
				// Convert the 4 bytes to an integer and mask out the opcode and args
				n = byteArrayToInt(byteBuffer);
				int opcode = n & Brick.OPCODE;
				int arg = (n & Brick.ARG) >> 8;
				switch (opcode) {

					case Brick.FORWARDS:
						movement.forwards(arg);
						break;

					case Brick.BACKWARDS:
						movement.backwards(arg);
						break;

					case Brick.ROTATE:
						movement.rotate(arg);
						break;

					case Brick.ROTATELEFT:
						movement.rotateLeft(arg);
						break;

					case Brick.ROTATERIGHT:
						movement.rotateRight(arg);
						break;

					case Brick.ROTATETO:
						movement.rotateTo(arg);
						break;

					case Brick.ARCLEFT:
						movement.arcLeft(arg);
						break;

					case Brick.ARCRIGHT:
						movement.arcRight(arg);
						break;

					case Brick.STOP:
						movement.stop();
						break;

					case Brick.KICK:
						movement.kick();
						break;
						
					case Brick.SETSPEED:
						movement.setSpeed(arg);
						break;

					case Brick.QUIT: // close connection
						break;

				}
				// respond to say command was acted on
				sendMessage(Brick.OK);
			} catch (Throwable e) {
				Logger.logToFile(e.toString());
				n = Brick.QUIT;
			}
		}
		closeConnection();
		Brick.quit();
	}
}
