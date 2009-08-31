package de.uniol.inf.is.odysseus.physicaloperator.base.access;

import java.io.IOException;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;

public class ByteBufferReceiverPO<W> extends AbstractPipe<ByteBuffer, W> {
	
	private static final Logger logger = LoggerFactory.getLogger( ByteBufferReceiverPO.class );

	private IObjectHandler<W> handler;
	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4); 
	private int currentSize = 0;
	private Router router;
	private String host;
	private int port;
	boolean opened;
	
	
	public ByteBufferReceiverPO(IObjectHandler<W> handler, String host, int port) throws IOException{
		super();
		this.handler = handler;
		router = Router.getInstance();
		this.host = host;
		this.port = port;
		this.opened = false;
	}
	
	@Override
	protected ByteBuffer cloneIfNessessary(ByteBuffer object, boolean exclusive, int port) {
		return object;
	}
	
	public ByteBufferReceiverPO(ByteBufferReceiverPO<W> byteBufferReceiverPO) {
		super();
		handler = (IObjectHandler<W>)byteBufferReceiverPO.handler.clone();
		size = byteBufferReceiverPO.size;
		currentSize = byteBufferReceiverPO.currentSize;
		router = byteBufferReceiverPO.router;
		host = byteBufferReceiverPO.host;
		port = byteBufferReceiverPO.port;
		opened = byteBufferReceiverPO.opened;
	}

	@Override
	protected synchronized void process_open() throws OpenFailedException {
		if (!opened){
			try {
				router.connectToServer(this, host, port);
				opened = true;
			} catch (Exception e) {
				throw new OpenFailedException(e);
			}
		}
	}
	
	@Override
	protected void process_done(int port) {
		opened = false;
		super.process_done(port);
	}
	
	@Override
	protected synchronized void process_next(ByteBuffer buffer, int port) {
		try {	
			while (buffer.remaining() > 0){

				//logger.debug("size "+size+" remaining "+buffer.remaining()+" currentSize "+currentSize);

				
				// ACHTUNG! ES KANN SEIN, DASS "SIZE" NOCH NICHT VOLLSTÄNDIG ÜBERTRAGEN IST
				// Neues Object?
				// TODO: Reihenfolge mit der die Size kodiert wird festlegen!!!
				if (size == -1){ 
					while(sizeBuffer.position() < 4 && buffer.remaining() > 0){
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles übertragen 
					if (sizeBuffer.position() == 4){
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						//logger.debug("NEW OBJEKT STARTED WITH SIZE "+size);
					}					
				}				
				// Es kann auch direkt nach der size noch was im Puffer sein!
				// Und Size kann gesetzt worden sein
				if (size != -1){
					// Ist das was dazukommt kleiner als die finale Größe?
					if (currentSize+buffer.remaining() <= size){
						currentSize = currentSize + buffer.remaining(); 
						handler.put(buffer);					
					}else{ 
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler übergeben
						//logger.debug(" "+(size-currentSize));
						handler.put(buffer, size-currentSize);
						// 2. das fertige Objekt weiterleiten
						transfer();			
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}

	@Override
	public boolean modifiesInput() {
		return false;
	}
	
	private synchronized void transfer() throws IOException, ClassNotFoundException {
		W toTrans = handler.create();
		//logger.debug("Transfer "+toTrans);
		transfer(toTrans);
		size = -1;
		sizeBuffer.clear();
		currentSize = 0;
	}

	@Override
	public ByteBufferReceiverPO<W> clone() {
		return new ByteBufferReceiverPO<W>(this);
	}
	
	@Override
	public String toString() {
		return super.toString()+" "+host+" "+port;
	}
}
