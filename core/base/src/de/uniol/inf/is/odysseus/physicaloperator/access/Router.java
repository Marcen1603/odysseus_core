package de.uniol.inf.is.odysseus.physicaloperator.access;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.collection.Pair;

public class Router extends Thread {
	
	static private Logger _logger = null;
	static private Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(Router.class);
		}
		return _logger;
	}

	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	Selector selector = null;
	static Router instance = null;
	private Map<IRouterReceiver,SocketChannel> routerReceiverMap = new HashMap<IRouterReceiver, SocketChannel>();
	private LinkedList<Pair<SocketChannel, IRouterReceiver>> deferredList = new LinkedList<Pair<SocketChannel, IRouterReceiver>>();
	boolean registerAction = false;
	boolean doRouting = true;

	public static synchronized Router getInstance() throws IOException {
		if (instance == null) {
			instance = new Router();
			instance.start();
		}
		return instance;
	}

	public Set<IRouterReceiver> getRouterReceiver(){
		return routerReceiverMap.keySet();
	}
	
	// Keine gute Idee, da der Router u.U. nie gestarted wird
//	public static synchronized Router getInstanceWithOutStarting()
//			throws IOException {
//		if (instance == null) {
//			instance = new Router();
//		}
//		return instance;
//	}
	
	public static synchronized boolean hasInstance(){
		return instance != null;
	}

	private Router() throws IOException {
		selector = Selector.open();
	}

	public void stopRouting() {
		doRouting = false;
		instance = null;
		selector.wakeup();
	}

	@Override
	public void run() {
		getLogger().debug("Router started ...");
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
					IRouterReceiver op = (IRouterReceiver) key.attachment();
					SocketChannel sc = (SocketChannel) key.channel();

					// System.out.println("Selection Key "+key.isConnectable()+" "+key.isReadable()+" "+op);
					if (key.isConnectable() && sc.finishConnect()) {
						getLogger().debug("Client connected to "+ sc+" from "+sc.socket().getLocalPort());
						// sc.register(selector, SelectionKey.OP_READ, op);

						if (sc.isConnected()) {
							key.interestOps(SelectionKey.OP_READ);
						} else {
							getLogger().error("NOT CONNECTED!!!!!!!!!!!!!");
						}
					} else if (key.isReadable()) {
						if (!sc.isConnected()) {
							key.interestOps(SelectionKey.OP_CONNECT);
							getLogger().error("NOT CONNECTED2!!!!!!!!!!!!!");
						} else {
							if (op != null) {
								readDataFromSocket(sc, op);
							} else {
								getLogger().error(sc.toString() + " "
										+ key.readyOps());
							}
						}
					} else {
						getLogger().error("WAS ANDERES " + key);
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
			}catch (java.nio.channels.CancelledKeyException e1){
				// Ignore
				//e1.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		getLogger().debug("Router terminated ...");
	}

	public void connectToServer(IRouterReceiver sink, String host, int port)
			throws Exception {
		getLogger().debug(sink+" connect to server "+host+" "+port);
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		// sc.configureBlocking(true);
		sc.connect(new InetSocketAddress(host, port));
		deferedRegister(sc, sink);
		selector.wakeup();
	}
	
	public void disconnectFromServer(IRouterReceiver sink) throws IOException{
		synchronized(routerReceiverMap){
			SocketChannel s = routerReceiverMap.remove(sink);
			if (s!=null){
				s.close();
			}
		}
	}

	private void deferedRegister(SocketChannel sc, IRouterReceiver sink) {
		synchronized (deferredList) {
			deferredList.add(new Pair<SocketChannel, IRouterReceiver>(
					sc, sink));
			synchronized(routerReceiverMap){
				routerReceiverMap.put(sink, sc);
			}
			registerAction = true;
		}
	}

	private synchronized void processRegister() {
		synchronized (deferredList) {
			while (deferredList.size() > 0) {
				Pair<SocketChannel, IRouterReceiver> pair = deferredList
						.poll();
				try {
					getLogger().debug("Registering "+pair.getE1()+" "+pair.getE2());
					pair.getE1().register(selector, SelectionKey.OP_CONNECT,
							pair.getE2());
				} catch (ClosedChannelException e) {
					e.printStackTrace();
				}
				getLogger().debug("done");
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
			IRouterReceiver os) throws Exception {
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
					os.process(buffer);
					buffer.clear();
				}
				buffer.flip(); // Wie geht das denn?
				while (buffer.hasRemaining()) {
					os.process(buffer);
				}
			}
			if (count < 0) {
				os.done();
				socketChannel.close();
				// System.err.println("Server "+socketChannel+" connection closed (no more Data)");
			}
		} catch (IOException e) {
			socketChannel.isConnectionPending();
			System.err.println("Server " + socketChannel
					+ " connection closed (IOException) " + e.getMessage());
			os.done();
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
