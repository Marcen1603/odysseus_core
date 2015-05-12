/*******************************************************************************
 * Copyright 2014 The Odysseus Team
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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Mazen Salous
 */
public class PeriodicHTTPTransportHandler extends AbstractPushTransportHandler {
	
	private int period = 5000;
	Timer timer = new Timer();
	
	/** Logger */
    private final Logger LOG = LoggerFactory.getLogger(PeriodicHTTPTransportHandler.class);
    
    /** HTTP Client used for send command */
    protected final HttpClient client = new DefaultHttpClient();
    protected String uri;
    protected Method method;
    protected String body;
    @SuppressWarnings("unused")
    private IAccessPattern transportPattern;

    public static enum Method {
        GET, POST, PUT, DELETE, HEAD;

        public static Method fromString(final String method) {
            try {
                return Method.valueOf(method.toUpperCase());
            } catch (final Exception e) {
                return GET;
            }
        }
    }

   
	/**
	 * * 
	 * */
    public PeriodicHTTPTransportHandler() {
        super();
    }

    /**
     * @param protocolHandler
     */
    public PeriodicHTTPTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
        init(options);
        this.period = options.getInt("period", 5000);
    }
    
    protected void init(OptionMap options) {
        if (options.get("uri") != null) {
            setURI(options.get("uri"));
        }
        if (options.get("method") != null) {
            setMethod(Method.fromString(options.get("method")));
        } else {
            setMethod(Method.GET);
        }
        if (options.get("body") != null) {
            setBody(options.get("body"));
        } else {
            setBody("");
        }
    }
    
    public void setMethod(Method method) {
        this.method = method;
    }

    public Method getMethod() {
        return this.method;
    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getBody() {
        return this.body;
    }
    
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final PeriodicHTTPTransportHandler handler = new PeriodicHTTPTransportHandler(protocolHandler, options);
        return handler;
    }
    
    @Override
    public String getName() {
        return "PeriodicHTTP";
    }

	@Override
	public void processInOpen() throws IOException {
		TimerTask periodicHTTP = new TimerTask() {
			
			@Override
			public void run() {
				HttpRequestBase request = null;
		          switch (PeriodicHTTPTransportHandler.this.method) {
		          case POST:
		              request = new HttpPost(PeriodicHTTPTransportHandler.this.uri);
		              final InputStreamEntity postRequestEntity = new InputStreamEntity(new ByteArrayInputStream(body.getBytes()), -1);
		              postRequestEntity.setContentType("binary/octet-stream");
		              postRequestEntity.setChunked(true);
		              ((HttpPost) request).setEntity(postRequestEntity);
		              break;
		          case PUT:
		              request = new HttpPut(PeriodicHTTPTransportHandler.this.uri);
		              final InputStreamEntity putRequestEntity = new InputStreamEntity(new ByteArrayInputStream(body.getBytes()), -1);
		              putRequestEntity.setContentType("binary/octet-stream");
		              putRequestEntity.setChunked(true);
		              ((HttpPut) request).setEntity(putRequestEntity);
		              break;
		          case DELETE:
		              request = new HttpDelete(PeriodicHTTPTransportHandler.this.uri);
		              break;
		          case HEAD:
		              request = new HttpHead(PeriodicHTTPTransportHandler.this.uri);
		              break;
		          case GET:
		          default:
		              request = new HttpGet(PeriodicHTTPTransportHandler.this.getURI());
		          }
		          try {
		              HttpResponse response = PeriodicHTTPTransportHandler.this.client.execute(request);
		              HttpEntity entity = response.getEntity();
		              if (entity != null) {
		                  byte[] byteArray = EntityUtils.toByteArray(entity);
		                  ByteBuffer byteBuffer = ByteBuffer.allocate(byteArray.length);
		                  byteBuffer.put(byteArray);
		                  PeriodicHTTPTransportHandler.this.fireProcess(byteBuffer);
		              }
		              if (LOG.isDebugEnabled()) {
		                  LOG.debug(request.getRequestLine().toString());
		              }
		          } catch (Exception e) {
		              LOG.error(e.getMessage(), e);
		          }
			}
		};
		this.timer.schedule(periodicHTTP, 0, this.period);
	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public void processInClose() throws IOException {
		this.timer.cancel();
	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub
	}
}
