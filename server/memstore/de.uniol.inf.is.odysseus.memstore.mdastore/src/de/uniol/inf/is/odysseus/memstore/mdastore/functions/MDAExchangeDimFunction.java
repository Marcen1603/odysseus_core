package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAExchangeDimFunction extends AbstractFunction<Void> {

	private static final long serialVersionUID = -2155748884930749034L;

	/*
	 * 1: store name 2: index of dimension to exchange 3: new dimension values
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] { { SDFDatatype.STRING },
			{ SDFDatatype.INTEGER }, { SDFDatatype.LIST_DOUBLE } };

	public MDAExchangeDimFunction() {
		super("MDAExchangeDim", 3, acceptedTypes, null);
	}

	@Override
	public Void getValue() {
		String name = getInputValue(0);
		Objects.requireNonNull(name);
		Long index = getInputValue(1);
		Objects.requireNonNull(index);
		List<Double> dim = getInputValue(2);
		Objects.requireNonNull(dim);
		MDARemoveDimFunction.removeDim(name, index.intValue());
		MDAAddDimWithIndexFunction.addDim(name, index.intValue(), dim);
		return null;
	}

}
