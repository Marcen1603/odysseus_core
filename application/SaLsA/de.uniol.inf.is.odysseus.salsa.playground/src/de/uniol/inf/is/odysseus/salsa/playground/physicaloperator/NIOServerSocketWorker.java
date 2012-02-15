package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class NIOServerSocketWorker<RelationalTuple> extends Thread {

	private NIOServerSocketSource nioServer;
	private ByteBuffer buffer = ByteBuffer.allocate(12 * 1024);

	private Charset charset = Charset.defaultCharset();
	private CharsetDecoder decoder = charset.newDecoder();
	private CharsetEncoder encoder = charset.newEncoder();

	volatile protected static Logger _logger = null;

	protected synchronized static Logger getLogger() {
		if (_logger == null) {
			_logger = LoggerFactory.getLogger(NIOServerSocketWorker.class);
		}
		return _logger;
	}

	public NIOServerSocketWorker(NIOServerSocketSource server) {
		this.nioServer = server;
	}

	@Override
	public void run() {
		getLogger().debug("Start NIOServer SocketWorker");
		boolean firstIt = true;

		try {
			while (!isInterrupted()) {
				Selector selector = nioServer.getSelector();
				selector.select();
				Set keys = selector.selectedKeys();
				String request = "";
				
				for (Iterator i = keys.iterator(); i.hasNext();) {
					SelectionKey key = (SelectionKey) i.next();
					i.remove();

					if (key == this.nioServer.getKey()) {
						if (key.isAcceptable()) {
							getLogger().debug("Accept Connection");
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
						request += decoder.decode(buffer).toString();	
						getLogger().debug(request);
						
						if (request.contains("\n")) {
							String[] splited = request.split("\n");
							String newRequest = "";
							for(String split: splited){
								if(split.startsWith("S") && split.endsWith("\r")){
									this.nioServer.request(split.trim());
								}
								else{
									if(split.endsWith("\r")){
										newRequest += split + '\n';
									}
									else{
										newRequest += split;	
									}
								}
							}
							request = "";
							request = newRequest;	
						}
						buffer.clear();
						
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


//boolean send = false;
//
//if (request.contains("\n")) {
//	String[] splited = request.split("\n");
//	String newRequest = "";
//	for(String split: splited){
//		if(split.startsWith("S") && split.endsWith(" ")){
//			getLogger().debug("Send: " + split);
//		}
//		else{
//			if(split.endsWith("\r")){
//				newRequest += split + '\n';
//			}
//			else{
//				newRequest += split;	
//			}
//		}
//	}
//	request = "";
//	request = newRequest;	
//}

// 
//String[] splited = request.split("\r\n");
//if (splited.length > 0) {
//	if (splited[0].startsWith("S")) {
//		getLogger().debug("S");
//		getLogger().debug("Send: " + splited[0].toString());
//
//		if (splited[1].startsWith("S")) {
//			request = request.substring(splited[0]
//					.length());
//
//			getLogger().debug("Rest: " + request);
//			if (splited.length > 0)
//				getLogger().debug(
//						"Rest_vergleich: "
//								+ splited[1]);
//		}
//
//	} else {
//		getLogger().debug("NO_S");
//		if (splited.length > 1) {
//			if (splited[1].startsWith("S")) {
//				request = request
//						.substring(splited[0]
//								.length() + 2);
//				getLogger().debug(
//						"Rest: " + request);
//				getLogger().debug(
//						"Rest_vergleich: "
//								+ splited[1]);
//			} else {
//				request = "";
//			}
//
//		}
//	}
//}