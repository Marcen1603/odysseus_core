package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math;

import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.common.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.common.sdf.schema.SDFProbabilisticDatatype;

/**
 * Max function for a discrete and a deterministic value.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticMaxNumberLHSFunction extends ProbabilisticMaxFunction {
    /**
 * 
 */
    private static final long serialVersionUID = 1254264947883248821L;

    /*
     * 
     * @see de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math.
     * ProbabilisticMultiplicationOperator#getValue()
     */
    @Override
    public final ProbabilisticDouble getValue() {
        final ProbabilisticDouble a = new ProbabilisticDouble(this.getNumericalInputValue(0), 1.0);
        final AbstractProbabilisticValue<?> b = this.getInputValue(1);
        Objects.requireNonNull(b);
        return this.getValueInternal(a, b);
    }

    /**
     * Accepted data types.
     */
    public static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFDatatype.NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

}
