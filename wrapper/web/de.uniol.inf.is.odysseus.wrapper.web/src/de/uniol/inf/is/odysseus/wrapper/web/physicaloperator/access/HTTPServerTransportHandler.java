/*******************************************************************************
 * Copyright (C) 2015  Christian Kuka <christian@kuka.cc>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package de.uniol.inf.is.odysseus.wrapper.web.physicaloperator.access;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id: HTTPServerTransportHandler.java |
 *          HTTPServerTransportHandler.java | HTTPServerTransportHandler.java $
 *
 */
public class HTTPServerTransportHandler extends AbstractPushTransportHandler {
	/** Logger */
	private final Logger LOG = LoggerFactory.getLogger(HTTPServerTransportHandler.class);
	private static final String PATH = "path";
	private static final String PORT = "port";
	private static final String NAME = "HTTPServer";
	private static final String IMMEDIATE_RESPONSE = "immediate_response";
	private static final String CONTENT_TYPE = "content_type";
	private String path;
	private int port;
	private Server server;
	private String immediateResponse = "ok";
	private String contentType = "text/plain;charset=utf-8";

	protected static Map<String, Queue<AsyncContext>> responseQueues = Collections.synchronizedMap(new HashMap<>());

	protected static synchronized Queue<AsyncContext> getResponseQueue(String path, int port) {
		Queue<AsyncContext> q = responseQueues.get(path + "_" + port);
		if (q == null) {
			q = new ConcurrentLinkedQueue<>();
			responseQueues.put(path + "_" + port, q);
		}
		return q;
	}

	/**
	* 
	*/
	public HTTPServerTransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public HTTPServerTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
		super(protocolHandler, options);
		init(options);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		final HTTPServerTransportHandler handler = new HTTPServerTransportHandler(protocolHandler, options);
		return handler;
	}

	protected void init(OptionMap options) {
		setPath(options.get(PATH, "/"));
		setPort(options.getInt(PORT, 8080));
		if (options.containsKey(IMMEDIATE_RESPONSE)) {
			if (options.get(IMMEDIATE_RESPONSE).equals("false")) {
				immediateResponse = null;
			} else {
				immediateResponse = options.getString(IMMEDIATE_RESPONSE);
			}
		}
		contentType = options.get(CONTENT_TYPE, contentType);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return NAME;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processInOpen() throws IOException {
		this.server = new Server(getPort());
		this.server.setHandler(new DataHandler());
		try {
			this.server.start();
		} catch (Exception e) {
			throw new IOException(e);
		}
		responseQueues = Collections.synchronizedMap(new HashMap<>());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processInClose() throws IOException {
		try {
			this.server.stop();
		} catch (Throwable e) {
			throw new IOException(e);
		} finally {
			this.server = null;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void processOutClose() throws IOException {
		Queue<AsyncContext> queue = getResponseQueue(path, port);
		while(!queue.isEmpty()) {
			AsyncContext asyncContext = queue.poll();
			asyncContext.complete();
		}
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String getPath() {
		return this.path;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getPort() {
		return this.port;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public synchronized void send(byte[] message) throws IOException {
		AsyncContext asyncContext = getResponseQueue(path, port).poll();
		if (asyncContext != null) {
			try {
				ServletResponse response = asyncContext.getResponse();
				response.setContentType(contentType);
				((HttpServletResponse) response).setStatus(HttpServletResponse.SC_OK);
				response.getOutputStream().write(message);
				response.flushBuffer();
				asyncContext.complete();
			} catch (Exception e) {
				LOG.warn("Exception on sending response.", e);
			}
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof HTTPServerTransportHandler)) {
			return false;
		}
		HTTPServerTransportHandler other = (HTTPServerTransportHandler) o;
		if (this.getPath() != null && other.getPath() != null && this.getPath().equals(other.getPath())
				&& this.getPort() == other.getPort()) {
			return true;
		}
		return false;
	}

	private class DataHandler extends AbstractHandler {

		/**
		 * Class constructor.
		 * 
		 * @param httpServerTransportHandler
		 *
		 */
		public DataHandler() {
		}

		@Override
		public synchronized void handle(String target, Request baseRequest, HttpServletRequest request,
				HttpServletResponse response) throws IOException, ServletException {
			if ((target != null) && (target.equals(getPath()))) {
				if (immediateResponse != null) {
					response.setContentType(contentType);
					response.setStatus(HttpServletResponse.SC_OK);
					response.getWriter().println(immediateResponse);
				} else {
					AsyncContext asyncContext = request.startAsync();
					HTTPServerTransportHandler.getResponseQueue(getPath(), getPort()).add(asyncContext);
				}
				ServletInputStream inputStream = request.getInputStream();
				// TODO: Consider alternative:
				fireProcess(inputStream);
				// ByteBuffer buffer =
				// ByteBuffer.allocate(inputStream.available() + Integer.SIZE /
				// 8);
				// buffer.putInt(inputStream.available());
				// while (inputStream.available() > 0) {
				// buffer.put((byte) inputStream.read());
				// }
				// fireProcess(buffer);
			} else {
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
			}
			if (immediateResponse != null) {
				response.flushBuffer();
			}
		}

	}

}
