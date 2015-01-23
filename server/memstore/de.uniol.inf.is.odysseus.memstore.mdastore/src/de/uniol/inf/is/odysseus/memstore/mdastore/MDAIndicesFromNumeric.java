package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAIndicesFromNumeric extends AbstractFunction<List<Integer>> {

	private static final long serialVersionUID = -8148362039305389820L;

	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.DOUBLE }, { SDFDatatype.DOUBLE } };

	public MDAIndicesFromNumeric() {
		super("MDAIndices", 3, acceptedTypes, SDFDatatype.LIST_INTEGER);
	}

	@Override
	public List<Integer> getValue() {
		String name = getInputValue(0);
		List<Double> value = Lists.newArrayList();
		value.add(getNumericalInputValue(1));
		value.add(getNumericalInputValue(2));
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		MDAStore<Double> store = MDAStoreManager.get(name);
		return MDAIndicesFromList.getValue(store, value);
	}

}