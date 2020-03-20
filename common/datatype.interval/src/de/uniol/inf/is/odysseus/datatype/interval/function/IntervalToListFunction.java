package de.uniol.inf.is.odysseus.datatype.interval.function;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.datatype.interval.datatype.IntervalDouble;
import de.uniol.inf.is.odysseus.datatype.interval.sdf.schema.SDFIntervalDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class IntervalToListFunction extends AbstractFunction<List<Double>> {

	private static final long serialVersionUID = 1L;
	private static final SDFDatatype[][] ACC_TYPES = new SDFDatatype[][] { SDFIntervalDatatype.TYPES };

    public IntervalToListFunction() {
        super("toList", 1, ACC_TYPES, SDFDatatype.LIST_DOUBLE);
    }

    @Override
    public List<Double> getValue() {
        final IntervalDouble a = this.getInputValue(0);
        List<Double> ret = new ArrayList<>();
        ret.add(a.getLeft());
        ret.add(a.getRight());
        return ret;
    }
	

}
