package de.uniol.inf.is.odysseus.action.ideaalwrapper;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Client writing tuples to client connected to
 * {@link StreamServer}
 * @author Simon Flandergan
 *
 */
public class StreamClient {

	private RelationalTupleObjectHandler<ITimeInterval> objectHandler;
	private Socket connection;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);

	public StreamClient(Socket connection, SDFAttributeList schema) throws IOException{
		this.connection = connection;
		
		this.objectHandler = new RelationalTupleObjectHandler<ITimeInterval>(schema);
	}
	
	public void writeObject(RelationalTuple<ITimeInterval> tuple) throws IOException {
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
			
		}else{
			connection.getChannel().close();
		}
	}

	public void closeSocket() {
		try {
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
