package de.uniol.inf.is.odysseus.wrapper.optriscamera.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.physicaloperator.access.protocol.NoProtocolHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.wrapper.optriscamera.physicaloperator.OptrisCameraTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "OPTRISCAMERA", category={LogicalOperatorCategory.SOURCE}, doc = "A source which can grab images from Optris IR cameras")
public class OptrisCameraSource extends AbstractAccessAO 
{
	private static final long serialVersionUID = 5884753377992791734L;

	public OptrisCameraSource() {
		setTransportHandler(OptrisCameraTransportHandler.NAME);
		setProtocolHandler(NoProtocolHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PUSH);
		
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute(getName(), "image", SDFImageJCVDatatype.IMAGEJCV));
		attributes.add(new SDFAttribute(getName(), "timestamp", SDFDatatype.START_TIMESTAMP));
		attributes.add(new SDFAttribute(getName(), OptrisCameraTransportHandler.ATTRNAME_FLAGSTATE, SDFDatatype.STRING));
		setAttributes(attributes);
	}
	
	public OptrisCameraSource(OptrisCameraSource other) {
		super(other);
	}
		
	@Override public AbstractLogicalOperator clone() {
		return new OptrisCameraSource(this);
	}
	
	@Parameter(name = OptrisCameraTransportHandler.SERIALNUMBER, type = StringParameter.class, optional = true, doc = "The serial number of the camera to use. If no serial number is specified, the first free camera will be used.")
	public void setSerialNumber(String serialNumber) { 
		addOption(OptrisCameraTransportHandler.SERIALNUMBER, serialNumber); 
	}
	
	public String getSerialNumber() { 
		return getOption(OptrisCameraTransportHandler.SERIALNUMBER); 
	}
}
