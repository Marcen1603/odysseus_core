package de.uniol.inf.is.odysseus.query.codegenerator.operator.rule;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.mep.IExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpression;
import de.uniol.inf.is.odysseus.core.server.planmanagement.TransformationConfiguration;
import de.uniol.inf.is.odysseus.query.codegenerator.modell.QueryAnalyseInformation;

public abstract class AbstractCRelationalMapPORule<T extends MapAO> extends AbstractRule<MapAO> {

	public AbstractCRelationalMapPORule(String name) {
		super(name);
	}

	@Override
	public boolean isExecutable(MapAO logicalOperator,
			TransformationConfiguration transformationConfiguration) {
		if (logicalOperator instanceof MapAO) {
			if (((MapAO) logicalOperator).getInputSchema().getType() == Tuple.class) {
				return true;
			}
		}
		return false;
	}


	@Override
	public void analyseOperator(MapAO logicalOperator,
			QueryAnalyseInformation transformationInformation) {

		MapAO mapAO = (MapAO) logicalOperator;

		for (NamedExpression e : mapAO.getExpressions()) {

			IExpression<?> temp = e.expression.getMEPExpression();
			if (temp.isFunction()) {
				transformationInformation.addMEPFunction(temp);
			}

		}

	}
}
