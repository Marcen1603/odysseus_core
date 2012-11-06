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
package de.uniol.inf.is.odysseus.securitypunctuation.handler;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.IProtocolHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.SizeByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;
import de.uniol.inf.is.odysseus.core.securitypunctuation.ISecurityPunctuation;

/**
 * @author Jan S�ren Schwarz
 *
 * @param <T>
 */
public class SASizeByteBufferHandler<T extends ByteBuffer> extends SizeByteBufferHandler<T> {

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private SAByteBufferHandler<T> objectHandler;
	/**
	 * Gibt an, ob das gerade �bertragene Tupel eine Security Punctuation ist
	 */
	private Boolean isSP = null;
	private String spType = "attribute";
	private ByteBuffer isSPBuffer = ByteBuffer.allocate(4);
	
    public SASizeByteBufferHandler() {
        super();
    }

    public SASizeByteBufferHandler(ITransportDirection direction, IAccessPattern access) {
        super(direction, access);
    }

    @Override
	public void open() throws UnknownHostException, IOException {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		getTransportHandler().open();
		isSPBuffer.clear();
		isSP = null;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		
	}

	@Override
	public void close() throws IOException {
		
		//close funktioniert anscheinend irgenwie noch nicht...
		getTransportHandler().close();
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		isSPBuffer.clear();
		isSP = null;
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		
	}

	@Override
	public void process(ByteBuffer buffer) {
		try {

//			System.out.println("SizeByteBufferHandler - buffer beim ankommen: " + buffer);
			buffer.order(byteOrder);
//			System.out.println("SizeByteBufferHandler - buffer beim ankommen 2: " + buffer);  
			
			while (buffer.remaining() > 0) {

//				System.out.println(buffer);
				// size ist dann ungleich -1 wenn die vollst�ndige
				// Gr��eninformation �bertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles �bertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						
//						System.out.println("size: " + size);
						
					}
				}

				// nach der Gr��eninformation wird die SP-Flag �bertragen
				if(isSP == null) {
					while (isSPBuffer.position() < 4 && buffer.remaining() > 0) {
						isSPBuffer.put(buffer.get());
					}
					// Wenn alles �bertragen
					if (isSPBuffer.position() == 4) {
						isSPBuffer.flip();
						int getInt = isSPBuffer.getInt();
						switch(getInt) {
							case(0): isSP = false; break;
							case(1): isSP = true; spType = "attribute"; break;
							case(2): isSP = true; spType = "predicate"; break;
						}
						currentSize = currentSize + 4;
					}
				}

//				System.out.println(buffer);
				// Es kann auch direkt nach der size noch was im Puffer
				// sein!
				// Und Size kann gesetzt worden sein
				if (size != -1 && isSP != null) {
					// Ist das was dazukommt kleiner als die finale Gr��e?
										
//					System.out.println("SizeByteBufferHandler - buffer.remaining: " + buffer.remaining());
//					System.out.println("SizeByteBufferHandler - size: " + size);
//					System.out.println("SizeByteBufferHandler - currentSize: " + currentSize);
					
					if (currentSize + buffer.remaining() < size) {
						currentSize = currentSize + buffer.remaining();
						objectHandler.put(buffer);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler �bergeben
//						 logger.debug(" "+(size-currentSize));
//						System.out.println("SizeByteBuffer - Buffer: " + buffer);
						objectHandler.put(buffer, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						if(!isSP) {
//							System.out.println("SizeByteBuffer - !isSP - Buffer: " + buffer);
							getTransfer().transfer(objectHandler.create());
						} else {
//							System.out.println("SizeByteBuffer - isSP - Buffer: " + buffer);
							getTransfer().transferSecurityPunctuation((ISecurityPunctuation) objectHandler.createSecurityAware(spType));
						}
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
						isSPBuffer.clear();
						isSP = null;
						// Dann in der While-Schleife weiterverarbeiten
					}
				}
			}			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BufferUnderflowException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	   public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access,
	            Map<String, String> options, IDataHandler<T> dataHandler, ITransferHandler<T> transfer) {
		SASizeByteBufferHandler<T> instance = new SASizeByteBufferHandler<T>(direction,access);
		instance.setDataHandler(dataHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new SAByteBufferHandler<T>(dataHandler);
		instance.setByteOrder(options.get("byteorder"));
		return instance;
	}
	
	@Override
	public String getName() {
		return "SASizeByteBuffer";
	}
}
