package de.uniol.inf.is.odysseus.core.datatype;

import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public interface IDatatypeProvider {
	List<SDFDatatype> getDatatypes();

}
