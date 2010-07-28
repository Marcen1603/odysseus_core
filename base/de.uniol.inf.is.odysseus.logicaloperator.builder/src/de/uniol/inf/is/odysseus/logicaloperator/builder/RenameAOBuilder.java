package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.Iterator;
import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class RenameAOBuilder extends AbstractOperatorBuilder {

	private static final String ALIASES = "ALIASES";
	private ListParameter<String> aliases = new ListParameter<String>(ALIASES,
			REQUIREMENT.MANDATORY, new DirectParameter<String>("rename alias",
					REQUIREMENT.MANDATORY));

	public RenameAOBuilder() {
		super(1, 1);
		setParameters(aliases);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		List<String> names = aliases.getValue();
		ILogicalOperator inputOp = inputOperators.get(0).operator;
		SDFAttributeList inputSchema = inputOp.getOutputSchema();
		SDFAttributeList outputSchema = new SDFAttributeList();
		Iterator<SDFAttribute> it = inputSchema.iterator();
		for (String str : names) {
			// use clone, so we have a datatype etc.
			SDFAttribute attribute = it.next().clone();
			attribute.setAttributeName(str);
			attribute.setSourceName(null);
			outputSchema.add(attribute);
		}

		RenameAO renameAO = new RenameAO();
		renameAO.setOutputSchema(outputSchema);

		return renameAO;
	}

	@Override
	protected boolean internalValidation() {
		List<String> names = aliases.getValue();
		ILogicalOperator inputOp = inputOperators.get(0).operator;
		SDFAttributeList inputSchema = inputOp.getOutputSchema();
		if (inputSchema.size() != names.size()) {
			throw new IllegalArgumentException(
					"number of aliases does not match number of input attributes for rename");
		}
		return true;
	}

}
