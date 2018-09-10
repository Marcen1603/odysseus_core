package de.uniol.inf.is.odysseus.video.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Option;
import de.uniol.inf.is.odysseus.core.datahandler.TupleDataHandler;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractAccessAO;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.OptionParameter;
import de.uniol.inf.is.odysseus.core.util.Constants;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.video.physicaloperator.AbstractVideoStreamProtocolHandler;

public class AbstractVideoSource extends AbstractAccessAO 
{
	private static final long serialVersionUID = -2340587019264268834L;
	
	private void updateAttributes() {
		List<SDFAttribute> attributes = new ArrayList<>();
		attributes.add(new SDFAttribute(getName(), "image", SDFImageJCVDatatype.IMAGEJCV));

		String timestampMode = getOption(AbstractVideoStreamProtocolHandler.TIMESTAMPMODE);
		if (timestampMode != null && !timestampMode.equalsIgnoreCase(AbstractVideoStreamProtocolHandler.TIMESTAMPMODE_NONE)) {
			attributes.add(new SDFAttribute(getName(), "starttimestamp", SDFDatatype.START_TIMESTAMP));
			attributes.add(new SDFAttribute(getName(), "endtimestamp", SDFDatatype.END_TIMESTAMP));
		}
		setAttributes(attributes);		
	}
	
	public AbstractVideoSource(String protocolHandler) {
		setTransportHandler("None");
		setProtocolHandler(protocolHandler);
		setDataHandler(new TupleDataHandler().getSupportedDataTypes().get(0));
		setWrapper(Constants.GENERIC_PULL);		
		updateAttributes();
	}
	
	public AbstractVideoSource(AbstractVideoSource other) {
		super(other);
	}
		
	@Override public AbstractLogicalOperator clone() {
		return new AbstractVideoSource(this);
	}
	
	@Parameter(type = OptionParameter.class, name = "options", optional = true, isList = true, doc = "Additional options.")
	@Override public void setOptions(List<Option> value) {
		super.setOptions(value);
		updateAttributes();
	}

	@Override public List<Option> getOptions() {
		return super.getOptions();
	}	
}
