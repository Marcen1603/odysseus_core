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
package de.uniol.inf.is.odysseus.wrapper.html.physicaloperator.access;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandlerListener;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @param <T>
 */
public class HTTPProtocolHandler<T> extends AbstractProtocolHandler<T> implements ITransportHandlerListener<ByteBuffer> {
    private static final Logger LOG = LoggerFactory.getLogger(HTTPProtocolHandler.class);
    private String              url;
    private BufferedReader      reader;
    private long                delay;

    /**
 * 
 */
    public HTTPProtocolHandler() {

    }

    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public IProtocolHandler<T> createInstance(Map<String, String> options, ITransportHandler transportHandler,
            IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
        HTTPProtocolHandler<T> instance = new HTTPProtocolHandler<T>();
        instance.url = options.get("url");
        instance.setDataHandler(dataHandler);
        instance.setTransportHandler(transportHandler);
        instance.setTransfer(transfer);
        transportHandler.addListener(instance);
        return instance;
    }

    @Override
    public void open() throws UnknownHostException, IOException {
        reader = new BufferedReader(new InputStreamReader(getTransportHandler().getInputStream()));
        getTransportHandler().open();
    }

    @Override
    public void process(ByteBuffer message) {
        getTransfer().transfer(getDataHandler().readData(message));
    }

    @Override
    public boolean hasNext() throws IOException {
        write(this.url.getBytes());
        delay();
        return getTransportHandler().getInputStream().available() > 0;
    }

    @Override
    public T getNext() throws IOException {
        StringBuilder builder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }
        return getDataHandler().readData(builder.toString());
    }

    @Override
    public void close() throws IOException {
        reader.close();
        getTransportHandler().close();
    }

    @Override
    public void write(byte[] message) throws IOException {
        getTransportHandler().send(message);
    }

    /**
     * @return the delay bewteen requests
     */
    public long getDelay() {
        return delay;
    }

    /**
     * @param delay
     */
    public void setDelay(long delay) {
        this.delay = delay;
    }

    @Override
    public void onConnect(ITransportHandler caller) {

    }

    @Override
    public void onDisonnect(ITransportHandler caller) {

    }

    private void delay() {
        if (delay > 0) {
            try {
                Thread.sleep(delay);
            }
            catch (InterruptedException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }

}
