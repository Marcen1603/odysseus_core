package de.uniol.inf.is.odysseus.p2p.operatorpeer.physicaloperator.base;

import java.io.IOException;
import java.nio.ByteBuffer;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IObjectHandler;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.IRouterReceiver;
import de.uniol.inf.is.odysseus.physicaloperator.base.access.Router;

public class P2PByteBufferReceiverPO<W> extends AbstractSource<W> implements
		IRouterReceiver, IP2PInputPO {

	private boolean connectToPipe = false;
	private IObjectHandler<W> handler;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private Router router;
	private String host;
	private int port;
	boolean opened;

	public P2PByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port)
			throws IOException {
		super();
		System.out.println("Initialisiere den P2P Byte Buffer");
		this.handler = handler;
		router = Router.getInstance();
		this.host = host;
		this.port = port;
		this.opened = false;
	}

	@SuppressWarnings("unchecked")
	public P2PByteBufferReceiverPO(P2PByteBufferReceiverPO<W> byteBufferReceiverPO) {
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

//		if(connectToPipe) {
//			System.out.println("Process p2pBytebuffer");
//		if (!opened) {
//			try {
//				router.connectToServer(this, host, port);
//				opened = true;
//			} catch (Exception e) {
//			}
//		}
		try {
			while (buffer.remaining() > 0) {

				// logger.debug("size "+size+" remaining "+buffer.remaining()+" currentSize "+currentSize);

				// ACHTUNG! ES KANN SEIN, DASS "SIZE" NOCH NICHT VOLLST�NDIG
				// �BERTRAGEN IST
				// Neues Object?
				// TODO: Reihenfolge mit der die Size kodiert wird festlegen!!!
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles �bertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						// logger.debug("NEW OBJEKT STARTED WITH SIZE "+size);
					}
				}
				// Es kann auch direkt nach der size noch was im Puffer sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Gr��e?
					if (currentSize + buffer.remaining() <= size) {
						currentSize = currentSize + buffer.remaining();
						handler.put(buffer);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler �bergeben
						// logger.debug(" "+(size-currentSize));
						handler.put(buffer, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						transfer();
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
//		}
	}

	private synchronized void transfer() throws IOException,
			ClassNotFoundException {
		W toTrans = handler.create();
		// logger.debug("Transfer "+toTrans);
		System.out.println("mach einen Transfer, weil wir ein Objekt erhaltren haben");
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		currentSize = 0;
	}

	@Override
	public P2PByteBufferReceiverPO<W> clone() {
		return new P2PByteBufferReceiverPO<W>(this);
	}

	@Override
	public String toString() {
		return super.toString() + " " + host + " " + port;
	}

	@Override
	public void setConnectedToPipe(boolean connectToPipe) {
		this.connectToPipe = connectToPipe;
	}

	@Override
	public void setP2P(boolean p2p) {
		
	}
}
