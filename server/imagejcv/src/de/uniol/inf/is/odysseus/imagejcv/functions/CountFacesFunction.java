package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS Function for counting faces in an image.
 * 
 * @author Kristian Bruns
 */
public class CountFacesFunction extends AbstractFunction<Integer> {
	
	private static final long serialVersionUID = 5723315739315933213L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{ SDFImageJCVDatatype.IMAGEJCV}};
	///private CascadeClassifier faceDetector;
	
	public CountFacesFunction() {
		super("facesCV", 1, CountFacesFunction.ACC_TYPES, SDFDatatype.INTEGER, true);
	}
	
	/**
	 * This function counts the number of faces in an image.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return Integer Number of Faces existing in image from input Value 0. 
	 */
	@Override
	public Integer getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final IplImage iplImage = image.getImage();
		final Mat matImage = new Mat(iplImage);
		RectVector faceDetections = new RectVector();
		faceDetector.detectMultiScale(matImage, faceDetections);
		return faceDetections.sizeof();*/
	}
	
}
