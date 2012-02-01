package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;

public class MarkerByteBufferReceiverPO<W> extends
		AbstractByteBufferReceiverPO<W> {

	private byte end;
	private byte start;

	public MarkerByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnection accessHandler, byte start, byte end)
			throws IOException {
		super(objectHandler, accessHandler);
		this.start = start;
		this.end = end;
	}

	public MarkerByteBufferReceiverPO(IObjectHandler<W> objectHandler,
			IAccessConnection accessHandler, byte marker) throws IOException {
		super(objectHandler, accessHandler);
		this.start = marker;
		this.end = marker;
	}

	public MarkerByteBufferReceiverPO(
			MarkerByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super(byteBufferReceiverPO);
		start = byteBufferReceiverPO.start;
		end = byteBufferReceiverPO.end;

	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		super.process_open();
	}

	@Override
	protected void process_close() {
		super.process_close();
	}

	@Override
	public synchronized void process(ByteBuffer buffer) {
		if (opened) {
			try {
				int pos = 0;
				while (buffer.remaining() > 0) {
					byte value = buffer.get();
					if (value == end) {
						int endPosition = buffer.position() - 1;
						buffer.position(pos);
						objectHandler.put(buffer, endPosition - pos);
						buffer.position(endPosition + 1);
						pos = buffer.position();
						transfer();
					}
					if (value == start) {
						objectHandler.clear();
						pos = buffer.position();
					}
				}
				if (pos >= 0) {
					buffer.position(pos);
					objectHandler.put(buffer);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
				accessHandler.reconnect();
			}
		}
	}

	@Override
	public AbstractSource<W> clone() {
		return new MarkerByteBufferReceiverPO<W>(this);
	}

}
