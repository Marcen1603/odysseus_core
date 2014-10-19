package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.CvMat;

import static org.bytedeco.javacpp.opencv_core.*;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function that inverses an image.
 * 
 * @author Kristian Bruns
 */
public class InverseImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6995742582650184331L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}};
	
	public InverseImageFunction() {
		super("invCV", 1, InverseImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Inverses each pixel of an image by subtracting it from 255.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Inversed image.
	 */
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(image);
		
		final IplImage iplImage = image.getImage();
		
		CvMat matImage = new CvMat();
		cvGetMat(iplImage, matImage);
		
		double value;
		for (int i=0; i < iplImage.width(); i++) {
			for (int j=0; j < iplImage.height(); j++) {
				value = matImage.get(i, j);
				matImage.put(i, j, 255 - value);
			}
			
		}
		
		cvGetImage(matImage, iplImage);
		
		image.setImage(iplImage);
		return image;
	}
	
}
