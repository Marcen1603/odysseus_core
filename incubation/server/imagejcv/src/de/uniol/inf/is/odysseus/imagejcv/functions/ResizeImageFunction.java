package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import com.google.common.base.Preconditions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class ResizeImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 933957119628810757L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV}, SDFDatatype.NUMBERS, SDFDatatype.NUMBERS
	};
	
	public ResizeImageFunction() {
		super("resizeCV", 3, ResizeImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int width = this.getNumericalInputValue(1).intValue();
		final int height = this.getNumericalInputValue(2).intValue();
		
		Objects.requireNonNull(image);
		
		Preconditions.checkArgument(width > 0, "Invalid dimension");
		Preconditions.checkArgument(height > 0, "Invalid dimension");
		
		IplImage iplImage = image.getImage();
		IplImage result = IplImage.create(width, height, IPL_DEPTH_8U, 1);
		
		cvResize(iplImage, result);
		
		image.setImage(result);
		
		return image;
	}

}
