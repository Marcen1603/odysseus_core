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
package com.xafero.turjumaan.server.sdk.core;

import java.lang.reflect.Field;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.inductiveautomation.opcua.stack.server.Endpoint;
import com.inductiveautomation.opcua.stack.server.tcp.SocketServer;
import com.inductiveautomation.opcua.stack.server.tcp.UaTcpServer;

/**
 * Some optional patches.
 */
final class Patches {

	/** The Constant log. */
	private static final Logger log = LoggerFactory.getLogger(Patches.class);

	/**
	 * Instantiates a new patches.
	 */
	private Patches() {
	}

	/**
	 * Patch for "Prosys OPC UA Client Lite".
	 */
	@SuppressWarnings("unchecked")
	static void patchCrazyEndpoint() {
		try {
			// Patch internal server map
			Field socketSvrFld = SocketServer.class.getDeclaredField("socketServers");
			socketSvrFld.setAccessible(true);
			Map<InetSocketAddress, SocketServer> socketSrvs = (Map<InetSocketAddress, SocketServer>) socketSvrFld
					.get(null);
			Iterator<Entry<InetSocketAddress, SocketServer>> ssi = socketSrvs.entrySet().iterator();
			if (!ssi.hasNext())
				return;
			Entry<InetSocketAddress, SocketServer> firstSrv = ssi.next();
			SocketServer socketSrv = firstSrv.getValue();
			Field serversFld = socketSrv.getClass().getDeclaredField("servers");
			serversFld.setAccessible(true);
			Map<String, UaTcpServer> servers = (Map<String, UaTcpServer>) serversFld.get(socketSrv);
			Entry<String, UaTcpServer> firstTcp = servers.entrySet().iterator().next();
			servers.put("", firstTcp.getValue());
			// Patch endPoint descriptions
			Field endpFld = UaTcpServer.class.getDeclaredField("endpoints");
			endpFld.setAccessible(true);
			List<Endpoint> endpoints = (List<Endpoint>) endpFld.get(firstTcp.getValue());
			for (Endpoint endpoint : endpoints.toArray(new Endpoint[0]))
				endpoints.add(new Endpoint(new URI(""), endpoint.getBindAddress().orElse(null),
						endpoint.getCertificate().get(), endpoint.getSecurityPolicy(), endpoint.getMessageSecurity()));
			// Log a short hint why we are doing this
			log.info("Patched for HELLO messages with empty endpoint URLs...");
		} catch (IllegalAccessException | NoSuchFieldException | SecurityException | URISyntaxException e) {
			throw new UnsupportedOperationException(e);
		}
	}
}