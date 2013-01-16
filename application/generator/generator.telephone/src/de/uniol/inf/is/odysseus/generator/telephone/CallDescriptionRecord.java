package de.uniol.inf.is.odysseus.generator.telephone;

/**
 * A class representing a telephone call like in 
 * http://doi.ieeecomputersociety.org/10.1109/TPDS.2012.24
 * 
 * @author Marco Grawunder
 *
 */

public class CallDescriptionRecord {
	public String src; // Caller's Number
	public String dst; // Callee's Number
	public long start; // Start time of the call
	public long end; // End time of the call
	public int district; // Area-Id where caller is located
	public double lat; // Latitude coordinate of the caller
	public double lon; // Longitude coordinate of the caller
	public long ts; // Emission timestamp of the CDR
	
	@Override
	public String toString() {
		return src+";"+dst+";"+start+";"+end+";"+district+";"+lat+";"+lon+";"+ts;
	}
}
