package de.uniol.inf.is.odysseus.image.functions;

import java.util.Objects;

import org.opencv.core.Mat;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.image.common.datatype.Image;
import de.uniol.inf.is.odysseus.image.common.sdf.schema.SDFImageDatatype;
import de.uniol.inf.is.odysseus.image.util.OpenCVUtil;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class GetAmountOfBlackFunction extends AbstractFunction<Double> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -159211926243474276L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageDatatype.IMAGE},
		{SDFDatatype.BOOLEAN}
	};
	
	public GetAmountOfBlackFunction() {
		super("getBlack", 2, GetAmountOfBlackFunction.ACC_TYPES, SDFImageDatatype.IMAGE);
	}
	
	@Override
	public Double getValue() {
		final Image image = (Image) this.getInputValue(0);
		final boolean border = (boolean) this.getInputValue(1);
		
		Objects.requireNonNull(image);
		
		final Mat iplImage = OpenCVUtil.imageToIplImage(image);
		
		double[] temp;
		int schwarz = 0;
		int gesamt = 0;
		boolean in = true;
		for (int i=0; i < iplImage.width(); i++) {
			if (border == true) {
				in = false;
			}
			for (int j=0; j < iplImage.height(); j++) {
				temp = iplImage.get(i, j);
				
				if (in == true) {
					gesamt ++;
				}
				
				if ((int) temp[0] == 125 && in == false) {
					in = true;
				} else if((int) temp[0] == 125 && in == true && border == true) {
					in = false;
				}
				
				if ((int) temp[0] == 0 && in == true) {
					schwarz++;
				}
			}
		}
		
		return ((double) schwarz / (double) gesamt);
	}
	
}
