package de.uniol.inf.is.odysseus.scars.operator.jdvesink.physicaloperator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import de.uniol.inf.is.odysseus.base.OpenFailedException;
import de.uniol.inf.is.odysseus.base.PointInTime;
import de.uniol.inf.is.odysseus.base.predicate.IPredicate;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.base.AbstractPipe;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.DatagramServer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.IServer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.NIOServer;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class JDVESinkPO<M extends IProbability & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval>
extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {
	
	public static final String SERVER_TYPE_UDP = "udp";
	public static final String SERVER_TYPE_SOCKET = "socket";
	
	private IServer server;
	private int port;
	private String serverType;
	private String hostAdress;
	
	public JDVESinkPO(String hostAdress, int port, String serverType) {
		this.port = port;
		this.hostAdress = hostAdress;
		this.serverType = serverType;
	}
	
	public JDVESinkPO(JDVESinkPO<M> sink) {
		this.port = sink.port;
		this.server = sink.server;
		this.hostAdress = sink.hostAdress;
		this.serverType = sink.serverType;
	}

	@Override
	public void processPunctuation(PointInTime timestamp, int port) {
		
	}

	@Override
	protected void process_open() throws OpenFailedException {
		super.process_open();
		if (JDVESinkPO.SERVER_TYPE_SOCKET.equals(this.serverType)) {
			this.server = new NIOServer(this.port);
		} else if (JDVESinkPO.SERVER_TYPE_UDP.equals(this.serverType)) {
			this.server = new DatagramServer(this.hostAdress, this.port);
		}
		this.server.start();
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		// iterate over schema and calculate size of byte buffer
		int bufferSize = 0;
		SDFAttributeList schema = this.getSubscribedToSource(0).getSchema();
		TupleIterator iterator = new TupleIterator(object, schema);
		for (TupleInfo info : iterator) {
			//atomare typen beachten, komplexe �bergehen
			if (!info.isTuple) {
				if (info.tupleObject instanceof Integer) {
					bufferSize += 4;
				} else if (info.tupleObject instanceof Long) {
					bufferSize += 8;
				} else if (info.tupleObject instanceof Float) {
					bufferSize += 4;
				} else if (info.tupleObject instanceof Double) {
					bufferSize += 8;
				} else if (info.tupleObject instanceof String) {
					bufferSize += ((String)info.tupleObject).length();
				} else if (info.tupleObject instanceof Byte) {
					bufferSize++;
				} else if (info.tupleObject instanceof Character) {
					bufferSize++;
				}
			} else {
				if (info.attribute.getDatatype().getURIWithoutQualName().equals("List")) {
					bufferSize += 4;
				}
			}
		}
		transfer(object);
		
		//Allocate byte buffer
		ByteBuffer buffer = ByteBuffer.allocate(bufferSize);
		buffer.order(ByteOrder.LITTLE_ENDIAN);
		
		// iterate over tuple and write elementary data to byte buffer
		iterator = new TupleIterator(object, schema);
		for (TupleInfo info : iterator) {
			if (!info.isTuple) {
				if (info.tupleObject instanceof Integer) {
					buffer.putInt((Integer) info.tupleObject);
				} else if (info.tupleObject instanceof Long) {
					buffer.putLong((Long) info.tupleObject);
				} else if (info.tupleObject instanceof Float) {
					buffer.putFloat((Float) info.tupleObject);
				} else if (info.tupleObject instanceof Double) {
					buffer.putDouble((Double) info.tupleObject);
				} else if (info.tupleObject instanceof String) {
					String str = (String)info.tupleObject;
					for (int i = 0; i < str.length(); i++) {
						buffer.putChar(str.charAt(i));
					}
				} else if (info.tupleObject instanceof Byte) {
					buffer.put((Byte) info.tupleObject);
				} else if (info.tupleObject instanceof Character) {
					buffer.putChar((Character)info.tupleObject);
				}
			} else if (info.attribute.getDatatype().getURIWithoutQualName().equals("List")) {
				@SuppressWarnings("unchecked")
				MVRelationalTuple<M> tuple = (MVRelationalTuple<M>) info.tupleObject;
				buffer.putInt(tuple.getAttributeCount());
			}
		}
		
		// push byte buffer to server
		buffer.flip();
		this.server.sendData(buffer);
	}

	@Override
	protected void process_close() {
		super.process_close();
		this.server.close();
	}

	@Override
	public OutputMode getOutputMode() {
		return OutputMode.INPUT;
	}

	@Override
	public AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> clone() {
		return new JDVESinkPO<M>(this);
	}

}
