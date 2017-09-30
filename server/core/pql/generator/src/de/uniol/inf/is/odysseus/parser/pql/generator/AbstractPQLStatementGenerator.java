package de.uniol.inf.is.odysseus.parser.pql.generator;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

public abstract class AbstractPQLStatementGenerator<T extends ILogicalOperator> implements IPQLStatementGenerator<T> {

	private static final Logger LOG = LoggerFactory.getLogger(AbstractPQLStatementGenerator.class);

	@Override
	public String generateStatement(T operator, Map<T, String> otherOperatorNames) {
		try {
			StringBuilder sb = new StringBuilder();

			sb.append(generateStatementStart(operator, otherOperatorNames.get(operator)));
			sb.append("(");

			String parameterString = generateParameters(operator);
			if (!Strings.isNullOrEmpty(parameterString)) {
				parameterString = parameterString.trim();
				sb.append("{").append(parameterString).append("}");
			}

			if (!operator.getSubscribedToSource().isEmpty()) {
				if (!Strings.isNullOrEmpty(parameterString)) {
					sb.append(",");
				}
				sb.append(generateSubscriptions(operator, otherOperatorNames));
			}
			sb.append(")");

			return reformat(operator, sb.toString());

		} catch (IllegalArgumentException ex) {
			LOG.error("Could not create pql-statement for logical operator {}", operator, ex);
			return "";
		}
	}
	
	protected abstract String generateParameters(T operator);

	protected String reformat(T operator, String completeStatement) {
		return completeStatement;
	}

	protected String generateStatementStart(T operator, String ownName) {
		StringBuilder sb = new StringBuilder();
		sb.append(ownName);
		sb.append(" = ");
		sb.append(determineOperatorKeyword(operator));
		return sb.toString();
	}


	protected String generateSubscriptions(T operator, Map<T, String> otherOperatorNames) {
		StringBuilder sb = new StringBuilder();
		LogicalSubscription[] subscriptions = operator.getSubscribedToSource().toArray(new LogicalSubscription[0]);
		for (int i = 0; i < subscriptions.length; i++) {
			ILogicalOperator target = subscriptions[i].getSource();
			sb.append(subscriptions[i].getSourceOutPort()).append(":");
			sb.append(otherOperatorNames.get(target));
			if (i < subscriptions.length - 1) {
				sb.append(",");
			}
		}
		return sb.toString();
	}

	protected final Object determineOperatorKeyword(T operator) {
		LogicalOperator annotation = operator.getClass().getAnnotation(LogicalOperator.class);
		if (annotation == null || Strings.isNullOrEmpty(annotation.name())) {
			String className = operator.getClass().getSimpleName();
			return className.substring(0, className.length() - 2);
		}
		return annotation.name();
	}


}
