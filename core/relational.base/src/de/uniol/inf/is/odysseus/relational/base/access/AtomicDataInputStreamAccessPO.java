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
package de.uniol.inf.is.odysseus.relational.base.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IDataHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

/**
 * @author Jonas Jacobi
 */
public class AtomicDataInputStreamAccessPO<M extends IMetaAttribute> extends
		AbstractIterableSource<RelationalTuple<M>> {

	static Logger _logger = null;

	static synchronized public Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory
					.getLogger(AtomicDataInputStreamAccessPO.class);
		}
		return _logger;
	}

	final private String hostName;
	final private int port;
	final private String user;
	final private String password;
	private ObjectInputStream channel;
	private RelationalTuple<M> buffer;
	private IDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;

	private SDFSchema schema;

	public boolean connectToPipe = false;
	private Socket socket;

	public boolean isConnectToPipe() {
		return connectToPipe;
	}

	public void setConnectToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}

	public AtomicDataInputStreamAccessPO(String host, int port,
			SDFSchema schema, String user, String password) {
		this.hostName = host;
		this.port = port;
		this.attributeData = new Object[schema.size()];
		createDataReader(schema);
		this.schema = schema;
		this.user = user;
		this.password = password;
	}

	private void createDataReader(SDFSchema schema) {
		this.dataReader = new IDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			IDataHandler handler = DataHandlerRegistry
					.getDataHandler(uri);
			if (handler == null) {
				throw new IllegalArgumentException("No handler for datatype "
						+ uri);
			}
            this.dataReader[i++] = handler;

			// String upperCaseURI = uri.toUpperCase();
			// if (upperCaseURI.equals("DOUBLE") || uri.equals("MV")) {
			// this.dataReader[i++] = new DoubleHandler();
			// } else if (upperCaseURI.equals("STRING")) {
			// this.dataReader[i++] = new StringHandler();
			// } else if (upperCaseURI.equals("INTEGER")) {
			// this.dataReader[i++] = new IntegerHandler();
			// } else if
			// (upperCaseURI.equals("LONG")||upperCaseURI.endsWith("TIMESTAMP"))
			// {
			// this.dataReader[i++] = new LongHandler();
			// } else {
			// throw new RuntimeException("illegal datatype " + upperCaseURI);
			// }
		}
	}

	@Override
	protected void process_done() {
		super.process_done();
		try {
			this.channel.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getLogger().debug("process_open()");

		// // if (!p2p) {
		// if (isOpen()) {
		// return;
		// }
		try {
			socket = new Socket(this.hostName, this.port);
			this.channel = new ObjectInputStream(socket.getInputStream());
			// Send login information
			if (user != null && password != null) {
				PrintWriter out = new PrintWriter
					    (socket.getOutputStream(), true);
				out.println(user);
				out.println(password);
			}
		} catch (IOException e) {
			throw new OpenFailedException(e.getMessage() + " " + this.hostName
					+ " " + this.port);
		}
		// for (IDataHandler reader : this.dataReader) {
		// reader.setStream(this.channel);
		// }
		this.isDone = false;
		// }
	}

	@Override
	protected void process_close() {
		try {
			getLogger().debug("Closing connection");
			socket.close();
			channel.close();
			getLogger().debug("Closing connection done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public synchronized boolean hasNext() {
		if (isOpen()) {

			if (buffer != null) {
				return true;
			}
			// if (p2p) {
			// if (!connectToPipe) {
			// return false;
			// }
			// if (this.channel == null) {
			// Socket s;
			// while (true) {
			// try {
			// s = new Socket(this.hostName, this.port);
			// this.channel = new ObjectInputStream(s.getInputStream());
			// } catch (Exception e) {
			// // throw new OpenFailedException(e.getMessage());
			// System.err.println("Konnte Quelle nicht öffnen");
			// try {
			// Thread.sleep(5000);
			// } catch (InterruptedException e1) {
			// // TODO Auto-generated catch block
			// e1.printStackTrace();
			// }
			// continue;
			// }
			// break;
			// }
			// }
			// }

			try {
				for (int i = 0; i < this.dataReader.length; ++i) {
					this.attributeData[i] = dataReader[i].readData(channel);
				}
			} catch (EOFException e) {
				this.isDone = true;
				propagateDone();
				return false;
			} catch (IOException e) {
				// TODO wie mit diesem fehler umgehen?
				// e.printStackTrace();
				this.isDone = true;
				propagateDone();
				return false;
			}
			this.buffer = new RelationalTuple<M>(this.attributeData);
			// this.buffer.setMetadata(this.metadataFactory.createMetadata());

			return true;
		}
        return false;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public synchronized void transferNext() {
		// System.out.println("TRANSFER BUFFER: " + this.buffer);
		if (this.buffer != null) {
			transfer(this.buffer);
			this.buffer = null;
		}
	}

	//
	// public void setMetadataFactory(
	// IMetadataFactory<M, RelationalTuple<M>> metadataFactory) {
	// this.metadataFactory = metadataFactory;
	// }

	// public boolean isP2P() {
	// return p2p;
	// }
	//
	// public void setP2P(boolean p2p) {
	// this.p2p = p2p;
	// }

	@Override
	public AtomicDataInputStreamAccessPO<M> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof AtomicDataInputStreamAccessPO)) {
			return false;
		}
		AtomicDataInputStreamAccessPO<?> adisapo = (AtomicDataInputStreamAccessPO<?>) ipo;
		if (this.hostName.equals(adisapo.hostName) && this.port == adisapo.port
				&& this.schema.equals(adisapo.schema)) {
			return true;
		}
		return false;
	}

}
