package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IntegerParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SpatialStore", doc = "Fills a spatio temporal data structure", category = {
		LogicalOperatorCategory.BASE })
public class STDataStructureAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -6941816005065513833L;

	private String dataStructureName;
	private String dataStructureType;
	private int geometryPosition;

	public STDataStructureAO() {
		super();
	}

	public STDataStructureAO(STDataStructureAO ao) {
		super(ao);
		setDataStructureName(ao.dataStructureName);
		setDataStructureType(ao.dataStructureType);
		setGeometryPosition(ao.geometryPosition);
	}

	@Override
	public STDataStructureAO clone() {
		return new STDataStructureAO(this);
	}

	@Parameter(name = "dataStructureName", optional = false, type = StringParameter.class, isList = false, doc = "The name of the data structure to create. This name can be used to access it.")
	public void setDataStructureName(String dataStructureName) {
		this.dataStructureName = dataStructureName;
	}

	public String getDataStructureName() {
		return this.dataStructureName;
	}

	@Parameter(name = "dataStructureType", optional = false, type = StringParameter.class, isList = false, doc = "The type of the data structure to create.")
	public void setDataStructureType(String dataStructureType) {
		this.dataStructureType = dataStructureType;
	}

	public String getDataStructureType() {
		return this.dataStructureType;
	}
	
	public int getGeometryPosition() {
		return geometryPosition;
	}

	@Parameter(name = "geometryPosition", optional = false, type = IntegerParameter.class, isList = false, doc = "The position in the incoming tuples where the geometry attribute is.")
	public void setGeometryPosition(int geometryPosition) {
		this.geometryPosition = geometryPosition;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("dataStructureName", "dataStructureName", SDFDatatype.STRING);
		attributeList.add(attr);
		
		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("spatialschema", (Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		return schema;
	}
}
