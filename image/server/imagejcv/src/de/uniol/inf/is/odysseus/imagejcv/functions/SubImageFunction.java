package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function that creates an ROI of an image.
 * 
 * @author Kristian Bruns
 */
public class SubImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4629416274635140999L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS,
		SDFDatatype.NUMBERS
	};
	
	public SubImageFunction() {
		super("subCV", 5, SubImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Creates an region of interest in the given image from input value 0.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Computed sub image.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final int x = this.getNumericalInputValue(1).intValue();
		final int y = this.getNumericalInputValue(2).intValue();
		final int width = this.getNumericalInputValue(3).intValue();
		final int height = this.getNumericalInputValue(4).intValue();
		
		Objects.requireNonNull(image);
		
		Preconditions.checkArgument(x >= 0 && width > 0 && (x + width) <= image.getWidth(),
									"Invalid Dimension");
		Preconditions.checkArgument(y >= 0 && height > 0 && (y + height) <= image.getHeight(), 
				                    "Invalid Dimension");
		
		ImageJCV result = new ImageJCV(image);
		final IplImage iplImage = result.getImage();
		
		final CvRect roi = new CvRect();
		roi.x(x);
		roi.y(y);
		roi.width(width);
		roi.height(height);
		
		cvSetImageROI(iplImage, roi);
		
		IplImage subImg = cvCreateImage(cvGetSize(iplImage), iplImage.depth(), iplImage.nChannels());
		
		cvCopy(iplImage, subImg, null);
		
		cvResetImageROI(iplImage);
		
		return result; */
	}
	
}
