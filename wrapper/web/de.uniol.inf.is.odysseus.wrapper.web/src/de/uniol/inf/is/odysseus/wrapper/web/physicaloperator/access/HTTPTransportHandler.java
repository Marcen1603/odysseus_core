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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.InputStreamEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 */
public class HTTPTransportHandler extends AbstractPullTransportHandler {
	private static final String BINARY_OCTET_STREAM = "binary/octet-stream";
	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(HTTPTransportHandler.class);
	/** HTTP Client used for send command */
	private HttpClient client;
	/** In and output for data transfer */
	private InputStream input;
	private OutputStream output;
	private String uri;
	private Method method;
	private String contentType;
	private Header[] headers;
	private String body;

	/**
	 * Specifies the 'chunked' flag of the {@linkplain InputStreamEntity}. This
	 * option will be set by {@link HTTPTransportHandler#init(OptionMap)}.
	 *
	 * @see AbstractHttpEntity#setChunked(boolean)
	 */
	private boolean chunked = true;

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
	*
	*/
	public HTTPTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public HTTPTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		try {
			/*
			 * Client needs to be created every single time as the connection is
			 * shutdown every time.
			 */
			this.client = new DefaultHttpClient();

			HttpRequestBase request = null;
			switch (this.method) {
			case PUT:
				request = new HttpPut(this.uri);
				/*
				 * TODO: should the message length in case of chunked = false
				 * set to message.length() ?
				 */
				final InputStreamEntity putRequestEntity = new InputStreamEntity(new ByteArrayInputStream(message), -1);
				putRequestEntity.setContentType(contentType);
				putRequestEntity.setChunked(chunked);
				((HttpPut) request).setEntity(putRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}
				break;
			case DELETE:
				request = new HttpDelete(this.uri);
				break;
			case HEAD:
				request = new HttpHead(this.uri);
				break;
			case GET:
				request = new HttpGet(this.uri);
				break;
			case POST:
			default:
				request = new HttpPost(this.uri);
				/*
				 * TODO: should the message length in case of chunked = false
				 * set to message.length() ?
				 */
				final InputStreamEntity postRequestEntity = new InputStreamEntity(new ByteArrayInputStream(message),
						-1);
				postRequestEntity.setContentType(contentType);
				postRequestEntity.setChunked(chunked);
				((HttpPost) request).setEntity(postRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}
			}

			this.client.execute(request);

		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
		final HTTPTransportHandler handler = new HTTPTransportHandler(protocolHandler, options);
		return handler;
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

		contentType = options.get("contenttype", BINARY_OCTET_STREAM);

		if (options.containsKey("httpheader")) {
			String[] tokens = options.get("httpheader").split(",");
			if (tokens.length % 2 != 0) {
				throw new IllegalArgumentException(
						"HttpHeader not correclty formatted. Use key1,value1,key2,value2, ...");
			}
			this.headers = new Header[tokens.length / 2];
			for (int i = 0; i < tokens.length / 2; i++) {
				this.headers[i] = new BasicHeader(tokens[i * 2], tokens[(i * 2) + 1]);
			}
		}

		chunked = options.getBoolean("chunked", true);
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
		this.fireOnDisconnect();
	}

