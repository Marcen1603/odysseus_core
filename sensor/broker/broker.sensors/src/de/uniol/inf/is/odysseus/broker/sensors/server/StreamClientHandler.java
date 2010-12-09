package de.uniol.inf.is.odysseus.broker.sensors.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import de.uniol.inf.is.odysseus.broker.sensors.generator.IStreamType;
import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamType;
import de.uniol.inf.is.odysseus.broker.sensors.generator.StreamTypeFactory;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.relational.base.RelationalTupleObjectHandler;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

/**
 * Handles a client connection for a stream to allow to distinguish between
 * several clients.
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
	 * @param channel
	 *            the channel
	 * @param type
	 *            the type
	 */
	public StreamClientHandler(SocketChannel channel, StreamType type) {
		this.channel = channel;
		this.streamType = StreamTypeFactory.createNewRun(type);
		this.relationalTupleHandler = new RelationalTupleObjectHandler<ITimeInterval>(this.streamType.getSchema());
	}

	private static long start = -1;
	
	private void sleepUntil(long fixedTime){
		while (System.currentTimeMillis() < fixedTime)
				;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		if(start == -1){
			start = System.currentTimeMillis() + 20000;
		}
		this.sleepUntil(start);
		
		long currentTime = 0L;
		while (true) {
			currentTime++;

			try {
				sleep(this.streamType.getWaitingMillis());
				transferTuple(System.currentTimeMillis() - start);
//				transferTuple(currentTime*this.streamType.getWaitingMillis());
				itemCount++;
			} catch (IOException e) {
				this.println("Client closed connection");
				break;
			} catch (InterruptedException e) {
				return;
			}
			
		}


	}

	/**
	 * Prints a message
	 * 
	 * @param message
	 *            the message
	 */
	public void println(String message) {
		System.out.println(this.streamType.getName() + ":\t " + message);
	}

	/**
	 * Transfer a tuple to the client.
	 * 
	 * @param currentTime
	 *            the current time
	 * @throws IOException
	 *             Signals that an I/O exception has occurred.
	 */
	public void transferTuple(long currentTime) throws IOException {
		RelationalTuple<ITimeInterval> tuple = this.streamType.getNextTuple(currentTime);
		this.relationalTupleHandler.put(tuple);
		ByteBuffer buffer = this.relationalTupleHandler.getByteBuffer();
		synchronized (gbuffer) {			
			gbuffer.clear();
			gbuffer.putInt(buffer.limit());
			gbuffer.flip();
			this.channel.write(gbuffer);
			this.channel.write(buffer);
		}
		this.println("Object written - " + tuple);
	}

	
	

	
}
