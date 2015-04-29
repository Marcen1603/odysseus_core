package de.uniol.inf.is.odysseus.condition.conditionql.parser.impl.anomaly;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.condition.conditionql.helper.OperatorBuildHelper;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.ConditionQLQuery;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.IConditionQLParser;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.annotations.ConditionQL;
import de.uniol.inf.is.odysseus.condition.conditionql.parser.enums.ConditionAlgorithm;
import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.planmanagement.query.ILogicalQuery;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;

@ConditionQL(conditionAlgorithm = ConditionAlgorithm.VALUEAREA)
public class ValueAreaConditionQLParser implements IConditionQLParser {

	public static final String QUERY_NAME = "SimpleSensor";

	@Override
	public ILogicalQuery parse(ISession session, ConditionQLQuery conditionQL) {

		String sourceName = conditionQL.getStream().getStreamName();
		//String valueName = "temp";
		// TODO Maybe define more than one are as good values
		// TODO Maybe make it possible to define explicit bad value areas
		double minValue = 20;
		double maxValue = 25;

		List<ILogicalOperator> allOperators = new ArrayList<ILogicalOperator>();
		ILogicalOperator source = OperatorBuildHelper.createSensorSource(session, sourceName);
		allOperators.add(source);

		ILogicalOperator valueAreaAnomalyDetection = OperatorBuildHelper.createValueAreaAnomalyDetectionAO(minValue,
				maxValue, source);
		allOperators.add(valueAreaAnomalyDetection);

		return OperatorBuildHelper.finishQuery(valueAreaAnomalyDetection, allOperators, QUERY_NAME);
	}

}
