package de.uniol.inf.is.odysseus.query.transformation.java.utils;

import de.uniol.inf.is.odysseus.core.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.TimestampAO;

public class Utils {
	
	
	public static TimestampAO createTimestampAO(ILogicalOperator operator,
			String dateFormat) {
		TimestampAO timestampAO = new TimestampAO();
		timestampAO.setDateFormat(dateFormat);
		
		timestampAO.setOutputSchema(operator.getOutputSchema());
		if (operator.getOutputSchema() != null) {

			for (SDFAttribute attr : operator.getOutputSchema()) {
				if (SDFDatatype.START_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.START_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setStartTimestamp(attr);
				}

				if (SDFDatatype.END_TIMESTAMP.toString().equalsIgnoreCase(
						attr.getDatatype().getURI())
						|| SDFDatatype.END_TIMESTAMP_STRING.toString()
								.equalsIgnoreCase(attr.getDatatype().getURI())) {
					timestampAO.setEndTimestamp(attr);
				}

			}
		}
		timestampAO.subscribeTo(operator, operator.getOutputSchema());
		timestampAO.setName(timestampAO.getStandardName());
		return timestampAO;
	}

}
