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
package de.uniol.inf.is.odysseus.wrapper.sick.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.sick.SICKConstants;

/**
 * Protocol Handler for the SICK protocol supporting LMS100 and LMS151 laser
 * scanner
 * 
 * @author Christian Kuka <christian.kuka@offis.de>
 */
public class SICKProtocolHandler<T> extends AbstractByteBufferHandler<T> {
    private static final Logger    LOG     = LoggerFactory.getLogger(SICKProtocolHandler.class);
    private final Charset          charset = Charset.forName("ASCII");
    protected ByteBufferHandler<T> objectHandler;
    private byte                   start;
    private byte                   end;
    @SuppressWarnings("unused")
    private String                 password;
    private String                 username;

    @Override
    public String getName() {
        return "SICK";
    }

    @Override
    public IProtocolHandler<T> createInstance(Map<String, String> options, ITransportHandler transportHandler,
            IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
        SICKProtocolHandler<T> instance = new SICKProtocolHandler<T>();
        instance.setDataHandler(dataHandler);
        instance.setTransportHandler(transportHandler);
        instance.setTransfer(transfer);
        instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
        instance.start = SICKConstants.START;
        instance.end = SICKConstants.END;
        if (options.get("byteorder") != null) {
            instance.setByteOrder(options.get("byteorder"));
        }
        if (options.get("username") != null) {
            instance.setUsername(options.get("username"));
        }
        else {
            instance.setUsername("client");
        }
        if (options.get("password") != null) {
            instance.setPassword(options.get("password"));
        }
        else {
            instance.setPassword("client");
        }
        transportHandler.addListener(instance);
        return instance;
    }

    // FIXME Currently not used
    private void setPassword(String password) {
        this.password = password;

    }

    private void setUsername(String username) {
        this.username = username;
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
    }

    @Override
    public void onConnect(ITransportHandler caller) {
        try {
            LOG.debug("Connected");
            if (this.username.equalsIgnoreCase("maintainer")) {
                this.write(SICKConstants.SET_ACCESS_MODE_MAINTAINER_COMMAND.getBytes(charset));
            }
            else if (this.username.equalsIgnoreCase("client")) {
                this.write(SICKConstants.SET_ACCESS_MODE_CLIENT_COMMAND.getBytes(charset));
            }
            else if (this.username.equalsIgnoreCase("service")) {
                this.write(SICKConstants.SET_ACCESS_MODE_SERVICE_COMMAND.getBytes(charset));
            }
            Thread.sleep(1000);
            Calendar now = Calendar.getInstance(TimeZone.getTimeZone("UTC"));

            StringBuilder timeString = new StringBuilder();
            timeString.append(SICKConstants.SET_DATETIME_COMMAND).append(" ");
            timeString.append("+").append(now.get(Calendar.YEAR)).append(" ");
            timeString.append("+").append((now.get(Calendar.MONTH) + 1)).append(" ");
            timeString.append("+").append( now.get(Calendar.DATE)).append(" ");
            timeString.append("+").append(now.get(Calendar.HOUR_OF_DAY)).append(" ");
            timeString.append("+").append(now.get(Calendar.MINUTE)).append(" ");
            timeString.append("+").append( now.get(Calendar.SECOND)).append(" ");
            timeString.append("+").append(now.get(Calendar.MILLISECOND));

            this.write(timeString.toString().getBytes(charset));
            Thread.sleep(1000);
            this.write(SICKConstants.STOP_SCAN_COMMAND.getBytes(charset));
            Thread.sleep(1000);
            this.write(SICKConstants.START_SCAN_COMMAND.getBytes(charset));
        }
        catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
        catch (InterruptedException e) {
            LOG.error(e.getMessage(), e);
        }
    }

    @Override
    public void close() throws IOException {
        this.write(SICKConstants.STOP_SCAN_COMMAND.getBytes(charset));
        getTransportHandler().close();
    }

    @Override
    public void process(ByteBuffer message) {
        message.flip();
        int startPosition = 0;
        while (message.remaining() > 0) {
            byte value = message.get();
            if (value == end) {
                int endPosition = message.position() - 2;
                message.position(startPosition);
                try {
                    objectHandler.put(message, endPosition - message.position() + 1);
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                message.position(endPosition + 2);
                startPosition = message.position() + 1;
                T object = null;
                try {
                    object = objectHandler.create();
                }
                catch (BufferUnderflowException e) {
                    LOG.error(e.getMessage(), e);
                }
                catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                catch (ClassNotFoundException e) {
                    LOG.error(e.getMessage(), e);
                }
                if (object != null) {
                    getTransfer().transfer(object);
                }
                else {
                    LOG.error("Empty object");
                }
            }
            if (value == start) {
                objectHandler.clear();
                startPosition = message.position();
            }
        }
        if (startPosition < message.limit()) {
            message.position(startPosition);
            try {
                objectHandler.put(message);
            }
            catch (IOException e) {
                LOG.error(e.getMessage(), e);
            }
        }
        message.clear();
    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
        LOG.debug(" Disconnected");
    }

    @Override
    public void write(byte[] message) throws IOException {
        CharsetDecoder decoder = this.charset.newDecoder();
        try {
            final byte[] messageBuffer = new byte[message.length + 2];
            messageBuffer[0] = SICKConstants.START;
            System.arraycopy(message, 0, messageBuffer, 1, message.length);
            messageBuffer[messageBuffer.length - 1] = SICKConstants.END;
            getTransportHandler().send(messageBuffer);

            LOG.debug("SICK: Send message {}", decoder.decode(ByteBuffer.wrap(message)));
        }
        catch (final IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }
}
