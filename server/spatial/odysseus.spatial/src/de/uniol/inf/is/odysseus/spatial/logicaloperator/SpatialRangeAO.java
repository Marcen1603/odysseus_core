package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.BinaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SPATIALRANGE", doc = "Puts out all objects in the given range around the given spatial object in the given data structure.", category = {
		LogicalOperatorCategory.SPATIAL })
public class SpatialRangeAO extends BinaryLogicalOp {

	private static final long serialVersionUID = 1771804202859128706L;

	private String dataStructureName;
	private String idAttribute;
	private String geometryAttribute;
	private double range;

	public SpatialRangeAO() {
		super();
	}

	public SpatialRangeAO(SpatialRangeAO ao) {
		super(ao);
		this.dataStructureName = ao.getDataStructureName();
		this.range = ao.getRange();
		this.idAttribute = ao.getIdAttribute();
		this.geometryAttribute = ao.getGeometryAttribute();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SpatialRangeAO(this);
	}

	public String getDataStructureName() {
		return dataStructureName;
	}

	@Parameter(name = "dataStructureName", optional = false, type = StringParameter.class, isList = false, doc = "The name of the data structure to search in.")
	public void setDataStructureName(String dataStructureName) {
		this.dataStructureName = dataStructureName;
	}

	public double getRange() {
		return range;
	}

	@Parameter(name = "range", optional = false, type = IntegerParameter.class, isList = false, doc = "Range around the given spatial object in meters.")
	public void setRange(double range) {
		this.range = range;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	@Parameter(name = "idAttribute", optional = true, type = StringParameter.class, isList = false, doc = "EXPERIMENTAL! Name of the attribute with the id of the object.")
	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	public String getGeometryAttribute() {
		return geometryAttribute;
	}

	@Parameter(name = "geometryAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the geometry of the object.F")
	public void setGeometryAttribute(String geometryAttribute) {
		this.geometryAttribute = geometryAttribute;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// Put out the original tuple with an extra field that contains a list
		// with the elements in the range

		// Use old schema from the data structure
		SDFSchema inputSchema = getInputSchema(0);
		List<SDFAttribute> attributes = new ArrayList<>(inputSchema.getAttributes());

		// Add the list of spatial points
		SDFAttribute attr = new SDFAttribute("neighbors", "neighbors", SDFDatatype.LIST_TUPLE);
		attributes.add(attr);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
