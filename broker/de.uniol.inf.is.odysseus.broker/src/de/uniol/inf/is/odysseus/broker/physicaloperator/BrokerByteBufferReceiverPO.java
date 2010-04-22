package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IRouterReceiver;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.Router;

public class BrokerByteBufferReceiverPO<W> extends AbstractSource<W> implements IRouterReceiver {

	private IObjectHandler<W> handler;
	private int size = -1;
	private int type = 0;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private ByteBuffer typeBuffer = ByteBuffer.allocate(4);
	private ByteBuffer timeBuffer = ByteBuffer.allocate(8);
	private int currentSize = 0;
	private Router router;
	private String host;
	private int port;
	boolean opened;

	public BrokerByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port) throws IOException {
		super();
		this.handler = handler;
		router = Router.getInstance();
		this.host = host;
		this.port = port;
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public BrokerByteBufferReceiverPO(BrokerByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super();
		handler = (IObjectHandler<W>) byteBufferReceiverPO.handler.clone();
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
		router = byteBufferReceiverPO.router;
		host = byteBufferReceiverPO.host;
		port = byteBufferReceiverPO.port;
		opened = byteBufferReceiverPO.opened;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		if (!opened) {
			try {
				router.connectToServer(this, host, port);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}

	@Override
	public void done() {
		propagateDone();
	}

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
							handler.put(buffer);
						} else {
							handler.put(buffer, size - currentSize);
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

	// @Override
	// public void process(ByteBuffer buffer) {
	// super.process(buffer);
	// System.out.println("Send punctuation");
	// sendPunctuation(PointInTime.currentPointInTime());
	// }

	private synchronized void transfer() throws IOException, ClassNotFoundException {
		W toTrans = handler.create();
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		typeBuffer.clear();
		currentSize = 0;
	}

	@Override
	public BrokerByteBufferReceiverPO<W> clone() {
		return new BrokerByteBufferReceiverPO<W>(this);
	}

	@Override
	public String toString() {
		return super.toString() + " " + host + " " + port;
	}

}
