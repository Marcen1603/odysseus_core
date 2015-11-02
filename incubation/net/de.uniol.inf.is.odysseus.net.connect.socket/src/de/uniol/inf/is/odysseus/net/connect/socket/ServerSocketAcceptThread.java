package de.uniol.inf.is.odysseus.net.connect.socket;

import java.net.ServerSocket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;

public class ServerSocketAcceptThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ServerSocketAcceptThread.class);
	
	private final ServerSocket serverSocket;
	
	private boolean running = false;
	
	public ServerSocketAcceptThread(ServerSocket serverSocket) {
		Preconditions.checkNotNull(serverSocket, "serverSocket must not be null!");

		this.serverSocket = serverSocket;
		
		setName("Server socket accept thread on port " + serverSocket.getLocalPort());
		setDaemon(true);
	}
	
	@Override
	public void run() {
		LOG.info("Starting server socket accept thread using port {}", serverSocket.getLocalPort());
		
		running = true;
		while( running ) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
			}
		}
		
		LOG.info("Server socket accept thread finished");
	}
	
	public void stopRunning() {
		running = false;
	}

}
