package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.server.util.Constants;

public class SenderAOPQLGenerator {

	static String generateSenderAOStatement(SenderAO operator, Map<ILogicalOperator, String> names) {
		StringBuilder sb = new StringBuilder();

		sb.append(names.get(operator)).append("=SENDER({");
		sb.append("sink='").append(operator.getSinkname()).append("'");
		appendIfNeeded(sb, "wrapper", determineWrapper(operator));
		appendIfNeeded(sb, "transport", operator.getTransportHandler());
		appendIfNeeded(sb, "protocol", operator.getProtocolHandler());
		appendIfNeeded(sb, "datahandler", operator.getDataHandler());

		sb.append(", options=").append(convertOptionsMap(operator.getOptionsMap()));
		sb.append("}");

		if (!operator.getSubscribedToSource().isEmpty()) {
			sb.append(",");
			appendSubscriptionNames(sb, operator, names);
		}
		sb.append(")");

		return sb.toString();

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

	private static void appendIfNeeded(StringBuilder sb, String key, String text) {
		if (!Strings.isNullOrEmpty(text)) {
			sb.append(",");
			sb.append(key).append("='").append(text).append("'");
		}
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

	private static String determineWrapper(SenderAO operator) {
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
