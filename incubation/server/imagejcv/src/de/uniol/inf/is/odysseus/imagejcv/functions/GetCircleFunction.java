package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.util.Objects;

import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;

/**
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
		super("circleCV", 2, GetCircleFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		
		Objects.requireNonNull(image);
		
		final IplImage iplImage = image.getImage();
		CvSeq circles = new CvSeq();
		
		cvCvtColor(iplImage, iplImage, CV_BGR2GRAY);
		
		cvHoughCircles(iplImage, circles, CV_HOUGH_GRADIENT, 3d, (iplImage.width() * iplImage.height()));
		
		for (int x=0; x < circles.total(); x++) {
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, x));
			CvPoint2D32f point = new CvPoint2D32f();
			point.x(circle.x());
			point.y(circle.y());
			CvPoint center = cvPointFrom32f(point);
			int radius = Math.round(circle.z());
			cvCircle(iplImage, center, radius, CvScalar.RED, 3, 8, 0);
		}
		
		image.setImage(iplImage);
		return image;
	}
	
}
