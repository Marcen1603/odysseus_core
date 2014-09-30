package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class SetImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5617318633630685561L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS
	};
	
	public SetImageFunction() {
		super("setCV", 4, SetImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int x = this.getNumericalInputValue(1).intValue();
		final int y = this.getNumericalInputValue(2).intValue();
		final int value = this.getNumericalInputValue(3).intValue();
		
		Objects.requireNonNull(image);
		
		IplImage iplImage = image.getImage();
		
		int index = y * iplImage.widthStep() + x * iplImage.nChannels();
		
		image.set(index, value);
		
		return image;
	}
	
}
