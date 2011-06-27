package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NIOServerSocketWorker<T> extends Thread {

	private NIOServerSocketSource<T> nioServer;
	private ByteBuffer buffer = ByteBuffer.allocate(8192);

	private Charset charset = Charset.defaultCharset();
	private CharsetDecoder decoder = charset.newDecoder();

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(NIOServerSocketWorker.class);
		}
		return _logger;
	}

	public NIOServerSocketWorker(NIOServerSocketSource<T> server) {
		this.nioServer = server;
	}

	@Override
	public void run() {
		getLogger().debug("Start NIOServer SocketWorker");
		
		StringBuilder message = new StringBuilder();
		
		try {
			while (!isInterrupted()) {
				Selector selector = nioServer.getSelector();
				selector.select();
				Set keys = selector.selectedKeys();

				  for (Iterator i = keys.iterator(); i.hasNext();) {
				        SelectionKey key = (SelectionKey) i.next();
				        i.remove();

				        if (key == this.nioServer.getKey()) {
				          if (key.isAcceptable()) {
				            SocketChannel client = this.nioServer.getServerSocketChannel().accept();
				            client.configureBlocking(false);
				            SelectionKey clientkey = client.register(selector, SelectionKey.OP_READ);
				            clientkey.attach(new Integer(0));
				          }
				        } else {
				          SocketChannel client = (SocketChannel) key.channel();
				          if (!key.isReadable())
				            continue;
				          int bytesread = client.read(buffer);
				          if (bytesread == -1) {
				            key.cancel();
				            client.close();
				            continue;
				          }
				          buffer.flip();
				          String request = decoder.decode(buffer).toString();
				          buffer.clear();
				    
				          if(request.endsWith("\r\n")){
				        	this.nioServer.request(message.toString());
				        	message.delete(0, message.length()-1);
				          }
				          else{
				        	  message.append(request);
				          }
				            
				        }
					}
				}

			this.nioServer.getServerSocketChannel().close();			
			getLogger().debug("Stopped NIOServer SocketWorker");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}