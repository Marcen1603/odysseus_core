package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAIndicesFromList extends AbstractFunction<List<Integer>> {

	private static final long serialVersionUID = -8148362039305389820L;

	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, SDFDatatype.LISTS };

	public MDAIndicesFromList() {
		super("MDAIndices", 2, acceptedTypes, SDFDatatype.LIST_INTEGER);
	}

	@Override
	public List<Integer> getValue() {
		String name = getInputValue(0);
		List<?> value = getInputValue(1);
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		MDAStore<Double> store = MDAStoreManager.get(name);
		return getValue(store, value);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Integer> getValue(MDAStore<?> store, List values) {
		if (store != null) {
			int[] indices = store.getCellIndices(values);
			Integer[] out = new Integer[indices.length];
			// internal start 0; external start 1
			for (int i = 0; i < indices.length; i++) {
				out[i] = indices[i] + 1;
			}
			return Arrays.asList(out);
		}
		Integer[] out = new Integer[0];
		return Arrays.asList(out);
	}

}