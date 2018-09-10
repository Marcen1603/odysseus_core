package de.uniol.inf.is.odysseus.video.logicaloperator;

import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.video.physicaloperator.OpenCVVideoStreamProtocolHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "OPENCVVIDEO", category={LogicalOperatorCategory.SOURCE}, doc = "A source which can read video files and receive video streams")
public class OpenCVVideoSource extends AbstractVideoSource 
{
	private static final long serialVersionUID = 1546347500363592552L;
	
	public OpenCVVideoSource() {
		super(OpenCVVideoStreamProtocolHandler.NAME);
	}
	
	public OpenCVVideoSource(OpenCVVideoSource other) {
		super(other);
	}
		
	@Override public AbstractLogicalOperator clone() {
		return new OpenCVVideoSource(this);
	}	
}
