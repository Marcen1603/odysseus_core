package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class GetAmountOfBlackFunction extends AbstractFunction<Double> {
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
	public Double getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		final boolean border = (boolean) this.getInputValue(1);
		
		Objects.requireNonNull(image);
		
		final IplImage iplImage = image.getImage();
		
		int schwarz = 0;
		int gesamt = 0;
		boolean in = true;
		ByteBuffer buffer = iplImage.getByteBuffer();
		for (int i=0; i < iplImage.width(); i++) {
			if (border == true) {
				in = false;
			}
			for (int j=0; j < iplImage.height(); j++) {
				int index = j * iplImage.widthStep() + i * iplImage.nChannels();
				int value = buffer.get(index) & 0xFF;
				
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
		
		return ((double) schwarz / (double) gesamt);
	}
}
