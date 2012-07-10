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
package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class SizeByteBufferHandler<T> extends AbstractByteBufferHandler<T>{

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBufferHandler<T> objectHandler;
	
	@Override
	public void open() throws UnknownHostException, IOException {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		getTransportHandler().open();
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		
	}

	@Override
	public void close() throws IOException {
		getTransportHandler().close();
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		
	}
	
	@Override
	public void process(ByteBuffer buffer) {
		try {
			while (buffer.remaining() > 0) {

				// size ist dann ungleich -1 wenn die vollständige
				// Größeninformation übertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles übertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				// Es kann auch direkt nach der size noch was im Puffer
				// sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Größe?
					if (currentSize + buffer.remaining() < size) {
						currentSize = currentSize + buffer.remaining();
						objectHandler.put(buffer);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler übergeben
						// logger.debug(" "+(size-currentSize));
						objectHandler.put(buffer, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						getTransfer().transfer(objectHandler.create());
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
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
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		SizeByteBufferHandler<T> instance = new SizeByteBufferHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
		instance.setByteOrder(options.get("byteorder"));
		transportHandler.addListener(instance);
		return instance;
	}

	@Override
	public String getName() {
		return "SizeByteBuffer";
	}


}
