package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.impl.PQLStatementGeneratorManager;

public class PQLGenerator {

	private static final String NAME_SEPARATOR = "_";

	public static String generatePQLStatement(ILogicalOperator startOperator) {
		Preconditions.checkNotNull(startOperator, "Operator for generating pql-statement must not be null!");

		List<ILogicalOperator> operators = Lists.newArrayList();
		collectOperators(startOperator, operators);

		Map<ILogicalOperator, String> names = generateOperatorNames(operators);
		List<ILogicalOperator> sourceOperators = determineSourceOperators(operators);

		List<String> pqlStatements = generateStatements(sourceOperators, names);
		return concatStatements(pqlStatements);
	}

	private static List<String> generateStatements(List<ILogicalOperator> sourceOperators, Map<ILogicalOperator, String> names) {
		List<String> statements = Lists.newArrayList();

		List<ILogicalOperator> visitedOperators = Lists.newArrayList();
		List<ILogicalOperator> operatorsToVisit = Lists.newArrayList();
		operatorsToVisit.addAll(sourceOperators);

		while (!operatorsToVisit.isEmpty()) {
			ILogicalOperator operator = operatorsToVisit.remove(0);
			
			IPQLStatementGenerator<ILogicalOperator> generator = PQLStatementGeneratorManager.getInstance().getPQLStatementGenerator(operator);
			String statement = generator.generateStatement(operator, names);
			statements.add(statement);

			visitedOperators.add(operator);
			for (LogicalSubscription subscription : operator.getSubscriptions()) {
				ILogicalOperator target = subscription.getTarget();
				if (!visitedOperators.contains(target) && areAllSourceSubscriptionsVisited(target, visitedOperators)) {
					operatorsToVisit.add(target);
				}
			}
		}

		return statements;
	}


	private static boolean areAllSourceSubscriptionsVisited(ILogicalOperator operator, List<ILogicalOperator> visitedOperators) {
		for (LogicalSubscription subscription : operator.getSubscribedToSource()) {
			ILogicalOperator target = subscription.getTarget();
			if (!visitedOperators.contains(target)) {
				return false;
			}
		}
		return true;
	}

	private static List<ILogicalOperator> determineSourceOperators(List<ILogicalOperator> operators) {
		List<ILogicalOperator> sources = Lists.newArrayList();

		for (ILogicalOperator operator : operators) {
			if (operator.getSubscribedToSource().size() == 0) {
				sources.add(operator);
			}
		}

		return sources;
	}

	private static void collectOperators(ILogicalOperator currentOperator, Collection<ILogicalOperator> list) {
		if (!list.contains(currentOperator) && !(currentOperator instanceof TopAO)) {
			list.add(currentOperator);

			for (LogicalSubscription subscription : currentOperator.getSubscriptions()) {
				collectOperators(subscription.getTarget(), list);
			}

			for (LogicalSubscription subscription : currentOperator.getSubscribedToSource()) {
				collectOperators(subscription.getTarget(), list);
			}
		}
	}

	private static Map<ILogicalOperator, String> generateOperatorNames(List<ILogicalOperator> operators) {
		Map<ILogicalOperator, String> names = Maps.newHashMap();

		for (ILogicalOperator operator : operators) {
			String opName = determineName(operator);
			String statementName = generateStatementName(names.values(), opName);

			names.put(operator, statementName);
		}

		return names;
	}

	private static String generateStatementName(Collection<String> usedNames, String opName) {
		int i = 0;
		String statementName = null;
		do {
			i++;
			statementName = opName + NAME_SEPARATOR + i;
		} while (usedNames.contains(statementName));
		return statementName;
	}

	private static String determineName(ILogicalOperator operator) {
		return operator.getClass().getSimpleName();
	}

	private static String concatStatements(List<String> pqlStatements) {
		StringBuilder sb = new StringBuilder();

		for (String pqlStatement : pqlStatements) {
			sb.append(pqlStatement).append("\n");
		}

		return sb.toString();
	}
}
