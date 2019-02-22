package de.uniol.inf.is.odysseus.core.mep;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public interface IFunctionSignatur {

	/**
	 * 
	 * @return the symbol for this signature
	 */
	String getSymbol();

	/**
	 * 
	 * @return the ordered set of parameters
	 */
	List<SDFDatatype[]> getParameters();

	/**
	 * 
	 * @param parameters A list of SDFDatatypes
	 * @return true, if the signature is the same as the given parameters
	 */
	boolean contains(List<SDFDatatype> parameters);

}
