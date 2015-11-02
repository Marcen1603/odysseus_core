package de.uniol.inf.is.odysseus.net.connect.socket;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

public class ServerSocketAcceptThread extends Thread {

	private static final Logger LOG = LoggerFactory.getLogger(ServerSocketAcceptThread.class);
	
	private final ServerSocket serverSocket;
	private final Collection<SocketReceiveThread> receiveThreads = Lists.newArrayList();
	
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
		try {
			
			while( running ) {
				Socket clientSocket = serverSocket.accept();
				LOG.debug("Accepted connection from {}", clientSocket.getInetAddress());
				
				SocketReceiveThread receiveThread = new SocketReceiveThread(clientSocket);
				receiveThread.start();
				receiveThreads.add(receiveThread);
			}
			
		} catch( IOException e ) {
			LOG.error("Can not accept connections from other nodes", e);
		}
		
		LOG.info("Server socket accept thread finished");
	}
	
	public void stopRunning() {
		running = false;
		
		for( SocketReceiveThread thread : receiveThreads ) {
			thread.stopRunning();
			
			try {
				thread.getClientSocket().close();
			} catch (IOException e) {
			}
		}
		
		receiveThreads.clear();
	}

}
