package de.uniol.inf.is.odysseus.parser.pql.generator.impl;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.StreamAO;
import de.uniol.inf.is.odysseus.parser.pql.generator.AbstractPQLStatementGenerator;

public class StreamAOPQLStatementGenerator extends AbstractPQLStatementGenerator<StreamAO> {

	@Override
	public Class<StreamAO> getOperatorClass() {
		return StreamAO.class;
	}

	@Override
	protected String generateParameters(StreamAO streamAO) {

		StringBuilder sb = new StringBuilder();
		if (Strings.isNullOrEmpty(streamAO.getNode())) {
			// locally executed
			sb.append("NAME='").append(streamAO.getName()).append("',");
			sb.append("SOURCE='").append(streamAO.getStreamname().getResourceName()).append("'");
		} else {
			// distributed to another maschine
			sb.append("NAME='").append(streamAO.getName()).append("',");
			sb.append("SOURCENAME='").append(streamAO.getStreamname().getResourceName()).append("',");
			sb.append("SCHEMA=").append(schemaToString(streamAO.getOutputSchema())).append(",");
			sb.append("DATAHANDLER='tuple',");
			sb.append("NODE='").append(streamAO.getNode()).append("'");
		}

		return sb.toString();
	}

	private static String schemaToString(SDFSchema outputSchema) {
		if (outputSchema.isEmpty()) {
			return "[]";
		}

		final StringBuilder sb = new StringBuilder();
		sb.append("[");
		final SDFAttribute[] attributes = outputSchema.toArray(new SDFAttribute[0]);
		for (int i = 0; i < attributes.length; i++) {
			final SDFAttribute attribute = attributes[i];
			sb.append("[");
			if (!Strings.isNullOrEmpty(attribute.getSourceName())) {
				sb.append("'").append(attribute.getSourceName()).append("',");
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
