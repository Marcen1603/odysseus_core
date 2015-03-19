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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.mortbay.jetty.Server;
import org.mortbay.jetty.handler.AbstractHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class HTTPServerTransportHandler extends AbstractPushTransportHandler {
    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(HTTPServerTransportHandler.class);
    private static final String URI = "uri";
    private static final String PORT = "port";
    private static final String NAME = "HTTPServer";
    private String uri;
    private Method method;
    private int port;
    private Server server;

    public static enum Method {
        GET, POST, PUT, DELETE, HEAD;

        public static Method fromString(final String method) {
            try {
                return Method.valueOf(method.toUpperCase());
            }
            catch (final Exception e) {
                return GET;
            }
        }
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
        if (options.get(URI) != null) {
            setURI(options.get(URI));
        }
        setPort(options.getInt(PORT, 8080));

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
        server = new Server(getPort());
        server.setHandler(new DataHandler());
        try {
            server.start();
        }
        catch (Exception e) {
            throw new IOException(e);
        }

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
            server.stop();
        }
        catch (Throwable e) {
            throw new IOException(e);
        }
        finally {
            server = null;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        // TODO Auto-generated method stub

    }

    public void setURI(String uri) {
        this.uri = uri;
    }

    public String getURI() {
        return this.uri;
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
    public void send(byte[] message) throws IOException {
        // TODO Auto-generated method stub

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
        if (this.getURI() == null && other.getURI() == null) {
            return true;
        }
        else if (this.getURI() != null && other.getURI() != null && this.getURI().equals(other.getURI())) {
            return true;
        }

        return false;
    }

    private class DataHandler extends AbstractHandler {

        /**
         * Class constructor.
         *
         */
        public DataHandler() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void handle(String target, HttpServletRequest request, HttpServletResponse response, int dispatch) throws IOException, ServletException {
            response.setContentType("text/html;charset=utf-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println("<h1>Hello World</h1>");

            fireProcess(request.getInputStream());
        }
    }
}
