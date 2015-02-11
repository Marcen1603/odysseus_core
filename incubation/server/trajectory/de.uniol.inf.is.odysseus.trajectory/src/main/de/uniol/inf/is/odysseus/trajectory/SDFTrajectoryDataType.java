package de.uniol.inf.is.odysseus.trajectory;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;

public class SDFTrajectoryDataType extends SDFDatatype {
	
	public SDFTrajectoryDataType(String URI) {
		super(URI, true);
	}
	
	public SDFTrajectoryDataType(SDFDatatype sdfDatatype) {
		super(sdfDatatype);
	}


	public SDFTrajectoryDataType(String datatypeName, KindOfDatatype type, SDFSchema schema) {
		super(datatypeName, type, schema, true);
	}

	public SDFTrajectoryDataType(String datatypeName, KindOfDatatype type, SDFDatatype subType) {
		super(datatypeName, type, subType, true);
	}

	public static final SDFDatatype TEST = new SDFTrajectoryDataType("Test", SDFDatatype.KindOfDatatype.BEAN, 
			new SDFSchema("", Tuple.class,
					new SDFAttribute(null, "id", SDFDatatype.STRING, null, null, null),
					new SDFAttribute(null, "points", SDFTrajectoryDataType.LIST_POINTS, null, null, null)));
	
	public static final SDFDatatype LIST_TEST = new SDFTrajectoryDataType("List_TEST",
			SDFDatatype.KindOfDatatype.LIST, SDFTrajectoryDataType.TEST);
	
	public static final SDFDatatype LIST_POINTS = new SDFTrajectoryDataType("List_POINTS",
			SDFDatatype.KindOfDatatype.LIST, SDFTrajectoryDataType.STRING);
}
