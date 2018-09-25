package de.uniol.inf.is.odysseus.memstore.mdastore.functions;

import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAIndicesFunction extends AbstractFunction<List<Integer>> {

	private static final long serialVersionUID = -8148362039305389820L;

	/*
	 * 1: store name
	 * 2: Values as list to retrieve indices for
	 */
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.LIST_DOUBLE }};

	public MDAIndicesFunction() {
		super("MDAIndices", 2, acceptedTypes, SDFDatatype.LIST_INTEGER);
	}

	@Override
	public List<Integer> getValue() {
		String name = getInputValue(0);
		List<Double> value = getInputValue(1);
		Objects.requireNonNull(name);
		MDAStore store = MDAStoreManager.get(name);
		return getValue(store, value);
	}

	public static List<Integer> getValue(MDAStore store, List<Double> values) {
		Objects.requireNonNull(store);
		return store.getCellIndices(values);
	}

}