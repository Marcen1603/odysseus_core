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

public class StreamClientHandler extends Thread {

	private RelationalTupleObjectHandler<ITimeInterval> relationalTupleHandler;	
	private SocketChannel channel;
	private ByteBuffer gbuffer = ByteBuffer.allocate(1024);
	private IStreamType streamType;
	private int itemCount = 0;

	public StreamClientHandler(SocketChannel channel, StreamType type) {
		this.channel = channel;
		this.streamType = StreamTypeFactory.createNewRun(type);
		this.relationalTupleHandler = new RelationalTupleObjectHandler<ITimeInterval>(this.streamType.getSchema());		
	}

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
	
	public void println(String message){
		System.out.println(this.streamType.getName()+":\t "+message);
	}
	
	
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
	
	
	public SDFAttributeList getPunctuationSchema() {
		SDFAttributeList schema = new SDFAttributeList();
		SDFAttribute a = new SDFAttribute("timestamp");
		a.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		schema.add(a);		
		return schema;
	}
	
	private void sendType(int type) throws IOException{
		ByteBuffer buffer = ByteBuffer.allocate(1024);
		buffer.clear();
		buffer.putInt(type);
		buffer.flip();
		this.channel.write(buffer);
	}


}
