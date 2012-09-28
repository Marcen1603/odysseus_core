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
package de.uniol.inf.is.odysseus.core.connection;

import java.io.IOException;
import java.net.ConnectException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.core.collection.IPair;
import de.uniol.inf.is.odysseus.core.collection.Pair;

public class NioConnection extends Thread implements IConnection {
	
	static private Logger _logger = null;
	static private Logger getLogger(){
		if (_logger == null){
			_logger = LoggerFactory.getLogger(NioConnection.class);
		}
		return _logger;
	}

	private ByteBuffer buffer = ByteBuffer.allocate(1024);
	Selector selector = null;
	static NioConnection instance = null;
	private Map<IAccessConnectionListener<ByteBuffer>,SocketChannel> receiverMap = new HashMap<IAccessConnectionListener<ByteBuffer>, SocketChannel>();
	private LinkedList<IPair<SocketChannel, IAccessConnectionListener<ByteBuffer>>> deferredList = new LinkedList<IPair<SocketChannel, IAccessConnectionListener<ByteBuffer>>>();
	boolean registerAction = false;
	boolean doRouting = true;
	private List<IConnectionListener> connectionlistener = new ArrayList<IConnectionListener>();

	public static synchronized NioConnection getInstance() throws IOException {
		if (instance == null) {
			instance = new NioConnection();
			instance.start();
		}
		return instance;
	}

	public Set<IAccessConnectionListener<ByteBuffer>> getReceiver(){
		return receiverMap.keySet();
	}
	
	public static synchronized boolean hasInstance(){
		return instance != null;
	}
	
	public void addConnectionListener(IConnectionListener listener){
		this.connectionlistener .add(listener);
	}
	
	public void removeConnectionListener(IConnectionListener listener){
		this.connectionlistener.remove(listener);
	}
	
	public void notifyConnectionListeners(ConnectionMessageReason reason){
		for(IConnectionListener listener : this.connectionlistener){
			listener.notify(this, reason);
		}
	}

	private NioConnection() throws IOException {
		selector = Selector.open();
	}

	public void stopRouting() {
		doRouting = false;
		//instance = null;
		selector.wakeup();
	}	

	@Override
	public void run() {
		getLogger().debug("Nio Connection Handler started ...");
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
					SelectionKey key = it.next();
					it.remove();
					@SuppressWarnings("unchecked")
					IAccessConnectionListener<ByteBuffer> op = (IAccessConnectionListener<ByteBuffer>) key.attachment();
					SocketChannel sc = (SocketChannel) key.channel();

					// System.out.println("Selection Key "+key.isConnectable()+" "+key.isReadable()+" "+op);
					if (key.isConnectable() && sc.finishConnect()) {
						getLogger().debug("Client connected to "+ sc+" from "+sc.socket().getLocalPort());
						// sc.register(selector, SelectionKey.OP_READ, op);

						if (sc.isConnected()) {
							key.interestOps(SelectionKey.OP_READ);
						} else {
							getLogger().error("NOT CONNECTED");
						}
					} else if (key.isReadable()) {
						if (!sc.isConnected()) {
							key.interestOps(SelectionKey.OP_CONNECT);
							getLogger().error("NOT CONNECTED!");
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
			}catch(ConnectException ce){
				//ce.printStackTrace();
				getLogger().error("Connection refused. "+ce.getMessage());
				notifyConnectionListeners(ConnectionMessageReason.ConnectionRefused);
			}
			catch(IOException ioe){
				getLogger().debug("Connection aborted. "+ioe.getMessage());				
				notifyConnectionListeners(ConnectionMessageReason.ConnectionAbort);
			}
		}
		getLogger().debug("Nio Connection Handler terminated ...");
	}
	
	public void connectToServer(IAccessConnectionListener<ByteBuffer> sink, String host, int port, String username, String password)
			throws Exception {
		getLogger().debug(sink+" connect to server "+host+" "+port);
		SocketChannel sc = SocketChannel.open();
		sc.configureBlocking(false);
		// sc.configureBlocking(true);
		sc.connect(new InetSocketAddress(host, port));
		if (username != null && password != null){
			ByteBuffer buffer = ByteBuffer.allocate(2*(username.length()+password.length()));
			for (int i=0;i<username.length();i++){
				buffer.putChar(username.charAt(i));
			}
			buffer.putChar('\n');
			for (int i=0;i<password.length();i++){
				buffer.putChar(password.charAt(i));
			}
			buffer.putChar('\n');
			buffer.reset();
			sc.write(buffer);
		}
		
		deferedRegister(sc, sink);
		selector.wakeup();
		notifyConnectionListeners(ConnectionMessageReason.ConnectionOpened);
	}
	
	public void disconnectFromServer(IAccessConnectionListener<ByteBuffer> sink) throws IOException{
		synchronized(receiverMap){
			SocketChannel s = receiverMap.remove(sink);
			if (s!=null){
				s.close();
			}
		}		
	}

	private void deferedRegister(SocketChannel sc, IAccessConnectionListener<ByteBuffer> sink) {
		synchronized (deferredList) {
			deferredList.add(new Pair<SocketChannel, IAccessConnectionListener<ByteBuffer>>(
					sc, sink));
			synchronized(receiverMap){
				receiverMap.put(sink, sc);
			}
			registerAction = true;
		}
	}

	private synchronized void processRegister() {
		synchronized (deferredList) {
			while (deferredList.size() > 0) {
				IPair<SocketChannel, IAccessConnectionListener<ByteBuffer>> pair = deferredList
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



	private void readDataFromSocket(SocketChannel socketChannel,
			IAccessConnectionListener<ByteBuffer> os) throws IOException {
		// ISink<ByteBuffer> os = clientMap.get(socketChannel);
		// System.out.println(os);
		// System.out.println("Read From Socket " + socketChannel.toString() +
		// " "
		// + os + " " + socketChannel.isConnected());
		int count = -1;
		try {
			synchronized (buffer) {
				buffer.clear();				
				while (socketChannel.isConnected() && (count = socketChannel.read(buffer)) > 0) {
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
		} catch(ClassNotFoundException ex){
			ex.printStackTrace();
			os.done();
			socketChannel.close();
		}
	}


}
