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
package de.uniol.inf.is.odysseus.server.opcua.binding;

import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.core.security.SecurityPolicy;
import com.inductiveautomation.opcua.stack.core.types.builtin.DateTime;
import com.inductiveautomation.opcua.stack.core.types.builtin.NodeId;
import com.inductiveautomation.opcua.stack.core.types.builtin.StatusCode;
import com.inductiveautomation.opcua.stack.core.types.enumerated.MessageSecurityMode;
import com.xafero.kitea.collections.impl.ObservableMap;
import com.xafero.turjumaan.core.sdk.enums.AttributeIds;
import com.xafero.turjumaan.server.sdk.core.OpcUaServer;
import com.xafero.turjumaan.server.sdk.core.OpcUaServerBuilder;

import de.uniol.inf.is.odysseus.core.server.OdysseusConfiguration;
import de.uniol.inf.is.odysseus.opcua.common.core.OPCValue;
import de.uniol.inf.is.odysseus.opcua.common.utilities.BasicUtils;
import de.uniol.inf.is.odysseus.server.opcua.access.OPCUATransportHandler;
import de.uniol.inf.is.odysseus.server.opcua.wrappers.Data;
import de.uniol.inf.is.odysseus.server.opcua.wrappers.DataItem;
import de.uniol.inf.is.odysseus.server.opcua.wrappers.Sink;

/**
 * The OPC UA server component for Odysseus.
 */
public class OPCUAComponent implements Closeable {

	/** The singleton instance. */
	public static final OPCUAComponent Instance = new OPCUAComponent();

	/** The log. */
	private final Logger log;

	/** The server. */
	private final OpcUaServer server;

	/** The model. */
	private final OdysseusNodeModel model;

	/** The sinks. */
	private final Map<OPCUATransportHandler<?>, Sink> sinks;

	/**
	 * Instantiates a new OPC UA component.
	 */
	public OPCUAComponent() {
		log = LoggerFactory.getLogger(OPCUAComponent.class);
		// Set end-point and security
		String endpoint = OdysseusConfiguration.instance.get("OpcUaCmp.Endpoint", "opc.tcp://localhost:12345/UA/OdyServer");
		// [None, Basic128Rsa15, Basic256, Basic256Sha256]
		SecurityPolicy policy = BasicUtils.toEnum(OdysseusConfiguration.instance.get("OpcUaCmp.Policy", "None"),
				SecurityPolicy.class);
		// [Invalid, None, Sign, SignAndEncrypt]
		MessageSecurityMode mode = BasicUtils.toEnum(OdysseusConfiguration.instance.get("OpcUaCmp.Mode", "None"),
				MessageSecurityMode.class);
		// Bind address and patches
		String bind = OdysseusConfiguration.instance.get("OpcUaCmp.Bind", null);
		boolean patchHello = OdysseusConfiguration.instance.getBoolean("OpcUaCmp.EmptyHello", false);
		// Get base model
		File modelXml = new File(OdysseusConfiguration.instance.get("OpcUaCmp.BaseNodeSet", "Opc.Ua.NodeSet2.xml"));
		downloadIfNeeded(modelXml);
		// Create special model
		model = new OdysseusNodeModel(modelXml.getAbsolutePath());
		sinks = new LinkedHashMap<>();
		// Set up server
		server = (new OpcUaServerBuilder()).policy(policy).mode(mode).bindAddress(bind).patchEmptyEndpoints(patchHello)
				.build(endpoint, model);
		server.run();
		// Log its success
		log.info("Endpoints => {}", Arrays.toString(server.getEndpointURIs()));
		log.info("Security modes => {}", Arrays.toString(server.getSecurityModes()));
	}

	/**
	 * Download a model if needed.
	 *
	 * @param model
	 *            the model
	 */
	private void downloadIfNeeded(File model) {
		if (model.exists() && model.canRead())
			return;
		try {
			try (InputStream in = getClass().getResourceAsStream("Opc.Ua.NodeSet2.xml");
					OutputStream out = new FileOutputStream(model)) {
				IOUtils.copy(in, out);
			}
			log.info("Downloaded model '{}'.", model);
		} catch (IOException ioe) {
			log.error("Couldn't download model '" + model.getName() + "'!", ioe);
		}
	}

	/**
	 * Send a new value to the address space.
	 *
	 * @param handler
	 *            the handler
	 * @param node
	 *            the node
	 * @param attr
	 *            the attribute
	 * @param val
	 *            the value
	 */
	public synchronized void send(OPCUATransportHandler<?> handler, NodeId node, AttributeIds attr,
			OPCValue<Double> val) {
		// Get or create one sink per handler
		Sink sink;
		if (sinks.containsKey(handler))
			sink = sinks.get(handler);
		else {
			sinks.put(handler, sink = new Sink(handler));
			Data data = model.data;
			data.getSinks().add(sink);
		}
		// Build name for item
		String name = node.getIdentifier().toString();
		// Get or create one data item per node
		ObservableMap<String, DataItem> items = sink.getValues();
		DataItem item;
		if (items.containsKey(name))
			item = items.get(name);
		else
			items.put(name, item = new DataItem());
		// Set current values
		item.time = new DateTime(val.getTimestamp());
		item.status = new StatusCode(val.getError());
		item.value = val.getValue();
	}

	/**
	 * Gets the model.
	 *
	 * @return the model
	 */
	public OdysseusNodeModel getModel() {
		return model;
	}

	@Override
	public void close() throws IOException {
		model.close();
		server.close();
		log.info("Server closed.");
	}
}