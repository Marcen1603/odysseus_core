package de.uniol.inf.is.odysseus.scars.objecttracking.metadata;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.objecttracking.metadata.IProbability;
import de.uniol.inf.is.odysseus.objecttracking.metadata.Probability;

/**
 * Combined metadata which implements each metadata that is used in the
 * StreamCars project.
 *
 * @author Volker Janz
 *
 */
public class StreamCarsMetaData implements IConnectionContainer, IGain, IProbability  {

	/* ############### KONSTRUKTOREN ################ */

	public StreamCarsMetaData() {
		this.connectionList = new ConnectionList();
	}

	public StreamCarsMetaData(ConnectionList inilist) {
		this.setConnectionList(inilist);
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
	public StreamCarsMetaData clone() {
		return null;
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

	/* ############### PROBABILITY ################ */

	Probability probObj = new Probability();

	public void setCovariance(double[][] sigma) {
		probObj.setCovariance(sigma);
	}

	public double[][] getCovariance() {
		return probObj.getCovariance();
	}

	public void setMVAttributeIndices(int[] indices) {
		probObj.setMVAttributeIndices(indices);
	}

	public int[] getMVAttributeIndices() {
		return probObj.getMVAttributeIndices();
	}

	public ArrayList<int[]> getAttributePaths() {
		return probObj.getAttributePaths();
	}

	public void setAttributePaths(ArrayList<int[]> paths) {
		probObj.setAttributePaths(paths);
	}

	public int getIndexOfKovMatrix(int[] path) {
		return probObj.getIndexOfKovMatrix(path);
	}

}
