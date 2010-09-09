package de.uniol.inf.is.odysseus.iec61970.dataaccessserver.communication;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.spi.SelectorProvider;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.uniol.inf.is.odysseus.iec61970.library.client.service.ICallBack;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.HSDAObject;
import de.uniol.inf.is.odysseus.iec61970.library.serializable.TSDAObject;



//import NIOTest.server.ChangeRequest;
//import NIOTest.server.EchoWorker;

/**
 * Abwicklung des Transfers eines Datenstroms zu den entsprechenden
 * Subscriptions mit  Hilfe von java.nio
 * 
 * @author Mart Köhler
 * 
 */
@SuppressWarnings("unchecked")
public class Provider implements Runnable {
	private InetAddress hostAddress;
	private int inPort;
	private int outPort;
	// The channel on which we'll accept connections
	private ServerSocketChannel serverServerChannel;
	private ServerSocketChannel serverClientChannel;
	// The selector we'll be monitoring
	private static Selector selector;

	// The buffer into which we'll read data when it's available
	private ByteBuffer readBuffer = ByteBuffer.allocate(8192);
	private byte[] receiveObj = null;
	private int size = -1;
	private int currentSize = 0;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4); 

	// benötigen wir, weil SelectionKey nicht thread safe ist und sonst mitten im Ablauf, seltsame Ergebnisse auftreten
	private List pendingChanges = new LinkedList();

	// Zuordnung von Sockets zu ByteBuffers bzw Daten
	private Map pendingData = new HashMap();
	
	//nicht das Interface, weil das sowieso intern gemacht wird
	private Server server = null;
	private Processing proc = null;
	private Thread procThread  = null;
	public Provider(Server server, InetAddress hostAddress, int port) throws IOException {
		this.server = server;
		this.hostAddress = hostAddress;
		this.inPort = port;
		this.outPort = port+1;
		Provider.selector = this.initSelector();
		this.proc = new Processing(getServer());
		this.procThread = new Thread(this.proc);
		this.procThread.start();
	}
	@Override
	public void run() {
		while (true) {
				// Änderungen an der Operationenzugehörigkeit verarbeiten
				synchronized (this.pendingChanges) {
					Iterator changes = this.pendingChanges.iterator();
					while (changes.hasNext()) {
						ChangeRequest change = (ChangeRequest) changes.next();
						switch (change.type) {
						case ChangeRequest.CHANGEOPS:
							SelectionKey key = change.socket
									.keyFor(Provider.selector);
							key.interestOps(change.ops);
						}
					}
					this.pendingChanges.clear();
				}
				try {
					Provider.selector.select();
				} catch (IOException e) {
					e.printStackTrace();
				}
				Iterator selectedKeys = Provider.selector.selectedKeys().iterator();
					while (selectedKeys.hasNext()) {
						SelectionKey key = (SelectionKey) selectedKeys.next();
						selectedKeys.remove();
						//An den entsprechenden Handler weiter reichen
						try {
							if (!key.isValid()) {
								continue;
							}
							if (key.isAcceptable()) {
								System.err.println("Eingehende Verbindung...");
									this.accept(key);
							
							} else if (key.isReadable()) {
									this.read(key);
								
							}
							 else if (key.isWritable()) {
								 this.write(key);
							}
						} catch (IOException e) {
							e.printStackTrace();
							try {
								key.channel().close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							key.cancel();
							System.out.println("Verbindung unterbrochen. Schliesse Channel...");
						} catch (Exception e) {
							try {
								key.channel().close();
							} catch (IOException e1) {
								e1.printStackTrace();
							}
							key.cancel();
							System.out.println("Verbindung unterbrochen. Schliesse Channel...");
						}
					}
		}
	}
	
	private synchronized void write(SelectionKey key) {
		SocketChannel socketChannel = (SocketChannel) key.channel();
		ByteBuffer sizeBuffer =  ByteBuffer.allocate(4);
		List queue = null;
		synchronized (this.pendingData) {
//			for(Object chan : this.pendingData.keySet()) {
				queue = (List) this.pendingData.get(socketChannel);
//				break;
//			}
			// Write until there's not more data ...
			while (queue!=null && !queue.isEmpty()) {
				ByteBuffer buf = (ByteBuffer) queue.remove(0);
				System.out.println(buf.array().toString());
				sizeBuffer.putInt(buf.limit());
//				buf.flip();
				sizeBuffer.flip();
				try {
					System.err.println("Schicke "+buf.remaining()+" Bytes an: "+socketChannel.socket().getInetAddress().getHostAddress()+":"+socketChannel.socket().getLocalPort());
					socketChannel.write(sizeBuffer);
				socketChannel.write(buf);
				} catch (IOException e) {
					e.printStackTrace();
				}
				buf.clear();
				if (buf.remaining() > 0) {
					// kann eigentlich nicht passieren, aber der puffer kann überlaufen
					break;
				}
				queue.remove(0);
			}

			if (queue != null && queue.isEmpty()) {
				key.interestOps(SelectionKey.OP_READ);
			}
		}	
	}
	// Der Aufruf geschieht extern, um auf die Sockets und Daten einfluss zu nehmen
	//Da wir über RMI keine komplexeren Objekte verschicken können, wenn sie nicht serialisierbar sind wie einen socketchannel, schicken wird verbindungsinformationen aus dem callback objekt
	public void sendPending(ICallBack callback, byte[] data) {
		SocketChannel chan = null;
		synchronized (this.pendingChanges) {
			for(SelectionKey key : Provider.selector.keys()) {
				if(key.channel() instanceof SocketChannel) {
					chan = (SocketChannel)key.channel();
						try {
							//System.out.println("1) "+chan.socket().getLocalAddress().getHostAddress()+chan.socket().getPort()+" 2)"+callback.getConnectionAddress()+callback.getConnectionPort());
							if(chan.socket().getInetAddress().getHostAddress().equals(callback.getConnectionAddress()) && chan.socket().getLocalPort()==1201) {
								this.pendingChanges.add(new ChangeRequest((SocketChannel)key.channel(), ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
								synchronized (this.pendingData) {
									List queue = (List) this.pendingData.get(key.channel());
									if (queue == null) {
										queue = new ArrayList();
										this.pendingData.put(key.channel(), queue);
									}
									queue.add(ByteBuffer.wrap(data));
								}
//								break;
							}
						} catch (RemoteException e) {
							e.printStackTrace();
						}
				}
			}
//			Iterator iter = this.selector.keys().iterator();
//			synchronized (iter) {
				
			
//				while(iter.hasNext()) {
//					SelectionKey key = (SelectionKey) iter.next();
//					if(key.channel() instanceof SocketChannel) {
//						sc = (SocketChannel) key.channel();
//						try {
//							System.out.println("voher"+sc.socket().getInetAddress().getHostAddress()+" "+callback.getConnectionAddress().getHostAddress());
//	//						if(sc.socket().getInetAddress().getHostAddress().equals(callback.getConnectionAddress().getHostAddress())) {
//								System.out.println("nachher");
//								// Operation des Sockets wird umgestellt
//								this.pendingChanges.add(new ChangeRequest(sc, ChangeRequest.CHANGEOPS, SelectionKey.OP_WRITE));
//	//							break;
//	//						}
//						} catch (RemoteException e) {
//							e.printStackTrace();
//						}
//					}
//				}
			}
			// schmeiß die Daten, die wir transferieren wollen in pendingData
//			synchronized (this.pendingData) {
//				List queue = (List) this.pendingData.get(chan);
//				if (queue == null) {
//					queue = new ArrayList();
//					System.out.println("pack auf pending");
//					this.pendingData.put(chan, queue);
//				}
//				queue.add(ByteBuffer.wrap(data));
//			}
//		}
		// select anstoßen, damit pendingChanges verarbeitet werden
		Provider.selector.wakeup();
	}
	
	private void accept(SelectionKey key) throws IOException {
		// unseren ServerSocket holen
		ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
		System.out.println("Connect auf port:"+serverSocketChannel.socket().getLocalPort());
		//Verbindung akzeptieren
		SocketChannel socketChannel = serverSocketChannel.accept();
		socketChannel.socket();
		socketChannel.configureBlocking(false);
		// jetzt kann gelesen werden, nachdem die Verbindung komplett aufgebaut wurde.
			socketChannel.register(Provider.selector, SelectionKey.OP_READ);
	}
	
	/**
	 * Der Port des ServerSelectors für die Annahme von Daten wird spezifiziert. Für den Transport von Daten zu Clients wird entsprechend der nächste Port verwendet
	 * @return
	 * @throws IOException
	 */
	private Selector initSelector() throws IOException {
		Selector socketSelector = SelectorProvider.provider().openSelector();
		this.serverServerChannel = ServerSocketChannel.open();
		serverServerChannel.configureBlocking(false);
		// Socket an Port und Hostadresse binden
		InetSocketAddress isa = new InetSocketAddress(this.hostAddress, this.inPort);
		serverServerChannel.socket().bind(isa);
		// Beim Selektor registrieren, dass wir an einer Connection interessiert sind
		serverServerChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
		//Wir wollen noch einen ServerSocket für den Datenaustausch mit Clients einrichten auf dem nächsten Port
		this.serverClientChannel = ServerSocketChannel.open();
		serverClientChannel.configureBlocking(false);
		isa = new InetSocketAddress(this.hostAddress, this.outPort);
		serverClientChannel.socket().bind(isa);
		serverClientChannel.register(socketSelector, SelectionKey.OP_ACCEPT);
		return socketSelector;
	}
	public Server getServer() {
		return server;
	}
	private void buildObject(ByteBuffer buffer, int i) {
		if(this.receiveObj==null) {
			this.receiveObj = new byte[size];
		}
		System.err.println("buildObject mit i(länge):"+i+" an position:"+(size-i)+" an puffer stelle:"+buffer.position());
		System.arraycopy(buffer.array(), buffer.position(), this.receiveObj, size-i, i);
		buffer.position(buffer.position()+i);
	}

private void buildObject(ByteBuffer buffer) {
	if(receiveObj==null) {
		this.receiveObj = new byte[size]; 
		//4 wegen der size
		System.arraycopy(buffer.array(), buffer.position(), this.receiveObj, 0, buffer.remaining());
		buffer.position(buffer.position()+buffer.remaining());
	}
	else {
		System.arraycopy(buffer.array(), buffer.position(), this.receiveObj, currentSize-buffer.remaining(), buffer.remaining());
		buffer.position(buffer.position()+buffer.remaining());
	}
}

private synchronized void read(SelectionKey key) throws Exception {
	SocketChannel socketChannel = (SocketChannel) key.channel();
	// Buffer vorher leeren
	this.readBuffer.clear();
	int read = socketChannel.read(this.readBuffer);
	//ohne das limitieren, funkioniert der rest irgendwie nicht
	this.readBuffer.limit(read);
	this.readBuffer.flip();

		synchronized (this.readBuffer) {
			while (this.readBuffer.remaining() > 0){
				//Gesamtgröße des Objekts rausfinden
				if (size == -1){
					this.sizeBuffer.clear();
					while(sizeBuffer.position() < 4 && this.readBuffer.remaining() != 0){
						sizeBuffer.put(this.readBuffer.get());
					}
					if (sizeBuffer.position() == 4){
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						System.out.println("Objekt Größe: "+size);
					}					
				}				
				if (size != -1 && this.readBuffer.remaining()>0){
					//Kleiner oder passt
					if (currentSize+this.readBuffer.remaining() <= size){
						currentSize = currentSize + this.readBuffer.remaining();
						buildObject(this.readBuffer);
						//da wir alles komplett genommen haben, können wir alles zurücksetzen
						if(currentSize==size) {
							proc.processSession(this, this.receiveObj);
							//Alles nötige Zurücksetzen
							size= -1;
							currentSize = 0;
							receiveObj = null;
						}
						//currentsize ist fest, aber remaining kann diesen Wert übertreffen durch in der zwischenzeit auftretende datenmengen und wir haben immernoch kein objekt
					}else{ 
						// Split
							buildObject(this.readBuffer, size-currentSize);
							proc.processSession(this, this.receiveObj);
							size = -1;
							currentSize = 0;
							receiveObj = null;
					}
					
				}
			}
		}

	if (read == -1) {
		key.channel().close();
		key.cancel();
		return;
	}
}
public static HSDAObject getHSDAData(byte[] bytes) throws java.io.IOException{
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes); 
    ObjectInputStream ois = new ObjectInputStream(bis); 
    HSDAObject data = null;
	try {
		data = (HSDAObject) ois.readObject();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
    ois.close(); 
    bis.close();
    return data;
}
public static TSDAObject getTSDAData(byte[] bytes) throws java.io.IOException{
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes); 
    ObjectInputStream ois = new ObjectInputStream(bis); 
    TSDAObject data = null;
	try {
		data = (TSDAObject) ois.readObject();
	} catch (ClassNotFoundException e) {
		e.printStackTrace();
	}
    ois.close(); 
    bis.close();
    return data;
}
	public static void removeCallBack(String host, int port) throws RemoteException{
			for(SelectionKey key : selector.keys()) {
				synchronized (key) {
					if(key.channel() instanceof SocketChannel) {
						SocketChannel chan = (SocketChannel)key.channel();
							if(chan.socket().getInetAddress().getHostAddress().equals(host) && chan.socket().getLocalPort()==port) {
								System.err.println("Wir entfernen einen Channel");
								try {
									key.channel().close();
								} catch (IOException e) {
									e.printStackTrace();
								}
								key.cancel();
								break;
							}
					}
				}
			}
		}
}
