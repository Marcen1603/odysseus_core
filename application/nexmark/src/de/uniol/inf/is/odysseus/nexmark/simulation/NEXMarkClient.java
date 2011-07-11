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
package de.uniol.inf.is.odysseus.nexmark.simulation;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.nexmark.generator.NEXMarkStreamType;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.ObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.sink.ByteBufferStreamHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleDataHandler;

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
	// public IObjectHandler<RelationalTuple<ITimeInterval>> objectHandler;
	boolean useNIO = false;
	private Socket connection;
	// private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private ByteBufferStreamHandler nioStreamHandler;

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
		IAtomicDataHandler handler = new RelationalTupleDataHandler(
				NEXMarkStreamType.getSchema(streamType));
		ObjectHandler<RelationalTuple<ITimeInterval>> objectHandler = new ObjectHandler<RelationalTuple<ITimeInterval>>(
				handler);
		nioStreamHandler = new ByteBufferStreamHandler(connection,
				objectHandler);
	}

	@Override
	public String toString() {
		return streamType.name + "-Client";
	}

	public void writeObject(RelationalTuple<ITimeInterval> tuple, boolean flush)
			throws IOException {

		if (useNIO) {
			nioStreamHandler.transfer(tuple);
		} else {
			objectOutputStream.writeObject(tuple);
			if (flush) {
				objectOutputStream.flush();
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
