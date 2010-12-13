package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.HashMap;

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

	@SuppressWarnings("unchecked")
	public StreamCarsMetaData( StreamCarsMetaData<K> data ) {
		super(data);
		this.connectionList = data.connectionList;
		this.gain = copyArray(data.gain);
		this.currentObjectTrackingLatency = data.currentObjectTrackingLatency;
		this.currentStartObjTrackingTime = data.currentStartObjTrackingTime;
		this.operatorLatencies = new HashMap<String, Long>((HashMap<String, Long>) data.operatorLatencies.clone());
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
	private String[] restrictedList;
	
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
		operatorLatencies.put(operatorId, System.nanoTime());
//		System.out.println("setObjectTrackingLatencyStart: " + operatorId + "; Time: " + System.nanoTime());
	}

	@Override
	public void setObjectTrackingLatencyEnd(String operatorId) {
		if(operatorLatencies.containsKey(operatorId)) {
			Long newVal = new Long(System.nanoTime() - operatorLatencies.get(operatorId).longValue());
			operatorLatencies.put(operatorId, newVal);
//			System.out.println("setObjectTrackingLatencyEnd: " + operatorId + "; New value: " + newVal);
		}
	}

	@Override
	public long getObjectTrackingLatency(String operatorId) {
//		System.out.println("getObjectTrackingLatency: " + operatorId + "; IsThere?!: " + operatorLatencies.containsKey(operatorId));
		Long lat;
		if(operatorLatencies.containsKey(operatorId)) {
			lat = new Long(operatorLatencies.get(operatorId).longValue());
		} else {
			lat = new Long(-1);
		}
		return lat;
	}

	public HashMap<String, Long> getOperatorLatencies() {
		return operatorLatencies;
	}

	public void setOperatorLatencies(HashMap<String, Long> operatorLatencies) {
		this.operatorLatencies = operatorLatencies;
	}

	

	@Override
	public String[] getRestrictedList() {
		return this.getRestrictedList();
	}

	@Override
	public double getRestrictedGain(int index) {
		return this.getGain()[index][index];
	}

	@Override
	public void setRestrictedGain(double[][] newGain, String[] restrictedList) {
		this.gain=newGain;
		this.restrictedList=restrictedList;
		
	}

	@Override
	public double getRestrictedGain(String attribute) {
		int y = 0;
		for (int i=0; i<=restrictedList.length-1; i++) {
			if (restrictedList[i].equals(attribute)){
			y=i;	
			}
			
		}
		return this.getGain()[y][y];
	}
}
