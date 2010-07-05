package de.uniol.inf.is.odysseus.parser.pql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.MapAO;
import de.uniol.inf.is.odysseus.parser.pql.IParameter.REQUIREMENT;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFExpression;

public class MapAOBuilder extends AbstractOperatorBuilder {

	private static final String EXPRESSIONS = "EXPRESSIONS";

	private ListParameter<SDFExpression> expressions = new ListParameter<SDFExpression>(
			EXPRESSIONS, REQUIREMENT.MANDATORY, new SDFExpressionParameter("",
					REQUIREMENT.MANDATORY));

	public MapAOBuilder() {
		setParameters(expressions);
	}

	@Override
	protected ILogicalOperator createOperator(List<ILogicalOperator> inputOps) {
		checkInputSize(inputOps, 1);
		List<SDFExpression> expressionList = expressions.getValue();
		MapAO mapAO = new MapAO();
		mapAO.setExpressions(expressionList);

		return mapAO;
	}

}
