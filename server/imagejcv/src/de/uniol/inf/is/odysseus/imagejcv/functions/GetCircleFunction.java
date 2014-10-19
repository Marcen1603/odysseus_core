package de.uniol.inf.is.odysseus.imagejcv.functions;

import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import java.util.Objects;

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
	 * @return ImageJCV Image with circles drawed on it.
	 */
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		
		Objects.requireNonNull(image);
		
		
		IplImage iplImage  = image.getImage();
		IplImage iplResult = cvCreateImage(cvGetSize(iplImage), IPL_DEPTH_8U, 1);
		
		CvMemStorage mem = CvMemStorage.create();
		
		if (iplImage.nChannels() > 1) {
			cvCvtColor(iplImage, iplResult, CV_BGR2GRAY);
		} else {
			iplResult = iplImage.clone();
		}
		
		CvSeq circles = cvHoughCircles(iplResult, mem, CV_HOUGH_GRADIENT, 3d, (iplResult.width() * iplResult.height()));
		
		for (int x=0; x < circles.total(); x++) {
			CvPoint3D32f circle = new CvPoint3D32f(cvGetSeqElem(circles, x));
			CvPoint2D32f point = new CvPoint2D32f();
			point.x(circle.x());
			point.y(circle.y());
			CvPoint center = cvPointFrom32f(point);
			int radius = Math.round(circle.z());
			cvCircle(iplResult, center, radius, CV_RGB(125, 125, 125), 1, 8, 0);
		}
		
		image.setImage(iplResult);
		return image;
	}
	
}
