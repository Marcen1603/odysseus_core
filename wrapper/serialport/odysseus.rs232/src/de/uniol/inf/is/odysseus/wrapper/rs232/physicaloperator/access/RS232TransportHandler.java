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
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Pair;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * Generic transport handler for RS232
 *
 * @author Christian Kuka <christian@kuka.cc>
 */
public class RS232TransportHandler extends AbstractTransportHandler implements SerialPortEventListener {
    /** How long to wait for the open to finish up. */
    public static final int TIMEOUTSECONDS = 30;
    private static final Map<String, Pair<CommPort, Integer>> ports = new HashMap<>();
    /** Logger */
    private static final Logger LOG = LoggerFactory.getLogger(RS232TransportHandler.class);

    private final static String PORT = "port";
    private final static String BAUD = "baud";
    private final static String PARITY = "parity";
    private final static String DATABITS = "databits";
    private final static String STOPBITS = "stopbits";
    private final static String READLINES = "readlines";

    /** The output stream */
    private PrintStream output;
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
        RS232TransportHandler.initializeSerialPorts();
    }

    public RS232TransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        RS232TransportHandler.initializeSerialPorts();
        this.init(options);
    }

    protected void init(final OptionMap options) {
        if (options.containsKey(RS232TransportHandler.PORT)) {
            this.portName = options.get(RS232TransportHandler.PORT);
        }
        if (options.containsKey(RS232TransportHandler.BAUD)) {
            this.baud = Integer.parseInt(options.get(RS232TransportHandler.BAUD));
        }
        else {
            this.baud = 9600;
        }
        if (options.containsKey(RS232TransportHandler.PARITY)) {
            this.parity = Integer.parseInt(options.get(RS232TransportHandler.PARITY));
        }
        else {
            this.parity = SerialPort.PARITY_NONE;
        }
        if (options.containsKey(RS232TransportHandler.DATABITS)) {
            this.databits = Integer.parseInt(options.get(RS232TransportHandler.DATABITS));
        }
        else {
            this.databits = SerialPort.DATABITS_8;
        }
        if (options.containsKey(RS232TransportHandler.STOPBITS)) {
            this.stopbits = Integer.parseInt(options.get(RS232TransportHandler.STOPBITS));
        }
        else {
            this.stopbits = SerialPort.STOPBITS_1;
        }
        if (options.containsKey(RS232TransportHandler.READLINES)) {
            this.readLines = Boolean.parseBoolean(options.get(RS232TransportHandler.READLINES));
        }
        else {
            this.readLines = true;
        }
    }

    @Override
    public void send(final byte[] message) throws IOException {
        if (this.output != null) {
            this.output.write(message);
            RS232TransportHandler.LOG.trace("> {}", new String(message, "UTF-8"));
        }
        else {
            RS232TransportHandler.LOG.error("Trying to write to a not opened connection!");
            throw new IOException("Trying to write to a not opened connection!");
        }
    }

    @Override
    public InputStream getInputStream() {
        try {
            return this.port.getInputStream();
        }
        catch (final IOException e) {
            RS232TransportHandler.LOG.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getName() {
        return "RS232";
    }

    @Override
    public void serialEvent(final SerialPortEvent event) {
        if (event.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            try {
                if (this.input.ready()) {
                    if (this.readLines) {
                        String message = "";
                        message = this.input.readLine();
                        ///
                        byte[] byteArray = message.getBytes();
                        ByteBuffer byteBufer = ByteBuffer.allocate(byteArray.length);
                        byteBufer.put(byteArray);
                        super.fireProcess(byteBufer);
                        //super.fireProcess(ByteBuffer.wrap(message.getBytes()));
                        ///
                    }
                    else {
                        final ByteBuffer buffer = ByteBuffer.allocate(1024);
                        while (this.input.ready()) {
                        	 final Integer r = this.input.read();
                             buffer.put(r.byteValue());
//                            final int r = this.input.read();
//                            buffer.putInt(r);
                        }
                        super.fireProcess(buffer);
                    }

                }
            }
            catch (final IOException e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
        }

    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        return new RS232TransportHandler(protocolHandler, options);
    }

    @Override
    public OutputStream getOutputStream() {
        return this.output;
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {
        try {
            this.port = RS232TransportHandler.openPort(this.getPortName());
            final SerialPort serialPort = (SerialPort) this.port;
            serialPort.setSerialPortParams(this.getBaud(), this.getDatabits(), this.getStopbits(), this.getParity());
            serialPort.notifyOnDataAvailable(true);
            serialPort.addEventListener(this);
            this.input = new BufferedReader(new InputStreamReader(this.port.getInputStream()));
            // Also open an output stream to communicate with the device via an
            // protocol handler
            this.output = new PrintStream(this.port.getOutputStream(), true);
        }
        catch (final NoSuchPortException e) {
            RS232TransportHandler.LOG.error("No such port {}", this.portName);
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (final UnsupportedCommOperationException e) {
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (final PortInUseException e) {
            RS232TransportHandler.LOG.error("Port {} in use", this.portName);
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (final TooManyListenersException e) {
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
        try {
            this.port = RS232TransportHandler.openPort(this.getPortName());
            final SerialPort serialPort = (SerialPort) this.port;
            serialPort.setSerialPortParams(this.getBaud(), this.getDatabits(), this.getStopbits(), this.getParity());
            this.output = new PrintStream(this.port.getOutputStream(), true);
        }
        catch (final NoSuchPortException e) {
            RS232TransportHandler.LOG.error("No such port {}", this.portName);
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (final UnsupportedCommOperationException e) {
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (final PortInUseException e) {
            RS232TransportHandler.LOG.error("Port {} in use", this.portName);
            RS232TransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }

    }

    @Override
    public void processInClose() throws IOException {
        if (this.input != null) {
            try {
                this.input.close();
            }
            catch (final Exception e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                this.input = null;
            }
        }
        if (this.output != null) {
            try {

                this.output.close();
            }
            catch (final Exception e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                this.output = null;
            }
        }
        if (this.port != null) {
            try {
                final SerialPort serialPort = (SerialPort) this.port;
                serialPort.removeEventListener();
                RS232TransportHandler.closePort(this.portName);
            }
            catch (final Exception e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                this.port = null;
            }
        }
    }

    @Override
    public void processOutClose() throws IOException {
        if (this.output != null) {
            try {

                this.output.close();
            }
            catch (final Exception e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                this.output = null;
            }
        }
        if (this.port != null) {
            try {
                RS232TransportHandler.closePort(this.portName);
            }
            catch (final Exception e) {
                RS232TransportHandler.LOG.error(e.getMessage(), e);
            }
            finally {
                this.port = null;
            }
        }
    }

    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof RS232TransportHandler)) {
            return false;
        }
        final RS232TransportHandler other = (RS232TransportHandler) o;
        if (!this.getPortName().equals(other.getPortName())) {
            return false;
        }
        else if (this.getBaud() != other.getBaud()) {
            return false;
        }
        else if (this.getParity() != other.getParity()) {
            return false;
        }
        else if (this.getDatabits() != other.getDatabits()) {
            return false;
        }
        else if (this.getStopbits() != other.getStopbits()) {
            return false;
        }

        return true;
    }

    public String getPortName() {
        return this.portName;
    }

    public int getBaud() {
        return this.baud;
    }

    public int getStopbits() {
        return this.stopbits;
    }

    public int getParity() {
        return this.parity;
    }

    public int getDatabits() {
        return this.databits;
    }

    private static CommPort openPort(final String name) throws NoSuchPortException, PortInUseException {
        final CommPort commPort;
        synchronized (RS232TransportHandler.ports) {
            if (!RS232TransportHandler.ports.containsKey(name)) {
                final CommPortIdentifier portId = CommPortIdentifier.getPortIdentifier(name);
                commPort = portId.open("RS232: " + UUID.randomUUID(), RS232TransportHandler.TIMEOUTSECONDS * 1000);
                RS232TransportHandler.ports.put(name, new Pair<>(commPort, new Integer(1)));
            }
            else {
                final Pair<CommPort, Integer> entry = RS232TransportHandler.ports.get(name);
                commPort = entry.getE1();
                int num = entry.getE2().intValue();
                RS232TransportHandler.ports.put(name, new Pair<>(commPort, new Integer(num++)));
            }
        }
        return commPort;
    }

    private static void closePort(final String name) {
        final CommPort commPort;
        synchronized (RS232TransportHandler.ports) {
            if (RS232TransportHandler.ports.containsKey(name)) {
                final Pair<CommPort, Integer> entry = RS232TransportHandler.ports.get(name);
                commPort = entry.getE1();
                int num = entry.getE2().intValue();
                if (num == 1) {
                    RS232TransportHandler.ports.remove(name);
                    commPort.close();
                }
                else {
                    RS232TransportHandler.ports.put(name, new Pair<>(commPort, new Integer(num--)));
                }
            }
        }
    }

    private static void initializeSerialPorts() {
        // Bugfix for Linux/Unix access to /dev/ttyACM*
        final String os = System.getProperty("os.name").toLowerCase();
        if (((os.indexOf("nix") >= 0) || (os.indexOf("nux") >= 0))) {
            final StringBuilder serialPorts = new StringBuilder();
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
}
