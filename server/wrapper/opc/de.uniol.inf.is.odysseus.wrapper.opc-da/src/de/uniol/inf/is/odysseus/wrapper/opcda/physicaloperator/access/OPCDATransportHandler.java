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
package de.uniol.inf.is.odysseus.wrapper.opcda.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.AutoReconnectController;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * OPC transport handler
 *
 * @author Christian Kuka <christian@kuka.cc>
 */
public class OPCDATransportHandler<T> extends AbstractTransportHandler {
    /** Logger */
    private final static Logger LOG = LoggerFactory.getLogger(OPCDATransportHandler.class);

    private final static String HOST = "host";
    private final static String DOMAIN = "domain";
    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String PROG_ID = "progid";
    private final static String CLS_ID = "clsid";
    private final static String PERIOD = "period";

    private String host;
    private String domain;
    private String username;
    private String password;
    private String progId;
    private String clsId;
    private int period;

    private Server server;
    private AutoReconnectController controller;
    private AccessBase access;

    private Tuple<IMetaAttribute> read;
    private TupleDataHandler dataHandler;

    public OPCDATransportHandler() {
    }

    public OPCDATransportHandler(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        super(protocolHandler, options);
        this.init(options);
    }

    protected void init(final Map<String, String> options) {
        if (options.containsKey(OPCDATransportHandler.HOST)) {
            this.host = options.get(OPCDATransportHandler.HOST);
        }
        else {
            this.host = "localhost";
        }
        if (options.containsKey(OPCDATransportHandler.DOMAIN)) {
            this.domain = options.get(OPCDATransportHandler.DOMAIN);
        }
        if (options.containsKey(OPCDATransportHandler.USERNAME)) {
            this.username = options.get(OPCDATransportHandler.USERNAME);
        }
        if (options.containsKey(OPCDATransportHandler.PASSWORD)) {
            this.password = options.get(OPCDATransportHandler.PASSWORD);
        }
        if (options.containsKey(OPCDATransportHandler.PROG_ID)) {
            this.progId = options.get(OPCDATransportHandler.PROG_ID);
        }
        if (options.containsKey(OPCDATransportHandler.CLS_ID)) {
            this.clsId = options.get(OPCDATransportHandler.CLS_ID);
        }
        if (options.containsKey(OPCDATransportHandler.PERIOD)) {
            this.period = Integer.parseInt(options.get(OPCDATransportHandler.PERIOD));
        }
        else {
            this.period = 500;
        }
    }

    @Override
    public String getName() {
        return "OPC-DA";
    }

    @Override
    public void send(final byte[] message) throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
        return new OPCDATransportHandler<T>(protocolHandler, options);
    }

    @Override
    public OutputStream getOutputStream() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public InputStream getInputStream() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void processInOpen() throws IOException {
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(this.host);
        if (this.domain != null) {
            ci.setDomain(this.domain);
        }
        if (this.username != null) {
            ci.setUser(this.username);
        }
        if (this.password != null) {
            ci.setPassword(this.password);
        }
        if (this.progId != null) {
            ci.setProgId(this.progId);
        }
        if (this.clsId != null) {
            ci.setClsid(this.clsId);
        }
        // new ScheduledThreadPoolExecutor(corePoolSize)
        this.server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        this.controller = new AutoReconnectController(this.server);
        try {
            this.controller.connect();
            // this.server.connect();
            this.access = new SyncAccess(this.server, this.period);
            this.read = new Tuple<>(this.getSchema().size(), false);
            this.dataHandler = new TupleDataHandler();
            this.dataHandler.init(this.getSchema());
            for (final SDFAttribute attribute : this.getSchema()) {
                final int position = this.getSchema().indexOf(attribute);
                this.access.addItem(attribute.getAttributeName(), new DataCallback() {
                    private final Logger log = LoggerFactory.getLogger(DataCallback.class);

                    @Override
                    public void changed(final Item item, final ItemState state) {
                        try {
                            if ((state.getValue().getType() == JIVariant.VT_UI1) || (state.getValue().getType() == JIVariant.VT_UI2) || (state.getValue().getType() == JIVariant.VT_UI4)
                                    || (state.getValue().getType() == JIVariant.VT_UINT)) {
                                OPCDATransportHandler.this.process(position, new Integer(state.getValue().getObjectAsUnsigned().getValue().intValue()));
                            }
                            else if ((state.getValue().getType() == JIVariant.VT_I1) || (state.getValue().getType() == JIVariant.VT_I2) || (state.getValue().getType() == JIVariant.VT_I4)
                                    || (state.getValue().getType() == JIVariant.VT_I8) || (state.getValue().getType() == JIVariant.VT_INT)) {
                                OPCDATransportHandler.this.process(position, new Integer(state.getValue().getObjectAsUnsigned().getValue().intValue()));
                            }
                            else {
                                OPCDATransportHandler.this.process(position, state.getValue().getObject());
                            }
                        }
                        catch (final JIException e) {
                            this.log.error(e.getMessage(), e);
                        }
                    }
                });
            }
            this.access.bind();
        }
        catch (final JIException e) {
            this.server.dispose();
            OPCDATransportHandler.LOG.error(String.format("%08X: %s", new Integer(e.getErrorCode()), this.server.getErrorMessage(e.getErrorCode())));
            throw new IOException(this.server.getErrorMessage(e.getErrorCode()));
        }
        catch (IllegalArgumentException | NotConnectedException | DuplicateGroupException | AddFailedException e) {
            this.server.dispose();
            OPCDATransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
    }

    void process(final int pos, final Object value) {
        OPCDATransportHandler.LOG.debug(String.format("%d: %s", new Integer(pos), value));
        synchronized (this.read) {
            this.read.setAttribute(pos, value);
            ByteBuffer buffer = ByteBuffer.allocate(this.dataHandler.memSize(this.read));
            this.dataHandler.writeData(buffer, this.read);
            fireProcess(buffer);
        }
    }

    @Override
    public void processInClose() throws IOException {
        try {
            if (this.access != null) {
                this.access.unbind();
            }
        }
        catch (final JIException e) {
            OPCDATransportHandler.LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        finally {
            if (this.controller != null) {
                this.controller.disconnect();
            }
            if (this.server != null) {
                this.server.disconnect();
                this.server.dispose();
            }
        }
    }

    @Override
    public void processOutOpen() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void processOutClose() throws IOException {
        throw new UnsupportedOperationException("Not implemented");
    }

    /**
     * 
     * @return the host
     */
    public String getHost() {
        return this.host;
    }

    /**
     * 
     * @return the domain
     */
    public String getDomain() {
        return this.domain;
    }

    /**
     * 
     * @return the username
     */
    public String getUsername() {
        return this.username;
    }

    /**
     * 
     * @return the password
     */
    public String getPassword() {
        return this.password;
    }

    /**
     *
     * @return the progId
     */
    public String getProgId() {
        return this.progId;
    }

    /**
     * @return the clsId
     */
    public String getClsId() {
        return this.clsId;
    }

    @Override
    public boolean isSemanticallyEqualImpl(final ITransportHandler obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        OPCDATransportHandler<?> other = (OPCDATransportHandler<?>) obj;
        if (this.domain == null) {
            if (other.domain != null)
                return false;
        }
        else if (!this.domain.equals(other.domain))
            return false;
        if (this.host == null) {
            if (other.host != null)
                return false;
        }
        else if (!this.host.equals(other.host))
            return false;
        if (this.username == null) {
            if (other.username != null)
                return false;
        }
        else if (!this.username.equals(other.username))
            return false;
        return true;
    }

}
