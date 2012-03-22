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
package de.uniol.inf.is.odysseus.core.server.physicaloperator.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.core.server.store.OsgiObjectInputStream;

/**
 * @author Jonas Jacobi, Marco Grawunder
 */
public class ObjectInputStreamAccessPO<M> extends
		AbstractIterableSource<M> {

	static Logger _logger = null;

	static synchronized public Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory
					.getLogger(ObjectInputStreamAccessPO.class);
		}
		return _logger;
	}

	final private String hostName;
	final private int port;
	final private IDataHandler<M> dataHandler;
	final private String user;
	final private String password;
	private OsgiObjectInputStream channel;
	private M buffer;
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

	public ObjectInputStreamAccessPO(String host, int port,
			SDFSchema schema, IDataHandler<M> dataHandler, String user, String password) {
		this.hostName = host;
		this.port = port;
		this.schema = schema;
		this.dataHandler = dataHandler;
		this.user = user;
		this.password = password;
	}

	@Override
	protected void process_done() {
		super.process_done();
		try {
			this.channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		getLogger().debug("process_open()");

		try {
			socket = new Socket(this.hostName, this.port);
			this.channel = new OsgiObjectInputStream(socket.getInputStream());
			buffer = null;
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
		this.isDone = false;
	}

	@Override
	protected void process_close() {
		try {
			getLogger().debug("Closing connection");
			socket.close();
			channel.close();
			getLogger().debug("Closing connection done");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

    @Override
	public synchronized boolean hasNext() {
		if (isOpen()) {

			if (buffer != null) {
				return true;
			}

			try {
				buffer = dataHandler.readData(channel);
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


	@Override
	public ObjectInputStreamAccessPO<M> clone() {
		throw new RuntimeException("Clone Not implemented yet");
	}

	@Override
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if (!(ipo instanceof ObjectInputStreamAccessPO)) {
			return false;
		}
		ObjectInputStreamAccessPO<?> adisapo = (ObjectInputStreamAccessPO<?>) ipo;
		if (this.hostName.equals(adisapo.hostName) && this.port == adisapo.port
				&& this.schema.equals(adisapo.schema)) {
			return true;
		}
		return false;
	}

}
