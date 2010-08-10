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
		this.gain = data.gain;
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
		if(this.connectionList == null || this.connectionList.size() == 0){
			return "no connections";
		}

		String cons = "[";
		for(Connection con : this.connectionList) {
			cons += "(";
			cons += con.getLeft().toString();
			cons += ":";
			cons += con.getRight().toString();
			cons += ":";
			cons += String.valueOf(con.getRating());
			cons += ")";
		}
		cons += "]";

		return cons;
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

	@Override
	public void setGain(double[][] newGain) {
		this.gain = newGain;
	}
}
