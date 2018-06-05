package de.uniol.inf.is.odysseus.wrapper.baslercamera.logicaloperator;

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
import de.uniol.inf.is.odysseus.wrapper.baslercamera.physicaloperator.BaslerCameraTransportHandler;

@LogicalOperator(maxInputPorts = 0, minInputPorts = 0, name = "BASLERCAMERA", category={LogicalOperatorCategory.SOURCE}, doc = "A source which can grab images from Basler cameras")
public class BaslerCameraSource extends AbstractAccessAO 
{
	private static final long serialVersionUID = 1546347500363592552L;

	public BaslerCameraSource() {
		setTransportHandler(BaslerCameraTransportHandler.NAME);
		setProtocolHandler(NoProtocolHandler.NAME);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PULL);
		
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute(getName(), "image", SDFImageJCVDatatype.IMAGEJCV));
		attributes.add(new SDFAttribute(getName(), "timestamp", SDFDatatype.START_TIMESTAMP));
		setAttributes(attributes);
	}
	
	public BaslerCameraSource(BaslerCameraSource other) {
		super(other);
	}
		
	@Override public AbstractLogicalOperator clone() {
		return new BaslerCameraSource(this);
	}
	
	@Parameter(name = BaslerCameraTransportHandler.SERIALNUMBER, type = StringParameter.class, optional = true, doc = "The serial number of the camera to use. If no serial number is specified, the first free camera will be used.")
	public void setSerialNumber(String serialNumber) { 
		addOption(BaslerCameraTransportHandler.SERIALNUMBER, serialNumber); 
	}
	
	public String getSerialNumber() { 
		return getOption(BaslerCameraTransportHandler.SERIALNUMBER); 
	}
}
