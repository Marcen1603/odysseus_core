package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.nio.ByteBuffer;
import java.util.Objects;

import org.bytedeco.javacpp.opencv_core.IplImage;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class InverseImageFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6995742582650184331L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}};
	
	public InverseImageFunction() {
		super("invCV", 1, InverseImageFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final ImageJCV image = (ImageJCV) this.getInputValue(0);
		Objects.requireNonNull(image);
		
		final IplImage iplImage = image.getImage();
		ByteBuffer buffer = iplImage.getByteBuffer();
		for (int i=0; i < iplImage.width(); i++) {
			for (int j=0; j < iplImage.height(); j++) {
				int index = j * iplImage.widthStep() + i * iplImage.nChannels();
				
				// TODO: There seems to be an error here, this line crashes on the last pixel.
				// The cause might be reading a 4 byte int, when each pixel is only 3 bytes and thus reading one byte over the end
				int value = buffer.getInt(index);
				buffer.putInt(index, 255 - value);
			}
			
		}
		image.setImage(iplImage);
		return image;
	}
	
}
