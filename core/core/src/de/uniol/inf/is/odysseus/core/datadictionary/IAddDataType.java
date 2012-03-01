package de.uniol.inf.is.odysseus.core.datadictionary;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

/** 
 * This Interface is needed in SDFDatatype to add the default 
 * data types to another class (typically the data dictionary)
 * @author Marco Grawunder
 *
 */

public interface IAddDataType {
	/**
	 * Add a new data type 
	 * @param name: The unique name of the data type
	 * @param dt: The data type 
	 */
	public void addDatatype(String name, SDFDatatype dt);

}
