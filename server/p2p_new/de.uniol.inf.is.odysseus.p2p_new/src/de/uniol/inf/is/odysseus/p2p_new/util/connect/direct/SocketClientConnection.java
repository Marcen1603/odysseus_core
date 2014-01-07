package de.uniol.inf.is.odysseus.p2p_new.util.connect.direct;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

import net.jxta.protocol.PipeAdvertisement;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class SocketClientConnection extends SocketConnection {

	private static final Logger LOG = LoggerFactory.getLogger(SocketClientConnection.class);
	private static final int MAX_CONNECT_WAITING_TIME_MILLIS = 15000;

	private final String serverAddress;
	private final int port;
	private final PipeAdvertisement pipeAdvertisement;
	
	public SocketClientConnection( String ip, int port, PipeAdvertisement pipeAdvertisement ) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(ip), "IP must not be null or empty!");
		Preconditions.checkArgument(port > 0, "Port must be positive");
		Preconditions.checkNotNull(pipeAdvertisement, "Pipe Advertisement for socket connection must not be null!");
		
		this.serverAddress = ip;
		this.port = port;
		this.pipeAdvertisement = pipeAdvertisement;
	}
	
	@Override
	public void connect() throws IOException {
		try {
			boolean connected = false;
			final long startTime = System.currentTimeMillis();
			while( !connected ) {
				try {
					Socket socket = new Socket();
					socket.connect(new InetSocketAddress(serverAddress, port), 2000);
					
					setSocket(socket);
					connected = true;
				} catch( SocketTimeoutException ex ) {
					if( System.currentTimeMillis() - startTime > MAX_CONNECT_WAITING_TIME_MILLIS) {
						throw new IOException("Connecting takes too long");
					}
				}
			}
		} catch (Throwable t) {
			LOG.error("Could not connect to server", t);
			setConnectFail();
			throw t;
		}
		
		super.connect();
	}
	
	public PipeAdvertisement getPipeAdvertisement() {
		return pipeAdvertisement;
	}
}
