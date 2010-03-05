package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.ServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;


/**
 * Server providing a Stream by fetching values from a
 * sensor. Based on java.nio
 * @author Simon Flandergan
 *
 */
public class StreamServer extends Thread {

	private ServerSocket socket;
	private ISourceClient sourceClient;
	private Logger logger;
	
	/**
	 * Creates a new Server for specific sensor, running on port
	 * @param sensor Sensor which should be used for fetching data
	 * @param port 
	 * @throws IOException
	 */
	public StreamServer (ISourceClient client, int port) throws IOException{
		ServerSocketChannel serverChannel = ServerSocketChannel.open();
		this.socket = serverChannel.socket();
		this.socket.bind(new InetSocketAddress(port));
		serverChannel.configureBlocking(true);
		
		this.sourceClient = client;
			
		this.logger = LoggerFactory.getLogger( StreamServer.class );
	}
	
	@Override
	public void run() {
		String schema = "";
		for (SDFAttribute attr : this.sourceClient.getSchema()){
			schema += attr.getAttributeName()+":"+attr.getDatatype().getQualName()+" ";
		}
		logger.info("\nServer with schema: ( "+schema+")\n listening on port: "+this.socket.getLocalPort());
		while (true) {
			// Wait for Client connection
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
				synchronized (this.sourceClient){
					//start sensor client if it's not yet running
					if (!this.sourceClient.isAlive()){
						this.sourceClient.start();
					}
				}
				
				this.sourceClient.addClient(new StreamClient(connection, this.sourceClient.getSchema()));
			} catch (IOException e) {
				logger.error("Client with ip: " + connection.getInetAddress()
						+ " could not be handled.");
				continue;
			}
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		this.closeSockets();
	}

	public void closeSockets() {
		try{
			if (this.socket != null && !this.socket.isClosed()) {
				this.socket.close();
			}
			
			if (this.sourceClient != null && this.sourceClient.isAlive()){
				this.sourceClient.cleanUp();
			}
		}catch (IOException e){
			e.printStackTrace();
		}
	}

}
