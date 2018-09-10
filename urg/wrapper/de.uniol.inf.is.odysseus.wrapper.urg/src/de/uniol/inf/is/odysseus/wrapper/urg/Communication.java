package de.uniol.inf.is.odysseus.wrapper.urg;

import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;

import de.uniol.inf.is.odysseus.wrapper.urg.utils.DeviceConnectionException;


public class Communication {
	private ArrayList<MessageListener> messageListeners = new ArrayList<MessageListener>();
	private InputStream is = null;
	private PrintStream os = null;
	private SerialPort port = null;
	private boolean connected = false;
	
	public static Communication getInstance() {
		if (instance == null) {
			instance = new Communication();
		}
		return instance;
	}
	
	private static Communication instance = null;
	private Communication() {	
	}
	
	public void addMessageListener(MessageListener listener) {
		messageListeners.add(listener);
	}
	
	public void removeMessageListener(MessageListener listener) {
		messageListeners.remove(listener);
	}
	
	public ArrayList<String> getAvailablePorts() {
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		ArrayList<String> res = new ArrayList<>();
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers
					.nextElement();
			if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				res.add(pid.getName());
			}
		}
		return res;
	}
	
	public void connect(String portName) {
		if (connected)
			return;
		
		Enumeration<?> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
		CommPortIdentifier portId = null;
		while (portIdentifiers.hasMoreElements()) {
			CommPortIdentifier pid = (CommPortIdentifier) portIdentifiers
					.nextElement();
			if (pid.getPortType() == CommPortIdentifier.PORT_SERIAL
					&& pid.getName().equals(portName)) {
				portId = pid;
				break;
			}
		}
		if (portId == null) {
			throw new DeviceConnectionException("Could not find any serial port device connected to " + portName + ".");
		}
		
		try {
			port = (SerialPort) portId.open("Odysses", 10000);
		} catch (PortInUseException e) {
			throw new DeviceConnectionException("Port already in use.");
		}
		
		try {
			port.setSerialPortParams(115200, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e1) {
			e1.printStackTrace();
		}
		
		connected = true;
		System.out.println("connected");
		try {
			port.addEventListener(new SerialPortEventListener() {
				@Override
				public void serialEvent(SerialPortEvent event) {
					switch (event.getEventType()) {
					case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
						outputBufferEmpty(event);
						break;
					case SerialPortEvent.DATA_AVAILABLE:
						dataAvailable(event);
						break;
					}
				}
			});
		} catch (TooManyListenersException e2) {
			e2.printStackTrace();
		}
		
		port.notifyOnDataAvailable(true);
		port.notifyOnOutputEmpty(true);
		
		try {
			is = port.getInputStream();
		} catch (IOException e) {
			System.err.println("Can't open input stream: write-only");
			is = null;
		}
		
		try {
			os = new PrintStream(port.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void disconnect() {
		if (!connected)
			return;
		
		if (is != null)
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		if (os != null)
			os.close();
		if (port != null)
			port.close();
		connected = false;
		System.out.println("disconnected");
	}
	
	public void executeCommand(Command c) {
		if (connected)
			os.print(c.getCommand());
	}
	
	/**
	 * Handle output buffer empty events. NOTE: The reception of this event is
	 * optional and not guaranteed by the API specification.
	 * 
	 * @param event
	 *            The output buffer empty event
	 */
	protected void outputBufferEmpty(SerialPortEvent event) {
		
	}

	/**
	 * Handle data available events.
	 * 
	 * @param event
	 *            The data available event
	 */
	protected void dataAvailable(SerialPortEvent event) {
		try {
			int len = is.available();
			byte[] res = new byte[len];
			is.read(res);
			
			if (len < 2)
				return;
			
			byte first = res[0];
			byte second = res[1];
			if (first != 'M')
				return;
			
			if (second != 'D' && second != 'S')
				return;
			
			for (MessageListener listener : messageListeners)
				listener.messageReceived(ByteBuffer.wrap(res));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
