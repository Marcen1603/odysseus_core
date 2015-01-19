package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.List;
import java.util.Objects;

import com.google.common.primitives.Doubles;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStore;
import de.uniol.inf.is.odysseus.memstore.mdastore.MDAStoreManager;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class MDAIndicesFromNumeric extends AbstractFunction<Integer[]> {

	private static final long serialVersionUID = -8148362039305389820L;
	
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.MATRIX_DOUBLE } };

	public MDAIndicesFromNumeric() {
		super("MDAIndices", 2, acceptedTypes, SDFDatatype.MATRIX_INTEGER);
	}

	@Override
	public Integer[] getValue() {
		String name = getInputValue(0);
		List<Double> value = Doubles.asList(((double[][]) getInputValue(1))[0]);
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		MDAStore<Double> store = MDAStoreManager.get(name);
		if (store != null) {
			int[] indices = store.getCellIndices(value);
			Integer[] out = new Integer[indices.length];
			// internal start 0; external start 1
			for (int i = 0; i < indices.length; i++) {
				out[i] = indices[i] + 1;
			}
			return out;
		}
		Integer[] out = new Integer[0];
		return out;
	}

}