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
package de.uniol.inf.is.odysseus.wrapper.opc.physicaloperator.access;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.UUID;
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

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.AbstractProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;

/**
 * OPC protocol handler
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class OPCProtocolHandler<T> extends AbstractProtocolHandler<T> {
	/** Logger */
	private final Logger LOG = LoggerFactory
			.getLogger(OPCProtocolHandler.class);

	private String host;

	private String domain;

	private String username;

	private String password;

	private String id;

	private Server server;
	private AccessBase access;

	public OPCProtocolHandler(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		super(direction, access, dataHandler);
		init(options);
	}

	@Override
	public String getName() {
		return "OPC";
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		// TODO Auto-generated method stub

	}

	@Override
	public void open() throws UnknownHostException, IOException {
		getTransportHandler().open();
		// TODO Auto-generated method stub
		final ConnectionInformation ci = new ConnectionInformation();
		ci.setHost(getHost());
		ci.setDomain(getDomain());
		ci.setUser(getUsername());
		ci.setPassword(getPassword());
		ci.setProgId(getId());
		server = new Server(ci, Executors.newSingleThreadScheduledExecutor());

		try {
			server.connect();
			access = new SyncAccess(server, 500);
			for (SDFAttribute attr : getDataHandler().getSchema()) {
				access.addItem(attr.getAttributeName(), new DataCallback() {
					@Override
					public void changed(Item item, ItemState state) {

						try {
							if ((state.getValue().getType() == JIVariant.VT_UI1)
									|| (state.getValue().getType() == JIVariant.VT_UI2)
									|| (state.getValue().getType() == JIVariant.VT_UI4)
									|| (state.getValue().getType() == JIVariant.VT_UINT)) {
								process(state.getValue().getObjectAsUnsigned()
										.getValue(), item.getId());
							} else if ((state.getValue().getType() == JIVariant.VT_I1)
									|| (state.getValue().getType() == JIVariant.VT_I2)
									|| (state.getValue().getType() == JIVariant.VT_I4)
									|| (state.getValue().getType() == JIVariant.VT_I8)
									|| (state.getValue().getType() == JIVariant.VT_INT)) {
								process(state.getValue().getObjectAsInt(),
										item.getId());
							} else {
								process(state.getValue().getObject(),
										item.getId());
							}
						} catch (JIException e) {
							LOG.error(e.getMessage(), e);
						}
					}
				});
			}
			access.bind();
		} catch (final JIException e) {
			this.server.dispose();
			LOG.error(String.format("%08X: %s", e.getErrorCode(),
					server.getErrorMessage(e.getErrorCode())));
			throw new IOException(server.getErrorMessage(e.getErrorCode()));
		} catch (IllegalArgumentException | NotConnectedException
				| DuplicateGroupException | AddFailedException e) {
			this.server.dispose();
			LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} catch (AlreadyConnectedException e) {
			LOG.warn(e.getMessage(), e);
		}
	}

	protected void process(Object value, String attribute) {
		// TODO Auto-generated method stub
		LOG.debug(String.format("%s: %d", attribute, value));
	}

	protected void process(Number value, String attribute) {
		// TODO Auto-generated method stub
		LOG.debug(String.format("%s: %d", attribute, value));
	}

	@Override
	public void close() throws IOException {
		try {
			if (access != null) {
				access.unbind();
			}
		} catch (JIException e) {
			LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} finally {
			if (this.server != null) {
				this.server.disconnect();
				this.server.dispose();
			}
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, Map<String, String> options,
			IDataHandler<T> dataHandler) {
		return new OPCProtocolHandler<>(direction, access, options, dataHandler);
	}

	@Override
	public void process(ByteBuffer message) {
		int size = message.limit();
		byte[] out = new byte[size];
		message.get(out, 0, size);
		for (byte b : out) {
			System.out.print(Integer.toHexString(b));
		}
		System.out.println();
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

	public String getId() {
		return this.id;
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> other) {
		return false;
	}

	private void init(Map<String, String> options) {
		this.host = options.containsKey("host") ? options.get("host")
				: "localhost";
		this.domain = options.containsKey("domain") ? options.get("domain")
				: "";
		this.username = options.containsKey("username") ? options
				.get("username") : "";
		this.password = options.containsKey("password") ? options
				.get("password") : "";
		this.id = options.containsKey("id") ? options.get("id") : UUID
				.randomUUID().toString();

	}
}
