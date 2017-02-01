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
package de.uniol.inf.is.odysseus.server.opcua.access;

import static de.uniol.inf.is.odysseus.server.opcua.util.SpaceUtils.convert2Xml;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ScheduledExecutorService;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.types.builtin.DataValue;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.builtin.unsigned.UInteger;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.server.opcua.binding.OPCUAComponent;

/**
 * The OPC UA server transport handler.
 *
 * @param <T>
 *            the generic type
 */
@SuppressWarnings("unused")
public class OPCUATransportHandler<T> extends AbstractTransportHandler implements ITransportHandler {

	/** The log. */
	private final Logger log = LoggerFactory.getLogger(OPCUATransportHandler.class);

	/** The Constant NAME. */
	private static final String NAME = "UAServer";

	/** The Constant ENDPOINT. */
	private static final String ENDPOINT = "endpoint";

	/** The Constant PERIOD. */
	private static final String PERIOD = "period";

	/** The Constant NODES. */
	private static final String NODES = "nodes";

	/** The model id. */
	private long modelId;

	/** The endpoint. */
	private String endpoint;

	/** The period. */
	private int period;

	/** The nodes. */
	private NodeId[] nodes;

	/** The subscription id. */
	private UInteger subscrId;

	/** The pool. */
	private ScheduledExecutorService pool;

	/** The read. */
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
	 * Initializes with configuration.
	 *
	 * @param cfg
	 *            the configuration
	 */
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

	/**
	 * Find configured node by id.
	 *
	 * @param key
	 *            the key
	 * @return the index
	 */
	protected int findNode(NodeId key) {
		for (int i = 0; i < nodes.length; i++)
			if (key.equals(nodes[i]))
				return i;
		return -1;
	}

	/**
	 * On new value.
	 *
	 * @param e
	 *            the entry
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
	 *            the position
	 * @param data
	 *            the data
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
	public void processInOpen() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processInClose() throws IOException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void processOutOpen() throws IOException {
		OptionMap opts = getOptionsMap();
		int optsCnt = opts.getUnreadOptions().size();
		if (optsCnt < 1) {
			log.info("No model to add.");
			return;
		}
		try {
			log.info("Got {} options...", optsCnt);
			String xml = convert2Xml(opts);
			log.info("Found {} characters...", xml.length());
			modelId = OPCUAComponent.Instance.getModel().addModel(xml);
			log.info("Inserted model into cesspool with ID={}!", modelId);
		} catch (Exception e) {
			throw new IOException(e);
		}
	}

	/**
	 * Split a line into an array.
	 *
	 * @param line
	 *            the line
	 * @return the string[]
	 */
	private String[] split(String line) {
		char sep = ',';
		char lb = '[';
		char rb = ']';
		boolean bracket = false;
		List<String> parts = new LinkedList<>();
		char[] chars = line.toCharArray();
		StringBuilder last = new StringBuilder();
		for (int i = 0; i < chars.length; i++) {
			char it = chars[i];
			if (it == sep && !bracket) {
				parts.add(last.toString());
				last.setLength(0);
			} else if (it == lb) {
				bracket = true;
				last.append(it);
			} else if (it == rb) {
				bracket = false;
				last.append(it);
			} else
				last.append(it);
		}
		if (last.length() >= 1)
			parts.add(last.toString());
		return parts.toArray(new String[parts.size()]);
	}

	@Override
	public void send(byte[] message) throws IOException {
		String text = new String(message, Charset.forName("UTF8"));
		String[] parts = split(text);
		for (int i = 0; i < parts.length; i++) {
			String part = parts[i];
			if (part.isEmpty())
				continue;
			String[] sub = part.split(Pattern.quote("["));
			double value = Double.parseDouble(sub[0]);
			long timestamp;
			long quality;
			long error;
			if (sub.length == 2) {
				String[] ultra = sub[1].replace(']', ' ').trim().split(Pattern.quote(","));
				timestamp = Long.parseLong(ultra[0].split("=")[1]);
				quality = Long.parseLong(ultra[1].split("=")[1]);
				error = Long.parseLong(ultra[2].split("=")[1]);
			} else {
				timestamp = DateTime.now().getUtcTime();
				quality = StatusCode.GOOD.getValue();
				error = StatusCode.GOOD.getValue();
			}
			// Build write command
			OPCValue<Double> ov = new OPCValue<Double>(timestamp, value, (int) quality, error);
			AttributeIds attr = AttributeIds.Value;
			NodeId node = nodes[i];
			// Log it!
			log.trace("got --> {} --> {} --> {} ", new Object[] { node, attr, ov });
			OPCUAComponent.Instance.send(this, node, attr, ov);
			OPCUAComponent.Instance.getModel().getModel().send(node, attr, ov);
		}
	}

	@Override
	public void processOutClose() throws IOException {
		if (modelId < 1) {
			log.info("No model to remove.");
			return;
		}
		try {
			String xml = OPCUAComponent.Instance.getModel().removeModel(modelId);
			log.info("Found {} characters...", xml.length());
			log.info("Ejected model with ID={} from cesspool!", modelId);
		} catch (Exception e) {
			throw new IOException(e);
		}
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