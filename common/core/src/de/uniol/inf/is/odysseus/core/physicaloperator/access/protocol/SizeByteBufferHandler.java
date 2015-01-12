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
import java.io.InputStream;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferUtil;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

public class SizeByteBufferHandler<T> extends AbstractByteBufferHandler<T> {
	private static final Logger LOG = LoggerFactory
			.getLogger(SizeByteBufferHandler.class);
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBufferHandler<T> objectHandler;

	public SizeByteBufferHandler() {
		super();
	}

	public SizeByteBufferHandler(ITransportDirection direction,
			IAccessPattern access, IDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		objectHandler = new ByteBufferHandler<T>(dataHandler);
	}

	@Override
	public void open() throws UnknownHostException, IOException {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		if (getTransportHandler() != null) {
			getTransportHandler().open();
		} else {
			throw new RuntimeException("No Transport handler set!");
		}
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

	@SuppressWarnings("rawtypes")
	@Override
	public void write(T object) throws IOException {
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		ByteBufferUtil.toBuffer(buffer, (IStreamObject) object,
				getDataHandler(), exportMetadata);
		// getDataHandler().writeData(buffer, object);
		buffer.flip();

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 4];
		insertInt(rawBytes, 0, messageSizeBytes);
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 4, messageSizeBytes);
		getTransportHandler().send(rawBytes);
	}

	@Override
	public boolean hasNext() throws IOException {
		if (getTransportHandler().getInputStream() != null) {
			return getTransportHandler().getInputStream().available() > 0;
		} else {
			return false;
		}
	}

	@Override
	public T getNext() throws IOException {
		InputStream input = getTransportHandler().getInputStream();
		int size = 0;
		int offset = 0;
		while (input.available() > 0) {
			size = input.read();
			input.read(objectHandler.getByteBuffer().array(), offset, size);			
			offset += size;
		}
		
		objectHandler.getByteBuffer().position(offset);
		
		try {
			return objectHandler.create();
		} catch (ClassNotFoundException | BufferUnderflowException e) {
			LOG.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public void process(long callerId, ByteBuffer message) {
		// TODO: handle callerId
		try {
			while (message.remaining() > 0) {

				// size ist dann ungleich -1 wenn die vollst�ndige
				// Gr��eninformation �bertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && message.remaining() > 0) {
						sizeBuffer.put(message.get());
					}
					// Wenn alles �bertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				// Es kann auch direkt nach der size noch was im Puffer
				// sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Gr��e?
					if (currentSize + message.remaining() < size) {
						currentSize = currentSize + message.remaining();
						objectHandler.put(message);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler �bergeben
						// logger.debug(" "+(size-currentSize));
						objectHandler.put(message, size - currentSize);
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
			LOG.error(e.getMessage(), e);
		} catch (BufferUnderflowException e) {
			LOG.error(e.getMessage(), e);
		} catch (ClassNotFoundException e) {
			LOG.error(e.getMessage(), e);
		}
	}

	@Override
	public IProtocolHandler<T> createInstance(ITransportDirection direction,
			IAccessPattern access, OptionMap options,
			IDataHandler<T> dataHandler) {
		SizeByteBufferHandler<T> instance = new SizeByteBufferHandler<T>(
				direction, access, dataHandler, options);

		return instance;
	}

	@Override
	public String getName() {
		return "SizeByteBuffer";
	}

	private static void insertInt(byte[] destArray, int offset, int value) {
		destArray[offset] = (byte) (value >>> 24);
		destArray[offset + 1] = (byte) (value >>> 16);
		destArray[offset + 2] = (byte) (value >>> 8);
		destArray[offset + 3] = (byte) (value);
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof SizeByteBufferHandler)) {
			return false;
		}
		SizeByteBufferHandler<?> other = (SizeByteBufferHandler<?>) o;
		if (!this.byteOrder.toString().equals(other.getByteOrder().toString())) {
			return false;
		}
		return true;
	}
}
