package de.uniol.inf.is.odysseus.spatial.logicaloperator.movingobject;

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

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "MovingObjectInterpolation", doc = "Interpolates the location of moving objects.", category = {
		LogicalOperatorCategory.SPATIAL })
public class MovingObjectInterpolationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 1235279982306004707L;

	public MovingObjectInterpolationAO() {
		super();
	}
	
	public MovingObjectInterpolationAO(MovingObjectInterpolationAO ao) {
		super(ao);
	}
	
	@Override
	public AbstractLogicalOperator clone() {
		return new MovingObjectInterpolationAO(this);
	}
	
	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		
		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(pos);

		// Add the attributes
		List<SDFAttribute> attributes = new ArrayList<>();
		SDFAttribute attribute1 = new SDFAttribute("", "movingObjectId", SDFDatatype.STRING);
		SDFAttribute attribute2 = new SDFAttribute("", "latitude", SDFDatatype.STRING);
		SDFAttribute attribute3 = new SDFAttribute("", "longitude", SDFDatatype.STRING);
		SDFAttribute attribute4 = new SDFAttribute("", "speedMetersPerSecond", SDFDatatype.STRING);
		SDFAttribute attribute5 = new SDFAttribute("", "direction", SDFDatatype.STRING);
		
		attributes.add(attribute1);
		attributes.add(attribute2);
		attributes.add(attribute3);
		attributes.add(attribute4);
		attributes.add(attribute5);

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}

}
