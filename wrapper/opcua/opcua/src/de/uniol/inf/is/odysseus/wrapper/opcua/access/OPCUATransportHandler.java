/*******************************************************************************
 * Copyright 2016 Georg Berendt
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
package de.uniol.inf.is.odysseus.wrapper.opcua.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.UaException;
import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.xafero.turjumaan.client.sdk.core.OpcUaClient;
import com.xafero.turjumaan.client.sdk.core.OpcUaClientBuilder;
import com.xafero.turjumaan.core.sdk.util.StatusUtils;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;

/**
 * The OPC UA wrapper transport handler.
 *
 * @param <T>
 *            the generic type
 */
public class OPCUATransportHandler<T> extends AbstractTransportHandler implements ITransportHandler {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(OPCUATransportHandler.class);

	/** The Constant NAME. */
	private static final String NAME = "OPC-UA";

	/** The Constant ENDPOINT. */
	private static final String ENDPOINT = "endpoint";

	/** The Constant PERIOD. */
	private static final String PERIOD = "period";

	/** The Constant SECURITY. */
	private static final String SECURITY = "security";

	/** The Constant ANONYM. */
	private static final String ANONYM = "anonymous";

	/** The Constant NODES. */
	private static final String NODES = "nodes";

	/** The endpoint. */
	private String endpoint;

	/** The period. */
	private int period;

	/** The highest level of security. */
	private boolean highestLevel;

	/** The anonymous user flag. */
	private boolean anonymous;

	/** The nodes. */
	private NodeId[] nodes;

	/** The current client. */
	private OpcUaClient current;

	/** The subscription id. */
	private UInteger subscrId;

	/** The scheduler pool. */
	private ScheduledExecutorService pool;

	/** The read meta tuple. */
	private Tuple<IMetaAttribute> read;

	/**
	 * Instantiates a new OPC UA transport handler.
	 */
	public OPCUATransportHandler() {
		super();
	}

	/**
	 * Instantiates a new OPC UA transport handler.
	 *
	 * @param handler
	 *            the handler
	 * @param config
	 *            the configuration
	 */
	public OPCUATransportHandler(IProtocolHandler<?> handler, OptionMap config) {
		super(handler, config);
		init(config);
	}

	/**
	 * Initializes with the configuration.
	 *
	 * @param cfg
	 *            the options
	 */
	protected void init(OptionMap cfg) {
		endpoint = cfg.get(ENDPOINT, "http://localhost:12345/ua");
		log.info("Using endpoint => {}", endpoint);
		period = Integer.parseInt(cfg.get(PERIOD, "500"));
		log.info("Using period => {}", period);
		highestLevel = Boolean.parseBoolean(cfg.get(SECURITY, "true"));
		anonymous = Boolean.parseBoolean(cfg.get(ANONYM, "true"));
		log.info("secure? {}    anonymous? {}", highestLevel, anonymous);
		String[] paths = cfg.get(NODES, "i=42 | i=53").split(Pattern.quote("|"));
		nodes = new NodeId[paths.length];
		for (int i = 0; i < paths.length; i++)
			nodes[i] = NodeId.parse(paths[i].trim());
		log.info("Using nodes => {}", Arrays.toString(nodes));
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> handler, OptionMap options) {
		return new OPCUATransportHandler<T>(handler, options);
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws IOException {
		try {
			OpcUaClient client = (new OpcUaClientBuilder()).useAnonymousToken(!anonymous).useHighestLevel(highestLevel)
					.build(endpoint);
			client.connect();
			log.info("Established connection to '{}' with mode '{}'...", client.getEndpointURI().getHost(),
					client.getSecurityMode());
			current = client;
			subscrId = current.subscribe(period).get();
			log.info("Your subscription's ID is {} and its interval is {}!", subscrId, period);
			for (NodeId nodeId : nodes) {
				Map<NodeId, UInteger> monIds = current.createMonitor(subscrId, nodeId).get();
				for (Entry<NodeId, UInteger> e : monIds.entrySet())
					log.info(" * '{}' got the monitor ID {}!", e.getKey(), e.getValue());
			}
			read = new Tuple<>(getSchema().size(), false);
			pool = Executors.newScheduledThreadPool(1);
			pool.scheduleAtFixedRate(requestPub(), period, period, TimeUnit.MILLISECONDS);
		} catch (InterruptedException | ExecutionException | UaException e) {
			throw new IOException(e);
		}
	}

	/**
	 * Create a thread for request publication.
	 *
	 * @return the runnable
	 */
	protected Runnable requestPub() {
		return new Runnable() {
			@Override
			public void run() {
				try {
					Map<NodeId, DataValue> pubResults = current.republish().get();
					for (Entry<NodeId, DataValue> e : pubResults.entrySet())
						newValue(e);
				} catch (InterruptedException | ExecutionException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}

	/**
	 * Find node in the options by its id.
	 *
	 * @param key
	 *            the NodeId
	 * @return the index
	 */
	protected int findNode(NodeId key) {
		for (int i = 0; i < nodes.length; i++)
			if (key.equals(nodes[i]))
				return i;
		return -1;
	}

	/**
	 * On every new value.
	 *
	 * @param e
	 *            the event with id and value
	 */
	protected void newValue(Entry<NodeId, DataValue> e) {
		int pos = findNode(e.getKey());
		long timestamp = e.getValue().getSourceTime().getUtcTime();
		Object value = e.getValue().getValue().getValue();
		int quality = (int) e.getValue().getStatusCode().getValue();
		long error = e.getValue().getStatusCode().getValue();
		OPCValue<Object> ov = new OPCValue<Object>(timestamp, value, quality, error);
		process(pos, ov);
	}

	/**
	 * Process a value.
	 *
	 * @param pos
	 *            the configured position
	 * @param data
	 *            the received data
	 */
	protected void process(int pos, OPCValue<Object> data) {
		log.trace(String.format("%d: %s", new Integer(pos), data));
		synchronized (read) {
			read.setAttribute(pos, data);
			Tuple<IMetaAttribute> toSend = read.clone();
			fireProcess(toSend);
		}
	}

	@Override
	public void processInClose() throws IOException {
		pool.shutdown();
		try {
			Map<UInteger, StatusCode> results = current.unsubscribe(subscrId).get();
			for (Entry<UInteger, StatusCode> e : results.entrySet())
				log.info("Your subscription {} has been deleted: {}", e.getKey(), StatusUtils.toString(e.getValue()));
		} catch (InterruptedException | ExecutionException e) {
			throw new IOException(e);
		}
		current.close();
		log.info("Closed connection to '{}'!", endpoint);
		current = null;
	}

	@Override
	public void processOutOpen() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void send(byte[] message) throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processOutClose() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public InputStream getInputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getOutputStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler obj) {
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		OPCUATransportHandler<?> other = (OPCUATransportHandler<?>) obj;
		if (other.endpoint.equals(this.endpoint) && other.period == this.period
				&& Arrays.equals(other.nodes, this.nodes))
			return true;
		return false;
	}
}