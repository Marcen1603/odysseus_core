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
package de.uniol.inf.is.odysseus.objecttracking.physicaloperator.access;

import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.DataHandlerRegistry;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAtomicDataHandler;
import de.uniol.inf.is.odysseus.core.server.util.LoggerHelper;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;

/**
 * @author Jonas Jacobi, Andre Bolles
 */
public class AtomicDataInputStreamAccessMVPO<M extends IProbability> extends AbstractSensorAccessPO<MVRelationalTuple<M>, M>{

	// private IMetadataFactory<M, RelationalTuple<M>> metadataFactory;
	final private String hostName;
	final private int port;
	private ObjectInputStream channel;
	private boolean isOpen;
	private MVRelationalTuple<M> buffer;
	private IAtomicDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;
	private SDFSchema outputSchema;
	private final String LOGGER_NAME = "AtomicDataInputStreamAccessMVPO";

//	private int limit;
//	
//	private int counter;

	private boolean p2p = false;
	private boolean connectToPipe = false;

	public void setConnectToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}

	public AtomicDataInputStreamAccessMVPO(String host, int port,
			SDFSchema schema) {
		this.hostName = host;
		this.port = port;
		this.attributeData = new Object[schema.size()];
		createDataReader(schema);
		this.outputSchema = schema;
	}

	private void createDataReader(SDFSchema schema) {
		this.outputSchema = schema;
		this.dataReader = new IAtomicDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			
			IAtomicDataHandler handler = DataHandlerRegistry.getDataHandler(uri);
			if(handler == null){
				throw new IllegalArgumentException("No handler for datatype " + uri);
			}
			else{
				this.dataReader[i++] = handler;
			}
			
//			if (uri.equals("Integer")) {
//				this.dataReader[i++] = new IntegerHandler();
//			} else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
//				this.dataReader[i++] = new LongHandler();
//			}
//			// double values and measurement values can
//			// be read the same way since measurement values
//			// are also double values.
//			else if (uri.equals("Double") || uri.equals("MV")) {
//				this.dataReader[i++] = new DoubleHandler();
//			} else if (uri.equals("String")) {
//				this.dataReader[i++] = new StringHandler();
//			} else if (uri.equals("Date")) {
//				this.dataReader[i++] = new DateHandler();
//			} else {
//				throw new RuntimeException("illegal datatype "+uri);
//			}
		}
	}

	@Override
	protected void process_done() {
		super.process_done();
		// Evaluation
		try {
			this.channel.close();
			LoggerHelper.getInstance(LOGGER_NAME).debug("Channel closed.");
		} catch (IOException e) {
			e.printStackTrace();
		}
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
//			for (IAtomicDataHandler reader : this.dataReader) {
//				reader.setStream(this.channel);
//			}
			this.isOpen = true;
			this.isDone = false;
		}
	}


	long start = -1;
	long end = -1;
	long duration = 0;
	int elemCount = 0;
	@Override
	public synchronized boolean hasNext() {
		
		if(this.start == -1){
			this.start = System.nanoTime();
		}
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
				this.attributeData[i] = dataReader[i].readData(channel);
			}
		} catch (EOFException e) {
			// System.out.println("READER DONE.");
			this.isDone = true;
			this.end = System.nanoTime();
			long duration = (end - start);
			long elems = this.elemCount * 1000L * 1000L * 1000L;
			System.out.println("elements/sec: " + ((double)(elems)/ (double)(duration)));
			
			propagateDone();
			return false;
		} catch (IOException e) {
			// System.out.println("READER DONE.");
			// TODO wie mit diesem fehler umgehen?
			return false;
		}

		this.buffer = new MVRelationalTuple<M>(this.attributeData);
		this.elemCount++;
		
		return true;
		// this.buffer = new MVRelationalTuple<M>(this.outputSchema,
		// this.attributeData);
		// this.buffer.setMetadata(this.metadataFactory.createMetadata());
	}

	@Override
	public boolean isDone() {
		return this.isDone;
	}

	@Override
	public synchronized void transferNext() {
//		System.out.println("BUFFER No: " + this.counter++ + " TRANSFER: " + this.buffer);
		transfer(this.buffer);
		this.buffer = null;
	}
	
	@Override
	public SDFSchema getOutputSchema() {
		return outputSchema;
	}

	//
	// public void setMetadataFactory(
	// IMetadataFactory<M, RelationalTuple<M>> metadataFactory) {
	// this.metadataFactory = metadataFactory;
	// }
	
	@Override
	public AtomicDataInputStreamAccessMVPO<M> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}


}
