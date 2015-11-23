package de.uniol.inf.is.odysseus.server.opcua.access;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
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
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.core.sdk.util.StatusUtils;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.server.opcua.binding.OPCUAComponent;
import de.uniol.inf.is.odysseus.server.opcua.core.OPCValue;

public class OPCUATransportHandler<T> extends AbstractTransportHandler implements ITransportHandler {

	private final Logger log = LoggerFactory.getLogger(OPCUATransportHandler.class);

	private static final String NAME = "OPC-UA";
	private static final String ENDPOINT = "endpoint";
	private static final String PERIOD = "period";
	private static final String NODES = "nodes";

	private String endpoint;
	private int period;
	private NodeId[] nodes;

	private OpcUaClient current;
	private UInteger subscrId;
	private ScheduledExecutorService pool;
	private Tuple<IMetaAttribute> read;

	public OPCUATransportHandler() {
		super();
	}

	public OPCUATransportHandler(IProtocolHandler<?> handler, OptionMap config) {
		super(handler, config);
		init(config);
	}

	protected void init(OptionMap cfg) {
		endpoint = cfg.get(ENDPOINT, "http://localhost:12345/ua");
		log.info("Using endpoint => {}", endpoint);
		period = Integer.parseInt(cfg.get(PERIOD, "500"));
		log.info("Using period => {}", period);
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
			OpcUaClient client = (new OpcUaClientBuilder()).build(endpoint);
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

	protected Runnable requestPub() {
		return new Runnable() {
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

	protected int findNode(NodeId key) {
		for (int i = 0; i < nodes.length; i++)
			if (key.equals(nodes[i]))
				return i;
		return -1;
	}

	protected void newValue(Entry<NodeId, DataValue> e) {
		int pos = findNode(e.getKey());
		long timestamp = e.getValue().getSourceTime().getUtcTime();
		Object value = e.getValue().getValue().getValue();
		int quality = (int) e.getValue().getStatusCode().getValue();
		long error = e.getValue().getStatusCode().getValue();
		OPCValue<Object> ov = new OPCValue<Object>(timestamp, value, quality, error);
		process(pos, ov);
	}

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
		log.info("Established cesspool under '{}'...", endpoint);
	}

	@Override
	public void send(byte[] message) throws IOException {
		String text = new String(message, Charset.forName("UTF8"));
		String[] parts = text.split(Pattern.quote("],"));
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			String[] sub = part.split(Pattern.quote("["));
			double value = Double.parseDouble(sub[0]);
			String[] ultra = sub[1].replace(']', ' ').trim().split(Pattern.quote(","));
			long timestamp = Long.parseLong(ultra[0].split("=")[1]);
			long quality = Long.parseLong(ultra[1].split("=")[1]);
			long error = Long.parseLong(ultra[2].split("=")[1]);
			// Build write command
			OPCValue<Double> ov = new OPCValue<Double>(timestamp, value, (int) quality, error);
			AttributeIds attr = AttributeIds.Value;
			NodeId node = nodes[i];
			// Log it!
			log.trace("got --> {} --> {} --> {} ", new Object[] { node, attr, ov });
			OPCUAComponent.Instance.send(this, node, attr, ov);
		}
	}

	@Override
	public void processOutClose() throws IOException {
		log.info("Closed cesspool under '{}'!", endpoint);
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