package de.uniol.inf.is.odysseus.keyvalue.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.AbstractOperatorBuilder;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.BooleanParameter;
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

	private final BooleanParameter keepInput = new BooleanParameter(
			"keepInput", REQUIREMENT.OPTIONAL);
	
	public KeyValueToTupleBuilder() {
		super("KeyValueToTuple", 1, 1);
		addParameters(outputschema, type, keepInput);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		KeyValueToTupleAO ao = new KeyValueToTupleAO();
		SDFSchema schema = new SDFSchema(this.type.getValue(), this.outputschema.getValue());
		ao.setOutputSchema(schema);
		if (keepInput.hasValue()){
			ao.setKeepInputObject(keepInput.getValue());
		}
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
