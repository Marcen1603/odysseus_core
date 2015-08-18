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
package de.uniol.inf.is.odysseus.wrapper.opcda.physicaloperator.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.IJIComObject;
import org.jinterop.dcom.core.JIArray;
import org.jinterop.dcom.core.JICurrency;
import org.jinterop.dcom.core.JIString;
import org.jinterop.dcom.core.JIUnsignedByte;
import org.jinterop.dcom.core.JIUnsignedInteger;
import org.jinterop.dcom.core.JIUnsignedShort;
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

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.infoservice.InfoService;
import de.uniol.inf.is.odysseus.core.infoservice.InfoServiceFactory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.wrapper.opcda.datatype.OPCValue;

/**
 * OPC transport handler
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * @author Marco Grawunder
 */
public class OPCDATransportHandler<T> extends AbstractTransportHandler {

	/** Logger */
	private final static Logger LOG = LoggerFactory
			.getLogger(OPCDATransportHandler.class);
	private final static InfoService INFO_SERVICE = InfoServiceFactory
			.getInfoService(OPCDATransportHandler.class);

	public static final String NAME = "OPC-DA";
	public final static String HOST = "host";
	public final static String DOMAIN = "domain";
	public final static String USERNAME = "username";
	public final static String PASSWORD = "password";
	public final static String PROG_ID = "progid";
	public final static String CLS_ID = "clsid";
	public final static String PERIOD = "period";
	public static final String PATH = "path";

	public static final int INTEGER = 1;
	public static final int DOUBLE = 2;
	public static final int BOOLEAN = 3;
	public static final int STRING = 4;
	public static final Map<Integer, Object> typeMapping = new HashMap<>();
	static {
		typeMapping.put(new Integer(JIVariant.VT_DATE), Date.class);
		typeMapping.put(new Integer(JIVariant.VT_CY), JICurrency.class);
		typeMapping.put(new Integer(JIVariant.VT_VARIANT), JIVariant.class);
		typeMapping.put(new Integer(JIVariant.VT_I4), Integer.class);
		typeMapping.put(new Integer(JIVariant.VT_INT), Integer.class);
		typeMapping.put(new Integer(JIVariant.VT_UI4), JIUnsignedInteger.class);
		typeMapping
				.put(new Integer(JIVariant.VT_UINT), JIUnsignedInteger.class);
		typeMapping.put(new Integer(JIVariant.VT_R4), Float.class);
		typeMapping.put(new Integer(JIVariant.VT_BOOL), Boolean.class);
		typeMapping.put(new Integer(JIVariant.VT_R8), Double.class);
		typeMapping.put(new Integer(JIVariant.VT_I2), Short.class);
		typeMapping.put(new Integer(JIVariant.VT_UI2), JIUnsignedShort.class);
		typeMapping.put(new Integer(JIVariant.VT_I1), Character.class);
		typeMapping.put(new Integer(JIVariant.VT_UI1), JIUnsignedByte.class);
		typeMapping.put(new Integer(JIVariant.VT_BSTR), JIString.class);
		typeMapping.put(new Integer(JIVariant.VT_ARRAY), JIArray.class);
		typeMapping.put(new Integer(JIVariant.VT_UNKNOWN), IJIComObject.class);
		typeMapping.put(new Integer(JIVariant.VT_DISPATCH), IJIComObject.class);
		typeMapping.put(new Integer(JIVariant.VT_I8), Long.class);
	}

	private String host;
	private String domain;
	private String username;
	private String password;
	private String progId;
	private String clsId;
	private int period;
	String[] pathes;

	private Server server;
	private AutoReconnectController controller;
	private AccessBase access;

	private Tuple<IMetaAttribute> read;
	private TupleDataHandler dataHandler;

	public OPCDATransportHandler() {
	}

	public OPCDATransportHandler(final IProtocolHandler<?> protocolHandler,
			final OptionMap options) {
		super(protocolHandler, options);
		this.init(options);
	}

