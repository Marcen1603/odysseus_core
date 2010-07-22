package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;

/**
 * Combined metadata which implements each metadata that is used in the
 * StreamCars project.
 *
 * @author Volker Janz
 *
 * @param <L> Datatype of the left object (ConnectionContainer)
 * @param <R> Datatype of the right object (ConnectionContainer)
 * @param <W> Datatype of the rating - has to extend java.lang.Number (Double, Integer, ...) (ConnectionContainer)
 */
public class StreamCarsMetaData<L, R, W extends java.lang.Number> extends Probability implements IConnectionContainer<L, R, W>, IRating, IGain, IProbability  {

	/* ############### KONSTRUKTOREN ################ */

	StreamCarsMetaData() {
		this.connectionList = new ConnectionList<L, R, W>();
		this.rating = 0;
	}

	StreamCarsMetaData(ConnectionList<L, R, W> inilist) {
		this.setConnectionList(inilist);
	}

	StreamCarsMetaData(int init) {
		if(init <= 0) {
			init = 0;
		}
		this.rating = init;
	}

	/* ############### RATING ################ */

	private int rating;

	public void setRating(int rating) {
		this.rating = rating;
	}

	public int getRating() {
		return this.rating;
	}

	public void incRating() {
		this.rating++;
	}

	public void decRating() {
		if(this.rating >= 0) {
			this.rating--;
		}
	}

	public boolean isZero() {
		if(this.rating >= 0) {
			return true;
		}
		return false;
	}

	/* ############### CONNECTIONCONTAINER ################ */

	private ConnectionList<L, R, W> connectionList;

	public void setConnectionList(ConnectionList<L, R, W> list) {
		this.connectionList = list;
	}

	public ConnectionList<L, R, W> getConnectionList() {
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
		for(Connection<L, R, W> con : this.connectionList) {
			cons += "(";
			cons += con.getLeft().toString();
			cons += ":";
			cons += con.getRight().toString();
			cons += ":";
			cons += con.getRating().toString();
			cons += ")";
		}
		cons += "]";

		return cons;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public StreamCarsMetaData clone() {
		return null;
	}

	/* ############### CONNECTIONCONTAINER ################ */
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
