package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
 * ODYSSEUS Function for getting circles inside an image.
 * 
 * @author Kristian Bruns
 */
public class GetCircleFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5487327701711246986L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
	};
	
	public GetCircleFunction() {
		super("circleCV", 1, GetCircleFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	/**
	 * Finds circles in an image, draw them onto the image and returns the image afterwards.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return ImageJCV Image with circles drawn on it.
	 */
	@Override
	public ImageJCV getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);		
		Objects.requireNonNull(image);		
		
		ImageJCV result = ImageFunctions.toGrayscaleImage(image, true);
		
		CvMemStorage mem = CvMemStorage.create();
		CvSeq circles = cvHoughCircles(result.getImage(), mem, CV_HOUGH_GRADIENT, 3d, (result.getWidth() * result.getHeight()));
		
		for (int x=0; x < circles.total(); x++) {
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, x));
			CvPoint2D32f point = new CvPoint2D32f();
			point.x(circle.x());
			point.y(circle.y());
			CvPoint center = cvPointFrom32f(point);
			int radius = Math.round(circle.z());
			cvCircle(result.getImage(), center, radius, CV_RGB(125, 125, 125), 1, 8, 0);
		}
		
		return result;*/
	}
	
}
