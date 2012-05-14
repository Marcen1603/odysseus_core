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

import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.net.Socket;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

/**
 * @author Dennis Wiemann
 */
public class SilabAtomicDataInputStreamPO<M extends IMetaAttribute> extends AbstractSensorAccessPO<Tuple<M>, M>{
	
	final private String hostName;
	final private int port;
	private BufferedInputStream channel;
	private boolean isOpen;
	private Tuple<M> buffer;
	private AbstractSILABDataHandler[] dataReader;
	private Object[] attributeData;
	private boolean isDone;
//	private int typeAttribute;
//	private int timestampAttribute;

	private boolean p2p = false;
	public boolean connectToPipe = false;

	public boolean isConnectToPipe() {
		return connectToPipe;
	}

	public void setConnectToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}
	
	public SilabAtomicDataInputStreamPO(String host, int port,
			SDFSchema schema) {
		this.hostName = host;
		this.port = port;
		this.attributeData = new Object[schema.size()];
		createDataReader(schema);
//		this.typeAttribute = ;
//		this.timestampAttribute = ;
	}
	
	private void createDataReader(SDFSchema schema) {
		this.dataReader = new AbstractSILABDataHandler[schema.size()];
		int i = 0;
		for (SDFAttribute attribute : schema) {
			String uri = attribute.getDatatype().getURI(false);
			if (uri.equals("Integer")) {
				this.dataReader[i++] = new SILABIntegerHandler();
			}/* else if (uri.equals("Long") || uri.endsWith("Timestamp")) {
				this.dataReader[i++] = new LongHandler();
			}*/
			// double values and measurement values can
			// be read the same way since measurement values
			// are also double values.
			else if (uri.equals("Double") || uri.equals("MV")) {
				this.dataReader[i++] = new SILABDoubleHandler();
			} else if (uri.equals("String")) {
				this.dataReader[i++] = new SILABStringHandler();
			} else {
				throw new RuntimeException("illegal datatype "+uri);
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
			for (AbstractSILABDataHandler reader : this.dataReader) {
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
			this.buffer = new Tuple<M>(this.attributeData);
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
	public SilabAtomicDataInputStreamPO<M> clone()  {
		throw new RuntimeException("Clone Not implemented yet");
	}

}
