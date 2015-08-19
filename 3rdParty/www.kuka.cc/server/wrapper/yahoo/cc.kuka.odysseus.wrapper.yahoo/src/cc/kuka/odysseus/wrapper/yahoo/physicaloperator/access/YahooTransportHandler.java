/*******************************************************************************
 * Copyright (C) 2014  Christian Kuka <christian@kuka.cc>
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
package cc.kuka.odysseus.wrapper.yahoo.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.ByteBuffer;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.protocol.Protocol;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class YahooTransportHandler extends AbstractPullTransportHandler {
    /** The logger. */
    static final Logger LOG = LoggerFactory.getLogger(YahooTransportHandler.class);
    /** The name of the protocol handler. */
    private static final String NAME = "Yahoo";
    /** The Yahoo API. */
    private static final String API = "http://query.yahooapis.com/v1/public/yql";

    /** Init parameter. */
    private static final String YQL = "yql";
    private static final String SIZE = "size";
    /** In and output for data transfer */
    private InputStream input;
    private OutputStream output;
    /** The YQL query. */
    private String yql = "";
    /** The result set size. */
    private int size = 1;

    /**
     *
     * Class constructor.
     *
     */
    public YahooTransportHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param protocolHandler
     * @param options
     */
    public YahooTransportHandler(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        super(protocolHandler, options);
        this.init(options);
    }

    protected void init(final OptionMap options) {
        if (options.containsKey(YahooTransportHandler.YQL)) {
            this.setYQL(options.get(YahooTransportHandler.YQL));
        }
        if (options.containsKey(YahooTransportHandler.SIZE)) {
            this.setSize(Integer.parseInt(options.get(YahooTransportHandler.SIZE)));
        }
    }

    @Override
    public InputStream getInputStream() {
        return this.input;
    }

    @Override
    public OutputStream getOutputStream() {
        return this.output;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        try {
            this.input = new YQLInputStream(this.getYQL(), this.getSize());
        }
        catch (final URISyntaxException e) {
            throw new IOException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.input = null;
        this.fireOnDisconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        throw new IOException("Out not supported");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        throw new IOException("Out not supported");
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        return new YahooTransportHandler(protocolHandler, options);
    }

    /**
     *
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return YahooTransportHandler.NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler o) {
        if (!(o instanceof YahooTransportHandler)) {
            return false;
        }
        final YahooTransportHandler other = (YahooTransportHandler) o;
        if (((this.getYQL() == null) && (other.getYQL() != null)) || ((this.getYQL() != null) && (other.getYQL() == null))) {
            return false;
        }
        else if ((this.getYQL() != null) && (other.getYQL() != null) && !this.getYQL().equals(other.getYQL())) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(final byte[] message) throws IOException {
        // TODO Auto-generated method stub

    }

    /**
     * @param yql
     *            the yql to set
     */
    public void setYQL(final String yql) {
        this.yql = yql;
    }

    /**
     * @return the yql
     */
    public String getYQL() {
        return this.yql;
    }

    /**
     * @param size
     *            the size to set
     */
    public void setSize(int size) {
        this.size = size;
    }

    /**
     * @return the size
     */
    public int getSize() {
        return this.size;
    }

    private class YQLInputStream extends InputStream {
        private ByteBuffer buffer = ByteBuffer.allocate(1024);

        /** HTTP Client */
        private final HttpClient client = new HttpClient();
        private final String yqlQuery;
        private boolean refetch = false;
        private int offset;
        private final int resultSize;

        /**
         *
         * Class constructor.
         *
         * @param yqlQuery
         * @param size
         * @throws URISyntaxException
         * @throws IOException
         * @throws HttpException
         */
        public YQLInputStream(final String yqlQuery, final int size) throws URISyntaxException, HttpException, IOException {
            this.offset = 0;
            this.resultSize = size;
            this.yqlQuery = yqlQuery;
            final URI uri = new URI(YahooTransportHandler.API);
            this.client.getHostConfiguration().setHost(uri.getHost(), uri.getPort(), Protocol.getProtocol(uri.getScheme()));
            this.fetch();
        }

        @Override
        public synchronized int read() throws IOException {
            if (this.isRefetch()) {
                try {
                    this.setRefetch(false);
                    this.fetch();
                }
                catch (final HttpException | URISyntaxException e) {
                    YahooTransportHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e);
                }
                return -1;
            }
            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            return this.buffer.get() & 0xFF;
        }

        @Override
        public int read(final byte[] b, final int off, final int len) throws IOException {
            if (this.isRefetch()) {
                try {
                    this.setRefetch(false);
                    this.fetch();
                }
                catch (final HttpException | URISyntaxException e) {
                    YahooTransportHandler.LOG.error(e.getMessage(), e);
                    throw new IOException(e);
                }
                return -1;
            }

            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            final int length = Math.min(len, this.buffer.remaining());
            this.buffer.get(b, off, length);
            return length;
        }

        @Override
        public void close() throws IOException {
            this.buffer.clear();
            this.offset = 0;
        }

        @Override
        public int available() throws IOException {
            return this.buffer.remaining();
        }

        protected void fetch() throws HttpException, IOException, URISyntaxException {
            final StringBuilder request = new StringBuilder();
            final URI uri = new URI(YahooTransportHandler.API);
            request.append(uri.toString()).append("?q=").append(this.buildYQL(this.yqlQuery)).append("&format=json&env=store%3A%2F%2Fdatatables.org%2Falltableswithkeys&diagnostics=false");
            HttpMethod method = new GetMethod(request.toString());
            method.setFollowRedirects(false);
            this.client.executeMethod(method);
            try {
                final JSONObject obj = new JSONObject(method.getResponseBodyAsString());
                final JSONObject results = ((JSONObject) obj.get("query")).getJSONObject("results");
                if (results != null) {
                    final byte[] response = results.toString().getBytes();

                    if (response.length > this.buffer.capacity()) {
                        final ByteBuffer newBuffer = ByteBuffer.allocate(response.length);
                        this.buffer = newBuffer;
                        YahooTransportHandler.LOG.debug("Extending buffer to " + this.buffer.capacity());
                    }
                    this.buffer.clear();
                    this.buffer.put(response);
                    this.buffer.flip();
                }
            }
            catch (final JSONException e) {
                YahooTransportHandler.LOG.error(e.getMessage(), e);
                throw new IOException(e);
            }
            finally {
                this.offset += this.resultSize;
            }
        }

        private boolean isRefetch() {
            return this.refetch;
        }

        private void setRefetch(final boolean refetch) {
            this.refetch = refetch;
        }

        private StringBuilder buildYQL(final String query) throws UnsupportedEncodingException {
            final StringBuilder yqlEncoded = new StringBuilder();
            yqlEncoded.append(URLEncoder.encode(String.format(query, "(" + this.offset + "," + (this.offset + this.resultSize) + ")"), "UTF-8"));
            return yqlEncoded;
        }
    }
}
