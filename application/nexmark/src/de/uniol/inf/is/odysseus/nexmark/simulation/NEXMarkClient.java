/*******************************************************************************
 * Copyright 2012 The Odysseus Team
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
/********************************************************************************** 
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.server.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.sink.ByteBufferStreamHandler;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;

/**
 * A NEXMarkClient wraps the data for a query: socket of connection, the
 * OutputStream and the Streamname
 */
public class NEXMarkClient {
	@SuppressWarnings("unused")
	private static final Logger logger = LoggerFactory
			.getLogger(NEXMarkClient.class);
	// Socket connection;
	private ObjectOutputStream objectOutputStream;
	public NEXMarkStreamType streamType;
	// public IObjectHandler<Tuple<ITimeInterval>> objectHandler;
	boolean useNIO = false;
	private Socket connection;
	// private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private ByteBufferStreamHandler nioStreamHandler;
	private ByteBufferHandler<Tuple<ITimeInterval>> objectHandler;

	// /**
	// * Filtert aus der sourceURI die Relation herraus, die simuliert werden
	// soll
	// *
	// * @param sourceURI
	// * @return Relationenname oder null bei falscher eingabe
	// */
	// private void filterStreamName(String sourceURI) throws
	// WrongStreamNameException {
	// if (sourceURI.endsWith("person")) {
	// streamType = TupleType.person;
	// } else if (sourceURI.endsWith("auction")) {
	// streamType = TupleType.auction;
	// } else if (sourceURI.endsWith("bid")) {
	// streamType = TupleType.bid;
	// } else if (sourceURI.endsWith("category")) {
	// streamType = TupleType.category;
	// } else {
	// throw new WrongStreamNameException(sourceURI);
	// }
	// }

	/**
	 * Creates a new {@link NEXMarkClient}, which gets the stream name to be
	 * simulated over the socket
	 * 
	 * @param connection
	 *            Socket of connection
	 * @throws IOException
	 *             if connection cannot be established
	 * @throws WrongStreamNameException
	 */
	public NEXMarkClient(Socket connection, NEXMarkStreamType type,
			boolean useNIO) throws IOException {
		this.useNIO = useNIO;
		this.streamType = type;
		this.connection = connection;
		// create stream for client
		if (!useNIO) {
			this.objectOutputStream = new ObjectOutputStream(
					connection.getOutputStream());
		}
		IDataHandler<?> handler = new TupleDataHandler().getInstance(
				NEXMarkStreamType.getSchema(streamType));
		objectHandler = new ByteBufferHandler<Tuple<ITimeInterval>>(
				handler);
		nioStreamHandler = new ByteBufferStreamHandler(connection);
	}

	@Override
	public String toString() {
		return streamType.name + "-Client";
	}

	public void writeObject(Tuple<ITimeInterval> tuple, boolean flush)
			throws IOException {
		if (tuple != null) {
			if (useNIO) {
				objectHandler.put(tuple);
				nioStreamHandler.transfer(objectHandler.getByteBuffer());
			} else {
				objectOutputStream.writeObject(tuple);
				if (flush) {
					objectOutputStream.flush();
				}
			}
		}
	}

	public void close() throws IOException {
		if (!useNIO) {
			objectOutputStream.close();
		} else {
			System.out.println("Connection " + streamType + " to "
					+ connection.getInetAddress() + " closed");
			connection.close();
		}
	}
}

// class WrongStreamNameException extends Exception {
// private static final long serialVersionUID = -6134876802326637502L;
//
// public WrongStreamNameException(String sourceURI) {
// super("Stream with name '" + sourceURI + "' does not exist!");
// }
// }
