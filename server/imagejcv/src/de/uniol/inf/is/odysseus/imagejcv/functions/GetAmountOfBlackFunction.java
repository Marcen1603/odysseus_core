package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;


import static org.bytedeco.javacpp.opencv_core.*;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class GetAmountOfBlackFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -1126952158558282901L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {
		{SDFImageJCVDatatype.IMAGEJCV},
		{SDFDatatype.BOOLEAN}
	};
	
	public GetAmountOfBlackFunction() {
		super("getBlackCV", 2, GetAmountOfBlackFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final boolean border = (boolean) this.getInputValue(1);
		
		Objects.requireNonNull(image);
		
		final IplImage iplImage = image.getImage();
		
		int schwarz = 0;
		int gesamt = 0;
		boolean in = true;
		
		CvMat matImage = new CvMat();
		cvGetMat(iplImage, matImage);
		
		for (int i=0; i < iplImage.width(); i++) {
			if (border == true) {
				in = false;
			}
			for (int j=0; j < iplImage.height(); j++) {
				int value = (int) matImage.get(i, j);
				
				if (in == true) {
					gesamt ++;
				}
				
				if (value == 125 && in == false) {
					in = true;
				} else if (value == 125 && in == true && border == true) {
					in = false;
				}
				
				if (value == 0 && in == true) {
					schwarz ++;
				}
			}
		}
		
		System.out.println((double) schwarz / (double) gesamt);
		
		return image;
	}
}
