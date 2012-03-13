/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.p2p.jxta.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JxtaConfiguration {

	static Logger logger = LoggerFactory.getLogger(JxtaConfiguration.class);

	final private String name;
	final private int tcpPort;
	final private int httpPort;
	final private String logging;
	final private boolean randomName;
	final private int connectionTime;
	final private boolean multicast;
	final private boolean http;
	final private boolean tcp;
	final private boolean useSuperpeer;
	final private String tcpRendevousUri;
	final private String httpRendevourUri;
	final private String tcpRelayUri;
	final private String httpRelayUri;
	final private String tcpInterfaceAddress;
	final private String httpInterfaceAddress;

	public JxtaConfiguration(String filename) throws IOException, FileNotFoundException {
		Properties properties = readProperties(filename);
		name = properties.getProperty("name");
		tcpPort = Integer.parseInt(properties.getProperty("tcpPort"));
		httpPort = Integer.parseInt(properties.getProperty("httpPort"));
		logging = properties.getProperty("logging");
		randomName = Boolean.parseBoolean(properties.getProperty("randomName"));
		connectionTime = Integer.parseInt(properties
				.getProperty("connectionTime"));
		multicast = Boolean.parseBoolean(properties.getProperty("multicast"));
		http = Boolean.parseBoolean(properties.getProperty("http"));
		tcp = Boolean.parseBoolean(properties.getProperty("tcp"));
		useSuperpeer = Boolean.parseBoolean(properties
				.getProperty("useSuperpeer"));
		tcpRendevousUri = properties.getProperty("tcp_rendevous_uri");
		httpRendevourUri = properties.getProperty("http_rendevous_uri");
		tcpRelayUri = properties.getProperty("tcp_relay_uri");
		httpRelayUri = properties.getProperty("http_relay_uri");
		tcpInterfaceAddress = properties.getProperty("tcp_interface_address");
		httpInterfaceAddress = properties.getProperty("http_interface_address");
	}

	/**
	 * Liest die Properties ein.
	 * 
	 * @return die Properties
	 * @throws IOException
	 *             wenn Properties file nicht gelesen werden kann
	 */
	private static Properties readProperties(String filename) throws IOException, FileNotFoundException {
		logger.debug("Reading Peer Properties from " + filename);
		FileInputStream in;
		in = new FileInputStream(filename);
		Properties properties = new Properties();
		properties.loadFromXML(in);
		return properties;
	}

	public String getName() {
		return name;
	}
	
	public int getTcpPort() {
		return tcpPort;
	}

	public int getHttpPort() {
		return httpPort;
	}

	public String getLogging() {
		return logging;
	}

	public boolean isRandomName() {
		return randomName;
	}

	public int getConnectionTime() {
		return connectionTime;
	}

	public boolean isMulticast() {
		return multicast;
	}

	public boolean isHttp() {
		return http;
	}

	public boolean isTcp() {
		return tcp;
	}

	public boolean isUseSuperPeer() {
		return useSuperpeer;
	}

	public String getTcpRendezvousUri() {
		return tcpRendevousUri;
	}

	public String getHttpRendezvousUri() {
		return httpRendevourUri;
	}

	public String getTcpRelayUri() {
		return tcpRelayUri;
	}

	public String getHttpRelayUri() {
		return httpRelayUri;
	}

	public String getTcpInterfaceAddress() {
		return tcpInterfaceAddress;
	}

	public String getHttpInterfaceAddress() {
		return httpInterfaceAddress;
	}

}
