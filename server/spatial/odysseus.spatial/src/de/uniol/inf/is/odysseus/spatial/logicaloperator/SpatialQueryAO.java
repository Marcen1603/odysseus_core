package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import com.vividsolutions.jts.geom.Point;

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
import de.uniol.inf.is.odysseus.spatial.logicaloperator.builder.PointParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "SpatialQuery", doc = "Queries a spatial data structure. Output are all tuples which are in the given region.", category = {
		LogicalOperatorCategory.SPATIAL })
public class SpatialQueryAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 6221501646713469823L;

	private String dataStructureName;
	private int geometryPosition;
	private List<Point> points;

	public SpatialQueryAO() {
		super();
	}

	public SpatialQueryAO(SpatialQueryAO ao) {
		super(ao);
		this.dataStructureName = ao.getDataStructureName();
		this.geometryPosition = ao.getGeometryPosition();
		this.points = ao.getPolygonPoints();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SpatialQueryAO(this);
	}

	@Parameter(name = "dataStructureName", optional = false, type = StringParameter.class, isList = false, doc = "The name of the data structure to search in.")
	public void setDataStructureName(String dataStructureName) {
		this.dataStructureName = dataStructureName;
	}

	public String getDataStructureName() {
		return this.dataStructureName;
	}

	@Parameter(name = "geometryPosition", optional = false, type = IntegerParameter.class, isList = false, doc = "The position in the tuple where the geometry is for which you want to have the neighbors.")
	public void setGeometryPosition(int geometryPosition) {
		this.geometryPosition = geometryPosition;
	}

	public int getGeometryPosition() {
		return geometryPosition;
	}

	// TODO It should be possible to use a datastream as input for the polygon
	@Parameter(name = "polygonCoordinates", optional = false, type = PointParameter.class, isList = true, doc = "")
	public void setPolygonPoints(List<Point> points) {
		this.points = points;
	}

	public List<Point> getPolygonPoints() {
		return points;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		// Put out the original tuple with an extra field that contains a list
		// with the kNN

		// Use old schema
		SDFSchema inputSchema = getInputSchema();
		List<SDFAttribute> attributes = new ArrayList<>(inputSchema.getAttributes());

		// Add the list of spatial points
		SDFAttribute attr = new SDFAttribute("results", "results", SDFDatatype.LIST_TUPLE);
		attributes.add(attr);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
