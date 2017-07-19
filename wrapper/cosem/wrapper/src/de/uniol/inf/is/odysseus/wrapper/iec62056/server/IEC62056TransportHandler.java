package de.uniol.inf.is.odysseus.wrapper.iec62056.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.lang.NotImplementedException;
import org.openmuc.jdlms.AuthenticationMechanism;
import org.openmuc.jdlms.RawMessageData;
import org.openmuc.jdlms.RawMessageListener;
import org.openmuc.jdlms.SecuritySuite;
import org.openmuc.jdlms.SecuritySuite.EncryptionMechanism;
import org.openmuc.jdlms.TcpConnectionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.AbstractPushTransportHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class IEC62056TransportHandler extends AbstractPushTransportHandler {

	public static final Logger log = LoggerFactory.getLogger(IEC62056TransportHandler.class);

	private int port;
	private final int defaulPort = 6789;
	private int maxClients;
	private String host;

	public IEC62056TransportHandler() {
	}

	public IEC62056TransportHandler(IProtocolHandler<?> protocolHandler, OptionMap options)
			throws UnknownHostException {
		super(protocolHandler, options);
		init(options);
	}

	private void init(OptionMap options) throws UnknownHostException {
		// options.checkRequiredException(hostKey);
		port = options.getInt("port", defaulPort);

	}

	public void init2() throws UnknownHostException {
		
		InetAddress inetAddress = InetAddress.getByName("127.0.0.1");
		SecuritySuite securitySuite = SecuritySuite.builder()
				.setPassword("Password".getBytes(StandardCharsets.US_ASCII))
				.setAuthenticationMechanism(AuthenticationMechanism.LOW)
				.setEncryptionMechanism(EncryptionMechanism.NONE).build();
		TcpConnectionBuilder connectionBuilder = new TcpConnectionBuilder(inetAddress).setTcpPort(6789)
				.setSecuritySuite(securitySuite).setRawMessageListener(new RawMessageListener() {
					@Override
					public void messageCaptured(RawMessageData rawMessageData) {
						// TODO: log data
						// logger.debug(.. rawMessageData.getMessageSource() ..
						System.out.println("here some data!!! ---> " + rawMessageData.toString());
					}
				});
		
	}

	@Override
	public String getName() {
		return "DLMS/COSEM";
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
	public void send(byte[] message) throws IOException {
		throw new NotImplementedException(
				"The method send(byte[]) of the IEC62056 transport handler is not implemented! "
						+ "Use the IEC62056 transport handler only with the None protocol handler so "
						+ "that the method send(Object) is called.");
	}

	@Override
	public boolean isSemanticallyEqualImpl(ITransportHandler other) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITransportHandler createInstance(IProtocolHandler<?> protocolHandler, OptionMap options) {
		// TODO Auto-generated method stub
		return null;
	}

}
