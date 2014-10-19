package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_core.*;

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
		
		IplImage iplImage1 = image1.getImage();
		IplImage iplImage2 = image2.getImage();
		
		IplImage grayImage1 = cvCreateImage(cvGetSize(iplImage1), IPL_DEPTH_8U, 1);
		IplImage grayImage2 = cvCreateImage(cvGetSize(iplImage2), IPL_DEPTH_8U, 1);
		IplImage iplResult = cvCreateImage(cvGetSize(iplImage1), IPL_DEPTH_8U, 1);
		ImageJCV result = new ImageJCV();
		
		if (iplImage1.nChannels() > 1) {
			cvCvtColor(iplImage1, grayImage1, CV_BGR2GRAY);
		} else {
			grayImage1 = iplImage1.clone();
		}
		
		if (iplImage2.nChannels() > 1) {
			cvCvtColor(iplImage2, grayImage2, CV_BGR2GRAY);
		} else {
			grayImage2 = iplImage2.clone();
		}
		
		cvAbsDiff(grayImage1, grayImage2, iplResult);
		
		result.setImage(iplResult);
		
		return result;
	}

}
