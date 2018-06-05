/**********************************************************************************
  * Copyright 2011 The Odysseus Team
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
package de.uniol.inf.is.odysseus.core.objecthandler;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.datahandler.IStreamObjectDataHandler;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;

public class ByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>> implements IObjectHandler<T> {

	private static final int BYTEBUF_INIT = 2048;
	ByteBuffer byteBuffer = null;
	protected IStreamObjectDataHandler<?> dataHandler;

	public ByteBufferHandler() {
	}

	@Override
	public IObjectHandler<T> getInstance(IStreamObjectDataHandler<T> dataHandler) {
		return new ByteBufferHandler<T>(dataHandler);
	}

	public ByteBufferHandler(IStreamObjectDataHandler<?> dataHandler) {
		byteBuffer = ByteBuffer.allocate(BYTEBUF_INIT);
		this.dataHandler = dataHandler;
	}

	public ByteBufferHandler(ByteBufferHandler<T> objectHandler) {
		super();
		this.dataHandler = objectHandler.dataHandler;
	}

	@Override
	public void clear() {
		byteBuffer.clear();
	}

	@Override
	public ByteBuffer getByteBuffer() {
		return byteBuffer;
	}

	@SuppressWarnings({ "unchecked" })
	@Override
	public synchronized T create() throws IOException, ClassNotFoundException, BufferUnderflowException {
		T retval = null;
		synchronized (byteBuffer) {
			byteBuffer.flip();
			retval = (T) this.dataHandler.readData(byteBuffer);
			byteBuffer.clear();
		}
		return retval;
	}

	private void checkAndResizeBuffer(ByteBuffer buffer, int size) {
		if (size + byteBuffer.position() >= byteBuffer.capacity()) {
			// TODO: More efficient overflow handling?
			ByteBuffer newBB = ByteBuffer.allocate((buffer.limit() + size + byteBuffer.position()) * 2);
			int pos = byteBuffer.position();
			byteBuffer.flip();
			newBB.put(byteBuffer);
			byteBuffer = newBB;
			byteBuffer.position(pos);
		}
	}

	@Override
	public void put(ByteBuffer buffer) throws IOException {
		synchronized (buffer) {
			synchronized (byteBuffer) {
				checkAndResizeBuffer(byteBuffer, buffer.remaining());
				byteBuffer.put(buffer);
			}
		}
	}

	@Override
	public void put(ByteBuffer buffer, int size) throws IOException {
		synchronized (buffer) {
			synchronized (byteBuffer) {
				checkAndResizeBuffer(byteBuffer, size);
				if (buffer.isDirect()) {
					// Fallback for direct buffers ... that do not implement e.g array
					for (int i = 0; i < size; i++) {
						byteBuffer.put(buffer.get());
					}
				} else {
					byteBuffer.put(buffer.array(), buffer.position(), size);
					buffer.position(buffer.position() + size);
				}
				// System.out.println("putBuffer2 "+buffer+" to "+byteBuffer);
			}
		}

	}

	@Override
	public void put(T value, boolean withMetadata) {
		synchronized (byteBuffer) {
			checkAndResizeBuffer(byteBuffer, dataHandler.memSize(value));
			byteBuffer.clear();
			this.dataHandler.writeData(byteBuffer, value);
			byteBuffer.flip();
		}
	}

	@Override
	public ByteBufferHandler<T> clone() {
		return new ByteBufferHandler<T>(this);
	}

	@Override
	public String getName() {
		return "ByteBufferHandler";
	}

	protected final IDataHandler<?> getDataHandler() {
		return dataHandler;
	}

}
