package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAIndexFunction extends AbstractFunction<Integer> {

	private static final long serialVersionUID = -8148362039305389820L;

	/*
	 * 1: store name
	 * 2: Value to retrieve index for
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.DOUBLE } };

	public MDAIndexFunction() {
		super("MDAIndex", 2, acceptedTypes, SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		String name = getInputValue(0);
		List<Double> value = Lists.newArrayList();
		value.add(getNumericalInputValue(1));
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		MDAStore store = MDAStoreManager.get(name);
		return MDAIndicesFunction.getValue(store, value).get(0);
	}

}