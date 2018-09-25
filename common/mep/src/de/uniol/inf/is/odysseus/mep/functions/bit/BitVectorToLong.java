package de.uniol.inf.is.odysseus.mep.functions.bit;

import java.math.BigInteger;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class BitVectorToLong extends AbstractFunction<Long> {

	private static final long serialVersionUID = 1609842016673128616L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.BITVECTOR}};
	
	public BitVectorToLong(){
		super("toLong", 1, accTypes, SDFDatatype.LONG, false);
	}
	
	@Override
	public Long getValue() {
		BitVector v = (BitVector) getInputValue(0);
		BigInteger bi = new BigInteger(v.getBytes());
		return bi.longValue();
	}
}