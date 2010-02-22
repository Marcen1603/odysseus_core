package de.uniol.inf.is.odysseus.iec61970;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.Pair;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;


public class IEC61970Router extends Thread{

	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	// private Map<SocketChannel, ISink<ByteBuffer>> clientMap = new
	// HashMap<SocketChannel, ISink<ByteBuffer>>();
	Selector selector = null;
	static IEC61970Router instance = null;
	private LinkedList<Pair<SocketChannel, ISink<ByteBuffer>>> deferredList = new LinkedList<Pair<SocketChannel, ISink<ByteBuffer>>>();
	boolean registerAction = false;
	boolean doRouting = true;

	public static synchronized IEC61970Router getInstance() throws IOException {
		if (instance == null) {
			instance = new IEC61970Router();
			instance.start();
		}
		return instance;
	}

	public static synchronized IEC61970Router getInstanceWithOutStarting()
			throws IOException {
		if (instance == null) {
			instance = new IEC61970Router();
		}
		return instance;
	}

	private IEC61970Router() throws IOException {
		selector = Selector.open();
	}

	public void stopRouting() {
		doRouting = false;
		instance = null;
		selector.wakeup();
	}

	@Override
	public void run() {
		// System.out.println("Router started ...");
		while (doRouting) {
			try {
				int n = selector.select();
				if (registerAction) {
					processRegister();
				}
				if (n == 0) {
					// System.out.println("Selector continue...");
					continue;
				}

				Iterator<SelectionKey> it = selector.selectedKeys().iterator();
				while (it.hasNext()) {
					SelectionKey key = (SelectionKey) it.next();
					it.remove();
					@SuppressWarnings("unchecked")
					ISink<ByteBuffer> op = (ISink<ByteBuffer>) key.attachment();
					SocketChannel sc = (SocketChannel) key.channel();

					// System.out.println("Selection Key "+key.isConnectable()+" "+key.isReadable()+" "+op);
					if (key.isConnectable() && sc.finishConnect()) {
						// System.out.println("Client connected to " +
						// sc+" from "+sc.socket().getLocalPort());
						// sc.register(selector, SelectionKey.OP_READ, op);

						if (sc.isConnected()) {
							System.out.println("Router connected und einsatzbereit");
							key.interestOps(SelectionKey.OP_READ);
						} else {
							System.err.println("NOT CONNECTED!!!!!!!!!!!!!");
						}
					} else if (key.isReadable()) {
						if (!sc.isConnected()) {
							key.interestOps(SelectionKey.OP_CONNECT);
							System.err.println("NOT CONNECTED2!!!!!!!!!!!!!");
						} else {
							if (op != null) {
								readDataFromSocket(sc, op);
							} else {
								System.out.println(sc.toString() + " "
										+ key.readyOps());
							}
						}
					} else {
						System.out.println("WAS ANDERES " + key);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void connectToServer(ISink<ByteBuffer> sink, String host, int port)
			throws Exception {
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		// sc.configureBlocking(true);
		System.out.println("connect auf: "+host+" :"+port);
		sc.connect(new InetSocketAddress(host, port));
		deferedRegister(sc, sink);
		selector.wakeup();
	}

	private void deferedRegister(SocketChannel sc, ISink<ByteBuffer> sink) {
		synchronized (deferredList) {
			deferredList.add(new Pair<SocketChannel, ISink<ByteBuffer>>(
					sc, sink));
			registerAction = true;
		}
	}

	private synchronized void processRegister() {
		synchronized (deferredList) {
			while (deferredList.size() > 0) {
				Pair<SocketChannel, ISink<ByteBuffer>> pair = deferredList
						.poll();
				try {
					// System.out.println("Registering "+pair.getE1()+" "+pair.getE2());
					pair.getE1().register(selector, SelectionKey.OP_CONNECT,
							pair.getE2());
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
				// System.out.println("done");
			}
			registerAction = false;
		}
	}

	private void readDataFromSocket(SocketChannel socketChannel,
			ISink<ByteBuffer> os) throws Exception {
		buffer.clear();
		try {
		int count;
			synchronized (buffer) {
				buffer.clear();
				while ((count = socketChannel.read(buffer)) > 0) {
					buffer.limit(count);
					buffer.flip();
					os.process(buffer, 0, true);
					buffer.clear();
				}
				buffer.flip(); // Wie geht das denn?
				while (buffer.hasRemaining()) {
					os.process(buffer, 0, true);
				}
				if (count < 0) {
					os.done(0);
					socketChannel.close();
					// System.err.println("Server "+socketChannel+" connection closed (no more Data)");
				}
			}
			

		
		} catch (IOException e) {
			socketChannel.isConnectionPending();
			System.err.println("Server " + socketChannel
					+ " connection closed (IOException) " + e.getMessage());
			os.done(0);
			socketChannel.close();
		}
	}
}
