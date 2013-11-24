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
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * OPC transport handler
 * 
 * @author Christian Kuka <christian@kuka.cc>
 */
public class OPCTransportHandler extends AbstractTransportHandler {
	/** Logger */
	@SuppressWarnings("unused")
	private final Logger LOG = LoggerFactory
			.getLogger(OPCTransportHandler.class);

	private String host;

	private String domain;

	private String username;

	private String password;

	private String id;

	public OPCTransportHandler() {
		super();
	}

	public OPCTransportHandler(IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(byte[] message) throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public ITransportHandler createInstance(
			IProtocolHandler<?> protocolHandler, Map<String, String> options) {
		OPCTransportHandler handler = new OPCTransportHandler(protocolHandler);
		handler.setOptionsMap(options);

		handler.host = options.containsKey("host") ? options.get("host")
				: "localhost";
		handler.domain = options.containsKey("domain") ? options.get("domain")
				: "";
		handler.username = options.containsKey("username") ? options
				.get("username") : "";
		handler.password = options.containsKey("password") ? options
				.get("password") : "";
		handler.id = options.containsKey("id") ? options.get("id") : UUID
				.randomUUID().toString();

		return handler;
	}

	@Override
	public InputStream getInputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public OutputStream getOutputStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getName() {
		return "OPC";
	}

	@Override
	public void processInOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutOpen() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processInClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public void processOutClose() throws IOException {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
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
}
