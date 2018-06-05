package de.uniol.inf.is.odysseus.keyvalue.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFKeyValueDatatype {
	public static final SDFDatatype KEYVALUEOBJECT = new SDFDatatype("KeyValueObject");

	public static final SDFDatatype LIST_KEYVALUEOBJECT = new SDFDatatype("List_KeyValueObject",
			SDFDatatype.KindOfDatatype.LIST, KEYVALUEOBJECT);

	public static List<SDFDatatype> getAll() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFKeyValueDatatype.KEYVALUEOBJECT);
		ret.add(SDFKeyValueDatatype.LIST_KEYVALUEOBJECT);
		return ret;
	}

}
