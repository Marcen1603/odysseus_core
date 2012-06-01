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
package de.uniol.inf.is.odysseus.action.dataSources;

import java.io.IOException;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.IObjectHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;

/**
 * Client writing tuples to client connected to
 * {@link StreamServer}
 * @author Simon Flandergan
 *
 */
public class StreamClient {

	private IObjectHandler<Tuple<IMetaAttribute>> objectHandler;
	private Socket connection;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);

	public StreamClient(Socket connection, SDFSchema schema) {
		this.connection = connection;
		
		this.objectHandler = new ByteBufferHandler<Tuple<IMetaAttribute>>(new TupleDataHandler().getInstance(schema));
	}
	
	public void writeObject(Tuple<IMetaAttribute> tuple) throws IOException {
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
