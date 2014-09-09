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
package de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author DGe
 */
public class HTTPStreamTransportHandler extends AbstractPullTransportHandler {
    
    public static final String URI = "uri";
	public static final String NAME = "HTTPStream";
	private InputStream input;
    private OutputStream output;
    private String uri;        
    /**
 * 
 */
    public HTTPStreamTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public HTTPStreamTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
        init(options);
    }

    @Override
    public void send(final byte[] message) throws IOException {
       
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final HTTPStreamTransportHandler handler = new HTTPStreamTransportHandler(protocolHandler, options);
        return handler;
    }

    protected void init(OptionMap options) {
        if (options.get(URI) != null) {
            setURI(options.get(URI));
        }       
    }

    @Override
    public InputStream getInputStream() {
        return this.input;
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.output;
    }

    @Override
    public void processInOpen() throws UnknownHostException, IOException {        
        URL u = new URL(this.uri);
        URLConnection uc = u.openConnection();
        uc.connect();
        this.input = uc.getInputStream();
    }

    @Override
    public void processOutOpen() throws UnknownHostException, IOException {
    	 URL u = new URL(this.uri);
         URLConnection uc = u.openConnection();
         uc.setDoOutput(true);
         uc.connect();
         this.output = uc.getOutputStream();
    }

    @Override
    public void processInClose() throws IOException {
        this.input = null;
        this.fireOnDisconnect();
    }

    @Override
    public void processOutClose() throws IOException {
        this.output = null;
        this.fireOnDisconnect();
    }
    
    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }
    
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
    	if(!(o instanceof HTTPStreamTransportHandler)) {
    		return false;
    	}
    	HTTPStreamTransportHandler other = (HTTPStreamTransportHandler)o;
    	if(this.getURI() == null && other.getURI() == null) {
    		return true;
    	} else if(this.getURI() != null && other.getURI() != null &&
    			this.getURI().equals(other.getURI())) {
    		return true;
    	}
    	
    	return false;
    }
  
}
