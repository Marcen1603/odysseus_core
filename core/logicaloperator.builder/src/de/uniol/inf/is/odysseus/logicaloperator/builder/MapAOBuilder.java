package de.uniol.inf.is.odysseus.logicaloperator.builder;

import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.logicaloperator.builder.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class MapAOBuilder extends AbstractOperatorBuilder {

	private static final String EXPRESSIONS = "EXPRESSIONS";

	private ListParameter<SDFExpression> expressions = new ListParameter<SDFExpression>(
			EXPRESSIONS, REQUIREMENT.MANDATORY, new SDFExpressionParameter(
					"map expression", REQUIREMENT.MANDATORY));

	public MapAOBuilder() {
		super(1, 1);
		setParameters(expressions);
	}

	@Override
	protected ILogicalOperator createOperatorInternal() {
		List<SDFExpression> expressionList = expressions.getValue();
		MapAO mapAO = new MapAO();
		mapAO.setExpressions(expressionList);

		return mapAO;
	}

	@Override
	protected boolean internalValidation() {
		return true;
	}

}
