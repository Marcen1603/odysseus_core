package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function that returns a pixel value.
 * 
 * @author Kristian Bruns
 */
public class GetImageFunction extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7230530057474654715L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS
	};
	
	public GetImageFunction() {
		super("getCV", 3, GetImageFunction.ACC_TYPES, SDFDatatype.DOUBLE);
	}
	
	/**
	 * Returns pixel value located on coordinates x and y from input Value 1 and 2.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return Double Pixel value.
	 */
	@Override
	public Double getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int x = this.getNumericalInputValue(1).intValue();
		final int y = this.getNumericalInputValue(2).intValue();
		
		Objects.requireNonNull(image);
		
		IplImage iplImage = image.getImage();
		
		int index = y * iplImage.widthStep() + x * iplImage.nChannels();
		
		return (double)image.get(index);
	}
}
