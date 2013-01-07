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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportExchangePattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class MarkerByteBufferHandler<T> extends AbstractByteBufferHandler<T> {

    private static final Logger    LOG = LoggerFactory.getLogger(MarkerByteBufferHandler.class);
    protected ByteBufferHandler<T> objectHandler;
    protected byte                 start;
    protected byte                 end;

    public MarkerByteBufferHandler() {
        super();
    }

    public MarkerByteBufferHandler(ITransportDirection direction, IAccessPattern access) {
        super(direction, access);
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        getTransportHandler().open();
    }

    @Override
    public void close() throws IOException {
        getTransportHandler().close();
    }

    @Override
    public void write(T object) throws IOException {
        throw new IllegalArgumentException("Currently not implemented");
    }

    @Override
    public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access,
            Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
        MarkerByteBufferHandler<T> instance = new MarkerByteBufferHandler<T>(direction, access);
        instance.setDataHandler(dataHandler);
        instance.setTransfer(transfer);
        instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
        instance.start = Byte.parseByte(options.get("start"));
        instance.end = Byte.parseByte(options.get("end"));
        instance.setByteOrder(options.get("byteorder"));
        return instance;
    }

    @Override
    public String getName() {
        return "MarkerByteBuffer";
    }

    @Override
    public void onConnect(ITransportHandler caller) {
    }

    @Override
    public void onDisonnect(ITransportHandler caller) {
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
    public ITransportExchangePattern getExchangePattern() {
        if (this.getDirection().equals(ITransportDirection.IN)) {
            return ITransportExchangePattern.InOnly;
        }
        else {
            return ITransportExchangePattern.OutOnly;
        }
    }
}
