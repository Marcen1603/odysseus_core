/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.rs232.physicaloperator.access;

import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Generic transport handler for RS232
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RS232TransportHandler extends AbstractTransportHandler implements
		SerialPortEventListener {
	/** How long to wait for the open to finish up. */
	public static final int TIMEOUTSECONDS = 30;
	/** Logger */
	private final Logger LOG = LoggerFactory
			.getLogger(RS232TransportHandler.class);
	/** The output stream */
	private PrintStream output;
	/** The chosen Port Identifier */
	private CommPortIdentifier portId;
	/** The chosen Port itself */
	private CommPort port;
	/** The baud rate */
	private int baud;
	/** The port identifier */
	private String portName;
	/** The number of stop bits */
	private int stopbits;
	/** The parity */
	private int parity;
	/** The number of data bits */
	private int databits;
	/** The input stream */
	private BufferedReader input;
	/**
	 * If set to true, readLine will be used (default), else bytes are read
	 * sequentially
	 */
	private boolean readLines;

	public RS232TransportHandler() {
		super();
		// Bugfix for Linux/Unix access to /dev/ttyACM*
		String os = System.getProperty("os.name").toLowerCase();
		if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			StringBuilder serialPorts = new StringBuilder();
			for (int i = 0; i < 5; i++) {
				if (serialPorts.length() == 0) {
					serialPorts.append("/dev/ttyACM" + i);
				} else {
					serialPorts.append(":/dev/ttyACM" + i);
				}
			}
			System.setProperty("gnu.io.rxtx.SerialPorts",
					serialPorts.toString());
		}
	}

	public RS232TransportHandler(IProtocolHandler<?> protocolHandler,
			Map<String, String> options) {
		super(protocolHandler, options);

		// Bugfix for Linux/Unix access to /dev/ttyACM*
		String os = System.getProperty("os.name").toLowerCase();
		if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
			StringBuilder serialPorts = new StringBuilder();
			for (int i = 0; i < 5; i++) {
				if (serialPorts.length() == 0) {
					serialPorts.append("/dev/ttyACM" + i);
				} else {
					serialPorts.append(":/dev/ttyACM" + i);
				}
			}
			System.setProperty("gnu.io.rxtx.SerialPorts",
					serialPorts.toString());
		}

		portName = options.get("port");
		baud = options.containsKey("baud") ? Integer.parseInt(options
				.get("baud")) : 9600;
		parity = options.containsKey("parity") ? Integer.parseInt(options
				.get("parity")) : SerialPort.PARITY_NONE;
		databits = options.containsKey("databits") ? Integer.parseInt(options
				.get("databits")) : SerialPort.DATABITS_8;
		stopbits = options.containsKey("stopbits") ? Integer.parseInt(options
				.get("stopbits")) : SerialPort.STOPBITS_1;
		readLines = options.containsKey("readlines") ? Boolean
				.parseBoolean("readlines") : true;

	}

	@Override
	public void send(byte[] message) throws IOException {
		if (this.output != null) {
			this.output.write(message);
			LOG.debug("RS232 Handler: > {}", message);
		} else {
			LOG.error("Trying to write to a not opened connection!");
		}
	}

	@Override
	public InputStream getInputStream() {
		return null;
	}

	@Override
	public String getName() {
		return "RS232";
	}

	@Override
	public void serialEvent(SerialPortEvent event) {
		if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {

			try {
				if (this.input.ready()) {
					if (readLines) {
						String message = "";
						message = this.input.readLine();
						super.fireProcess(ByteBuffer.wrap(message.getBytes()));
					} else {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						while (this.input.ready()) {
							int r = this.input.read();
							buffer.putInt(r);
						}
						super.fireProcess(buffer);
					}

				}
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		RS232TransportHandler handler = new RS232TransportHandler(
				protocolHandler, options);
		return handler;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			this.portId = CommPortIdentifier.getPortIdentifier(portName);
			this.port = portId.open("RS232: " + this.hashCode(),
					TIMEOUTSECONDS * 1000);
			SerialPort serialPort = (SerialPort) this.port;
			serialPort.setSerialPortParams(this.baud, this.databits,
					this.stopbits, this.parity);
			this.output = new PrintStream(port.getOutputStream(), true);
			this.input = new BufferedReader(new InputStreamReader(
					port.getInputStream()));
			serialPort.notifyOnDataAvailable(true);
			serialPort.addEventListener(this);
		} catch (NoSuchPortException e) {
			LOG.error("No such port {}", this.portName);
			LOG.error(e.getMessage(), e);
		} catch (UnsupportedCommOperationException e) {
			LOG.error(e.getMessage(), e);
		} catch (PortInUseException e) {
			LOG.error("Port {} in use", this.portName);
			LOG.error(e.getMessage(), e);
		} catch (TooManyListenersException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		try {
			this.portId = CommPortIdentifier.getPortIdentifier(portName);
			this.port = portId.open("RS232: " + this.hashCode(),
					TIMEOUTSECONDS * 1000);
			SerialPort serialPort = (SerialPort) this.port;
			serialPort.setSerialPortParams(this.baud, this.databits,
					this.stopbits, this.parity);
			this.output = new PrintStream(port.getOutputStream(), true);
			serialPort.notifyOnDataAvailable(true);
			serialPort.addEventListener(this);
		} catch (NoSuchPortException e) {
			LOG.error("No such port {}", this.portName);
			LOG.error(e.getMessage(), e);
		} catch (UnsupportedCommOperationException e) {
			LOG.error(e.getMessage(), e);
		} catch (PortInUseException e) {
			LOG.error("Port {} in use", this.portName);
			LOG.error(e.getMessage(), e);
		} catch (TooManyListenersException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void processInClose() throws IOException {
		if (this.input != null) {
			try {

				this.input.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				this.input = null;
			}
		}
		if (this.port != null) {
			try {
				SerialPort serialPort = (SerialPort) this.port;
				serialPort.removeEventListener();
				serialPort.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				this.port = null;
			}
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if (this.output != null) {
			try {

				this.output.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				this.output = null;
			}
		}
		if (this.port != null) {
			try {
				SerialPort serialPort = (SerialPort) this.port;
				serialPort.removeEventListener();
				serialPort.close();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				this.port = null;
			}
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof RS232TransportHandler)) {
			return false;
		}
		RS232TransportHandler other = (RS232TransportHandler) o;
		if (!this.getPortName().equals(other.getPortName())) {
			return false;
		} else if (this.getBaud() != other.getBaud()) {
			return false;
		} else if (this.getParity() != other.getParity()) {
			return false;
		} else if (this.getDatabits() != other.getDatabits()) {
			return false;
		} else if (this.getStopbits() != other.getStopbits()) {
			return false;
		}

		return true;
	}

	public String getPortName() {
		return portName;
	}

	public int getBaud() {
		return baud;
	}

	public int getStopbits() {
		return stopbits;
	}

	public int getParity() {
		return parity;
	}

	public int getDatabits() {
		return databits;
	}
}
