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
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.ByteArrayRequestEntity;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.HeadMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HTTPTransportHandler extends AbstractTransportHandler {
	/** Logger */
	private Logger LOG = LoggerFactory.getLogger(HTTPTransportHandler.class);
	/** HTTP Client used for send command */
	private final HttpClient client = new HttpClient();
	/** In and output for data transfer */
	private InputStream input;
	private OutputStream output;
	private String uri;
	private Method method;
	@SuppressWarnings("unused")
	private IAccessPattern transportPattern;

	public static enum Method {
		GET, POST, PUT, DELETE, HEAD;

		public static Method fromString(String method) {
			try {
				return Method.valueOf(method.toUpperCase());
			} catch (Exception e) {
				return GET;
			}
		}
	}

	/**
 * 
 */
	public HTTPTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public HTTPTransportHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
		PostMethod request = new PostMethod(message.toString());
		RequestEntity postRequestEntity = new ByteArrayRequestEntity(message);
		request.setRequestEntity(postRequestEntity);
		this.client.executeMethod(request);
	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		HTTPTransportHandler handler = new HTTPTransportHandler(protocolHandler);
		handler.uri = options.get("uri");
		String method = options.get("method");
		if ((method != null) && (!method.isEmpty())) {
			handler.method = Method.fromString(method);
		} else {
			handler.method = Method.GET;
		}
		return handler;
	}

	@Override
	public InputStream getInputStream() {
		return this.input;
	}

	@Override
	public String getName() {
		return "HTTP";
	}

	@Override
	public OutputStream getOutputStream() {
		return this.output;
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		this.input = new HTTPInputStream(this.method, this.uri);
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {
		this.output = new HTTPOutputStream(this.method, this.uri);
	}

	@Override
	public void processInClose() throws IOException {
		this.input = null;
		fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		this.output = null;
		fireOnDisconnect();
	}

	private class HTTPInputStream extends InputStream {
		/** HTTP Client */
		private final HttpClient client = new HttpClient();
		private final Method method;
		private final String uri;
		private InputStream stream;

		public HTTPInputStream(Method method, String uri) {
			this.method = method;
			this.uri = uri;
		}

		@Override
		public int read() throws IOException {
			if (isStreamEmpty()) {
				fetch();
			}
			return this.stream.read();
		}

		@Override
		public int available() throws IOException {
			if (isStreamEmpty()) {
				fetch();
			}
			return stream.available();
		}

		private boolean isStreamEmpty() throws IOException {
			return (this.stream == null) || (this.stream.available() == 0);
		}

		private void fetch() throws HttpException, IOException {
			HttpMethod request = null;
			switch (this.method) {
			case POST:
				request = new PostMethod(this.uri);
				break;
			case PUT:
				request = new PutMethod(this.uri);
				break;
			case DELETE:
				request = new DeleteMethod(this.uri);
				break;
			case HEAD:
				request = new HeadMethod(this.uri);
				break;
			case GET:
			default:
				request = new GetMethod(this.uri);
			}
			this.client.executeMethod(request);
			this.stream = request.getResponseBodyAsStream();
		}
	}

	private class HTTPOutputStream extends OutputStream {
		/** HTTP Client */
		private final HttpClient client = new HttpClient();
		private ByteBuffer buffer = ByteBuffer.allocate(1024);
		private final Method method;
		private final String uri;

		public HTTPOutputStream(Method method, String uri) {
			this.method = method;
			this.uri = uri;
		}

		@Override
		public void write(int b) throws IOException {
			if (1 + buffer.position() >= buffer.capacity()) {
				ByteBuffer newBuffer = ByteBuffer.allocate((1 + buffer
						.position()) * 2);
				int pos = buffer.position();
				buffer.flip();
				newBuffer.put(buffer);
				buffer = newBuffer;
				buffer.position(pos);
				LOG.debug("Extending buffer to " + buffer.capacity());
			}
			buffer.put((byte) b);
		}

		@Override
		public void flush() throws IOException {
			HttpMethod request = null;
			switch (this.method) {
			case POST:
				request = new PostMethod(this.uri);
				RequestEntity postRequestEntity = new ByteArrayRequestEntity(
						buffer.array());
				((PostMethod) request).setRequestEntity(postRequestEntity);
				break;
			case PUT:
				request = new PutMethod(this.uri);
				RequestEntity putRequestEntity = new ByteArrayRequestEntity(
						buffer.array());
				((PutMethod) request).setRequestEntity(putRequestEntity);
				break;
			case DELETE:
				request = new DeleteMethod(this.uri);
				break;
			case HEAD:
				request = new HeadMethod(this.uri);
				break;
			case GET:
			default:
				request = new GetMethod(this.uri);
			}
			this.client.executeMethod(request);
			
		}
	}
}
