package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

/**
 * 
 * @author Tobias Brandt
 *
 */
@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SpatialKNN", doc = "Puts out the kNN for the input spatial object from the given data structure.", category = {
		LogicalOperatorCategory.SPATIAL })
public class SpatialKNNAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -7854900334883253801L;

	private String dataStructureName;
	private int geometryPosition;
	private int k;

	public SpatialKNNAO() {
		super();
	}

	public SpatialKNNAO(SpatialKNNAO ao) {
		super(ao);
		this.dataStructureName = ao.getDataStructureName();
		this.geometryPosition = ao.getGeometryPosition();
		this.k = ao.getK();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SpatialKNNAO(this);
	}

	@Parameter(name = "dataStructureName", optional = false, type = StringParameter.class, isList = false, doc = "The name of the data structure to search in.")
	public void setDataStructureName(String dataStructureName) {
		this.dataStructureName = dataStructureName;
	}

	public String getDataStructureName() {
		return this.dataStructureName;
	}

	public int getGeometryPosition() {
		return geometryPosition;
	}

	@Parameter(name = "geometryPosition", optional = false, type = IntegerParameter.class, isList = false, doc = "The position in the tuple where the geometry is for which you want to have the neighbors.")
	public void setGeometryPosition(int geometryPosition) {
		this.geometryPosition = geometryPosition;
	}

	public int getK() {
		return k;
	}

	@Parameter(name = "k", optional = false, type = IntegerParameter.class, isList = false, doc = "The number of neighbors you want to get for the given geometry.")
	public void setK(int k) {
		this.k = k;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// Put out the original tuple with an extra field that contains a list
		// with the kNN

		// Use old schema
		SDFSchema inputSchema = getInputSchema();
		List<SDFAttribute> attributes = new ArrayList<>(inputSchema.getAttributes());

		// Add the list of spatial points
		SDFAttribute attr = new SDFAttribute("kNN", "kNN", SDFDatatype.LIST_TUPLE);
		attributes.add(attr);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
