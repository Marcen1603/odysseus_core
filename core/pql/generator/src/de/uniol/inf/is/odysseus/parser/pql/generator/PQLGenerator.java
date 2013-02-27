package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
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
import de.uniol.inf.is.odysseus.core.predicate.IPredicate;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.sourcedescription.sdf.schema.SDFExpression;

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

		try {
			Map<String, String> parameterMap = determineParameters(operator);

			StringBuilder sb = new StringBuilder();

			sb.append(names.get(operator));
			sb.append("=");
			sb.append(determineOperatorKeyword(operator));
			sb.append("(");
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
			if (!operator.getSubscribedToSource().isEmpty()) {
				sb.append(",");
				LogicalSubscription[] subscriptions = operator.getSubscribedToSource().toArray(new LogicalSubscription[0]);
				for (int i = 0; i < subscriptions.length; i++) {
					ILogicalOperator target = subscriptions[i].getTarget();
					sb.append(names.get(target));
					if (i < subscriptions.length - 1) {
						sb.append(",");
					}
				}
			}
			sb.append(")");

			return sb.toString();

		} catch (IntrospectionException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
			LOG.error("Could not create pql-statement for logical operator {}", operator, ex);
			return "";
		}
	}

	private static Map<String, String> determineParameters(ILogicalOperator operator) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		Map<String, String> parameterMap = Maps.newHashMap();

		BeanInfo beanInfo = Introspector.getBeanInfo(operator.getClass(), Object.class);
		for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
			Method writeMethod = descriptor.getWriteMethod();

			if (writeMethod != null && writeMethod.isAnnotationPresent(Parameter.class)) {
				Parameter parameter = writeMethod.getAnnotation(Parameter.class);

				Method readMethod = descriptor.getReadMethod();

				if (readMethod != null) {
					@SuppressWarnings(value = { "all" })
					String valueAsString = convertSingleValue(readMethod.invoke(operator, null));

					putIfValid(parameterMap, parameter.name().equals("") ? descriptor.getName() : parameter.name(), valueAsString);
				} else {
					LOG.warn("Could not get readMethod for parameter {} of operator-type {}", descriptor.getName(), operator.getClass());
				}
			}

		}
		return parameterMap;
	}

	private static Object determineOperatorKeyword(ILogicalOperator operator) {
		LogicalOperator annotation = operator.getClass().getAnnotation(LogicalOperator.class);
		if (annotation == null || Strings.isNullOrEmpty(annotation.name())) {
			String className = operator.getClass().getSimpleName();
			return className.substring(0, className.length() - 2);
		}
		return annotation.name();
	}

	private static String convertSingleValue(Object value) {
		if (value == null) {
			return null;
		}

		if (value instanceof List) {
			List<?> list = (List<?>) value;
			return convertList(list);
		} else if (value instanceof Map) {
			Map<?, ?> map = (Map<?, ?>) value;
			return convertMap(map);
		} else if (value instanceof String) {
			return "'" + ((String) value) + "'";
		} else if (value instanceof Boolean) {
			return "'" + String.valueOf((Boolean) value) + "'";
		} else if (value instanceof IPredicate) {
			return "RelationalPredicate('" + value.toString() + "')";
		} else if (value instanceof SDFExpression) {
			return "'" + value.toString() + "'";
		} else {
			return value != null ? value.toString() : null;
		}
	}

	private static void putIfValid(Map<String, String> parameterMap, String parameter, String text) {
		if (text == null || text.length() == 0) {
			return;
		}
		parameterMap.put(parameter, text);
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
		if (!list.contains(currentOperator)) {
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

	private static String convertList(List<?> list) {
		if (list == null || list.isEmpty()) {
			return "[]";
		}
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < list.size(); i++) {
			Object element = list.get(i);
			sb.append(convertSingleValue(element));
			if (i < list.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String convertMap(Map<?, ?> map) {
		if (map == null || map.isEmpty()) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		Object[] keys = map.keySet().toArray();
		for (int i = 0; i < keys.length; i++) {
			Object key = keys[i];
			Object value = map.get(key);
			sb.append(convertSingleValue(key)).append("=").append(convertSingleValue(value));
			if (i < keys.length - 1) {
				sb.append(",");
			}
		}

		sb.append("]");
		return sb.toString();
	}

}
