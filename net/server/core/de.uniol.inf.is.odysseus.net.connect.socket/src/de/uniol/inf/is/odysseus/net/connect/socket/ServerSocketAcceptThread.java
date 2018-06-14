package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ServerSocketAcceptThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ServerSocketAcceptThread.class);
	
	private final ServerSocket serverSocket;
	private final IServerSocketAcceptListener listener;
	
	private boolean running = false;
	
	public ServerSocketAcceptThread(ServerSocket serverSocket, IServerSocketAcceptListener listener) {
		Preconditions.checkNotNull(serverSocket, "serverSocket must not be null!");
		Preconditions.checkNotNull(listener, "listener must not be null!");

		this.serverSocket = serverSocket;
		this.listener = listener;
		
		setName("Server socket accept thread on port " + serverSocket.getLocalPort());
		setDaemon(true);
	}
	
	@Override
	public void run() {
		LOG.info("Starting server socket accept thread using port {}", serverSocket.getLocalPort());
		
		running = true;
		try {
			
			while( running ) {
				LOG.debug("Waiting for next connection");
				Socket clientSocket = serverSocket.accept();
				LOG.debug("Accepted connection from {}:{}", clientSocket.getInetAddress(), clientSocket.getPort());
				
				listener.acceptedConnection(clientSocket);
			}
			
		} catch( IOException e ) {
			LOG.warn("Can not accept connections from other nodes", e);
		}
		
		LOG.info("Server socket accept thread finished");
	}
	
	public void stopRunning() {
		running = false;
	}

}
