package de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "RECOGNIZEFACES", doc = "Recognizes faces of previous detected faces", category = { LogicalOperatorCategory.BASE })
public class FaceRecognizerAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 3632770189119157370L;

	public FaceRecognizerAO() {
	}

	public FaceRecognizerAO(FaceRecognizerAO sliceImageAO) {
		super(sliceImageAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FaceRecognizerAO(this);
	}

	private void calcOutputSchema() {
		List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
		SDFAttribute attr = new SDFAttribute("id", "id", SDFDatatype.INTEGER);
		SDFAttribute name = new SDFAttribute("name", "name", SDFDatatype.STRING);
		SDFAttribute confidence = new SDFAttribute("confidence", "confidence", SDFDatatype.DOUBLE);
		attrs.add(attr);
		attrs.add(name);
		attrs.add(confidence);
		setOutputSchema(new SDFSchema(getInputSchema().getURI(),
				getInputSchema().getType(), attrs));
	}

	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		calcOutputSchema();
		return getOutputSchema();
	}
}
