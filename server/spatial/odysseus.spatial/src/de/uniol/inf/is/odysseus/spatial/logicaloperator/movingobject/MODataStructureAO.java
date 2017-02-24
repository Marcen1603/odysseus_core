package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

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
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.DoubleParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MovingObjectStore", doc = "Fills a moving object data structure", category = {
		LogicalOperatorCategory.BASE })
public class MODataStructureAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 4642138146159983902L;

	private String dataStructureName;
	private String dataStructureType;
	private String geometryAttribute;
	private String idAttribute;
	private double distancePerMovingObject;

	public MODataStructureAO() {
		super();
	}

	public MODataStructureAO(MODataStructureAO ao) {
		super(ao);
		setDataStructureName(ao.getDataStructureName());
		setDataStructureType(ao.getDataStructureType());
		setGeometryAttribute(ao.getGeometryAttribute());
		setIdAttribute(ao.getIdAttribute());
		setDistancePerMovingObject(ao.getDistancePerMovingObject());
	}

	@Override
	public MODataStructureAO clone() {
		return new MODataStructureAO(this);
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

	public String getGeometryAttribute() {
		return geometryAttribute;
	}

	@Parameter(name = "geometryAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the geometry of the moving object.")
	public void setGeometryAttribute(String geometryAttribute) {
		this.geometryAttribute = geometryAttribute;
	}

	public String getIdAttribute() {
		return idAttribute;
	}

	@Parameter(name = "idAttribute", optional = false, type = StringParameter.class, isList = false, doc = "Name of the attribute with the id of the moving object.")
	public void setIdAttribute(String idAttribute) {
		this.idAttribute = idAttribute;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("dataStructureName", "dataStructureName", SDFDatatype.STRING);
		attributeList.add(attr);

		@SuppressWarnings("unchecked")
		SDFSchema schema = SDFSchemaFactory.createNewSchema("spatialschema",
				(Class<? extends IStreamObject<?>>) Tuple.class, attributeList, getInputSchema());
		return schema;
	}

	public double getDistancePerMovingObject() {
		return distancePerMovingObject;
	}

	@Parameter(name = "distancePerMO", optional = false, type = DoubleParameter.class, isList = false, doc = "The distance to store per Moving Object in meters.")
	public void setDistancePerMovingObject(double distancePerMovingObject) {
		this.distancePerMovingObject = distancePerMovingObject;
	}

}
