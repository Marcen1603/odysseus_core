package de.uniol.inf.is.odysseus.sports.sportsql.parser.buildhelper;

import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.physicaloperator.ISource;
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFExpression;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.MapAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SelectAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.NamedExpressionItem;
import de.uniol.inf.is.odysseus.core.server.physicaloperator.SelectPO;
import de.uniol.inf.is.odysseus.core.server.predicate.ComplexPredicateHelper;
import de.uniol.inf.is.odysseus.mep.MEP;
import de.uniol.inf.is.odysseus.relational.base.predicate.RelationalPredicate;

public class OperatorBuildHelper {

	public static MapAO getMapAO(List<NamedExpressionItem> expressions,
			ILogicalOperator source) {
		MapAO mapAO = new MapAO();
		mapAO.setExpressions(expressions);
		mapAO.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return mapAO;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static SelectPO getTimeSelect(int startMinute, int endMinute,
			ISource source) {
		SelectAO selectAO = new SelectAO();

		// Predicate we want to produce:
		// minute >= ${parameterTimeStart_minute} AND minute <= ${parameterTimeEnd_minute} AND second >= 0

		// 1. minute >= ${parameterTimeStart_minute}
		String firstPredicateString = "minute >= " + startMinute;
		SDFExpression firstPredicateExpression = new SDFExpression(
				firstPredicateString, MEP.getInstance());
		RelationalPredicate firstPredicate = new RelationalPredicate(
				firstPredicateExpression);

		// 2. minute <= ${parameterTimeEnd_minute}
		String secondPredicateString = "minute <= " + endMinute;
		SDFExpression secondPredicateExpression = new SDFExpression(
				secondPredicateString, MEP.getInstance());
		RelationalPredicate secondPredicate = new RelationalPredicate(
				secondPredicateExpression);

		// 3. second >= 0
		String thirdPredicateString = "second >= 0";
		SDFExpression thirdPredicateExpression = new SDFExpression(
				thirdPredicateString, MEP.getInstance());
		RelationalPredicate thirdPredicate = new RelationalPredicate(
				thirdPredicateExpression);

		IPredicate firstAndPrdicate = ComplexPredicateHelper
				.createAndPredicate(firstPredicate, secondPredicate);
		IPredicate fullAndPredicate = ComplexPredicateHelper
				.createAndPredicate(firstAndPrdicate, thirdPredicate);

		selectAO.setPredicate(fullAndPredicate);
		SelectPO<?> selectPO = new SelectPO(selectAO.getPredicate());
		selectPO.subscribeToSource(source, 0, 0, source.getOutputSchema());
		return selectPO;
	}

}
