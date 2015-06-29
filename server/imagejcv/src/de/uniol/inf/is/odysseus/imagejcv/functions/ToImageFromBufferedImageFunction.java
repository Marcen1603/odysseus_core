package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * MEP function which converts an Image to an ImageJCV.
 */
public class ToImageFromBufferedImageFunction extends AbstractFunction<ImageJCV> 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619758038343188832L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageDatatype.IMAGE}};
	
	public ToImageFromBufferedImageFunction() {
		super("toImageCV", 1, ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() 
	{
		Image image = getInputValue(0);
		return ImageJCV.fromBufferedImage(image.getImage());
	}
	
}
