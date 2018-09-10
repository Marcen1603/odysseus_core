package de.uniol.inf.is.odysseus.imagejcv.functions;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.imagejcv.common.sdf.schema.SDFImageJCVDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * ODYSSEUS function for creating a data matrix from an image.
 * 
 * @author Kristian Bruns
 */
public class ToMatrixFunction extends AbstractFunction<double[][]> {

	private static final long serialVersionUID = 4292224794692417493L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] {{SDFImageJCVDatatype.IMAGEJCV}};
	
	public ToMatrixFunction() {
		super("toMatrixCV", 1, ToMatrixFunction.ACC_TYPES, SDFDatatype.MATRIX_DOUBLE);
	}
	
	/**
	 * Returns data matrix from a given image.
	 * 
	 * @author Kristian Bruns
	 * 
	 * @return double[][] Data matrix of the image.
	 */
	@Override
	public double[][] getValue() {
		throw new UnsupportedOperationException("This MEP function needs to be re-implemented!");
		
/*		final ImageJCV image = this.getInputValue(0);
		Objects.requireNonNull(image);
		double[][] matrix = image.getMatrix();
		return matrix;*/
	}
	
}
