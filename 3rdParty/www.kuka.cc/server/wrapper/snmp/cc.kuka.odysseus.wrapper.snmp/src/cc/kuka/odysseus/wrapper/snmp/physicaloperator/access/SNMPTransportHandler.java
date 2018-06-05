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
package cc.kuka.odysseus.wrapper.snmp.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/**
 * @author Christian Kuka <christian@kuka.cc>
 * @version $Id$
 *
 */
public class SNMPTransportHandler extends AbstractPullTransportHandler {
    private class SNMPTransportHandlerInputStream extends InputStream {
        ByteBuffer buffer = ByteBuffer.allocate(30 * 1024);
        private boolean refetch = false;
        private Snmp snmp;
        private PDU pdu;
        private CommunityTarget comtarget;

        /**
         * Class constructor.
         * 
         * @throws IOException
         *
         */
        public SNMPTransportHandlerInputStream() throws IOException {
            Address addr = new UdpAddress(getHost() + "/" + getPort());
            TransportMapping<?> transport = new DefaultUdpTransportMapping();
            transport.listen();
            this.comtarget = new CommunityTarget();
            this.comtarget.setCommunity(new OctetString(getCommunity()));
            this.comtarget.setVersion(SnmpConstants.version1);
            this.comtarget.setAddress(addr);
            this.comtarget.setRetries(2);
            this.comtarget.setTimeout(5000);
            this.pdu = new PDU();
            this.pdu.add(new VariableBinding(new OID(getOid())));
            this.pdu.setType(PDU.GET);
            this.snmp = new Snmp(transport);
            this.fetch();
        }

