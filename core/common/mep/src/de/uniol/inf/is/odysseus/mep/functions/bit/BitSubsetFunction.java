package de.uniol.inf.is.odysseus.mep.functions.bit;

import de.uniol.inf.is.odysseus.core.collection.BitVector;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class BitSubsetFunction extends AbstractFunction<BitVector> {

	private static final long serialVersionUID = 7553187248074371162L;
	private static final SDFDatatype[][] accTypes = new SDFDatatype[][] { new SDFDatatype[]{SDFDatatype.BITVECTOR},new SDFDatatype[]{SDFDatatype.INTEGER},new SDFDatatype[]{SDFDatatype.INTEGER}};

	public BitSubsetFunction() {
		super("subset", 3, accTypes, SDFDatatype.BITVECTOR, false);
	}

	@Override
	public BitVector getValue() {
		BitVector in = getInputValue(0);
		int start = getNumericalInputValue(1).intValue();
		int end = getNumericalInputValue(2).intValue();
		int size = end-start+1;
		BitVector out = new BitVector(size);
		for (int i=0;i<size;i++){
			out.setBit(i, in.getBit(start+i));
		}
		return out;
		
	}

}
