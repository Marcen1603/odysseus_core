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
package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.core.metadata.PointInTime;
import de.uniol.inf.is.odysseus.core.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.AbstractByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IAccessConnection;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IObjectHandler;

/**
 * The BrokerByteBufferReceiverPO is a physical source which is able to receive elements of type W.
 * It works like {@link ByteBufferReceiverPO}, but it differs between normal elements and punctuations. 
 * The first four bytes (an integer) represents the type of the following bytes:
 * - 0 = normal element 
 * - 1 = punctuation 
 * - 2 = done
 * 
 * A normal element consists of 4 bytes for an integer which indicates the size and multiple bytes for the raw data.
 * The punctuation consists of 8 bytes for a long which represents the timestamp.
 * Done means that a source has no more elements.
 *
 * @param <W> the generic type
 */
public class BrokerByteBufferReceiverPO<W> extends AbstractByteBufferReceiverPO<W> {

	/** The size of the following element. */
	private int size = -1;
	
	/** The type of the following element. */
	private int type = 0;
	
	/** The size buffer. */
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	
	/** The type buffer. */
	private ByteBuffer typeBuffer = ByteBuffer.allocate(4);
	
	/** The time buffer. */
	private ByteBuffer timeBuffer = ByteBuffer.allocate(8);
	
	/** The current size. */
	private int currentSize = 0;
	

	
	
	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param handler the handler which wraps an element
	 * @param host the host
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BrokerByteBufferReceiverPO(IObjectHandler<W> handler, IAccessConnection accessHandler) throws IOException {
		super(handler, accessHandler);
	}

	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param byteBufferReceiverPO the original to copy from
	 * @ 
	 */
	public BrokerByteBufferReceiverPO(BrokerByteBufferReceiverPO<W> byteBufferReceiverPO)  {
		super(byteBufferReceiverPO);
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
	}

	@Override
	public synchronized void process_open() throws OpenFailedException {
		sizeBuffer.clear();
		typeBuffer.clear();
		timeBuffer.clear();
		size = -1;
		super.process_open();
	}

	@Override
	public void process_close() {
		sizeBuffer.clear();
		typeBuffer.clear();
		timeBuffer.clear();
		size = -1;
		super.process_close();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.access.IRouterReceiver#process(java.nio.ByteBuffer)
	 */
	@Override
	public void process(ByteBuffer buffer) {
		try {

			while (buffer.remaining() > 0) {
				if (size == -1) {
					// type
					while (typeBuffer.position() < 4 && buffer.remaining() > 0) {
						typeBuffer.put(buffer.get());
					}
					if (typeBuffer.position() == 4) {
						typeBuffer.flip();
						type = typeBuffer.getInt();
					}

					// size
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
					}
				}
				if (size != -1) {
					if (type == 0) {
						if (currentSize + buffer.remaining() <= size) {
							currentSize = currentSize + buffer.remaining();
							objectHandler.put(buffer);
						} else {
							objectHandler.put(buffer, size - currentSize);
							transfer();
						}
					} else {
						if (type == 1) {

							if (currentSize + buffer.remaining() <= size) {
								currentSize = currentSize + buffer.remaining();
								timeBuffer.put(buffer);
							} else {
								for (int i = 0; i < (size - currentSize); i++) {
									timeBuffer.put(buffer.get());
								}
								timeBuffer.flip();
								long time = timeBuffer.getLong();
								sendPunctuation(new PointInTime(time));
								size = -1;
								sizeBuffer.clear();
								typeBuffer.clear();
								timeBuffer.clear();
								currentSize = 0;
							}

						} else {
							done();
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.core.server.physicaloperator.AbstractSource#clone()
	 */
	@Override
	public BrokerByteBufferReceiverPO<W> clone()  {
		return new BrokerByteBufferReceiverPO<W>(this);
	}


}
