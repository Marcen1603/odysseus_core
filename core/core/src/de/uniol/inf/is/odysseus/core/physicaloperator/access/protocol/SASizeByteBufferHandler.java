package de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.collection.SecurityPunctuation;
import de.uniol.inf.is.odysseus.core.datahandler.IDataHandler;
import de.uniol.inf.is.odysseus.core.objecthandler.ByteBufferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.ITransferHandler;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.transport.ITransportHandler;

/**
 * @author Jan Sören Schwarz
 *
 * @param <T>
 */
public class SASizeByteBufferHandler<T> extends SizeByteBufferHandler<T> {

	private int size = -1;
	private ByteBuffer sizeBuffer = ByteBuffer.allocate(4);
	private int currentSize = 0;
	private ByteBufferHandler<T> objectHandler;
	/**
	 * Gibt an, ob das gerade übertragene Tupel eine Security Punctuation ist
	 */
	private Boolean isSP = null;
	private ByteBuffer isSPBuffer = ByteBuffer.allocate(4);

	@Override
	public void open() throws UnknownHostException, IOException {
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		getTransportHandler().open();
		isSPBuffer.clear();
		isSP = null;
	}

	@Override
	public void onConnect(ITransportHandler caller) {
		
	}

	@Override
	public void close() throws IOException {
		
		//close funktioniert anscheinend irgenwie noch nicht...
		getTransportHandler().close();
		sizeBuffer.clear();
		size = -1;
		currentSize = 0;
		isSPBuffer.clear();
		isSP = null;
	}

	@Override
	public void onDisonnect(ITransportHandler caller) {
		
	}

	@Override
	public void process(ByteBuffer buffer) {
		try {

//			System.out.println("SizeByteBufferHandler - buffer beim ankommen: " + buffer);
			buffer.order(byteOrder);
//			System.out.println("SizeByteBufferHandler - buffer beim ankommen 2: " + buffer);  
			
			while (buffer.remaining() > 0) {

//				System.out.println(buffer);
				// size ist dann ungleich -1 wenn die vollständige
				// Größeninformation übertragen wird
				// Ansonsten schon mal soweit einlesen
				if (size == -1) {
					while (sizeBuffer.position() < 4 && buffer.remaining() > 0) {
						sizeBuffer.put(buffer.get());
					}
					// Wenn alles übertragen
					if (sizeBuffer.position() == 4) {
						sizeBuffer.flip();
						size = sizeBuffer.getInt();
						
//						System.out.println("size: " + size);
						
					}
				}

				// nach der Größeninformation wird die SP-Flag übertragen
				if(isSP == null) {
					while (isSPBuffer.position() < 4 && buffer.remaining() > 0) {
						isSPBuffer.put(buffer.get());
					}
					// Wenn alles übertragen
					if (isSPBuffer.position() == 4) {
						isSPBuffer.flip();						
						if(isSPBuffer.getInt() == 1) {
							isSP = true;
						} else {
							isSP = false;
						}
						currentSize = currentSize + 4;
					}
				}

//				System.out.println(buffer);
				// Es kann auch direkt nach der size noch was im Puffer
				// sein!
				// Und Size kann gesetzt worden sein
				if (size != -1) {
					// Ist das was dazukommt kleiner als die finale Größe?
										
//					System.out.println("SizeByteBufferHandler - buffer.remaining: " + buffer.remaining());
//					System.out.println("SizeByteBufferHandler - size: " + size);
//					System.out.println("SizeByteBufferHandler - currentSize: " + currentSize);
					
					if (currentSize + buffer.remaining() < size) {
						currentSize = currentSize + buffer.remaining();
						objectHandler.put(buffer);
					} else {
						// Splitten (wir sind mitten in einem Objekt
						// 1. alles bis zur Grenze dem Handler übergeben
//						 logger.debug(" "+(size-currentSize));
//						System.out.println("SizeByteBuffer - Buffer: " + buffer);
						objectHandler.put(buffer, size - currentSize);
						// 2. das fertige Objekt weiterleiten
						if(!isSP) {
//							System.out.println("SizeByteBuffer - !isSP - Buffer: " + buffer);
							getTransfer().transfer(objectHandler.create());
						} else {
//							System.out.println("SizeByteBuffer - isSP - Buffer: " + buffer);
							getTransfer().transferSecurityPunctuation((SecurityPunctuation) objectHandler.createSecurityAware());
						}
						size = -1;
						sizeBuffer.clear();
						currentSize = 0;
						// Dann in der While-Schleife weiterverarbeiten
					}
				}
			}

			isSPBuffer.clear();
			isSP = null;
			
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BufferUnderflowException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public IProtocolHandler<T> createInstance(Map<String, String> options,
			ITransportHandler transportHandler, IDataHandler<T> dataHandler,
			ITransferHandler<T> transfer) {
		SASizeByteBufferHandler<T> instance = new SASizeByteBufferHandler<T>();
		instance.setDataHandler(dataHandler);
		instance.setTransportHandler(transportHandler);
		instance.setTransfer(transfer);
		instance.objectHandler = new ByteBufferHandler<T>(dataHandler);
		instance.setByteOrder(options.get("byteorder"));
		transportHandler.addListener(instance);
		return instance;
	}
	
	@Override
	public String getName() {
		return "SASizeByteBuffer";
	}
}
