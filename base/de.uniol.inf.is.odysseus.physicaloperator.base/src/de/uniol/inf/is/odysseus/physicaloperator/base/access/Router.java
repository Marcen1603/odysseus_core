package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.base.UnsortedPair;
import de.uniol.inf.is.odysseus.physicaloperator.base.ISink;

public class Router extends Thread {

	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	// private Map<SocketChannel, ISink<ByteBuffer>> clientMap = new
	// HashMap<SocketChannel, ISink<ByteBuffer>>();
	Selector selector = null;
	static Router instance = null;
	private LinkedList<UnsortedPair<SocketChannel, ISink<ByteBuffer>>> deferredList = new LinkedList<UnsortedPair<SocketChannel, ISink<ByteBuffer>>>();
	boolean registerAction = false;
	boolean doRouting = true;

	public static synchronized Router getInstance() throws IOException {
		if (instance == null) {
			instance = new Router();
			instance.start();
		}
		return instance;
	}

	public static synchronized Router getInstanceWithOutStarting()
			throws IOException {
		if (instance == null) {
			instance = new Router();
		}
		return instance;
	}

	private Router() throws IOException {
		selector = Selector.open();
	}

	public void stopRouting() {
		doRouting = false;
		instance = null;
		selector.wakeup();
	}

	public void run() {
		// System.out.println("Router started ...");
		while (doRouting) {
			try {
				int n = selector.select();
				// Selector blockiert. Neue Elemente können nur nach oder
				// vor select erfolgen. Deswegen die nachträgliche Registrierung
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

					// if (key.isAcceptable()){
					// ServerSocketChannel server =
					// (ServerSocketChannel)key.channel();
					// SocketChannel channel = server.accept();
					// if (channel != null){
					// channel.configureBlocking(false);
					// channel.register(selector, SelectionKey.OP_READ);
					// synchronized(buffer){
					// buffer.clear();
					// buffer.put("Hi there!\r\n".getBytes());
					// buffer.flip();
					// channel.write(buffer);
					// }
					// }
					// }

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
		sc.connect(new InetSocketAddress(host, port));
		deferedRegister(sc, sink);
		selector.wakeup();
	}

	private void deferedRegister(SocketChannel sc, ISink<ByteBuffer> sink) {
		synchronized (deferredList) {
			deferredList.add(new UnsortedPair<SocketChannel, ISink<ByteBuffer>>(
					sc, sink));
			registerAction = true;
		}
	}

	private synchronized void processRegister() {
		synchronized (deferredList) {
			while (deferredList.size() > 0) {
				UnsortedPair<SocketChannel, ISink<ByteBuffer>> pair = deferredList
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

	// private Selector startServer(Selector selector, int port) throws
	// IOException,
	// ClosedChannelException {
	// for (int i=0;i<4;i++){
	// System.out.println("Listening on port "+port+i);
	// ServerSocketChannel serverChannel = ServerSocketChannel.open();
	// ServerSocket serverSocket = serverChannel.socket();
	// serverSocket.bind(new InetSocketAddress(port+i));
	// serverChannel.configureBlocking(false);
	// serverChannel.register(selector, SelectionKey.OP_ACCEPT);
	// }
	// return selector;
	// }

	private void readDataFromSocket(SocketChannel socketChannel,
			ISink<ByteBuffer> os) throws Exception {
		// ISink<ByteBuffer> os = clientMap.get(socketChannel);
		// System.out.println(os);
		// System.out.println("Read From Socket " + socketChannel.toString() +
		// " "
		// + os + " " + socketChannel.isConnected());
		int count;
		try {
			synchronized (buffer) {
				buffer.clear();
				while ((count = socketChannel.read(buffer)) > 0) {
					buffer.flip();
					os.process(buffer, 0, true);
					buffer.clear();
				}
				buffer.flip(); // Wie geht das denn?
				while (buffer.hasRemaining()) {
					os.process(buffer, 0, true);
				}
			}
			if (count < 0) {
				os.done(0);
				socketChannel.close();
				// System.err.println("Server "+socketChannel+" connection closed (no more Data)");
			}
		} catch (IOException e) {
			socketChannel.isConnectionPending();
			System.err.println("Server " + socketChannel
					+ " connection closed (IOException) " + e.getMessage());
			os.done(0);
			socketChannel.close();
			// throw e;
		}
	}

	// private void testConnect() throws Exception {
	// IObjectHandler<RelationalTuple<IClone>> handler = new
	// RelationalTupleObjectHandler<IClone>(
	// NEXMarkStreamType.getSchema(NEXMarkStreamType.PERSON));
	// ISink<ByteBuffer> po = new ByteBufferReceiverPO<RelationalTuple<IClone>>(
	// handler, "localhost", 65430, 1);
	// po.open();
	// handler = new RelationalTupleObjectHandler<IClone>(NEXMarkStreamType
	// .getSchema(NEXMarkStreamType.AUCTION));
	// po = new ByteBufferReceiverPO<RelationalTuple<IClone>>(handler,
	// "localhost", 65431, 1);
	// po.open();
	//
	// handler = new RelationalTupleObjectHandler<IClone>(NEXMarkStreamType
	// .getSchema(NEXMarkStreamType.BID));
	// po = new ByteBufferReceiverPO<RelationalTuple<IClone>>(handler,
	// "localhost", 65432, 1);
	// po.open();
	//
	// handler = new RelationalTupleObjectHandler<IClone>(NEXMarkStreamType
	// .getSchema(NEXMarkStreamType.CATEGORY));
	// po = new ByteBufferReceiverPO<RelationalTuple<IClone>>(handler,
	// "localhost", 65433, 1);
	// po.open();
	// }
	//
	// public static void main(String[] args) throws Exception {
	// Router r = Router.getInstance();
	// r.testConnect();
	// }

}
