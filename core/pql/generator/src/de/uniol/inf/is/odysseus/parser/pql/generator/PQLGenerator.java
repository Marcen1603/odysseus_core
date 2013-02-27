package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TopAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

public class PQLGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(PQLGenerator.class);
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

			String statement = generateStatement(operator, names);
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

	private static String generateStatement(ILogicalOperator operator, Map<ILogicalOperator, String> names) {
		if (operator instanceof AccessAO) {
			return AccessAOPQLGenerator.generateAccessAOStatement((AccessAO) operator, names.get(operator));
		} 

		Map<String, String> parameterMap = removeNullValues(operator.getParameterInfos());

		try {
			StringBuilder sb = new StringBuilder();

			appendOperatorName(sb, operator, names.get(operator));
			sb.append("(");
			
			appendParameters(sb, parameterMap);
			if (!operator.getSubscribedToSource().isEmpty()) {
				if(!parameterMap.isEmpty()) {
					sb.append(",");
				}
				appendSubscriptionNames(sb, operator, names);
			}
			sb.append(")");

			return sb.toString();

		} catch (IllegalArgumentException ex) {
			LOG.error("Could not create pql-statement for logical operator {}", operator, ex);
			return "";
		}
	}

	private static void appendSubscriptionNames(StringBuilder sb, ILogicalOperator operator, Map<ILogicalOperator, String> names) {
		LogicalSubscription[] subscriptions = operator.getSubscribedToSource().toArray(new LogicalSubscription[0]);
		for (int i = 0; i < subscriptions.length; i++) {
			ILogicalOperator target = subscriptions[i].getTarget();
			sb.append(names.get(target));
			if (i < subscriptions.length - 1) {
				sb.append(",");
			}
		}
	}

	private static void appendOperatorName(StringBuilder sb, ILogicalOperator operator, String name) {
		sb.append(name);
		sb.append("=");
		sb.append(determineOperatorKeyword(operator));
	}

	private static void appendParameters(StringBuilder sb, Map<String, String> parameterMap) {
		if (!parameterMap.isEmpty()) {
			sb.append("{");
			String[] keys = parameterMap.keySet().toArray(new String[0]);
			for (int i = 0; i < keys.length; i++) {
				sb.append(keys[i]).append("=").append(parameterMap.get(keys[i]));
				if (i < keys.length - 1) {
					sb.append(",");
				}
			}
			sb.append("}");
		}
	}

	private static Map<String, String> removeNullValues(Map<String, String> parameterInfos) {
		Map<String, String> result = Maps.newHashMap();
		for( String key : parameterInfos.keySet()) {
			if( parameterInfos.get(key) != null ) {
				result.put(key, parameterInfos.get(key));
			}
		}
		return result;
	}

	private static Object determineOperatorKeyword(ILogicalOperator operator) {
		LogicalOperator annotation = operator.getClass().getAnnotation(LogicalOperator.class);
		if (annotation == null || Strings.isNullOrEmpty(annotation.name())) {
			String className = operator.getClass().getSimpleName();
			return className.substring(0, className.length() - 2);
		}
		return annotation.name();
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
