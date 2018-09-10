package de.uniol.inf.is.odysseus.image.functions;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.Objects;

import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;

/**
 * @author Kristian Bruns
 */
public class GetCircleFunction extends AbstractFunction<Image> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4143062369928029518L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageDatatype.IMAGE},
	};
	
	public GetCircleFunction() {
		super("circle", 1, GetCircleFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
	}
	
	@Override
	public Image getValue() {
		final Image image = (Image) this.getInputValue(0);
		
		Objects.requireNonNull(image);
		
		final Mat iplImage = OpenCVUtil.imageToIplImage(image);
		Mat circles = new Mat();
		
		Imgproc.cvtColor(iplImage, iplImage, Imgproc.COLOR_BGR2GRAY);
		
		Imgproc.HoughCircles(iplImage, circles, Imgproc.CV_HOUGH_GRADIENT, 3d, (iplImage.width() * iplImage.height()));
		
		for (int x=0; x<circles.cols(); x++) {
			double vCircle[] = new double[3];
			vCircle = circles.get(0, x);
			
			Point center = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
			int radius = (int) Math.round(vCircle[2]);
			Core.circle(iplImage, center, radius, new Scalar(125, 125, 125), 3, 8, 0);
		}
		
		return OpenCVUtil.iplImageToImage(iplImage, image) ;
		
		
	}
	

}
