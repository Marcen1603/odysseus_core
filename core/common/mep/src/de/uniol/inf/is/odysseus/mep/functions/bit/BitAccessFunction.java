package de.uniol.inf.is.odysseus.mep.functions.bit;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class BitAccessFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = 7553187248074371162L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.BITVECTOR},new SDFDatatype[]{SDFDatatype.INTEGER }};

	public BitAccessFunction() {
		super("[]", 2, accTypes, SDFDatatype.BOOLEAN, false);
	}

	
	@Override
	public Boolean getValue() {
		BitVector in = getInputValue(0);
		int pos = getNumericalInputValue(1).intValue();
		return in.getBit(pos);
	}

}
