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
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.GetParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

public class PQLGenerator {

	private static final Logger LOG = LoggerFactory.getLogger(PQLGenerator.class);
	private static final String NAME_SEPARATOR = "_";

	public String generatePQLStatement(ILogicalOperator startOperator) {
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
			return generateAccessAOStatement((AccessAO) operator, names.get(operator));
		}

		try {
			Map<String, String> parameterMap = Maps.newHashMap();
			BeanInfo beanInfo = Introspector.getBeanInfo(operator.getClass(), Object.class);
			for (PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
				Method readMethod = descriptor.getReadMethod();

				if (readMethod != null && readMethod.isAnnotationPresent(GetParameter.class)) {
					GetParameter parameter = readMethod.getAnnotation(GetParameter.class);
					@SuppressWarnings(value = { "all" })
					String valueAsString = convertSingleValue(readMethod.invoke(operator, null));
					putIfNeeded(parameterMap, parameter, valueAsString);
				}

			}

			StringBuilder sb = new StringBuilder();

			sb.append(names.get(operator));
			sb.append("=");
			sb.append(determineOperatorKeyword(operator));
			sb.append("(");
			if (!parameterMap.isEmpty()) {
				sb.append("{");
				String[] keys = parameterMap.keySet().toArray(new String[0]);
				for (int i = 0; i < keys.length; i++) {
					sb.append(keys[i].substring(3)).append("=").append(parameterMap.get(keys[i]));
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
					sb.append(names.get(subscriptions[i].getTarget()));
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

	private static String generateAccessAOStatement(AccessAO operator, String name) {
		StringBuilder sb = new StringBuilder();
		TimestampAO timestampAO = determineTimestampAO(operator);

		sb.append(name).append("=ACCESS({");
		sb.append("source='").append(operator.getSourcename()).append("'");
		appendIfNeeded(sb, "wrapper", determineWrapper(operator));
		appendIfNeeded(sb, "transport", operator.getTransportHandler());
		appendIfNeeded(sb, "protocol", operator.getProtocolHandler());
		appendIfNeeded(sb, "datahandler", operator.getDataHandler());
		appendIfNeeded(sb, "objecthandler", operator.getObjectHandler());
		if (timestampAO != null) {
			appendIfNeeded(sb, "dateformat", timestampAO.getDateFormat());
		}
		sb.append(", options=").append(convertOptionsMap(operator.getOptionsMap()));
		sb.append(", schema=").append(convertSchema(operator.getOutputSchema()));
		List<String> inputSchema = operator.getInputSchema();
		if (inputSchema != null && !inputSchema.isEmpty()) {
			sb.append(", inputschema=").append(convertInputSchema(inputSchema));
		}
		sb.append("})");
		return sb.toString();
	}

	private static Object convertOptionsMap(Map<String, String> optionsMap) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		
		String[] keys = optionsMap.keySet().toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {
			
			sb.append("[");
			
			Object key = keys[i];
			String value = optionsMap.get(key);
			sb.append(convertValue(key)).append(",").append(convertValue(value));
			
			sb.append("]");
			
			if (i < keys.length - 1) {
				sb.append(",");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	private static String determineWrapper(AccessAO operator) {
		if (!Strings.isNullOrEmpty(operator.getWrapper())) {
			return operator.getWrapper();
		} else {
			if (operator.getTransportHandler().equals("NonBlockingTcp")) {
				return Constants.GENERIC_PUSH;
			} else {
				return Constants.GENERIC_PULL;
			}
		}
	}

	private static String convertInputSchema(List<String> inputSchema) {
		StringBuilder sb = new StringBuilder();
		sb.append("[");
		for (int i = 0; i < inputSchema.size(); i++) {
			sb.append(inputSchema.get(i));
			if (i < inputSchema.size() - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static TimestampAO determineTimestampAO(AccessAO operator) {
		try {
			return (TimestampAO) operator.getSubscriptions().iterator().next().getTarget();
		} catch (Throwable t) {
			return null;
		}
	}

	private static String convertSchema(SDFSchema outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		StringBuilder sb = new StringBuilder();
		sb.append("[");
		SDFAttribute[] attributes = outputSchema.getAttributes().toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			SDFAttribute attribute = attributes[i];
			sb.append("[");
			sb.append(convertValue(attribute.getAttributeName()));
			sb.append(",");
			sb.append(convertValue(attribute.getDatatype().getURI()));
			sb.append("]");
			if (i < attributes.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static void appendIfNeeded(StringBuilder sb, String key, String text) {
		if (!Strings.isNullOrEmpty(text)) {
			sb.append(",");
			sb.append(key).append("='").append(text).append("'");
		}
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
		} else {
			return value != null ? value.toString() : null;
		}
	}

	private static void putIfNeeded(Map<String, String> parameterMap, GetParameter parameter, String text) {
		if (text == null) {
			text = "";
		}
		parameterMap.put(parameter.name(), text);
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
			sb.append(convertValue(element));
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
			sb.append(convertValue(key)).append("=").append(convertValue(value));
			if (i < keys.length - 1) {
				sb.append(",");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	private static Object convertValue(Object element) {
		if (element == null) {
			return "";
		}

		if (element instanceof String) {
			return "'" + element.toString() + "'";
		}
		return element.toString();
	}
}
