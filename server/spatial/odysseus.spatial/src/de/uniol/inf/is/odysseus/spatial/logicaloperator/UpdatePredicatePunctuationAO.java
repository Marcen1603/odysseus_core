package de.uniol.inf.is.odysseus.spatial.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(name = "CreateUpdatePredicatePunctuation", maxInputPorts = 1, minInputPorts = 1, doc = "Creates a punctuation with which a predicate can be updates of the receiving operator support it.", category = {
		LogicalOperatorCategory.PROCESSING })
public class UpdatePredicatePunctuationAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -3716840502527149014L;

	private String predicateTemplate;

	public UpdatePredicatePunctuationAO() {
		super();
	}

	public UpdatePredicatePunctuationAO(UpdatePredicatePunctuationAO ao) {
		super(ao);
		this.predicateTemplate = ao.getPredicateTemplate();
	}

	public String getPredicateTemplate() {
		return this.predicateTemplate;
	}

	@Parameter(name = "predicateTemplate", optional = false, type = StringParameter.class, isList = false, doc = "Template of the predicate.")
	public void setPredicateTemplate(String predicateTemplate) {
		this.predicateTemplate = predicateTemplate;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new UpdatePredicatePunctuationAO(this);
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
