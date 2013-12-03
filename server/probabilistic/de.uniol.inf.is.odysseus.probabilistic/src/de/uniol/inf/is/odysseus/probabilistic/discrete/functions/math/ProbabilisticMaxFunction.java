package de.uniol.inf.is.odysseus.probabilistic.discrete.functions.math;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.apache.commons.math3.util.FastMath;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.AbstractProbabilisticValue;
import de.uniol.inf.is.odysseus.probabilistic.discrete.datatype.ProbabilisticDouble;
import de.uniol.inf.is.odysseus.probabilistic.functions.AbstractProbabilisticFunction;
import de.uniol.inf.is.odysseus.probabilistic.sdf.schema.SDFProbabilisticDatatype;

/**
 * Max function for two discrete values.
 * 
 * @author Christian Kuka <christian@kuka.cc>
 * 
 */
public class ProbabilisticMaxFunction extends AbstractProbabilisticFunction<ProbabilisticDouble> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7231636038856632912L;
	/**
	 * Accepted data types.
	 */
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS, SDFProbabilisticDatatype.PROBABILISTIC_NUMBERS };

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getArity()
	 */
	@Override
	public final int getArity() {
		return 2;
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getAcceptedTypes(int)
	 */
	@Override
	public final SDFDatatype[] getAcceptedTypes(final int argPos) {
		if (argPos < 0) {
			throw new IllegalArgumentException("negative argument index not allowed");
		}
		if (argPos > 0) {
			throw new IllegalArgumentException(this.getSymbol() + " has only " + this.getArity() + " argument(s).");
		}
		return ProbabilisticMaxFunction.ACC_TYPES[argPos];
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IFunction#getSymbol()
	 */
	@Override
	public final String getSymbol() {
		return "sMax";
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getValue()
	 */
	@Override
	public ProbabilisticDouble getValue() {
		final AbstractProbabilisticValue<?> a = this.getInputValue(0);
		final AbstractProbabilisticValue<?> b = this.getInputValue(1);
		Objects.requireNonNull(a);
		Objects.requireNonNull(b);
		return this.getValueInternal(a, b);
	}

	/**
	 * Compute the max value of the given probabilistic values.
	 * 
	 * @param a
	 *            The probabilistic value
	 * @param b
	 *            The probabilistic value
	 * @return The probabilistic max value
	 */
	protected final ProbabilisticDouble getValueInternal(final AbstractProbabilisticValue<?> a, final AbstractProbabilisticValue<?> b) {
		final Map<Double, Double> values = new HashMap<Double, Double>();
		for (final Entry<?, Double> aEntry : a.getValues().entrySet()) {
			for (final Entry<?, Double> bEntry : b.getValues().entrySet()) {
				final double value = FastMath.max(((Number) aEntry.getKey()).doubleValue(), ((Number) bEntry.getKey()).doubleValue());
				if (values.containsKey(value)) {
					values.put(value, values.get(value) + (aEntry.getValue() * bEntry.getValue()));
				} else {
					values.put(value, aEntry.getValue() * bEntry.getValue());
				}
			}
		}
		return new ProbabilisticDouble(values);
	}

	/*
	 * 
	 * @see de.uniol.inf.is.odysseus.core.mep.IExpression#getReturnType()
	 */
	@Override
	public final SDFDatatype getReturnType() {
		return SDFProbabilisticDatatype.PROBABILISTIC_DOUBLE;
	}

}
