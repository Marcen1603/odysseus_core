package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalSubscription;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;
import de.uniol.inf.is.odysseus.core.server.usermanagement.ISessionManagement;
import de.uniol.inf.is.odysseus.core.server.util.Constants;
import de.uniol.inf.is.odysseus.core.usermanagement.ISession;
import de.uniol.inf.is.odysseus.parser.pql.generator.AbstractPQLStatementGenerator;

public class AccessAOPQLStatementGenerator extends AbstractPQLStatementGenerator<AccessAO> {

	private static final Logger LOG = LoggerFactory.getLogger(AccessAOPQLStatementGenerator.class);
	private static IDataDictionary dataDictionary;
	private static ISession activeUser;

	public void bindDataDictionary(IDataDictionary dd) {
		dataDictionary = dd;

		LOG.debug("DataDictionary bound {}", dd);
	}

	public void bindSessionManagement(ISessionManagement sm) {
		activeUser = sm.loginSuperUser(null, "no");

		LOG.debug("SessionManagement bound {}", sm);
	}

	@Override
	public Class<AccessAO> getOperatorClass() {
		return AccessAO.class;
	}

	public void unbindDataDictionary(IDataDictionary dd) {
		if (dd == dataDictionary) {
			dataDictionary = null;

			LOG.debug("DataDictionary unbound {}", dd);
		}
	}

	public void unbindSessionManagement(ISessionManagement sm) {
		activeUser = null;
		LOG.debug("SessionManagement unbound {}", sm);
	}

	@Override
	protected String generateParameters(AccessAO operator) {
		operator = determineRealAccessAO(operator);
		final StringBuilder sb = new StringBuilder();
		final TimestampAO timestampAO = determineTimestampAO(operator);
		
		appendIfNeeded(sb, "wrapper", determineWrapper(operator), true);
		appendIfNeeded(sb, "transport", operator.getTransportHandler());
		appendIfNeeded(sb, "protocol", operator.getProtocolHandler());
		appendIfNeeded(sb, "datahandler", operator.getDataHandler());
		appendIfNeeded(sb, "objecthandler", operator.getObjectHandler());
		if (timestampAO != null) {
			appendIfNeeded(sb, "dateformat", timestampAO.getDateFormat());
		}
		sb.append(", options=").append(convertOptionsMap(operator.getOptionsMap()));
		sb.append(", schema=").append(convertSchema(operator.getOutputSchema()));
		final List<String> inputSchema = operator.getInputSchema();
		if (inputSchema != null && !inputSchema.isEmpty()) {
			sb.append(", inputschema=").append(convertInputSchema(inputSchema));
		}

		return sb.toString();
	}
	
	private static void appendIfNeeded(StringBuilder sb, String key, String text) {
		appendIfNeeded(sb, key, text, false);
	}
	
	private static void appendIfNeeded(StringBuilder sb, String key, String text, boolean first) {
		if (!Strings.isNullOrEmpty(text)) {
			if(!first) {
				sb.append(",");
			}
			sb.append(key).append("='").append(text).append("'");
		}
	}

	private static String convertInputSchema(List<String> inputSchema) {
		final StringBuilder sb = new StringBuilder();
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

	private static Object convertOptionsMap(Map<String, String> optionsMap) {
		final StringBuilder sb = new StringBuilder();
		sb.append("[");

		final String[] keys = optionsMap.keySet().toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {

			sb.append("[");

			final Object key = keys[i];
			final String value = optionsMap.get(key);
			sb.append(convertValue(key)).append(",").append(convertValue(value));

			sb.append("]");

			if (i < keys.length - 1) {
				sb.append(",");
			}
		}

		sb.append("]");
		return sb.toString();
	}

	private static String convertSchema(SDFSchema outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.getAttributes().toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("['");
			sb.append(attribute.getAttributeName());
			sb.append("', '");
			sb.append(attribute.getDatatype().getURI());
			sb.append("']");
			if (i < attributes.length - 1) {
				sb.append(",");
			}
		}
		sb.append("]");
		return sb.toString();
	}

	private static String convertValue(Object element) {
		if (element == null) {
			return "";
		}

		if (element instanceof String) {
			return "'" + element.toString() + "'";
		}
		return element.toString();
	}

	private static AccessAO determineAccessAO(ILogicalOperator start) {
		if (start instanceof AccessAO) {
			return (AccessAO) start;
		}

		for (final LogicalSubscription subscription : start.getSubscribedToSource()) {
			final AccessAO accessAO = determineAccessAO(subscription.getTarget());
			if (accessAO != null) {
				return accessAO;
			}
		}

		return null;
	}

	private static AccessAO determineRealAccessAO(AccessAO operator) {
//		final ILogicalOperator op = dataDictionary.getStreamForTransformation(operator.getSourcename(), activeUser);
//		return op != null ? determineAccessAO(op) : operator;
		return operator;
	}

	private static TimestampAO determineTimestampAO(AccessAO operator) {
		try {
			return (TimestampAO) operator.getSubscriptions().iterator().next().getTarget();
		} catch (final Throwable t) {
			return null;
		}
	}

	private static String determineWrapper(AccessAO operator) {
		if (!Strings.isNullOrEmpty(operator.getWrapper())) {
			return operator.getWrapper();
		}
		if (operator.getTransportHandler().equals("NonBlockingTcp")) {
			return Constants.GENERIC_PUSH;
		}
		
		return Constants.GENERIC_PULL;
	}
}
