package de.uniol.inf.is.odysseus.scars.operator.jdvesink.physicaloperator;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Collections;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.metadata.ILatency;
import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
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

	private LinkedList<Long> predAssLatencies;
	private LinkedList<Long> predLatencies;
	private LinkedList<Long> hypoSelLatencies;
	private LinkedList<Long> hypoGenLatencies;
	private LinkedList<Long> hypoEvalMahaLatencies;
	private LinkedList<Long> hypoEvalMultiLatencies;
	private LinkedList<Long> filterCovUpdateLatencies;
	private LinkedList<Long> filterEstUpdateLatencies;
	private LinkedList<Long> filterGainUpdateLatencies;

	private int countMax = 2000;
	private boolean performanceOutputOdy = false;
	private boolean performanceOutputObj = false;

	private boolean performanceOutputPredAss = false;
	private boolean performanceOutputPred = false;
	private boolean performanceOutputHypoSel = false;
	private boolean performanceOutputHypoGen = false;
	private boolean performanceOutputHypoEvalMaha = false;
	private boolean performanceOutputHypoEvalMulti = false;
	private boolean performanceOutputFilterCovUpdate = false;
	private boolean performanceOutputFilterEstUpdate = false;
	private boolean performanceOutputFilterGain = false;

	public JDVESinkPO(String hostAdress, int port, String serverType) {
		this.port = port;
		this.hostAdress = hostAdress;
		this.serverType = serverType;
		this.objecttrackingLatencies = new LinkedList<Long>();
		this.odysseusLatencies = new LinkedList<Long>();
		this.hypoSelLatencies = new LinkedList<Long>();
		this.hypoGenLatencies = new LinkedList<Long>();
		this.hypoEvalMahaLatencies = new LinkedList<Long>();
		this.hypoEvalMultiLatencies = new LinkedList<Long>();
		this.predAssLatencies = new LinkedList<Long>();
		this.predLatencies = new LinkedList<Long>();
		this.filterCovUpdateLatencies = new LinkedList<Long>();
		this.filterEstUpdateLatencies = new LinkedList<Long>();
		this.filterGainUpdateLatencies = new LinkedList<Long>();
		this.performanceOutputOdy = false;
		this.performanceOutputObj = false;
		this.performanceOutputHypoSel = false;
		this.performanceOutputHypoGen = false;
		this.performanceOutputHypoEvalMaha = false;
		this.performanceOutputHypoEvalMulti = false;
		this.performanceOutputPred = false;
		this.performanceOutputPredAss = false;
		this.performanceOutputFilterCovUpdate = false;
		this.performanceOutputFilterEstUpdate = false;
		this.performanceOutputFilterGain = false;
	}

	public JDVESinkPO(JDVESinkPO<M> sink) {
		this.performanceOutputOdy = sink.performanceOutputOdy;
		this.performanceOutputObj = sink.performanceOutputObj;
		this.performanceOutputHypoSel = sink.performanceOutputHypoSel;
		this.performanceOutputHypoGen = sink.performanceOutputHypoGen;
		this.performanceOutputHypoEvalMaha = sink.performanceOutputHypoEvalMaha;
		this.performanceOutputHypoEvalMulti = sink.performanceOutputHypoEvalMulti;
		this.performanceOutputPredAss = sink.performanceOutputPredAss;
		this.performanceOutputPred = sink.performanceOutputPred;
		this.performanceOutputFilterCovUpdate = sink.performanceOutputFilterCovUpdate;
		this.performanceOutputFilterEstUpdate = sink.performanceOutputFilterEstUpdate;
		this.performanceOutputFilterGain = sink.performanceOutputFilterGain;

		this.port = sink.port;
		this.server = sink.server;
		this.hostAdress = sink.hostAdress;
		this.serverType = sink.serverType;
		this.odysseusLatencies = new LinkedList<Long>(
				sink.odysseusLatencies);
		this.odysseusLatencies = new LinkedList<Long>(
				sink.odysseusLatencies);
		this.hypoSelLatencies = new LinkedList<Long>(
				sink.hypoSelLatencies);
		this.hypoGenLatencies = new LinkedList<Long>(
				sink.hypoGenLatencies);
		this.hypoEvalMahaLatencies = new LinkedList<Long>(
				sink.hypoEvalMahaLatencies);
		this.hypoEvalMultiLatencies = new LinkedList<Long>(
				sink.hypoEvalMultiLatencies);
		this.predLatencies = new LinkedList<Long>(
				sink.predLatencies);
		this.predAssLatencies = new LinkedList<Long>(
				sink.predAssLatencies);
		this.filterCovUpdateLatencies = new LinkedList<Long>(
				sink.filterCovUpdateLatencies);
		this.filterEstUpdateLatencies = new LinkedList<Long>(
				sink.filterEstUpdateLatencies);
		this.filterGainUpdateLatencies = new LinkedList<Long>(
				sink.filterGainUpdateLatencies);
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
		SDFAttribute attr0 = new SDFAttribute("Odysseus latency median");
		attr0.setDatatype(SDFDatatypeFactory.getDatatype("Long"));
		SDFAttribute attr1 = new SDFAttribute("Objecttracking latency median");
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

//		Association Selection
//		Association Generation
//		Association Evaluation - Mahalanobis
//		Association Evaluation - Multi Distance
//		Prediction Assign
//		Prediction


		// Create new output tuple for graphical performance output
		// 1 -> odysseus latency meridian
		// 2 -> objecttracking latency meridian
		// 3 -> actual odysseus latency
		// 4 -> actual objecttracking latency
		MVRelationalTuple<M> output = new MVRelationalTuple<M>(4);
		output.setMetadata(object.getMetadata());

		// the lists which contain the performance values should only contain <= countMax elements
		// so if there are countMax elements, remove the oldest

		// overall performance

		if(odysseusLatencies.size() == this.countMax) {
			odysseusLatencies.removeFirst();
		}

		if(objecttrackingLatencies.size() == this.countMax) {
			objecttrackingLatencies.removeFirst();
		}

		// operator performance

		if(predAssLatencies.size() == this.countMax) {
			predAssLatencies.removeFirst();
		}

		if(predLatencies.size() == this.countMax) {
			predLatencies.removeFirst();
		}

		if(hypoSelLatencies.size() == this.countMax) {
			hypoSelLatencies.removeFirst();
		}

		if(hypoGenLatencies.size() == this.countMax) {
			hypoGenLatencies.removeFirst();
		}

		if(hypoEvalMahaLatencies.size() == this.countMax) {
			hypoEvalMahaLatencies.removeFirst();
		}

		if(hypoEvalMultiLatencies.size() == this.countMax) {
			hypoEvalMultiLatencies.removeFirst();
		}

		if(filterCovUpdateLatencies.size() == this.countMax) {
			filterCovUpdateLatencies.removeFirst();
		}

		if(filterEstUpdateLatencies.size() == this.countMax) {
			filterEstUpdateLatencies.removeFirst();
		}

		if(filterGainUpdateLatencies.size() == this.countMax) {
			filterGainUpdateLatencies.removeFirst();
		}

		odysseusLatencies.add(object.getMetadata().getLatency());
		objecttrackingLatencies.add(object.getMetadata().getObjectTrackingLatency());

		// operator latencies
		predLatencies.add(object.getMetadata().getObjectTrackingLatency("Prediction"));
		predAssLatencies.add(object.getMetadata().getObjectTrackingLatency("Prediction Assign"));
		hypoSelLatencies.add(object.getMetadata().getObjectTrackingLatency("Association Selection"));
		hypoGenLatencies.add(object.getMetadata().getObjectTrackingLatency("Association Generation"));
		hypoEvalMahaLatencies.add(object.getMetadata().getObjectTrackingLatency("Association Evaluation - Mahalanobis"));
		hypoEvalMultiLatencies.add(object.getMetadata().getObjectTrackingLatency("Association Evaluation - Multi Distance"));
		filterCovUpdateLatencies.add(object.getMetadata().getObjectTrackingLatency("Filter Cov Update"));
		filterEstUpdateLatencies.add(object.getMetadata().getObjectTrackingLatency("Filter Est Update"));
		filterGainUpdateLatencies.add(object.getMetadata().getObjectTrackingLatency("Filter Gain"));

		// actual odysseus latency
		output.setAttribute(2, odysseusLatencies.getLast());

		// actual objecttracking latency
		output.setAttribute(3, objecttrackingLatencies.getLast());

		// odysseus latency median
		Collections.sort(odysseusLatencies);
		output.setAttribute(0, this.median(odysseusLatencies));

		// objecttracking latency median
		Collections.sort(objecttrackingLatencies);
		output.setAttribute(1, this.median(objecttrackingLatencies));

		transfer(output);

		// overall performance output

		if(odysseusLatencies.size() == countMax && !this.performanceOutputOdy) {
			Collections.sort(odysseusLatencies);
			long odyLatency = this.median(odysseusLatencies);
			System.out.println("######### ODYSSEUS LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(odyLatency))/1000000) + "ms (" + String.valueOf(odyLatency) + " ns) #########");
			this.performanceOutputOdy = true;
		}

		if(objecttrackingLatencies.size() == countMax && !this.performanceOutputObj) {
			Collections.sort(objecttrackingLatencies);
			long objLatency = this.median(objecttrackingLatencies);
			System.out.println("######### OBJECT TRACKING LATENCY (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(objLatency))/1000000) + "ms (" + String.valueOf(objLatency) + " ns) #########");
			this.performanceOutputObj = true;
		}

		// ###### operator performance output

		// ## PREDICTION (PredictionAssignPO, PredictionPO)

		if(predAssLatencies.size() == countMax && !this.performanceOutputPredAss) {
			Collections.sort(predAssLatencies);
			System.out.println("######### OBJECT TRACKING : PredictionAssignPO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(predAssLatencies)))/1000000) + "ms (" + String.valueOf(this.median(predAssLatencies)) + " ns) #########");
			this.performanceOutputPredAss = true;
		}

		if(predLatencies.size() == countMax && !this.performanceOutputPred) {
			Collections.sort(predLatencies);
			System.out.println("######### OBJECT TRACKING : PredictionPO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(predLatencies)))/1000000) + "ms (" + String.valueOf(this.median(predLatencies)) + " ns) #########");
			this.performanceOutputPred = true;
		}

		// ## ASSOCIATION (HypothesisGenerationPO, HypothesisEvaluationPO (for each algorithm), HypothesisSelectionPO)

		if(hypoGenLatencies.size() == countMax && !this.performanceOutputHypoGen) {
			Collections.sort(hypoGenLatencies);
			System.out.println("######### OBJECT TRACKING : HypothesisGenerationPO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(hypoGenLatencies)))/1000000) + "ms (" + String.valueOf(this.median(hypoGenLatencies)) + " ns) #########");
			this.performanceOutputHypoGen = true;
		}

		if(hypoSelLatencies.size() == countMax && !this.performanceOutputHypoSel) {
			Collections.sort(hypoSelLatencies);
			System.out.println("######### OBJECT TRACKING : HypothesisSelectionPO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(hypoSelLatencies)))/1000000) + "ms (" + String.valueOf(this.median(hypoSelLatencies)) + " ns) #########");
			this.performanceOutputHypoSel = true;
		}

		if(hypoEvalMahaLatencies.size() == countMax && !this.performanceOutputHypoEvalMaha) {
			Collections.sort(hypoEvalMahaLatencies);
			System.out.println("######### OBJECT TRACKING : HypothesisEvaluationPO : MahalanobisDistance (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(hypoEvalMahaLatencies)))/1000000) + "ms (" + String.valueOf(this.median(hypoEvalMahaLatencies)) + " ns) #########");
			this.performanceOutputHypoEvalMaha = true;
		}

		if(hypoEvalMultiLatencies.size() == countMax && !this.performanceOutputHypoEvalMulti) {
			Collections.sort(hypoEvalMultiLatencies);
			System.out.println("######### OBJECT TRACKING : HypothesisEvaluationPO : MultiDistance (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(hypoEvalMultiLatencies)))/1000000) + "ms (" + String.valueOf(this.median(hypoEvalMultiLatencies)) + " ns) #########");
			this.performanceOutputHypoEvalMulti = true;
		}

		// ## FILTERING (FilterCovarianceUpdatePO, FilterEstimateUpdatePO, FilterGainPO)

		if(filterCovUpdateLatencies.size() == countMax && !this.performanceOutputFilterCovUpdate) {
			Collections.sort(filterCovUpdateLatencies);
			System.out.println("######### OBJECT TRACKING : FilterCovarianceUpdatePO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(filterCovUpdateLatencies)))/1000000) + "ms (" + String.valueOf(this.median(filterCovUpdateLatencies)) + " ns) #########");
			this.performanceOutputFilterCovUpdate = true;
		}

		if(filterEstUpdateLatencies.size() == countMax && !this.performanceOutputFilterEstUpdate) {
			Collections.sort(filterEstUpdateLatencies);
			System.out.println("######### OBJECT TRACKING : FilterEstimateUpdatePO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(filterEstUpdateLatencies)))/1000000) + "ms (" + String.valueOf(this.median(filterEstUpdateLatencies)) + " ns) #########");
			this.performanceOutputFilterEstUpdate = true;
		}

		if(filterGainUpdateLatencies.size() == countMax && !this.performanceOutputFilterGain) {
			Collections.sort(filterGainUpdateLatencies);
			System.out.println("######### OBJECT TRACKING : FilterGainPO (MEDIAN) [after " + this.countMax + "] -> " + String.valueOf(Float.valueOf(String.valueOf(this.median(filterGainUpdateLatencies)))/1000000) + "ms (" + String.valueOf(this.median(filterGainUpdateLatencies)) + " ns) #########");
			this.performanceOutputFilterGain = true;
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
