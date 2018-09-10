package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ImageJCV MEP Function which returns the number of channels of an image
 * 
 * @author Henrik Surm
 */
public class GetNumChannelsFunction extends AbstractFunction<Integer> 
{
	private static final long serialVersionUID = -3156065227607183226L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
	{
		{SDFImageJCVDatatype.IMAGEJCV}
	};
	
	public GetNumChannelsFunction() 
	{
		super("getNumChannelsCV", 1, GetNumChannelsFunction.ACC_TYPES, SDFDatatype.INTEGER);
	}
	
	@Override public Integer getValue() 
	{
		ImageJCV image = (ImageJCV) this.getInputValue(0);
		return image.getNumChannels();
	}
}
