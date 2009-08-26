package de.uniol.inf.is.odysseus.relational.base.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.IIterableSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.DoubleHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IntegerHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.LongHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.StringHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Jonas Jacobi
 */
public class AtomicDataInputStreamAccessPO<M extends IClone> extends
		AbstractSource<RelationalTuple<M>> implements
		IIterableSource<RelationalTuple<M>>{//, IP2PInputPO 

	// private IMetadataFactory<M, RelationalTuple<M>> metadataFactory;
	final private String hostName;
	final private int port;
	private ObjectInputStream channel;
	private boolean isOpen;
	private RelationalTuple<M> buffer;
	private IAtomicDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;

	private boolean p2p = false;
	public boolean connectToPipe = false;

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
	}

	private void createDataReader(SDFAttributeList schema) {
		this.dataReader = new IAtomicDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			if (uri.equals("Integer")) {
				this.dataReader[i++] = new IntegerHandler();
			} else if (uri.equals("Long")) {
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
				throw new RuntimeException("illegal datatype");
			}
		}
	}

	@Override
	protected void process_done() {
		super.process_done();
		this.isOpen = false;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {

		if (!p2p) {
			if (this.isOpen) {
				return;
			}
			try {
				Socket socket = new Socket(this.hostName, this.port);
				this.channel = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				throw new OpenFailedException(e.getMessage());
			}
			for (IAtomicDataHandler reader : this.dataReader) {
				reader.setStream(this.channel);
			}
			this.isOpen = true;
			this.isDone = false;
		}
	}

	@Override
	public boolean hasNext() {

		if (p2p) {
			if (!connectToPipe) {
				return false;
			}
			if (this.channel == null) {
				Socket s;
				while (true) {
					try {
						s = new Socket(this.hostName, this.port);
						this.channel = new ObjectInputStream(s.getInputStream());
					} catch (Exception e) {
						// throw new OpenFailedException(e.getMessage());
						System.err
								.println("Konnte Quelle nicht öffnen");
						try {
							Thread.sleep(5000);
						} catch (InterruptedException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						continue;
					}
					break;
				}
			}
		}

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
			return false;
		}
		this.buffer = new RelationalTuple<M>(this.attributeData);
		// this.buffer.setMetadata(this.metadataFactory.createMetadata());

		return true;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void transferNext() {
		System.out.println("TRANSFER BUFFER: " + this.buffer);
		transfer(this.buffer);
		this.buffer = null;
	}

	//
	// public void setMetadataFactory(
	// IMetadataFactory<M, RelationalTuple<M>> metadataFactory) {
	// this.metadataFactory = metadataFactory;
	// }

//	public boolean isP2P() {
//		return p2p;
//	}
//
//	public void setP2P(boolean p2p) {
//		this.p2p = p2p;
//	}

}
