package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "CreateUpdatePredicatePunctuation", maxInputPorts = 1, minInputPorts = 1, doc = "Creates a punctuation with which a predicate can be updated if the receiving operator support it.", category = {
		LogicalOperatorCategory.PROCESSING })
public class CreateUpdatePredicatePunctuationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -3716840502527149014L;

	private String predicateTemplate;
	private List<String> targetOperatorNames;

	public CreateUpdatePredicatePunctuationAO() {
		super();
	}

	public CreateUpdatePredicatePunctuationAO(CreateUpdatePredicatePunctuationAO ao) {
		super(ao);
		this.predicateTemplate = ao.getPredicateTemplate();
		this.targetOperatorNames = ao.getTargetOperatorNames();
	}
	
	@Parameter(name = "predicateTemplate", optional = false, type = StringParameter.class, isList = false, doc = "The new predicate.")
	public void setPredicateTemplate(String newPredicate) {
		this.predicateTemplate = newPredicate;
	}
	
	public String getPredicateTemplate() {
		return this.predicateTemplate;
	}
	
	public List<String> getTargetOperatorNames() {
		return targetOperatorNames;
	}

	@Parameter(type = StringParameter.class, name = "targetOperatorNames", isList = true, optional = false, doc = "A list of operators for which these punctuations are for.")
	public void setTargetOperatorNames(List<String> targetOperatorNames) {
		this.targetOperatorNames = targetOperatorNames;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new CreateUpdatePredicatePunctuationAO(this);
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {

		// Use old schema from the input
		SDFSchema inputSchema = getInputSchema(pos);

		// We don't have any attributes
		List<SDFAttribute> attributes = new ArrayList<>();

		// Create the new schema
		SDFSchema outputSchema = SDFSchemaFactory.createNewWithAttributes(attributes, inputSchema);
		return outputSchema;
	}
	
}
