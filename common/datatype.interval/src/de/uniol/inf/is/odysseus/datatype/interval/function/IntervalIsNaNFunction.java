/**
 * 
 */
package de.uniol.inf.is.odysseus.datatype.interval.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class IntervalIsNaNFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 5385637510028588300L;
	
	public static final SDFDatatype[][] accTypes = new SDFDatatype[][] {
			{ SDFIntervalDatatype.INTERVAL_BYTE, SDFIntervalDatatype.INTERVAL_SHORT,
					SDFIntervalDatatype.INTERVAL_INTEGER, SDFIntervalDatatype.INTERVAL_FLOAT,
					SDFIntervalDatatype.INTERVAL_DOUBLE, SDFIntervalDatatype.INTERVAL_LONG } };

	public IntervalIsNaNFunction() {
		super("isNaN", 1, accTypes, SDFIntervalDatatype.BOOLEAN);
	}

	@Override
	public Boolean getValue() {
		IntervalDouble interval = getInputValue(0);
		return Double.isNaN(interval.inf()) && Double.isNaN(interval.sup());
	}

}
