package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.OptionMap;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.SenderAO;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.parser.pql.generator.AbstractPQLStatementGenerator;

public class SenderAOPQLStatementGenerator extends AbstractPQLStatementGenerator<SenderAO> {

	@Override
	public Class<SenderAO> getOperatorClass() {
		return SenderAO.class;
	}
	
	@Override
	protected String generateParameters(SenderAO operator) {
		StringBuilder sb = new StringBuilder();

		sb.append("sink='").append(operator.getSinkname()).append("'");
		appendIfNeeded(sb, "wrapper", determineWrapper(operator));
		appendIfNeeded(sb, "transport", operator.getTransportHandler());
		appendIfNeeded(sb, "protocol", operator.getProtocolHandler());
		appendIfNeeded(sb, "datahandler", operator.getDataHandler());

		sb.append(", options=").append(convertOptionsMap(operator.getOptionsMap()));
		sb.append(", name='").append(operator.getName()).append("'");
		sb.append(", writemetadata=").append(operator.isWriteMetaData());

		return sb.toString();
	}

	private static void appendIfNeeded(StringBuilder sb, String key, String text) {
		if (!Strings.isNullOrEmpty(text)) {
			sb.append(",");
			sb.append(key).append("='").append(text).append("'");
		}
	}

	private static Object convertOptionsMap(OptionMap options) {
		StringBuilder sb = new StringBuilder();
		Map<String, Object> optionsMap = options.getOptions();
		sb.append("[");

		String[] keys = optionsMap.keySet().toArray(new String[0]);
		for (int i = 0; i < keys.length; i++) {

			sb.append("[");

			Object key = keys[i];
			Object value = optionsMap.get(key);
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
		} 

		if (operator.getTransportHandler().equals("NonBlockingTcp")) {
			return Constants.GENERIC_PUSH;
		} 
		
		return Constants.GENERIC_PULL;
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
