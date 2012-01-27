package de.uniol.inf.is.odysseus.logicaloperator.intervalapproach;

import de.uniol.inf.is.odysseus.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatype;

@LogicalOperator(name = "TimestampToPayload", minInputPorts = 1, maxInputPorts = 1)
public class TimestampToPayloadAO extends AbstractLogicalOperator {

	private static final long serialVersionUID = 7506659021418301530L;

	public TimestampToPayloadAO() {
	};

	public TimestampToPayloadAO(TimestampToPayloadAO timestampToPayloadAO) {
		super(timestampToPayloadAO);
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		SDFAttributeList outputSchema = null;
		if (getInputSchema(0) != null) {
			outputSchema = new SDFAttributeList(getInputSchema(0).getURI(),
					getInputSchema(0));
		}else{
			outputSchema = new SDFAttributeList("");
		}

		SDFAttribute starttimeStamp = new SDFAttribute(null,
				"meta_valid_start", SDFDatatype.TIMESTAMP);
		outputSchema.add(starttimeStamp);
		SDFAttribute endtimeStamp = new SDFAttribute(null, "meta_valid_end",
				SDFDatatype.TIMESTAMP);
		outputSchema.add(endtimeStamp);
		return outputSchema;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new TimestampToPayloadAO(this);
	}

}
