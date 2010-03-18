package de.uniol.inf.is.odysseus.broker.movingobjects.simulation;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import java.util.logging.Logger;

import de.uniol.inf.is.odysseus.broker.movingobjects.generator.MovingObjectsStreamType;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;

/**
 * A Client wraps the data for a query: socket of connection, the 
 * OutputStream and the Streamname 
 */
public class MovingObjectsClient {
	private static final Logger logger = Logger.getLogger( MovingObjectsClient.class.getCanonicalName() );
	public MovingObjectsStreamType streamType;
	public RelationalTupleObjectHandler<ITimeInterval> objectHandler;
	private Socket connection;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);

	/**
	 * Creates a new {@link NEXMarkClient}, which gets the stream name to be simulated over the socket
	 * 
	 * @param connection
	 *            Socket of connection
	 * @throws IOException
	 *             if connection cannot be established
	 * @throws WrongStreamNameException
	 */
	public MovingObjectsClient(Socket connection, MovingObjectsStreamType type) throws IOException {		
		this.streamType = type; 
		this.connection = connection;		
		objectHandler = new RelationalTupleObjectHandler<ITimeInterval>(MovingObjectsStreamType.getSchema(streamType));
	}

	@Override
	public String toString() {
		return streamType.name + "-Client";
	}

	public void writeObject(RelationalTuple<ITimeInterval> tuple, boolean flush) throws IOException {		
			if (tuple != null){
				objectHandler.put(tuple);
				ByteBuffer buffer = objectHandler.getByteBuffer();
				synchronized(gbuffer){
					gbuffer.clear();
					gbuffer.putInt(buffer.limit());
					gbuffer.flip();
					SocketChannel ch = connection.getChannel();
					ch.write(gbuffer);
					ch.write(buffer);
				}
				logger.info("written Object of size "+buffer.limit()+" "+tuple);
			}else{
				connection.getChannel().close();
			}
		
		

		
	}

	public void close() throws IOException {
		System.out.println("Connection "+streamType+" to "+connection.getInetAddress()+" closed");
		connection.close();
		
	}
}
