package de.uniol.inf.is.odysseus.imagejcv.functions;

import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.CvMat;

import static org.bytedeco.javacpp.opencv_core.*;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class RaiseContrastFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7606324725154709923L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		{SDFDatatype.INTEGER}
	};
	
	public RaiseContrastFunction() {
		super("contrastCV", 2, RaiseContrastFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int value = this.getNumericalInputValue(1).intValue();
		
		Objects.requireNonNull(image);
		
		IplImage iplImage = image.getImage();
		
		CvMat matImage = new CvMat();
		cvGetMat(iplImage, matImage);
		
		cvScale(matImage, matImage, value, 0);
		
		cvGetImage(matImage, iplImage);
		
		image.setImage(iplImage);
		return image;
	}

}
