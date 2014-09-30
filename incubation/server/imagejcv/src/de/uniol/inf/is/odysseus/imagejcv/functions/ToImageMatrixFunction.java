package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import com.google.common.base.Preconditions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class ToImageMatrixFunction extends AbstractFunction<ImageJCV> {
	/**
	 * 
	 */
	private static final long serialVersionUID = -953369344266988794L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {SDFDatatype.MATRIXS};
	
	public ToImageMatrixFunction() {
		super("toImageMatrixCV", 1, ToImageMatrixFunction.ACC_TYPES, SDFImageJCVDatatype.IMAGEJCV);
	}
	
	@Override
	public ImageJCV getValue() {
		final double data[][] = this.getInputValue(0);
		Objects.requireNonNull(data);
		Preconditions.checkArgument(data.length > 0, "Invalid Dimension");
		Preconditions.checkArgument(data[0].length > 0, "Invalid Dimension");
		ImageJCV image = new ImageJCV(data);
		return image;
	}
	
}
