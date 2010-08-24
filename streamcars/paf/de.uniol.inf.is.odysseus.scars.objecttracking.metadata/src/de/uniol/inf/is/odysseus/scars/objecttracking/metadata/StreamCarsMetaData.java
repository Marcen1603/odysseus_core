package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.metadata.ObjectTrackingMetadata;


/**
 * Combined metadata which implements each metadata that is used in the
 * StreamCars project.
 *
 * @author Volker Janz
 *
 */
public class StreamCarsMetaData<K> extends ObjectTrackingMetadata<K> implements 
	IConnectionContainer, IGain {

	/* ############### KONSTRUKTOREN ################ */

	public StreamCarsMetaData() {
		super();
		this.connectionList = new ConnectionList();
	}
	
	public StreamCarsMetaData( StreamCarsMetaData<K> data ) {
		super(data);
		this.connectionList = data.connectionList;
		this.gain = copyArray(data.gain);
	}

	/* ############### CONNECTIONCONTAINER ################ */

	private ConnectionList connectionList;

	public void setConnectionList(ConnectionList list) {
		this.connectionList = list;
	}

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
		if(this.connectionList == null || this.connectionList.size() == 0){
			sb.append("no connections");
		} else {
			sb.append("connections[");
			for(Connection con : this.connectionList) {
				sb.append("(");
				sb.append(String.valueOf(con.getRating()));
				sb.append(": ");
				sb.append(arrayToString(con.getLeftPath()));
				sb.append("-->");
				sb.append(arrayToString(con.getRightPath()));
			}
			sb.append("]");
		}

		sb.append("}");
		return sb.toString();
	}
	
	private static String arrayToString( int[] array ) {
		if( array == null ) return "null";
		StringBuilder sb = new StringBuilder();
		sb.append("{");
		for( int i = 0; i < array.length; i++ ) { 
			sb.append(i);
			if( i != array.length - 1 ) 
				sb.append(",");
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
}
