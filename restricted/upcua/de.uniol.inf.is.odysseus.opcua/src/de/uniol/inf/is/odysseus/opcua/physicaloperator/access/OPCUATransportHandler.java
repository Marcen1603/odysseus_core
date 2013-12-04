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
package de.uniol.inf.is.odysseus.opcua.physicaloperator.access;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.apache.http.conn.ssl.SSLSocketFactory;
import org.opcfoundation.ua.application.Application;
import org.opcfoundation.ua.application.Client;
import org.opcfoundation.ua.application.Session;
import org.opcfoundation.ua.application.SessionChannel;
import org.opcfoundation.ua.builtintypes.NodeId;
import org.opcfoundation.ua.common.ServiceFaultException;
import org.opcfoundation.ua.common.ServiceResultException;
import org.opcfoundation.ua.core.Attributes;
import org.opcfoundation.ua.core.Identifiers;
import org.opcfoundation.ua.transport.SecureChannel;
import org.opcfoundation.ua.transport.security.Cert;
import org.opcfoundation.ua.transport.security.CertificateValidator;
import org.opcfoundation.ua.transport.security.KeyPair;
import org.opcfoundation.ua.transport.security.PrivKey;
import org.opcfoundation.ua.transport.security.SecurityMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IIteratable;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPullTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.opcua.Activator;

public class OPCUATransportHandler extends AbstractPullTransportHandler implements IIteratable<KeyValueObject<IMetaAttribute>> {

	public static final String NAME = "OPCUA";

	private final Logger logger = LoggerFactory.getLogger(OPCUATransportHandler.class);

	public static final String ENDPOINT = "endpoint";
	public static final String PRIVATE_KEY = "privatekey";
	public static final String CERT = "cert";
	public static final String PASSWORD = "password";
	public static final String PARENT_NODE = "parentnode";
	public static final String PULL_DELAY = "pulldelay";
	public static final String DEPTH = "depth";
	public static final String REMOTE_CERT = "remotecert";

	private String endpoint;
	private String password;

	private String privateKeyFile;
	private String cert;
	private String remoteCert;
	private KeyPair keyPair;

	private String parentNode;
	private long pulldelay = 1000;
	private int depth = 100;

	private Client client;
	private SecureChannel channel;
	private SessionChannel sessionChannel;
	private boolean connected = false;

	private Map<String, NodeId> keyToNodeMappings = new TreeMap<>();

	private Cert serverCertificate;

	public OPCUATransportHandler() {
		super();
	}

	/**
	 * @param protocolHandler
	 */
	public OPCUATransportHandler(final IProtocolHandler<?> protocolHandler) {
		super(protocolHandler);
	}

	@Override
	public void send(final byte[] message) throws IOException {
		System.out.println("SEND: " + ByteBuffer.wrap(message).asCharBuffer().toString());
		throw new IllegalArgumentException("Currently not implemented");
	}

	@Override
	public ITransportHandler createInstance(final IProtocolHandler<?> protocolHandler, final Map<String, String> options) {
		final OPCUATransportHandler handler = new OPCUATransportHandler(protocolHandler);
		handler.setOptionsMap(options);
		handler.init(options);
		return handler;
	}

