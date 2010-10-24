package de.uniol.inf.is.odysseus.relational.base.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.IPhysicalOperator;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.access.DoubleHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IntegerHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.LongHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.StringHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

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
	private ObjectInputStream channel;
	private RelationalTuple<M> buffer;
	private IAtomicDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;
	
	private SDFAttributeList schema;

	private boolean p2p = false;
	public boolean connectToPipe = false;
	private Socket socket;

	public boolean isConnectToPipe() {
		return connectToPipe;
	}

	public void setConnectToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}

	public AtomicDataInputStreamAccessPO(String host, int port,
			SDFAttributeList schema) {
		this.hostName = host;
		this.port = port;
		this.attributeData = new Object[schema.size()];
		createDataReader(schema);
		this.schema = schema;
	}

	private void createDataReader(SDFAttributeList schema) {
		this.dataReader = new IAtomicDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			if (uri.equals("Integer")) {
				this.dataReader[i++] = new IntegerHandler();
			} else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
				this.dataReader[i++] = new LongHandler();
			}
			// double values and measurement values can
			// be read the same way since measurement values
			// are also double values.
			else if (uri.equals("Double") || uri.equals("MV")) {
				this.dataReader[i++] = new DoubleHandler();
			} else if (uri.equals("String")) {
				this.dataReader[i++] = new StringHandler();
			} else {
				throw new RuntimeException("illegal datatype " + uri);
			}
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
		} catch (IOException e) {
			throw new OpenFailedException(e.getMessage()+" "+this.hostName+" "+this.port);
		}
		for (IAtomicDataHandler reader : this.dataReader) {
			reader.setStream(this.channel);
		}
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
					this.attributeData[i] = dataReader[i].readData();
				}
			} catch (EOFException e) {
				this.isDone = true;
				propagateDone();
				return false;
			} catch (IOException e) {
				// TODO wie mit diesem fehler umgehen?
				//e.printStackTrace();
				return false;
			}
			this.buffer = new RelationalTuple<M>(this.attributeData);
			// this.buffer.setMetadata(this.metadataFactory.createMetadata());

			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public synchronized void transferNext() {
		// System.out.println("TRANSFER BUFFER: " + this.buffer);
		if (this.buffer != null){
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
	
	public boolean process_isSemanticallyEqual(IPhysicalOperator ipo) {
		if(!(ipo instanceof AtomicDataInputStreamAccessPO)) {
			return false;
		}
		AtomicDataInputStreamAccessPO adisapo = (AtomicDataInputStreamAccessPO) ipo;
		if(this.hostName.equals(adisapo.hostName) && this.port == adisapo.port && this.schema.equals(adisapo.schema)) {
			return true;
		}
		return false;
	}

}
