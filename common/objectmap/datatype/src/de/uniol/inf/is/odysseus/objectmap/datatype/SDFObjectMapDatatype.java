package de.uniol.inf.is.odysseus.objectmap.datatype;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;

public class SDFObjectMapDatatype {

	public static final SDFDatatype OBJECTMAP = new SDFDatatype("ObjectMap");

	public static final SDFDatatype LIST_OBJECTMAP = new SDFDatatype("List_ObjectMap",
			SDFDatatype.KindOfDatatype.LIST, OBJECTMAP);

	public static List<SDFDatatype> getAll() {
		List<SDFDatatype> ret = new ArrayList<>();
		ret.add(SDFObjectMapDatatype.OBJECTMAP);
		ret.add(SDFObjectMapDatatype.LIST_OBJECTMAP);
		return ret;
	}
}
