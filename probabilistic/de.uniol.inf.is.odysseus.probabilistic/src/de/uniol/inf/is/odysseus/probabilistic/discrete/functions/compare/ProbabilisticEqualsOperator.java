package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.compare;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.mep.functions.compare.EqualsOperator2;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Equals operator for discrete probabilistic values
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticEqualsOperator extends EqualsOperator2 {

	/**
     * 
     */
	private static final long serialVersionUID = -6794365002703714420L;
	public static final SDFDatatype[] accTypes = new SDFDatatype[] { SDFProbabilisticDatatype.PROBABILISTIC_BYTE, SDFProbabilisticDatatype.PROBABILISTIC_SHORT, SDFProbabilisticDatatype.PROBABILISTIC_INTEGER, SDFProbabilisticDatatype.PROBABILISTIC_FLOAT,
			SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE, SDFProbabilisticDatatype.PROBABILISTIC_LONG, SDFDatatype.BYTE, SDFDatatype.SHORT, SDFDatatype.INTEGER, SDFDatatype.LONG, SDFDatatype.DOUBLE, SDFDatatype.FLOAT };

	@Override
	public SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > (this.getArity() - 1)) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticEqualsOperator.accTypes;
	}
}
