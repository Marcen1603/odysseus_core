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
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JIVariant;
import org.openscada.opc.lib.common.AlreadyConnectedException;
import org.openscada.opc.lib.common.ConnectionInformation;
import org.openscada.opc.lib.common.NotConnectedException;
import org.openscada.opc.lib.da.AccessBase;
import org.openscada.opc.lib.da.AddFailedException;
import org.openscada.opc.lib.da.DataCallback;
import org.openscada.opc.lib.da.DuplicateGroupException;
import org.openscada.opc.lib.da.Item;
import org.openscada.opc.lib.da.ItemState;
import org.openscada.opc.lib.da.Server;
import org.openscada.opc.lib.da.SyncAccess;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * OPC transport handler
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class OPCDATransportHandler<T> extends AbstractTransportHandler implements IIteratable<Tuple<IMetaAttribute>> {
    /** Logger */
    private final Logger LOG = LoggerFactory.getLogger(OPCDATransportHandler.class);

    private String host;

    private String domain;

    private String username;

    private String password;

    private String progId;
    private String clsId;

    private Server server;
    private AccessBase access;

    private List<Tuple<IMetaAttribute>> read = new LinkedList<>();

    public OPCDATransportHandler() {
    }

    public OPCDATransportHandler(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        super(protocolHandler, options);
        this.host = options.containsKey("host") ? options.get("host") : "localhost";
        this.domain = options.containsKey("domain") ? options.get("domain") : "";
        this.username = options.containsKey("username") ? options.get("username") : null;
        this.password = options.containsKey("password") ? options.get("password") : null;
        this.progId = options.containsKey("progId") ? options.get("progId") : null;
        this.clsId = options.containsKey("clsId") ? options.get("clsId") : null;
    }

    @Override
    public String getName() {
        return "OPC";
    }

    @Override
    public void send(byte[] message) throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, Map<String, String> options) {
        return new OPCDATransportHandler<T>(protocolHandler, options);
    }

    @Override
    public OutputStream getOutputStream() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public InputStream getInputStream() {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void processInOpen() throws IOException {
        final ConnectionInformation ci = new ConnectionInformation();
        ci.setHost(getHost());
        if (getDomain() != null) {
            ci.setDomain(getDomain());
        }
        if (getUsername() != null) {
            ci.setUser(getUsername());
        }
        if (getPassword() != null) {
            ci.setPassword(getPassword());
        }
        if (getProgId() != null) {
            ci.setProgId(getProgId());
        }
        if (getClsId() != null) {
            ci.setClsid(getClsId());
        }
        server = new Server(ci, Executors.newSingleThreadScheduledExecutor());
        try {
            server.connect();
            access = new SyncAccess(server, 500);
            for (SDFAttribute attribute : this.getSchema()) {
                final int position = getSchema().indexOf(attribute);
                access.addItem(attribute.getAttributeName(), new DataCallback() {

                    @Override
                    public void changed(Item item, ItemState state) {

                        try {
                            if ((state.getValue().getType() == JIVariant.VT_UI1) || (state.getValue().getType() == JIVariant.VT_UI2) || (state.getValue().getType() == JIVariant.VT_UI4)
                                    || (state.getValue().getType() == JIVariant.VT_UINT)) {
                                process(position, state.getValue().getObjectAsUnsigned().getValue().intValue());

                            }
                            else if ((state.getValue().getType() == JIVariant.VT_I1) || (state.getValue().getType() == JIVariant.VT_I2) || (state.getValue().getType() == JIVariant.VT_I4)
                                    || (state.getValue().getType() == JIVariant.VT_I8) || (state.getValue().getType() == JIVariant.VT_INT)) {

                                process(position, state.getValue().getObjectAsUnsigned().getValue().intValue());
                            }
                            else {
                                process(position, state.getValue().getObject());
                            }
                        }
                        catch (JIException e) {
                            LOG.error(e.getMessage(), e);
                        }
                    }
                });
            }
            access.bind();
        }
        catch (final JIException e) {
            this.server.dispose();
            LOG.error(String.format("%08X: %s", e.getErrorCode(), server.getErrorMessage(e.getErrorCode())));
            throw new IOException(server.getErrorMessage(e.getErrorCode()));
        }
        catch (IllegalArgumentException | NotConnectedException | DuplicateGroupException | AddFailedException e) {
            this.server.dispose();
            LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        catch (AlreadyConnectedException e) {
            LOG.warn(e.getMessage(), e);
        }
    }

    private void process(int pos, Object value) {
        LOG.debug(String.format("%d: %s", pos, value));
        Tuple<IMetaAttribute> curTuple = null;
        if (read.size() > 0) {
            curTuple = read.get(read.size() - 1);
            if (curTuple.getAttribute(pos) != null) {
                curTuple = null;
            }
        }
        if (curTuple == null) {
            curTuple = new Tuple<>(this.getSchema().size(), false);
            read.add(curTuple);
        }
        curTuple.setAttribute(pos, value);

    }

    @Override
    public void processInClose() throws IOException {
        try {
            if (access != null) {
                access.unbind();
            }
        }
        catch (JIException e) {
            LOG.error(e.getMessage(), e);
            throw new IOException(e);
        }
        finally {
            if (this.server != null) {
                this.server.disconnect();
                this.server.dispose();
            }
        }
    }

    @Override
    public void processOutOpen() throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public void processOutClose() throws IOException {
        throw new IllegalArgumentException("Not implemented");
    }

    @Override
    public boolean hasNext() {
        return read.size() > 0;
    }

    @Override
    public Tuple<IMetaAttribute> getNext() {
        return read.remove(0);
    }

    public String getHost() {
        return this.host;
    }

    public String getDomain() {
        return this.domain;
    }

    public String getUsername() {
        return this.username;
    }

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
    public boolean isSemanticallyEqualImpl(ITransportHandler other) {
        return false;
    }

   
}
