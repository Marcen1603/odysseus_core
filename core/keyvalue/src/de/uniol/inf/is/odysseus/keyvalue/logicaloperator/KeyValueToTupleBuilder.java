package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.IOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.ListParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

public class KeyValueToTupleBuilder extends AbstractOperatorBuilder {

	private static final long serialVersionUID = -490395475058441123L;

	private final ListParameter<SDFAttribute> outputschema = new ListParameter<SDFAttribute>(
			"SCHEMA", REQUIREMENT.MANDATORY, new CreateSDFAttributeParameter(
					"ATTRIBUTE", REQUIREMENT.MANDATORY, getDataDictionary()));
	
	private final StringParameter type = new StringParameter(
			"type", REQUIREMENT.MANDATORY);

	public KeyValueToTupleBuilder() {
		super("KeyValueToTuple", 0, 0);
		addParameters(outputschema, type);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		KeyValueToTupleAO ao = new KeyValueToTupleAO();
		SDFSchema schema = new SDFSchema(this.type.getValue(), this.outputschema.getValue());
		ao.setOutputSchema(schema);
		return ao;
	}
	
	@Override
	public IOperatorBuilder cleanCopy() {
		return new KeyValueToTupleBuilder();
	}

	@Override
	protected boolean internalValidation() {
		// Currently not much to do
		return true;
	}


}
