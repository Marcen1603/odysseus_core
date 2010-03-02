package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;

/**
 * A NEXMarkClient wraps the data for a query: socket of connection, the 
 * OutputStream and the Streamname 
 */
public class NEXMarkClient {
	private static final Logger logger = LoggerFactory.getLogger( NEXMarkClient.class );
	// Socket connection;
	private ObjectOutputStream objectOutputStream;
	public NEXMarkStreamType streamType;
	public RelationalTupleObjectHandler<ITimeInterval> objectHandler;
	boolean useNIO = false;
	private Socket connection;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);

//	/**
//	 * Filtert aus der sourceURI die Relation herraus, die simuliert werden soll
//	 * 
//	 * @param sourceURI
//	 * @return Relationenname oder null bei falscher eingabe
//	 */
//	private void filterStreamName(String sourceURI) throws WrongStreamNameException {
//		if (sourceURI.endsWith("person")) {
//			streamType = TupleType.person;
//		} else if (sourceURI.endsWith("auction")) {
//			streamType = TupleType.auction;
//		} else if (sourceURI.endsWith("bid")) {
//			streamType = TupleType.bid;
//		} else if (sourceURI.endsWith("category")) {
//			streamType = TupleType.category;
//		} else {
//			throw new WrongStreamNameException(sourceURI);
//		}
//	}

	/**
	 * Creates a new {@link NEXMarkClient}, which gets the stream name to be simulated over the socket
	 * 
	 * @param connection
	 *            Socket of connection
	 * @throws IOException
	 *             if connection cannot be established
	 * @throws WrongStreamNameException
	 */
	public NEXMarkClient(Socket connection, NEXMarkStreamType type, boolean useNIO) throws IOException {
		this.useNIO = useNIO;
		this.streamType = type; 
		this.connection = connection;
		// create stream for client
		if (!useNIO){
			this.objectOutputStream = new ObjectOutputStream(connection.getOutputStream());
		}
		objectHandler = new RelationalTupleObjectHandler<ITimeInterval>(NEXMarkStreamType.getSchema(streamType));
	}

	@Override
	public String toString() {
		return streamType.name + "-Client";
	}

	public void writeObject(RelationalTuple<ITimeInterval> tuple, boolean flush) throws IOException {
		
		if (useNIO){
			if (tuple != null){
				objectHandler.put(tuple);
				ByteBuffer buffer = objectHandler.getByteBuffer();
				
	//			for (int i=0;i<b.length;i++){
	//				System.out.print((char)b[i]+"("+b[i]+")");
	//			}
	//			System.out.println();
				synchronized(gbuffer){
					gbuffer.clear();
					gbuffer.putInt(buffer.limit());
					gbuffer.flip();
					SocketChannel ch = connection.getChannel();
					ch.write(gbuffer);
					ch.write(buffer);
				}
				logger.debug("written Object of size "+buffer.limit()+" "+tuple);
			}else{
				connection.getChannel().close();
			}
		}else{
			objectOutputStream.writeObject(tuple);
			if (flush){
				objectOutputStream.flush();
			}
		}
		

		
	}

	public void close() throws IOException {
		if (!useNIO){
			objectOutputStream.close();
		}else{
			System.out.println("Connection "+streamType+" to "+connection.getInetAddress()+" closed");
			connection.close();
		}
	}
}

//class WrongStreamNameException extends Exception {
//	private static final long serialVersionUID = -6134876802326637502L;
//
//	public WrongStreamNameException(String sourceURI) {
//		super("Stream with name '" + sourceURI + "' does not exist!");
//	}
//}
