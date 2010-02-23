package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Server providing a Stream by fetching values from a
 * sensor. Based on java.nio
 * @author Simon Flandergan
 *
 */
public class StreamServer extends Thread {

	private ServerSocket socket;
	private SocketSensorClient sensorClient;
	private Logger logger;
	
	/**
	 * Creates a new Server for specific sensor, running on port
	 * @param sensor Sensor which should be used for fetching data
	 * @param port 
	 * @throws IOException
	 */
	public StreamServer (Sensor sensor, int port) throws IOException{
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		this.socket = serverChannel.socket();
		this.socket.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(true);
		
		this.sensorClient = new SocketSensorClient(sensor.getIp(), sensor.getPort(), 
				sensor.getInterval(), sensor.getMessage(), sensor.getSchema());
			
		this.logger = LoggerFactory.getLogger( StreamServer.class );
	}
	
	@Override
	public void run() {
		while (true) {
			// Wait for Client connection
			logger.debug("waiting for connection on port: " + this.socket.getLocalPort());
			Socket connection = null;
			try {
				connection = socket.accept();
			} catch (IOException e) {
				//socket closed
				break;
			}
			
			logger.debug("Connection from: " + connection.getInetAddress());

			// Handle client connection
			try {
				synchronized (this.sensorClient){
					//start sensor client if it's not yet running
					if (this.sensorClient.isAlive()){
						this.sensorClient.start();
					}
				}
				
				this.sensorClient.addClient(new StreamClient(connection, this.sensorClient.getSchema()));
				
				//increase client count
				
			} catch (IOException e) {
				logger.error("Client with ip: " + connection.getInetAddress()
						+ " could not be handled.");
				continue;
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		if (this.socket != null && !this.socket.isClosed()) {
			this.socket.close();
		}
		
		if (this.sensorClient != null && this.sensorClient.isAlive()){
			this.sensorClient.closeSocket();
		}
	}

}
