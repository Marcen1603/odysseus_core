package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

public class ProbabilisticMinNumberRHSFunction extends ProbabilisticMinFunction {
    /**
	 * 
	 */
    private static final long serialVersionUID = -1071626683925542134L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.
     * ProbabilisticMultiplicationOperator#getValue()
     */
    @Override
    public final ProbabilisticDouble getValue() {
        final AbstractProbabilisticValue<?> a = this.getInputValue(0);
        final ProbabilisticDouble b = new ProbabilisticDouble(this.getNumericalInputValue(1), 1.0);
        Objects.requireNonNull(a);
        return this.getValueInternal(a, b);
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFDatatype.NUMBERS };

}
