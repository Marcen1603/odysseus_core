package de.uniol.inf.is.odysseus.probabilistic.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

/**
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
@LogicalOperator(name = "ExistenceToPayload", minInputPorts = 1, maxInputPorts = 1)
public class ExistenceToPayloadAO extends AbstractLogicalOperator {

	public ExistenceToPayloadAO() {
	}

	public ExistenceToPayloadAO(ExistenceToPayloadAO existenceToPayloadAO) {
		super(existenceToPayloadAO);
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		SDFAttribute existence = new SDFAttribute(null, "meta_existence", SDFDatatype.DOUBLE);

		List<SDFAttribute> outputAttributes = new ArrayList<SDFAttribute>();
		String name = "";

		if (getInputSchema(0) != null) {
			outputAttributes.addAll(getInputSchema(0).getAttributes());
			name = getInputSchema(0).getURI();
		}
		outputAttributes.add(existence);

		setOutputSchema(new SDFSchema(name, outputAttributes));

		return getOutputSchema();
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ExistenceToPayloadAO(this);
	}
}
