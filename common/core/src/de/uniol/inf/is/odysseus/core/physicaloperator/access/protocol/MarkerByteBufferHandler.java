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

public class MarkerByteBufferHandler<T extends IStreamObject<? extends IMetaAttribute>>
		extends AbstractObjectHandlerByteBufferHandler<T> {

	public static final String START = "start";
	public static final String END = "end";

	private static final Logger LOG = LoggerFactory.getLogger(MarkerByteBufferHandler.class);

	protected byte start;
	protected byte end;

	public MarkerByteBufferHandler() {
		super();
	}

	public MarkerByteBufferHandler(ITransportDirection direction, IAccessPattern access,
			IStreamObjectDataHandler<T> dataHandler, OptionMap optionsMap) {
		super(direction, access, dataHandler, optionsMap);
		optionsMap.checkRequiredException(START);
		final String startMarker = optionsMap.get(START);
		if (startMarker.length() == 1) {
			// single character
			start = (byte) startMarker.charAt(0);
		} else {
			// string with decimal digits
			start = Byte.parseByte(startMarker);
		}
		if (optionsMap.containsKey(END)) {
			final String endMarker = optionsMap.get(END);
			if (endMarker.length() == 1) {
				// single character
				end = (byte) endMarker.charAt(0);
			} else {
				// string with decimal digits
				end = Byte.parseByte(endMarker);

			}
		} else {
			end = start;
		}
	}

	@Override
	public void write(T object) throws IOException {
		// write with marker
		ByteBuffer buffer = prepareObject(object);
		int messageSizeBytes = buffer.remaining();
		byte[] rawBytes = new byte[messageSizeBytes + 2];

		//insertInt(rawBytes, 0, start);
		rawBytes[0] = start;
		buffer.get(rawBytes, 1, messageSizeBytes);
		//insertInt(rawBytes, messageSizeBytes + 4, end);
		rawBytes[messageSizeBytes+1] = end;

		getTransportHandler().send(rawBytes);
	}

	@Override
	public synchronized void process(long callerId, ByteBuffer message) {
		// TODO: handle callerId
		try {
			int startPosition = -1;
			while (message.remaining() > 0) {
				byte value = message.get();

				// Could be inside an object --> ignore and wait for next start
				if (startPosition > 0 && value == end) {
					int endPosition = message.position() - 1;
					message.position(startPosition);
					try {
						objectHandler.put(message, endPosition - message.position());
					} catch (IOException e) {
						LOG.error(e.getMessage(), e);
					}
					message.position(endPosition + 1);
					startPosition = message.position() + 1;

					processObject();
				}
				if (value == start) {
					objectHandler.clear();
					startPosition = message.position();
				}
			}
			if (startPosition < message.limit()) {
				message.position(startPosition);
				try {
					objectHandler.put(message);
				} catch (IOException e) {
					LOG.error(e.getMessage(), e);
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
	public IProtocolHandler<T> createInstance(ITransportDirection direction, IAccessPattern access, OptionMap options,
			IStreamObjectDataHandler<T> dataHandler) {
		MarkerByteBufferHandler<T> instance = new MarkerByteBufferHandler<T>(direction, access, dataHandler, options);

		return instance;
	}

	@Override
	public String getName() {
		return "MarkerByteBuffer";
	}

	@Override
	public boolean isSemanticallyEqualImpl(IProtocolHandler<?> o) {
		if (!(o instanceof MarkerByteBufferHandler)) {
			return false;
		}
		MarkerByteBufferHandler<?> other = (MarkerByteBufferHandler<?>) o;
		if (this.start != other.getStart() || this.end != other.getEnd()
				|| !this.byteOrder.toString().equals(other.getByteOrder().toString())) {
			return false;
		}
		return true;
	}

	public byte getStart() {
		return start;
	}

	public byte getEnd() {
		return end;
	}
}