	protected void init(final OptionMap options) {
		if (options.containsKey(OPCDATransportHandler.HOST)) {
			this.host = options.get(OPCDATransportHandler.HOST);
		} else {
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
			this.period = Integer.parseInt(options
					.get(OPCDATransportHandler.PERIOD));
		} else {
			this.period = 500;
		}
		if (options.containsKey(PATH)) {
			String path = options.get(PATH);
			this.pathes = path.split(";");
		} else {
			throw new IllegalArgumentException("No path given!");
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void send(final byte[] message) throws IOException {
		throw new UnsupportedOperationException("Not implemented");
	}

	@Override
	public ITransportHandler createInstance(
			final IProtocolHandler<?> protocolHandler, final OptionMap options) {
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
		this.server = new Server(ci,
				Executors.newSingleThreadScheduledExecutor());
		this.controller = new AutoReconnectController(this.server);
		try {
			this.controller.connect();
			// this.server.connect();
			this.access = new SyncAccess(this.server, this.period);
			this.read = new Tuple<>(this.getSchema().size(), false);
			this.dataHandler = new TupleDataHandler();
			this.dataHandler.init(this.getSchema());
			for (int pos = 0; pos < this.pathes.length; pos++) {
				final int position = pos;
				this.access.addItem(this.pathes[pos], new DataCallback() {
					private final Logger log = LoggerFactory
							.getLogger(DataCallback.class);

					@Override
					public void changed(final Item item, final ItemState state) {
						try {
							final Object value;
							final Object type = typeMapping.get(state
									.getValue().getType());
							JIVariant v = state.getValue();
							if (type == Integer.class) {
								value = new Integer(v.getObjectAsInt());
							} else if (type == JIUnsignedShort.class
									|| type == JIUnsignedByte.class) {
								value = new Integer(v.getObjectAsUnsigned()
										.getValue().intValue());
							} else if (type == Float.class) {
								value = new Float(v.getObjectAsFloat());
							} else if (type == Double.class) {
								value = new Double(v.getObjectAsDouble());
							} else if (type == Date.class) {
								value = v.getObjectAsDate();
							} else if (type == Boolean.class) {
								value = new Boolean(v.getObjectAsBoolean());
							} else if (type == Short.class) {
								value = new Short(v.getObjectAsShort());
							} else if (type == Character.class) {
								value = new Character(v.getObjectAsChar());
							} else if (type == JIString.class) {
								value = new String(v.getObjectAsString2());
							} else if (type == Long.class || type == JIUnsignedInteger.class) {
								value = new Long(v.getObjectAsLong());
							} else {
								value = state.getValue().getObject();
							}
							OPCValue<Object> data = new OPCValue<>(state
									.getTimestamp().getTimeInMillis(), value,
									state.getQuality().shortValue(), state
											.getErrorCode());

							OPCDATransportHandler.this.process(position, data);

						} catch (final JIException e) {
							this.log.error(e.getMessage(), e);
							INFO_SERVICE.error(e.getMessage(), e);
						}
					}
				});
			}
			this.access.bind();
		} catch (final JIException e) {
			this.server.dispose();
			OPCDATransportHandler.LOG.error(String.format("%08X: %s",
					new Integer(e.getErrorCode()),
					this.server.getErrorMessage(e.getErrorCode())));
			throw new IOException(this.server.getErrorMessage(e.getErrorCode()));
		} catch (IllegalArgumentException | NotConnectedException
				| DuplicateGroupException | AddFailedException e) {
			this.server.dispose();
			OPCDATransportHandler.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		}
	}

	void process(final int pos, OPCValue<Object> data) {
		if (LOG.isTraceEnabled()) {
			OPCDATransportHandler.LOG.trace(String.format("%d: %s",
					new Integer(pos), data));
		}
		synchronized (this.read) {
			this.read.setAttribute(pos, data);
			// No need to wrap into an buffer anymore --> just send the tuple
			// ByteBuffer buffer = ByteBuffer.allocate(this.dataHandler
			// .memSize(this.read));
			// this.dataHandler.writeData(buffer, this.read);
			Tuple<IMetaAttribute> toSend = read.clone();
			fireProcess(toSend);
		}
	}

	@Override
	public void processInClose() throws IOException {
		try {
			if (this.access != null) {
				this.access.unbind();
			}
		} catch (final JIException e) {
			OPCDATransportHandler.LOG.error(e.getMessage(), e);
			throw new IOException(e);
		} finally {
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
		} else if (!this.domain.equals(other.domain))
			return false;
		if (this.host == null) {
			if (other.host != null)
				return false;
		} else if (!this.host.equals(other.host))
			return false;
		if (this.username == null) {
			if (other.username != null)
				return false;
		} else if (!this.username.equals(other.username))
			return false;
		return true;
	}

}
