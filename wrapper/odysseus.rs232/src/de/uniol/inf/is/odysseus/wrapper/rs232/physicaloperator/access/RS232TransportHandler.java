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
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * 
 * Generic transport handler for RS232
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 * 
 */
public class RS232TransportHandler extends AbstractTransportHandler {
	/** How long to wait for the open to finish up. */
	public static final int TIMEOUTSECONDS = 30;
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(RS232TransportHandler.class);
	/** The output stream */
	private PrintStream output;
	/** The chosen Port Identifier */
	private CommPortIdentifier portId;
	/** The chosen Port itself */
	private CommPort port;
	/** The baud rate */
	private int baud;
	/** The port identifier */
	private String name;
	/** The number of stop bits */
	private int stopbits;
	/** The parity */
	private int parity;
	/** The number of data bits */
	private int databits;

	@Override
	public void send(byte[] message) throws IOException {
		if (this.output != null) {
			this.output.write(message);
			LOG.debug("RS232 Handler: > {}", message);
		}
	}

	@Override
	public ITransportHandler createInstance(Map<String, String> options) {
		RS232TransportHandler handler = new RS232TransportHandler();
		handler.name = options.get("name");
		handler.baud = options.containsKey("baud") ? Integer.parseInt(options
				.get("baud")) : 9600;
		handler.parity = options.containsKey("parity") ? Integer
				.parseInt(options.get("parity")) : SerialPort.PARITY_NONE;
		handler.databits = options.containsKey("databits") ? Integer
				.parseInt(options.get("databits")) : SerialPort.DATABITS_8;
		handler.stopbits = options.containsKey("stopbits") ? Integer
				.parseInt(options.get("stopbits")) : SerialPort.STOPBITS_1;
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		if (this.port != null) {
			try {
				return this.port.getInputStream();
			} catch (IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}
		return null;
	}

	@Override
	public String getName() {
		return "RS232";
	}

	@Override
	public void process_open() throws IOException {
		try {
			this.portId = CommPortIdentifier.getPortIdentifier(name);
			this.port = portId
					.open("RS232: "+this.hashCode(), TIMEOUTSECONDS * 1000);
			SerialPort serialPort = (SerialPort) this.port;
			serialPort.setSerialPortParams(this.baud, this.databits,
					this.stopbits, this.parity);
			this.output = new PrintStream(port.getOutputStream(), true);

		} catch (NoSuchPortException e) {
			LOG.error(e.getMessage(), e);
		} catch (UnsupportedCommOperationException e) {
			LOG.error(e.getMessage(), e);
		} catch (PortInUseException e) {
			LOG.error(e.getMessage(), e);
		}

	}

	@Override
	public void process_close() throws IOException {
		if (this.port != null) {
			this.port.close();
		}
	}

}
