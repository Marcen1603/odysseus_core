package de.uniol.inf.is.odysseus.salsa.playground.physicaloperator;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Iterator;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;

public class NIOServerSocketWorker<RelationalTuple> extends Thread {

	private NIOServerSocketSource nioServer;
	// private ByteBuffer buffer = ByteBuffer.allocateDirect(20*1024);
	private ByteBuffer buffer = ByteBuffer.allocate(60 * 1024);

	//private static Byte END = 0x24;// '$';
	private static Byte END = '$';
	

	private int endIndex = 0;

	private Charset charset = Charset.forName("ASCII");
	private CharsetDecoder decoder = charset.newDecoder();

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
							SocketChannel client = this.nioServer
									.getServerSocketChannel().accept();
							client.configureBlocking(false);
							SelectionKey clientkey = client.register(selector,
									SelectionKey.OP_READ);
							clientkey.attach(new Integer(0));
						}
					} else {
						SocketChannel client = (SocketChannel) key.channel();
						if (!key.isReadable())
							continue;
						int nbytes = 0;
						int pos = 0;
						
						
						//Liest die Daten vom Channel in den Buffer...
						while ((nbytes = client.read(buffer)) > 1) {
							
														
							
							
							//Buffer remaining ... 
							//buffer.remaining();
							
							//Geht den aktuellen Buffer durch bis zu nbytes
							for (int c = 0; c < nbytes; c++) {
								
								getLogger().debug("Position im Buffer: " +buffer.position() + " and c =" +c);
								
								//Vergleicht das aktuelle Zeichen mit dem End-Zeichen.
								if (buffer.get(c) == NIOServerSocketWorker.END) {
									//Setzt das Limit auf die aktuelle Position und die aktuelle Position auf 0
									buffer.position(c);
									buffer.flip();
									//Decodiert von 0 bis zum limit
									
									
									getLogger().debug("decode from: " + buffer.position() + " bis " + buffer.limit() + " | c= " + c);
									
									getLogger().debug(decoder.decode(buffer).toString());
									
									getLogger().debug("Setze von aktuelle Position " + buffer.position() + " auf c = " + c);
									//Setzt die Position noch einmal auf c
									buffer.position(c+1);
									c++;
									
									
									getLogger().debug("Setze das aktuelle limit " + buffer.limit() + " auf nbytes = " + nbytes);
									//Setzt den endmarker auf das ende der Vorschleife.
									buffer.limit(nbytes);
									
									getLogger().debug("Schiebe: " + c + " auf null und limit auf " + nbytes);
									//Entfernt alles aus dem buffer bis zu c und schiebt c auf 0 bis zum limit. 
									buffer.compact();
									
									//buffer.position(nbytes-c);		
								}
							}
							
							
						}
						
						buffer.clear();
						if (nbytes == -1) {
							key.cancel();
							client.close();
							break;
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
	
	
	/*
	endIndex = pos + c;

	if (endIndex >= 0) {

		buffer.flip();
		buffer.limit(endIndex);
		
		pos = buffer.position();
		
		getLogger().debug("Start: " + buffer.position() + " End: " + endIndex);
		
		final CharBuffer charBuffer = decoder
				.decode(buffer);
		
		getLogger().debug(charBuffer.toString());
		
//		getLogger().debug("Test - Start:" +
//		 buffer.position() + " End: " +
//		 endIndex);

		
		//this.nioServer.request(charBuffer.toString());

		buffer.compact();
		pos -= charBuffer.length()+1;
		endIndex = -1;
		if(pos < buffer.limit()){
			getLogger().debug("Limit: " + buffer.limit() + " Akt. Position: " + pos);
		}
		else{
			buffer.position(pos);
		}
	}
}

}
*/