	private void init(final Map<String, String> options) {

		if (options.containsKey(ENDPOINT)) {
			setEndpoint(options.get(ENDPOINT));
		}
		if (options.containsKey(PRIVATE_KEY)) {
			privateKeyFile = options.get(PRIVATE_KEY);
		}
		if (options.containsKey(CERT)) {
			cert = options.get(CERT);
		}

		if (options.containsKey(PASSWORD)) {
			password = options.get(PASSWORD);
		}
		if (options.containsKey(PARENT_NODE)) {
			parentNode = options.get(PARENT_NODE);
		}
		if (options.containsKey(PULL_DELAY)) {
			pulldelay = Long.parseLong(options.get(PULL_DELAY));
		}
		if (options.containsKey(DEPTH)) {
			depth = Integer.parseInt(options.get(DEPTH));
		}
		if (options.containsKey(REMOTE_CERT)) {
			remoteCert = options.get(REMOTE_CERT);
		}

		try {
			if (privateKeyFile == null) {
				if (cert == null) {
					logger.debug("no private key and cert file. using odysseus' default private information exchange for the upc ua client");
					X509Certificate defCert = (X509Certificate) Activator.getSecurityProvider().getDefaultCertificate();
					RSAPrivateKey defKey = (RSAPrivateKey) Activator.getSecurityProvider().getDefaultKey();
					keyPair = new KeyPair(new Cert(defCert), new PrivKey(defKey));
				} else {
					throw new IllegalArgumentException("Certificate found, but a private key is missing!");
				}
			} else {
				if (cert == null) {
					throw new IllegalArgumentException("Private key file given, but the certificate is missing");
				} else {
					logger.debug("loading given private key and certificate for opc client...");
					logger.debug("Opening my certificate: " + cert);
					File certFile = new File(cert);
					Cert clientCertificate = Cert.load(certFile);

					logger.debug("Opening my private key: " + privateKeyFile);
					File privKeyFile = new File(privateKeyFile);
					PrivKey clientPrivateKey = PrivKey.load(privKeyFile, password);
					keyPair = new KeyPair(clientCertificate, clientPrivateKey);
				}
			}

			logger.debug("Opening remote certificate: " + remoteCert);
			File remoteCertFile = new File(remoteCert);
			serverCertificate = Cert.load(remoteCertFile);

			logger.debug("Creating client...");
			Application app = new Application();
			app.getHttpsSettings().setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			app.getHttpsSettings().setCertificateValidator(CertificateValidator.ALLOW_ALL);
			app.getHttpsSettings().setKeyPair(keyPair);
			app.addApplicationInstanceCertificate(keyPair);
			client = new Client(app);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getName() {
		return NAME;
	}

	@Override
	public void processInOpen() throws UnknownHostException, IOException {
		try {
			SecurityMode mode = SecurityMode.BASIC256_SIGN_ENCRYPT;
			logger.debug("Creating secure channel with security mode: " + mode);

			channel = client.createSecureChannel(endpoint, endpoint, mode, serverCertificate);

			logger.debug("Open session");
			Session session = client.createSession(channel);
			sessionChannel = session.createSessionChannel(channel, client);
			logger.debug("activating session...");
			sessionChannel.activate();
			logger.debug("querying adress space mapping from upc server...");
			NodeId masternode = Identifiers.ObjectsFolder;
			if (parentNode != null && !parentNode.isEmpty()) {
				masternode = NodeId.parseNodeId(parentNode);
			}
			this.keyToNodeMappings = OPCClient.fetchKeyNames(sessionChannel, masternode, depth);
			logger.debug("UPC UA is up and running");
			this.connected = true;
		} catch (ServiceResultException ex) {
			if (ex.getCause() instanceof InvalidKeyException) {
				System.err.println("Due to restrictions by law, your Java Cryptography Extension (JCE) does not provide enough key strength.");
				System.err.println("You have to install the JCE policy to your java home!");
				System.err.println("For Java 7, you will find this here: http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html");
			}
			throw new IOException(ex);
		}

	}

	@Override
	public boolean hasNext() {
		return this.connected;
	}

	@Override
	public KeyValueObject<IMetaAttribute> getNext() {
		try {

			KeyValueObject<IMetaAttribute> kvo = new KeyValueObject<>();
			for (Entry<String, NodeId> entry : this.keyToNodeMappings.entrySet()) {
				String value = OPCClient.getAttributeValue(sessionChannel, entry.getValue(), Attributes.Value);
				kvo.addAttributeValue(entry.getKey(), value);
			}
			try {
				Thread.sleep(pulldelay);
			} catch (InterruptedException e) {
			}
			// System.out.println(kvo.toStringWithNewlines());
			// System.out.println("******************************************************************");
			return kvo;
		} catch (ServiceResultException ex) {
			ex.printStackTrace();
		}
		return null;
	}

	@Override
	public void processInClose() throws IOException {
		this.connected = false;
		logger.debug("Closing UPC UA connection...");
		try {
			sessionChannel.close();
		} catch (ServiceFaultException e) {
			e.printStackTrace();
		} catch (ServiceResultException e) {
			e.printStackTrace();
		}
		channel.close();
		logger.debug("Connection closed");
		this.fireOnDisconnect();
	}

	@Override
	public void processOutOpen() throws UnknownHostException, IOException {

	}

	@Override
	public void processOutClose() throws IOException {

		this.fireOnDisconnect();
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler o) {
		if (!(o instanceof OPCUATransportHandler)) {
			return false;
		}
		OPCUATransportHandler other = (OPCUATransportHandler) o;
		if (!this.endpoint.equals(other.endpoint)) {
			return false;
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.
	 * ITransportHandler#getInputStream()
	 */
	@Override
	public InputStream getInputStream() {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.
	 * ITransportHandler#getOutputStream()
	 */
	@Override
	public OutputStream getOutputStream() {
		throw new RuntimeException("Sorry. Currently not implemented");
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

}
