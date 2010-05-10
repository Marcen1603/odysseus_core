package de.uniol.inf.is.odysseus.broker.sensors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType;
import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamType;
import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamTypeFactory;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * Handles a client connection for a stream to allow to distinguish between several clients.
 * 
 * @author Dennis Geesen
 */
public class StreamClientHandler extends Thread {

	/** The relational tuple handler. */
	private RelationalTupleObjectHandler<ITimeInterval> relationalTupleHandler;	
	
	/** The channel. */
	private SocketChannel channel;
	
	/** The tuple buffer. */
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	
	/** The type of the provided stream. */
	private IStreamType streamType;
	
	/** The count of items which has been sent. */
	private int itemCount = 0;	

	/**
	 * Instantiates a new stream client handler.
	 *
	 * @param channel the channel
	 * @param type the type
	 */
	public StreamClientHandler(SocketChannel channel, StreamType type) {
		this.channel = channel;		
		this.streamType = StreamTypeFactory.createNewRun(type);
		this.relationalTupleHandler = new RelationalTupleObjectHandler<ITimeInterval>(this.streamType.getSchema());		
	}

	/* (non-Javadoc)
	 * @see java.lang.Thread#run()
	 */
	public void run() {
		while (true) {
			long currentTime =  System.currentTimeMillis();
			if (itemCount < streamType.getMaxItems()) {
				try {
					transferTuple(currentTime);
					itemCount++;
				} catch (IOException e) {
					this.println("Client closed connection");
					break;
				}
			}else{
				if(streamType.isPunctuationEnabled()){
					try{
						transferPunctuation(currentTime);						
					} catch (IOException e) {
						this.println("Client closed connection");
						break;
					}
				}else{
					this.println("All items written!");
					try {
						sendType(2);
						this.channel.close();
						this.println("Connection closed.");
					} catch (IOException e) {						
						e.printStackTrace();
					}
					break;
				}
			}
			try {
				sleep(this.streamType.getWaitingMillis());
			} catch (InterruptedException e) {
				return;
			}

		}
	}
	
	/**
	 * Prints a message
	 *
	 * @param message the message
	 */
	public void println(String message){
		System.out.println(this.streamType.getName()+":\t "+message);
	}
	
	
	/**
	 * Transfer a tuple to the client.
	 *
	 * @param currentTime the current time
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void transferTuple(long currentTime) throws IOException{
		RelationalTuple<ITimeInterval> tuple = this.streamType.getNextTuple(currentTime);		
		this.relationalTupleHandler.put(tuple);
		ByteBuffer buffer = this.relationalTupleHandler.getByteBuffer();		
		synchronized (gbuffer) {		
			sendType(0);
			gbuffer.clear();
			gbuffer.putInt(buffer.limit());
			gbuffer.flip();
			this.channel.write(gbuffer);
			this.channel.write(buffer);
		}
		this.println("Object written - " + tuple);
	}
	
	/**
	 * Transfers a punctuation to the client.
	 *
	 * @param currentTime the current time
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	public void transferPunctuation(long currentTime) throws IOException{		
		long time = this.streamType.getNextPunctuation(currentTime);		
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.clear();
		buffer.putLong(time);
		buffer.flip();
		synchronized (gbuffer) {
			sendType(1);
			gbuffer.clear();
			gbuffer.putInt(buffer.limit());
			gbuffer.flip();
			this.channel.write(gbuffer);
			this.channel.write(buffer);
		}
		this.println("Punctuation written - " + time);
	}
	
	
	/**
	 * Gets the punctuation schema.
	 *
	 * @return the punctuation schema
	 */
	public SDFAttributeList getPunctuationSchema() {
		SDFAttributeList schema = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute("timestamp");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		schema.add(a);		
		return schema;
	}
	
	/**
	 * Send the type of the tuple.
	 * 0 = normal element
	 * 1 = punctuation
	 * 2 = done
	 *
	 * @param type the type
	 * @throws IOException Signals that an I/O exception has occurred.
	 */
	private void sendType(int type) throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.clear();
		buffer.putInt(type);
		buffer.flip();
		this.channel.write(buffer);
	}


}
