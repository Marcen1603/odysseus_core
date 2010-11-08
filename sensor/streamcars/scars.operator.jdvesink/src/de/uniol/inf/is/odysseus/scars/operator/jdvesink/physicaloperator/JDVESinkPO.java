package de.uniol.inf.is.odysseus.scars.operator.jdvesink.physicaloperator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import de.uniol.inf.is.odysseus.intervalapproach.ITimeInterval;
import de.uniol.inf.is.odysseus.latency.ILatency;
import de.uniol.inf.is.odysseus.metadata.PointInTime;
import de.uniol.inf.is.odysseus.objecttracking.MVRelationalTuple;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IPredictionFunctionKey;
import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractPipe;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.predicate.IPredicate;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IConnectionContainer;
import de.uniol.inf.is.odysseus.scars.objecttracking.metadata.IObjectTrackingLatency;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.DatagramServer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.IServer;
import de.uniol.inf.is.odysseus.scars.operator.jdvesink.server.NIOServer;
import de.uniol.inf.is.odysseus.scars.util.TupleInfo;
import de.uniol.inf.is.odysseus.scars.util.TupleIterator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;

public class JDVESinkPO<M extends IProbability & IObjectTrackingLatency & IPredictionFunctionKey<IPredicate<MVRelationalTuple<M>>> & IConnectionContainer & ITimeInterval & ILatency>
		extends AbstractPipe<MVRelationalTuple<M>, MVRelationalTuple<M>> {

	public static final String SERVER_TYPE_UDP = "udp";
	public static final String SERVER_TYPE_SOCKET = "socket";

	private IServer server;
	private int port;
	private String serverType;
	private String hostAdress;

	// timing stuff
	private ArrayList<Long> objecttrackingLatencies;
	private ArrayList<Long> odysseusLatencies;
	private int countMax = 300;

	public JDVESinkPO(String hostAdress, int port, String serverType) {
		this.port = port;
		this.hostAdress = hostAdress;
		this.serverType = serverType;
		this.objecttrackingLatencies = new ArrayList<Long>();
		this.odysseusLatencies = new ArrayList<Long>();
	}

	public JDVESinkPO(JDVESinkPO<M> sink) {
		this.port = sink.port;
		this.server = sink.server;
		this.hostAdress = sink.hostAdress;
		this.serverType = sink.serverType;
		this.odysseusLatencies = new ArrayList<Long>(
				sink.odysseusLatencies);
		this.odysseusLatencies = new ArrayList<Long>(
				sink.odysseusLatencies);
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
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList schema = new SDFAttributeList();
		SDFAttribute attr0 = new SDFAttribute("Odysseus latency");
		attr0.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr1 = new SDFAttribute("Objecttracking latency");
		attr1.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr2 = new SDFAttribute("Odysseus latency meridian");
		attr2.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr3 = new SDFAttribute("Objecttracking latency meridian");
		attr3.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		schema.add(attr0);
		schema.add(attr1);
		schema.add(attr2);
		schema.add(attr3);
		return schema;
	}

	@Override
	protected void process_next(MVRelationalTuple<M> object, int port) {
		object.getMetadata().setLatencyEnd(System.nanoTime());

		// Create new output tuple for graphical performance output
		// 1 -> actual odysseus latency
		// 2 -> actual objecttracking latency
		// 3 -> odysseus latency meridian
		// 4 -> objecttracking latency meridian
		MVRelationalTuple<M> output = new MVRelationalTuple<M>(4);

		if(odysseusLatencies.size() == this.countMax) {
			odysseusLatencies.remove(0);
		}

		if(objecttrackingLatencies.size() == this.countMax) {
			objecttrackingLatencies.remove(0);
		}

		odysseusLatencies.add(object.getMetadata().getLatency());
		objecttrackingLatencies.add(object.getMetadata().getObjectTrackingLatency());

		// actual odysseus latency
		output.setMetadata(object.getMetadata());
		output.setAttribute(0, odysseusLatencies.get(odysseusLatencies.size()-1));

		// actual objecttracking latency
		output.setMetadata(object.getMetadata());
		output.setAttribute(1, objecttrackingLatencies.get(objecttrackingLatencies.size()-1));

		// odysseus latency meridian
		Collections.sort(odysseusLatencies);
		output.setAttribute(2, this.median(odysseusLatencies));

		// objecttracking latency meridian
		Collections.sort(objecttrackingLatencies);
		output.setAttribute(3, this.median(objecttrackingLatencies));

		transfer(output);

		if(odysseusLatencies.size() == countMax) {
			Collections.sort(odysseusLatencies);
			long odyLatency = this.median(odysseusLatencies);
			System.out.println("######### ODYSSEUS LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(odyLatency) + " #########");
		}

		if(objecttrackingLatencies.size() == countMax) {
			Collections.sort(objecttrackingLatencies);
			long objLatency = this.median(objecttrackingLatencies);
			System.out.println("######### OBJECT TRACKING LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(objLatency) + " #########");
		}




//		if (odysseusLatencies.size() < countMax) {
//			odysseusLatencies.add(object.getMetadata().getLatency());
//			System.out.println("######### ODYSSEUS LATENCY (TUPLE) [" + odysseusLatencies.size() + "] -> " + String.valueOf(odysseusLatencies.get(odysseusLatencies.size()-1)) + " #########");
//			MVRelationalTuple<M> output = new MVRelationalTuple<M>(1);
//			output.setMetadata(object.getMetadata());
//			output.setAttribute(0, odysseusLatencies.get(odysseusLatencies.size()-1));
//			transfer(output);
//		} else {
//			Collections.sort(odysseusLatencies);
//			long odyLatency = this.median(odysseusLatencies);
//			System.out.println("######### ODYSSEUS LATENCY (MEDIAN) -> " + String.valueOf(odyLatency) + " #########");
//			this.odysseusLatencies.clear();
//		}
//
//		if (objecttrackingLatencies.size() < countMax) {
//			objecttrackingLatencies.add(object.getMetadata()
//					.getObjectTrackingLatency());
//			System.out.println("######### OBJECT TRACKING LATENCY (TUPLE) [" + objecttrackingLatencies.size() + "] -> " + String.valueOf(objecttrackingLatencies.get(objecttrackingLatencies.size()-1)) + " #########");
//		} else {
//			Collections.sort(objecttrackingLatencies);
//			long objLatency = this.median(objecttrackingLatencies);
//			System.out.println("######### OBJECT TRACKING LATENCY (MEDIAN) -> " + String.valueOf(objLatency) + " #########");
//			this.objecttrackingLatencies.clear();
//		}


		// iterate over schema and calculate size of byte buffer
		int bufferSize = 0;
		SDFAttributeList schema = this.getSubscribedToSource(0).getSchema();
		TupleIterator iterator = new TupleIterator(object, schema);
		for (TupleInfo info : iterator) {
			// atomare typen beachten, komplexe �bergehen
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
					bufferSize += ((String) info.tupleObject).length();
				} else if (info.tupleObject instanceof Byte) {
					bufferSize++;
				} else if (info.tupleObject instanceof Character) {
					bufferSize++;
				}
			} else {
				if (info.attribute.getDatatype().getURIWithoutQualName()
						.equals("List")) {
					bufferSize += 4;
				}
			}
		}
		//transfer(object);

		// Allocate byte buffer
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
					String str = (String) info.tupleObject;
					for (int i = 0; i < str.length(); i++) {
						buffer.putChar(str.charAt(i));
					}
				} else if (info.tupleObject instanceof Byte) {
					buffer.put((Byte) info.tupleObject);
				} else if (info.tupleObject instanceof Character) {
					buffer.putChar((Character) info.tupleObject);
				}
			} else if (info.attribute.getDatatype().getURIWithoutQualName()
					.equals("List")) {
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

	// ================================================ median
	public long median(ArrayList<Long> m) {
		int middle = m.size() / 2;
		if (m.size() % 2 == 1) {
			return m.get(middle);
		} else {
			long median = (m.get(middle - 1) + m.get(middle)) / 2;
			return median;
		}
	}
}
