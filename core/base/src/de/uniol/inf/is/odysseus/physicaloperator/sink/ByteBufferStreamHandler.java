package de.uniol.inf.is.odysseus.physicaloperator.sink;
/** Copyright [2011] [The Odysseus Team]
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*     http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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
