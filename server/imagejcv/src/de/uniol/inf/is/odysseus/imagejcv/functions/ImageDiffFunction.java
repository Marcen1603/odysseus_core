package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.IPL_DEPTH_8U;
import static org.bytedeco.javacpp.opencv_core.cvAbsDiff;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function for creating a difference image between two images.
 * 
 * @author Kristian Bruns
 */
public class ImageDiffFunction extends AbstractFunction<ImageJCV> {
	
	private static final long serialVersionUID = -7650954639707065688L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}, {SDFImageJCVDatatype.IMAGEJCV}};
	
	public ImageDiffFunction() {
		super("imageDiffCV", 2, ImageDiffFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Function that computes difference image between two images.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Difference Image between image from inputValue 0 and image from inputValue 1.
	 */
	@Override
	public ImageJCV getValue() {
		final ImageJCV image1 = (ImageJCV) this.getInputValue(0);
		final ImageJCV image2 = (ImageJCV) this.getInputValue(1);
		
		Objects.requireNonNull(image1);
		Objects.requireNonNull(image2);		
		
		ImageJCV gray1 = image1.toGrayscaleImage();
		ImageJCV gray2 = image2.toGrayscaleImage();
		ImageJCV result = new ImageJCV(image1.getWidth(), image1.getHeight(), IPL_DEPTH_8U, 1);
		
		cvAbsDiff(gray1.getImage(), gray2.getImage(), result.getImage());
		
		return result;
	}

}
