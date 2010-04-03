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

public class StreamClientHandler extends Thread {

	private RelationalTupleObjectHandler<ITimeInterval> relationalTupleHandler;
	private SocketChannel channel;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private IStreamType streamType;
	
	public StreamClientHandler(SocketChannel channel, StreamType type) {
		this.channel = channel;
		this.streamType = StreamTypeFactory.createNewRun(type);
		this.relationalTupleHandler = new RelationalTupleObjectHandler<ITimeInterval>(this.streamType.getSchema());
	}

	public void run() {
		while (true) {
			try {				
				RelationalTuple<ITimeInterval> tuple = this.streamType.getNextTuple();
				this.relationalTupleHandler.put(tuple);
				ByteBuffer buffer = this.relationalTupleHandler.getByteBuffer();
				synchronized (gbuffer) {
					gbuffer.clear();
					gbuffer.putInt(buffer.limit());
					gbuffer.flip();
					this.channel.write(gbuffer);
					this.channel.write(buffer);
				}
			System.out.println("Object written - "+tuple);
			} catch (IOException e) {
				System.out.println("Client closed connection");
				break;
			}
			try {
				sleep(this.streamType.getWaitingMillis());
			} catch (InterruptedException e) {
				return;
			}
		}
	}

	

}
