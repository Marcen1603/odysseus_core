package de.uniol.inf.is.odysseus.imagejcv.functions;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.datatype.ImageJCV;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * @author Kristian Bruns
 */
public class ToMatrixFunction extends AbstractFunction<double[][]> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 4292224794692417493L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}};
	
	public ToMatrixFunction() {
		super("toMatrixCV", 1, ToMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
	}
	
	@Override
	public double[][] getValue() {
		final ImageJCV image = this.getInputValue(0);
		Objects.requireNonNull(image);
		double[][] matrix = image.getMatrix();
		return matrix;
	}
	
}
