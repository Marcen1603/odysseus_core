package de.uniol.inf.is.odysseus.sourcedescription.sdf.schema;

import java.util.HashMap;

public class SDFDatatypeFactory {
	// Eine Cache-Struktur, dass Datentypen-Objekte nicht mehrfach
	// erzeugt werden
	static HashMap<String, SDFDatatype> createdTypes = new HashMap<String, SDFDatatype>();

	private SDFDatatypeFactory() {
	}

	static public SDFDatatype getDatatype(String URI) {
		// Erst mal testen, ob es das Objekt schon gibt
		if (createdTypes.containsKey(URI)) {
			return (SDFDatatype) createdTypes.get(URI);
		}
		SDFDatatype dt = new SDFDatatype(URI);
		createdTypes.put(URI, dt);
		return dt;
	}
}