        @Override
        public int available() throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }
            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            return this.buffer.remaining();
        }

        @Override
        public void close() throws IOException {
            this.buffer.clear();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public synchronized int read() throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }
            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            return this.buffer.get() & 0xFF;
        }

        @Override
        public synchronized int read(final byte[] b, final int off, final int len) throws IOException {
            if (this.isRefetch()) {
                this.setRefetch(false);
                this.fetch();
            }

            if (this.buffer.remaining() == 0) {
                this.setRefetch(true);
                return -1;
            }
            final int length = Math.min(len, this.buffer.remaining());

            this.buffer.get(b, off, length);
            return length;
        }

        private void fetch() throws IOException {
            ResponseEvent event = this.snmp.get(this.pdu, this.comtarget);
            this.buffer.clear();
            PDU pduresponse = event.getResponse();
            if (pduresponse != null) {
                Enumeration<? extends VariableBinding> iter = pduresponse.getVariableBindings().elements();
                while (iter.hasMoreElements()) {
                    VariableBinding element = iter.nextElement();
                    String data = element.toString();
                    String[] keyValue = data.split("=");
                    if (keyValue.length > 1) {
                        String value = keyValue[1].trim();
                        this.buffer.putInt(value.getBytes().length);
                        this.buffer.put(value.getBytes());
                    }
                }
            }
            Exception error = event.getError();
            if (error != null) {
                LOG.error(error.getMessage(), error);
            }
            this.buffer.flip();
        }

        private boolean isRefetch() {
            return this.refetch;
        }

        private void setRefetch(final boolean refetch) {
            this.refetch = refetch;
        }
    }
    private class SNMPTransportHandlerOutputStream extends OutputStream {
        private boolean closing = false; /* To avoid recursive closing */
        private ByteBuffer buffer = ByteBuffer.allocate(30 * 1024);

        /**
         * Class constructor.
         *
         */
        public SNMPTransportHandlerOutputStream() {
            // TODO Auto-generated constructor stub
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void close() throws IOException {
            synchronized (this) {
                if (!this.closing) {
                    this.closing = true;
                    this.flush();
                    this.buffer = null;
                }
            }
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void flush() throws IOException {
            synchronized (this) {
                this.buffer.flip();
                if (this.buffer.hasRemaining()) {
                    Address addr = GenericAddress.parse(getHost());
                    TransportMapping<?> transport = new DefaultUdpTransportMapping();
                    Snmp snmp = new Snmp(transport);

                    transport.listen();
                    CommunityTarget target = new CommunityTarget();
                    target.setCommunity(new OctetString(getCommunity()));
                    target.setAddress(addr);
                    target.setRetries(2);
                    target.setTimeout(5000);
                    target.setVersion(SnmpConstants.version1);

                    PDU pdu = new PDU();
                    for (SDFAttribute attribute : getSchema()) {
                        if (attribute.getDatatype().equals(SDFDatatype.INTEGER)) {
                            pdu.add(new VariableBinding(new OID(getOid()), new Integer32(this.buffer.getInt())));
                        }
                        else {
                            pdu.add(new VariableBinding(new OID(getOid()), new OctetString(new String(this.buffer.array(), getCharset()))));
                        }

                    }
                    pdu.setType(PDU.SET);

                    ResponseListener listener = new ResponseListener() {
                        @Override
                        public void onResponse(ResponseEvent event) {
                            ((Snmp) event.getSource()).cancel(event.getRequest(), this);
                            LOG.debug(event.getResponse().getErrorStatusText());
                        }
                    };
                    snmp.send(pdu, target, null, listener);
                    snmp.close();
                }
                this.buffer.clear();
            }

        }

        /**
         * {@inheritDoc}
         */
        @Override
        public void write(final int b) throws IOException {
            synchronized (this) {
                this.checkOverflow();
                this.buffer.put((byte) b);
            }
        }

        private void checkOverflow() {
            if (((Integer.SIZE / 8) + this.buffer.position()) >= this.buffer.capacity()) {
                final ByteBuffer newBuffer = ByteBuffer.allocate((this.buffer.position() + (Integer.SIZE / 8)) * 2);
                final int pos = this.buffer.position();
                this.buffer.flip();
                newBuffer.put(this.buffer);
                this.buffer = newBuffer;
                this.buffer.position(pos);
            }
        }
    }

    static final Logger LOG = LoggerFactory.getLogger(SNMPTransportHandler.class);
    private static final String NAME = "SNMP";
    private static final String COMMUNITY = "community";
    private static final String HOST = "host";

    private static final String PORT = "port";
    private static final String OID = "oid";
    private String host;
    private int port;

    private String oid;
    private String community;

    private SNMPTransportHandlerInputStream inputStream;

    private SNMPTransportHandlerOutputStream outputStream;

    /**
     *
     */
    public SNMPTransportHandler() {
        super();
    }

    /**
     *
     * Class constructor.
     *
     * @param protocolHandler
     *            The protocol handler
     * @param options
     *            The options
     */
    private SNMPTransportHandler(final IProtocolHandler<?> protocolHandler, OptionMap options) {
        super(protocolHandler, options);
        this.init();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final OptionMap options) {
        final SNMPTransportHandler instance = new SNMPTransportHandler(protocolHandler, options);
        return instance;
    }

    /**
     * @return the community
     */
    public String getCommunity() {
        return this.community;
    }

    /**
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public InputStream getInputStream() {
        return this.inputStream;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return SNMPTransportHandler.NAME;
    }

    /**
     * @return the oid
     */
    public String getOid() {
        return this.oid;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OutputStream getOutputStream() {
        return this.outputStream;
    }

    /**
     * @return the port
     */
    public int getPort() {
        return this.port;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isSemanticallyEqualImpl(ITransportHandler o) {
        if (!(o instanceof SNMPTransportHandler)) {
            return false;
        }
        final SNMPTransportHandler other = (SNMPTransportHandler) o;
        if (!this.getHost().equalsIgnoreCase(other.getHost())) {
            return false;
        }
        if (!this.getOid().equalsIgnoreCase(other.getOid())) {
            return false;
        }
        if (this.getPort() != other.getPort()) {
            return false;
        }
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInClose() throws IOException {
        this.inputStream.close();
        this.fireOnDisconnect();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processInOpen() throws IOException {
        this.inputStream = new SNMPTransportHandlerInputStream();
        this.fireOnConnect();

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutClose() throws IOException {
        this.outputStream.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void processOutOpen() throws IOException {
        this.outputStream = new SNMPTransportHandlerOutputStream();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(byte[] message) throws IOException {
        LOG.trace(">  {} ", Arrays.toString(message));
        throw new UnsupportedOperationException();
    }

    /**
     * @param community
     *            the community to set
     */
    public void setCommunity(String community) {
        this.community = community;
    }

    /**
     * @param host
     *            the host to set
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * @param oid
     *            the oid to set
     */
    public void setOid(String oid) {
        this.oid = oid;
    }

    /**
     * @param port
     *            the port to set
     */
    public void setPort(int port) {
        this.port = port;
    }

    private void init() {
        final OptionMap options = this.getOptionsMap();
        setHost(options.get(HOST, "localhost"));
        setPort(Integer.parseInt(options.get(PORT, "161")));
        setOid(options.get(OID));
        setCommunity(options.get(COMMUNITY, "public"));

    }

}
