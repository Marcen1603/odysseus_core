package de.uniol.inf.is.odysseus.memstore.mdastore;

import java.util.List;
import java.util.Objects;

import com.google.common.collect.Lists;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

/**
 * For 1-dimensional MDAStores.
 * @author Michael Brand
 *
 */
public class MDAIndexFromNumeric extends AbstractFunction<Integer> {

	private static final long serialVersionUID = -8148362039305389820L;

	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
			{ SDFDatatype.STRING }, { SDFDatatype.DOUBLE } };

	public MDAIndexFromNumeric() {
		super("MDAIndex", 2, acceptedTypes, SDFDatatype.INTEGER);
	}

	@Override
	public Integer getValue() {
		String name = getInputValue(0);
		List<Double> value = Lists.newArrayList();
		value.add(getNumericalInputValue(1));
		Objects.requireNonNull(name);
		Objects.requireNonNull(value);
		MDAStore<Double> store = MDAStoreManager.get(name);
		return MDAIndicesFromList.getValue(store, value).get(0);
	}

}