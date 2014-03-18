package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.functions.compare.EqualsOperator2;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for discrete probabilistic values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticEqualsOperator extends EqualsOperator2 {

	/**
     * 
     */
	private static final long serialVersionUID = -6794365002703714420L;
	/**
	 * Accepted data types.
	 */
	private static final SDFDatatype[] ACC_TYPES1 = new SDFDatatype[] {
			SDFProbabilisticDatatype.PROBABILISTIC_BYTE,
			SDFProbabilisticDatatype.PROBABILISTIC_SHORT,
			SDFProbabilisticDatatype.PROBABILISTIC_INTEGER,
			SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE,
			SDFProbabilisticDatatype.PROBABILISTIC_LONG, SDFDatatype.BYTE,
			SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG,
			SDFDatatype.DOUBLE, SDFDatatype.FLOAT };
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][]{ACC_TYPES1,ACC_TYPES1};
	
	public ProbabilisticEqualsOperator() {
		super(ACC_TYPES);
	}
}
