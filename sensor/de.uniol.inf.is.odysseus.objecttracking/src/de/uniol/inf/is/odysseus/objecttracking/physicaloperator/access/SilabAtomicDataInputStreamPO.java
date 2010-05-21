package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access;

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.AbstractAtomicByteDataHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.DoubleByteHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IntegerByteHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.StringByteHandler;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * @author Dennis Wiemann
 */
public class SilabAtomicDataInputStreamPO<M extends IMetaAttribute> extends AbstractSensorAccessPO<RelationalTuple<M>, M>{
	
	final private String hostName;
	final private int port;
	private BufferedInputStream channel;
	private boolean isOpen;
	private RelationalTuple<M> buffer;
	private AbstractAtomicByteDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;
	private SDFAttributeList outputSchema;
	private int typeAttribute;
	private int timestampAttribute;

	private boolean p2p = false;
	public boolean connectToPipe = false;

	public boolean isConnectToPipe() {
		return connectToPipe;
	}

	public void setConnectToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}
	
	public SilabAtomicDataInputStreamPO(String host, int port,
			SDFAttributeList schema) {
		this.hostName = host;
		this.port = port;
		this.attributeData = new Object[schema.size()];
		createDataReader(schema);
		this.outputSchema = schema;
//		this.typeAttribute = ;
//		this.timestampAttribute = ;
	}
	
	private void createDataReader(SDFAttributeList schema) {
		this.dataReader = new AbstractAtomicByteDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			if (uri.equals("Integer")) {
				this.dataReader[i++] = new IntegerByteHandler();
			}/* else if (uri.equals("Long")) {
				this.dataReader[i++] = new LongHandler();
			}*/
			// double values and measurement values can
			// be read the same way since measurement values
			// are also double values.
			else if (uri.equals("Double") || uri.equals("MV")) {
				this.dataReader[i++] = new DoubleByteHandler();
			} else if (uri.equals("String")) {
				this.dataReader[i++] = new StringByteHandler();
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
	protected void process_open() throws OpenFailedException {
		if (!p2p) {
			if (this.isOpen) {
				return;
			}
			try {
				Socket socket = new Socket(this.hostName, this.port);
				this.channel = new BufferedInputStream(socket.getInputStream());
			} catch (IOException e) {
				throw new OpenFailedException(e.getMessage());
			}
			for (AbstractAtomicByteDataHandler reader : this.dataReader) {
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
						this.channel = new BufferedInputStream(s.getInputStream());
					} catch (Exception e) {
						// throw new OpenFailedException(e.getMessage());
						System.err.println("Konnte Quelle nicht öffnen");
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
//		if(!this.attributeData[this.typeAttribute].equals("punctuation")){
			this.buffer = new RelationalTuple<M>(this.attributeData);
			// this.buffer.setMetadata(this.metadataFactory.createMetadata());
//		}
//		else{
//			this.sendPunctuation(new PointInTime(((Integer)this.attributeData[this.timestampAttribute]).intValue()));
//		}
		
		return true;
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public void transferNext() {
//		System.out.println("TRANSFER BUFFER: " + this.buffer);
		transfer(this.buffer);
		this.buffer = null;
	}
	
	@Override
	public SDFAttributeList getOutputSchema(){
		return this.outputSchema;
	}
	
	@Override
	public SilabAtomicDataInputStreamPO<M> clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException();
	}

}
