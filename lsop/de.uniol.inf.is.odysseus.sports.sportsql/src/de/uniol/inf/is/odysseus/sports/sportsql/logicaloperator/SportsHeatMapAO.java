package de.uniol.inf.is.odysseus.sports.sportsql.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(name = "HEATMAP", minInputPorts = 1, maxInputPorts = 1, doc = "This operator creates a value array for heatmaps", category = { LogicalOperatorCategory.ADVANCED })
public class SportsHeatMapAO extends AbstractLogicalOperator {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5048701632775540705L;

	public SportsHeatMapAO() {

	}

	public SportsHeatMapAO(SportsHeatMapAO heatmap) {
		// TODO Auto-generated constructor stub
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new SportsHeatMapAO(this);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		// add detected sentiment to schema
		SDFSchema inSchema = getInputSchema(0);
		SDFAttribute map = new SDFAttribute(null, "map", SDFDatatype.STRING, null, null, null);
		SDFAttribute map2 = new SDFAttribute(null, "map2", SDFDatatype.STRING, null, null, null);
		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		outputAttributes.addAll(inSchema.getAttributes());
		outputAttributes.add(map);
		outputAttributes.add(map2);
		SDFSchema outSchema = new SDFSchema(inSchema, outputAttributes);
		setOutputSchema(outSchema);

		return getOutputSchema();
	}

}
