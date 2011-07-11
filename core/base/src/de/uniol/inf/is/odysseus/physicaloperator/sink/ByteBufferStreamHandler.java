package de.uniol.inf.is.odysseus.physicaloperator.sink;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;

@SuppressWarnings("rawtypes")
public class ByteBufferStreamHandler implements ISinkStreamHandler<Object> {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ByteBufferStreamHandler.class);
	
	private IObjectHandler objectHandler;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private Socket connection;


	public ByteBufferStreamHandler(Socket connection, IObjectHandler objectHandler){
		this.objectHandler = objectHandler;
		this.connection = connection;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void transfer(Object object) throws IOException {
			if (object != null){
				objectHandler.put(object);
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

	@Override
	public void done() throws IOException {
		connection.getChannel().close();
	}

}
