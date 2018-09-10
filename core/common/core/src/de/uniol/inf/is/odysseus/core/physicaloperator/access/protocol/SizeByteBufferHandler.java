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
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.IAccessPattern;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportDirection;

public class SizeByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>> extends AbstractObjectHandlerByteBufferHandler<T> {
	private static final Logger LOG = LoggerFactory
			.getLogger(SizeByteBufferHandler.class);

	private int size = -1;
	
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	
	public SizeByteBufferHandler() {
		super();
	}

	public SizeByteBufferHandler(ITransportDirection direction,
			IAccessPattern access, IStreamObjectDataHandler<T> dataHandler,
			OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
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
	public void close() throws IOException {
		getTransportHandler().close();
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
	}

	@Override
	public void write(T object) throws IOException {
		// write with message size
		ByteBuffer buffer = prepareObject(object);

		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 4];
		insertInt(rawBytes, 0, messageSizeBytes);
		// buffer.array() returns the complete array (1024 bytes) and
		// did not apply the "real" size of the object
		buffer.get(rawBytes, 4, messageSizeBytes);
		getTransportHandler().send(rawBytes);
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
			// as long as there is something contained --> process
			while (message.remaining() > 0) {

				// size is negative if new object or no object read
				if (size == -1) {
					while (sizeBuffer.position() < 4 && message.remaining() > 0) {
						sizeBuffer.put(message.get());
					}
					// info for size complete
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				// handle message part, if size is completly read
				if (size != -1) {
					// could be less than needed for object --> wait
					if (currentSize + message.remaining() < size) {
						currentSize = currentSize + message.remaining();
						objectHandler.put(message);
					} else {
						// Splitt: message contains more than the current object
						// add every for the current object
						objectHandler.put(message, size - currentSize);
						// create object 
						processObject();
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
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
			IStreamObjectDataHandler<T> dataHandler) {
		SizeByteBufferHandler<T> instance = new SizeByteBufferHandler<T>(
				direction, access, dataHandler, options);
		return instance;
	}

	@Override
	public String getName() {
		return "SizeByteBuffer";
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
