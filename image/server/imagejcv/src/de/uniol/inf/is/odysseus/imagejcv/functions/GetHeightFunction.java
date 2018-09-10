package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ImageJCV MEP Function which returns the height of an image
 * 
 * @author Henrik Surm
 */
public class GetHeightFunction extends AbstractFunction<Integer> 
{
	private static final long serialVersionUID = 9107084664576800452L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] 
	{
		{SDFImageJCVDatatype.IMAGEJCV}
	};
	
	public GetHeightFunction() 
	{
		super("getHeightCV", 1, GetHeightFunction.ACC_TYPES, SDFDatatype.INTEGER);
	}
	
	@Override public Integer getValue() 
	{
		ImageJCV image = (ImageJCV) this.getInputValue(0);
		return image.getHeight();
	}
}
