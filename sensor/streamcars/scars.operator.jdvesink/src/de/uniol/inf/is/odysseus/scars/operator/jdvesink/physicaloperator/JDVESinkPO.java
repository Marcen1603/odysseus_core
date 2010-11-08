package de.uniol.inf.is.odysseus.scars.operator.jdvesink.physicaloperator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.LinkedList;

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
	private LinkedList<Long> objecttrackingLatencies;
	private LinkedList<Long> odysseusLatencies;
	private int countMax = 300;
	private boolean performanceOutputOdy = false;
	private boolean performanceOutputObj = false;

	public JDVESinkPO(String hostAdress, int port, String serverType) {
		this.port = port;
		this.hostAdress = hostAdress;
		this.serverType = serverType;
		this.objecttrackingLatencies = new LinkedList<Long>();
		this.odysseusLatencies = new LinkedList<Long>();
		this.performanceOutputOdy = false;
		this.performanceOutputObj = false;
	}

	public JDVESinkPO(JDVESinkPO<M> sink) {
		this.performanceOutputOdy = sink.performanceOutputOdy;
		this.performanceOutputObj = sink.performanceOutputObj;
		this.port = sink.port;
		this.server = sink.server;
		this.hostAdress = sink.hostAdress;
		this.serverType = sink.serverType;
		this.odysseusLatencies = new LinkedList<Long>(
				sink.odysseusLatencies);
		this.odysseusLatencies = new LinkedList<Long>(
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
		SDFAttribute attr0 = new SDFAttribute("Odysseus latency meridian");
		attr0.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr1 = new SDFAttribute("Objecttracking latency meridian");
		attr1.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr2 = new SDFAttribute("Odysseus latency");
		attr2.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr3 = new SDFAttribute("Objecttracking latency");
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
		// 1 -> odysseus latency meridian
		// 2 -> objecttracking latency meridian
		// 3 -> actual odysseus latency
		// 4 -> actual objecttracking latency
		MVRelationalTuple<M> output = new MVRelationalTuple<M>(4);
		output.setMetadata(object.getMetadata());

		if(odysseusLatencies.size() == this.countMax) {
			odysseusLatencies.removeFirst();
		}

		if(objecttrackingLatencies.size() == this.countMax) {
			objecttrackingLatencies.removeFirst();
		}

		odysseusLatencies.add(object.getMetadata().getLatency());
		objecttrackingLatencies.add(object.getMetadata().getObjectTrackingLatency());

		// actual odysseus latency
		output.setAttribute(2, odysseusLatencies.getLast());

		// actual objecttracking latency
		output.setAttribute(3, objecttrackingLatencies.getLast());

		// odysseus latency meridian
		Collections.sort(odysseusLatencies);
		output.setAttribute(0, this.median(odysseusLatencies));

		// objecttracking latency meridian
		Collections.sort(objecttrackingLatencies);
		output.setAttribute(1, this.median(objecttrackingLatencies));

		transfer(output);

		if(odysseusLatencies.size() == countMax && !this.performanceOutputOdy) {
			Collections.sort(odysseusLatencies);
			long odyLatency = this.median(odysseusLatencies);
			System.out.println("######### ODYSSEUS LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(odyLatency) + " #########");
			this.performanceOutputOdy = true;
		}

		if(objecttrackingLatencies.size() == countMax && !this.performanceOutputObj) {
			Collections.sort(objecttrackingLatencies);
			long objLatency = this.median(objecttrackingLatencies);
			System.out.println("######### OBJECT TRACKING LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(objLatency) + " #########");
			this.performanceOutputObj = true;
		}


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
	public long median(LinkedList<Long> m) {
		int middle = m.size() / 2;
		if (m.size() % 2 == 1) {
			return m.get(middle);
		} else {
			long median = (m.get(middle - 1) + m.get(middle)) / 2;
			return median;
		}
	}
}
