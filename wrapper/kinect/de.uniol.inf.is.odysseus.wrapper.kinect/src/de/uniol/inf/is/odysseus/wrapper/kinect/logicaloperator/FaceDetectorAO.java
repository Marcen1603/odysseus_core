package de.uniol.inf.is.odysseus.wrapper.kinect.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.wrapper.kinect.sdf.schema.SDFKinectDatatype;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "DETECTFACES", doc="Detects faces in the images from the Kinect Camera", category={LogicalOperatorCategory.BASE})
public class FaceDetectorAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 3632770189119157370L;

	public FaceDetectorAO() {
	}
	
	public FaceDetectorAO(FaceDetectorAO sliceImageAO) {
		super(sliceImageAO);
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new FaceDetectorAO(this);
	}
	
	
	private void calcOutputSchema() {
			List<SDFAttribute> attrs = new ArrayList<SDFAttribute>();
			SDFAttribute faces = new SDFAttribute("face",
					"face", SDFKinectDatatype.BUFFERED_IMAGE, null, null, null);				
			attrs.add(faces);
			setOutputSchema(SDFSchemaFactory.createNewWithAttributes(attrs, getInputSchema()));
	}
	
	@Override
	public SDFSchema getOutputSchemaIntern(int pos) {
		calcOutputSchema();
		return getOutputSchema();
	}	
}
