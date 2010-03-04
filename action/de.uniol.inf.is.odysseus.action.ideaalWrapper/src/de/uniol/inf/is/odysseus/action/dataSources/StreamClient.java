package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
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

	private RelationalTupleObjectHandler<IMetaAttribute> objectHandler;
	private Socket connection;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);

	public StreamClient(Socket connection, SDFAttributeList schema) throws IOException{
		this.connection = connection;
		
		this.objectHandler = new RelationalTupleObjectHandler<IMetaAttribute>(schema);
	}
	
	public void writeObject(RelationalTuple<IMetaAttribute> tuple) throws IOException {
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
		}
	}

	public void closeSocket() {
		try {
			this.connection.getChannel().close();
			this.connection.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
