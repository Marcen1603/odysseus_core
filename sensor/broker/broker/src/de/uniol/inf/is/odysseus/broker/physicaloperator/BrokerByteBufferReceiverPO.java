package de.uniol.inf.is.odysseus.broker.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.access.ByteBufferReceiverPO;
import de.uniol.inf.is.odysseus.physicaloperator.access.IObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.access.IRouterReceiver;
import de.uniol.inf.is.odysseus.physicaloperator.access.Router;

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
public class BrokerByteBufferReceiverPO<W> extends AbstractSource<W> implements IRouterReceiver {

	/** The handler which wraps e.g. into an relational tuple. */
	private IObjectHandler<W> handler;
	
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
	
	/** The router. */
	private Router router;
	
	/** The host. */
	private String host;
	
	/** The port. */
	private int port;
	
	/** Determines if connection is open. */
	boolean opened;

	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param handler the handler which wraps an element
	 * @param host the host
	 * @param port the port
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public BrokerByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port) throws IOException {
		super();
		this.handler = handler;
		router = Router.getInstance();
		this.host = host;
		this.port = port;
		this.opened = false;
	}

	/**
	 * Instantiates a new BrokerByteBufferReceiverPO.
	 *
	 * @param byteBufferReceiverPO the original to copy from
	 * @ 
	 */
	@SuppressWarnings("unchecked")
	public BrokerByteBufferReceiverPO(BrokerByteBufferReceiverPO<W> byteBufferReceiverPO)  {
		super();
		handler = (IObjectHandler<W>) byteBufferReceiverPO.handler.clone();
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
		router = byteBufferReceiverPO.router;
		host = byteBufferReceiverPO.host;
		port = byteBufferReceiverPO.port;
		opened = byteBufferReceiverPO.opened;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#process_open()
	 */
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

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.access.IRouterReceiver#done()
	 */
	@Override
	public void done() {
		propagateDone();
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.access.IRouterReceiver#process(java.nio.ByteBuffer)
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

	/**
	 * Transfers a tuple from handler to subscribed sinks.
	 *
	 * @throws IOException Signals that an I/O exception has occurred.
	 * @throws ClassNotFoundException Signals that a class could not be found
	 */
	private synchronized void transfer() throws IOException, ClassNotFoundException {
		W toTrans = handler.create();
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		typeBuffer.clear();
		currentSize = 0;
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#clone()
	 */
	@Override
	public BrokerByteBufferReceiverPO<W> clone()  {
		return new BrokerByteBufferReceiverPO<W>(this);
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.physicaloperator.AbstractSource#toString()
	 */
	@Override
	public String toString() {
		return super.toString() + " " + host + " " + port;
	}
	
	@Override
	public String getSourceName() {
		return toString();
	}

}
