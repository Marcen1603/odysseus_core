package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ImageJCV MEP Function which returns the depth of an image
 * 
 * @author Henrik Surm
 */
public class GetDepthFunction extends AbstractFunction<Integer> 
{
	private static final long serialVersionUID = -19711760681394101L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
	{
		{SDFImageJCVDatatype.IMAGEJCV}
	};
	
	public GetDepthFunction() 
	{
		super("getDepthCV", 1, GetDepthFunction.ACC_TYPES, SDFDatatype.INTEGER);
	}
	
	@Override public Integer getValue() 
	{
		ImageJCV image = (ImageJCV) this.getInputValue(0);
		return image.getDepth();
	}
}
