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
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.connection.IAccessConnectionListener;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportPattern;

/**
 * Generic transport handler for RS232
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class RS232TransportHandler extends AbstractTransportHandler implements SerialPortEventListener,
        IAccessConnectionListener<ByteBuffer> {
    /** How long to wait for the open to finish up. */
    public static final int    TIMEOUTSECONDS = 30;
    /** Logger */
    private Logger             LOG            = LoggerFactory.getLogger(RS232TransportHandler.class);
    /** The output stream */
    private PrintStream        output;
    /** The chosen Port Identifier */
    private CommPortIdentifier portId;
    /** The chosen Port itself */
    private CommPort           port;
    /** The baud rate */
    private int                baud;
    /** The port identifier */
    private String             portName;
    /** The number of stop bits */
    private int                stopbits;
    /** The parity */
    private int                parity;
    /** The number of data bits */
    private int                databits;
    /** The input stream */
    private BufferedReader     input;
    /** Pipe in and output for data transfer */
    private PipedInputStream   pipeInput;
    private PipedOutputStream  pipeOutput;
    private boolean            usePipe        = false;
    private ITransportPattern  transportPattern;

    public RS232TransportHandler() {
    }

    public RS232TransportHandler(ITransportPattern transportPattern, Map<String, String> options) {
        this.transportPattern = transportPattern;
        this.portName = options.get("port");
        this.baud = options.containsKey("baud") ? Integer.parseInt(options.get("baud")) : 9600;
        this.parity = options.containsKey("parity") ? Integer.parseInt(options.get("parity")) : SerialPort.PARITY_NONE;
        this.databits = options.containsKey("databits") ? Integer.parseInt(options.get("databits"))
                : SerialPort.DATABITS_8;
        this.stopbits = options.containsKey("stopbits") ? Integer.parseInt(options.get("stopbits"))
                : SerialPort.STOPBITS_1;
        if (this.transportPattern == ITransportPattern.PULL) {
            this.pipeInput = new PipedInputStream();
            try {
                this.pipeOutput = new PipedOutputStream(this.pipeInput);
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        // Bugfix for Linux/Unix access to /dev/ttyACM*
        String os = System.getProperty("os.name").toLowerCase();
        if ((os.indexOf("nix") >= 0 || os.indexOf("nux") >= 0)) {
            StringBuilder serialPorts = new StringBuilder();
            for (int i = 0; i < 5; i++) {
                if (serialPorts.length() == 0) {
                    serialPorts.append("/dev/ttyACM" + i);
                }
                else {
                    serialPorts.append(":/dev/ttyACM" + i);
                }
            }
            System.setProperty("gnu.io.rxtx.SerialPorts", serialPorts.toString());
        }

    }

    public RS232TransportHandler(RS232TransportHandler rs232TransportHandler) {
        this.transportPattern = rs232TransportHandler.transportPattern;
        this.portName = rs232TransportHandler.portName;
        this.baud = rs232TransportHandler.baud;
        this.parity = rs232TransportHandler.parity;
        this.databits = rs232TransportHandler.databits;
        this.stopbits = rs232TransportHandler.stopbits;
        this.usePipe = rs232TransportHandler.usePipe;
        if (this.transportPattern == ITransportPattern.PULL) {
            this.pipeInput = new PipedInputStream();
            try {
                this.pipeOutput = new PipedOutputStream(this.pipeInput);
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

    @Override
    public void send(byte[] message) throws IOException {
        if (this.output != null) {
            this.output.write(message);
            LOG.debug("RS232 Handler: > {}", message);
        }
    }

    @Override
    public ITransportHandler createInstance(ITransportPattern transportPattern, Map<String, String> options) {
        return new RS232TransportHandler(transportPattern, options);
    }

    @Override
    public InputStream getInputStream() {
        this.usePipe = true;
        return this.pipeInput;
    }

    @Override
    public String getName() {
        return "RS232";
    }

    @Override
    public void process_open() {
        try {
            this.portId = CommPortIdentifier.getPortIdentifier(portName);
            this.port = portId.open("RS232: " + this.hashCode(), TIMEOUTSECONDS * 1000);
            SerialPort serialPort = (SerialPort) this.port;
            serialPort.setSerialPortParams(this.baud, this.databits, this.stopbits, this.parity);
            this.output = new PrintStream(port.getOutputStream(), true);
            this.input = new BufferedReader(new InputStreamReader(port.getInputStream()));
            serialPort.notifyOnDataAvailable(true);
            serialPort.addEventListener(this);
        }
        catch (NoSuchPortException e) {
            LOG.error("No such port {}", this.portName);
        }
        catch (UnsupportedCommOperationException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (PortInUseException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (TooManyListenersException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }

    }

    @Override
    public void process_close() {
        if (this.input != null) {
            try {
                this.input.close();
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            finally {
                this.input = null;
            }
        }
        if (this.output != null) {
            this.output.close();
            this.output = null;
        }
        if (this.port != null) {
            ((SerialPort) this.port).close();
            this.port = null;
        }

    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            String message = "";
            try {
                message = this.input.readLine();
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
            if (this.transportPattern == ITransportPattern.PULL) {
                try {
                    this.pipeOutput.write(message.getBytes());
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
            }
            else {
                super.fireProcess(ByteBuffer.wrap(message.getBytes()));
            }
        }

    }

    @Override
    public RS232TransportHandler clone() {
        return new RS232TransportHandler(this);
    }

    @Override
    public void process(ByteBuffer buffer) throws ClassNotFoundException {
        super.fireProcess(buffer);
    }

    @Override
    public void done() {

    }

    @Override
    public boolean isOpened() {
        return this.port != null;
    }
}
