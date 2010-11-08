package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.HashMap;
import java.util.LinkedList;

import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;


/**
 * Combined metadata which implements each metadata that is used in the
 * StreamCars project.
 *
 * @author Volker Janz
 *
 */
public class StreamCarsMetaData<K> extends ObjectTrackingMetadata<K> implements
	IConnectionContainer, IGain, IObjectTrackingLatency {

	/* ############### KONSTRUKTOREN ################ */

	public StreamCarsMetaData() {
		super();
		this.connectionList = new ConnectionList();
		this.operatorLatencies = new HashMap<String, Long>();
	}

	public StreamCarsMetaData( StreamCarsMetaData<K> data ) {
		super(data);
		this.connectionList = data.connectionList;
		this.gain = copyArray(data.gain);
		this.currentObjectTrackingLatency = data.currentObjectTrackingLatency;
		this.currentStartObjTrackingTime = data.currentStartObjTrackingTime;
		this.operatorLatencies = new HashMap<String, Long>(data.operatorLatencies);
	}

	/* ############### CONNECTIONCONTAINER ################ */

	private ConnectionList connectionList;

	@Override
	public void setConnectionList(ConnectionList list) {
		this.connectionList = list;
	}

	@Override
	public ConnectionList getConnectionList() {
		return this.connectionList;
	}

	/**
	 * Returns a string with the actual connection list as:
	 * "[(left:right:rating)][(left:right:rating)][(left:right:rating)]..."
	 */
	@Override
	public String toString(){
		StringBuilder sb = new StringBuilder();
		sb.append("StreamCarsMetaData{");
		sb.append("Latency: " + (this.getLatency()) + " | ");
		if(this.connectionList == null || this.connectionList.size() == 0){
			sb.append("no connections");
		} else {
			sb.append("connections[");
			for(IConnection con : this.connectionList) {
				sb.append("(");
				sb.append(String.valueOf(con.getRating()));
				sb.append(": ");
				sb.append(con.getLeftPath());
				sb.append("-->");
				sb.append(con.getRightPath());
			}
			sb.append("]");
		}

		sb.append("}");
		return sb.toString();
	}

	@Override
	public StreamCarsMetaData<K> clone() {
		return new StreamCarsMetaData<K>(this);
	}

	/* ############### GAIN ################ */

	private double[][] gain;
	@Override
	public double[][] getGain() {
		return this.gain;
	}

	private double[][] copyArray( double[][] toCopy ) {
		if( toCopy == null ) return null;

		double[][] copy = new double[toCopy.length][];
		for( int i = 0; i < toCopy.length; i++ ) {
			copy[i] = new double[toCopy[i].length];

			for( int j = 0; j < toCopy[i].length; j++ ) {
				copy[i][j] = toCopy[i][j];
			}

		}

		return copy;
	}

	@Override
	public void setGain(double[][] newGain) {
		this.gain = newGain;
	}

	/* ############### OBJECTTRACKINGLATENCY ################ */

	private long currentObjectTrackingLatency;
	private long currentStartObjTrackingTime;

	private HashMap<String, Long> operatorLatencies;

	@Override
	public void setObjectTrackingLatencyStart() {
		this.currentStartObjTrackingTime = System.nanoTime();
	}

	@Override
	public void setObjectTrackingLatencyEnd() {
		this.currentObjectTrackingLatency += (System.nanoTime() - this.currentStartObjTrackingTime);
	}

	@Override
	public long getObjectTrackingLatency() {
		long retVal = this.currentObjectTrackingLatency;
		this.currentObjectTrackingLatency = 0;
		return retVal;
	}

	@Override
	public void setObjectTrackingLatencyStart(String operatorId) {
		if(operatorLatencies.containsKey(operatorId)) {
			operatorLatencies.remove(operatorId);
		}
		operatorLatencies.put(operatorId, System.nanoTime());
	}

	@Override
	public void setObjectTrackingLatencyEnd(String operatorId) {
		if(operatorLatencies.containsKey(operatorId)) {
			Long newVal = new Long(System.nanoTime() - operatorLatencies.get(operatorId).longValue());
			operatorLatencies.remove(operatorId);
			operatorLatencies.put(operatorId, newVal);
		}
	}

	@Override
	public long getObjectTrackingLatency(String operatorId) {
		Long lat;
		if(operatorLatencies.containsKey(operatorId)) {
			lat = new Long(operatorLatencies.get(operatorId).longValue());
			operatorLatencies.remove(operatorId);
		} else {
			lat = new Long(-1);
		}
		return lat;
	}
}