	@Override
	public void processOutClose() throws IOException {
		this.client.getConnectionManager().shutdown();
		this.output = null;
		this.fireOnDisconnect();
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

	private class HTTPInputStream extends InputStream {
		/** HTTP Client */
		private final HttpClient client = new DefaultHttpClient();
		private final Method method;
		private final String uri;
		private ByteBuffer buffer = ByteBuffer.allocate(1024);
		private boolean refetch = false;

		public HTTPInputStream(final Method method, final String uri) {
			this.method = method;
			this.uri = uri;
			try {
				fetch();
			} catch (HttpException | IOException e) {
				LOG.error(e.getMessage(), e);
			}
		}

		@Override
		public synchronized int read() throws IOException {
			if (isRefetch()) {
				try {
					setRefetch(false);
					this.fetch();
				} catch (HttpException e) {
					LOG.error(e.getMessage(), e);
					throw new IOException(e);
				}
				return -1;
			}
			if (buffer.remaining() == 0) {
				setRefetch(true);
				return -1;
			}
			return buffer.get() & 0xFF;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			if (isRefetch()) {
				try {
					setRefetch(false);
					this.fetch();
				} catch (HttpException e) {
					LOG.error(e.getMessage(), e);
					throw new IOException(e);
				}
				return -1;
			}

			if (buffer.remaining() == 0) {
				setRefetch(true);
				return -1;
			}
			len = Math.min(len, buffer.remaining());
			buffer.get(b, off, len);
			return len;
		}

		@Override
		public void close() throws IOException {
			try {
				buffer.clear();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			} finally {
				// this.client.getConnectionManager().shutdown();
			}
		}

		@Override
		public int available() throws IOException {
			return this.buffer.remaining();
		}

		private boolean isRefetch() {
			return this.refetch;
		}

		private void setRefetch(boolean refetch) {
			this.refetch = refetch;
		}

		private void fetch() throws HttpException, IOException {
			HttpRequestBase request = null;
			switch (this.method) {
			case POST:
				request = new HttpPost(this.uri);
				final InputStreamEntity postRequestEntity = new InputStreamEntity(
						new ByteArrayInputStream(body.getBytes()), -1);
				postRequestEntity.setContentType(contentType);
				postRequestEntity.setChunked(true);
				((HttpPost) request).setEntity(postRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}
				break;
			case PUT:
				request = new HttpPut(this.uri);
				final InputStreamEntity putRequestEntity = new InputStreamEntity(
						new ByteArrayInputStream(body.getBytes()), -1);
				putRequestEntity.setContentType(contentType);
				putRequestEntity.setChunked(true);
				((HttpPut) request).setEntity(putRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}
				break;
			case DELETE:
				request = new HttpDelete(this.uri);
				break;
			case HEAD:
				request = new HttpHead(this.uri);
				break;
			case GET:
			default:
				request = new HttpGet(this.uri);
			}
			try {
				HttpResponse response = this.client.execute(request);
				HttpEntity entity = response.getEntity();
				this.buffer.clear();
				if (entity != null) {
					byte[] b = EntityUtils.toByteArray(entity);
					if (b.length + 1 > this.buffer.capacity()) {
						final ByteBuffer newBuffer = ByteBuffer.allocate(b.length);
						this.buffer = newBuffer;
						HTTPTransportHandler.this.LOG.debug("Extending buffer to " + this.buffer.capacity());
					}
					this.buffer.put(b);
				}
				if (LOG.isDebugEnabled()) {
					LOG.debug(request.getRequestLine().toString());
				}
				this.buffer.flip();
			} catch (Exception e) {
				LOG.error(e.getMessage(), e);
			}
		}
	}

	private class HTTPOutputStream extends OutputStream {
		/** HTTP Client */
		private final HttpClient client = new DefaultHttpClient();
		private ByteBuffer buffer = ByteBuffer.allocate(1024);
		private final Method method;
		private final String uri;

		public HTTPOutputStream(final Method method, final String uri) {
			this.method = method;
			this.uri = uri;
		}

		@Override
		public void write(final int b) throws IOException {
			if ((1 + this.buffer.position()) >= this.buffer.capacity()) {
				final ByteBuffer newBuffer = ByteBuffer.allocate((1 + this.buffer.position()) * 2);
				final int pos = this.buffer.position();
				this.buffer.flip();
				newBuffer.put(this.buffer);
				this.buffer = newBuffer;
				this.buffer.position(pos);
				HTTPTransportHandler.this.LOG.debug("Extending buffer to " + this.buffer.capacity());
			}
			this.buffer.put((byte) b);
		}

		@Override
		public void flush() throws IOException {
			this.buffer.flip();
			HttpRequestBase request = null;
			switch (this.method) {
			case POST:
				request = new HttpPost(this.uri);
				InputStreamEntity postRequestEntity = new InputStreamEntity(
						new ByteArrayInputStream(this.buffer.array()), -1);
				postRequestEntity.setContentType(contentType);
				postRequestEntity.setChunked(true);
				((HttpPost) request).setEntity(postRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}

				break;
			case PUT:
				request = new HttpPut(this.uri);
				InputStreamEntity putRequestEntity = new InputStreamEntity(
						new ByteArrayInputStream(this.buffer.array()), -1);
				putRequestEntity.setContentType(contentType);
				putRequestEntity.setChunked(true);
				((HttpPut) request).setEntity(putRequestEntity);
				if (headers != null) {
					request.setHeaders(headers);
				}

				break;
			case DELETE:
				request = new HttpDelete(this.uri);
				break;
			case HEAD:
				request = new HttpHead(this.uri);
				break;
			case GET:
			default:
				request = new HttpGet(this.uri);
			}
			this.client.execute(request);
			if (LOG.isTraceEnabled()) {
				LOG.trace(request.getRequestLine().toString());
			}

		}

		@Override
		public void close() throws IOException {
			this.client.getConnectionManager().shutdown();
		}
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof HTTPTransportHandler)) {
			return false;
		}
		HTTPTransportHandler other = (HTTPTransportHandler) o;
		if (this.getURI() == null && other.getURI() != null || this.getURI() != null && other.getURI() == null) {
			return false;
		} else if (this.getURI() != null && other.getURI() != null && !this.getURI().equals(other.getURI())) {
			return false;
		} else if (!this.getMethod().equals(other.getMethod())) {
			return false;
		} else if (!this.getBody().equals(other.getBody())) {
			return false;
		}
		return true;
	}
}
