package de.uniol.inf.is.odysseus.mep.functions.bit;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractBinaryBitVectorInputOperator;

public class BitVectorAndOperator extends AbstractBinaryBitVectorInputOperator<BitVector> {
	
	private static final long serialVersionUID = -730023743642274236L;

	public BitVectorAndOperator() {
		super("&", SDFDatatype.BITVECTOR);
	}

	@Override
	public int getPrecedence() {
		return 2;
	}
	
	@Override
	public BitVector getValue() {
		BitVector left = getInputValue(0);
		BitVector right =  getInputValue(1);
		return BitVector.and(left,right);
	}

}
