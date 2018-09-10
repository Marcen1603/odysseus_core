package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import java.util.List;
import java.util.Map;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.CSVFileSource;

public class CSVFileSourcePQLStatementGenerator extends StandardPQLStatementGenerator<CSVFileSource> {

	@Override
	public Class<CSVFileSource> getOperatorClass() {
		return CSVFileSource.class;
	}
	
	@Override
	protected Map<String, String> determineParameterMap(CSVFileSource operator) {
		Map<String, String> defaultParameterMap = super.determineParameterMap(operator);
		
		defaultParameterMap.put("SCHEMA", schemaToString(operator.getAccessAOName().getResourceName(), operator.getOutputSchema().getAttributes()));
		
		return defaultParameterMap;
	}

	private static String schemaToString(String sourceName, List<SDFAttribute> outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("[");
			if( !Strings.isNullOrEmpty(sourceName)) {
				sb.append("'").append(sourceName).append("',");
			}
			sb.append("'").append(attribute.getAttributeName());
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
}
