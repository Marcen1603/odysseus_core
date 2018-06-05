package de.uniol.inf.is.odysseus.core.server.physicaloperator.sink;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.physicaloperator.sink.ISinkStreamHandler;


public class ByteBufferStreamHandler implements ISinkStreamHandler<ByteBuffer> {

	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory.getLogger(ByteBufferStreamHandler.class);
	
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private Socket connection;


	public ByteBufferStreamHandler(Socket connection){
		this.connection = connection;
	}
	
	@Override
	public void transfer(ByteBuffer buffer) throws IOException {
			if (buffer != null){
				synchronized(gbuffer){
					gbuffer.clear();
					gbuffer.putInt(buffer.limit());
					gbuffer.flip();
					
					connection.getOutputStream().write(gbuffer.array(), gbuffer.position(), gbuffer.limit());
					connection.getOutputStream().write(buffer.array(), buffer.position(), buffer.limit());
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
