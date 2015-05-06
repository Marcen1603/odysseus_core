package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

abstract public class AbstractToTupleFunction extends AbstractFunction<Tuple<IMetaAttribute>> {
	
	private static final long serialVersionUID = -2571631849135423566L;
	
	public AbstractToTupleFunction(int size){
		super("toTuple",size, null, SDFDatatype.TUPLE);
		
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		Tuple<IMetaAttribute> ret = new Tuple<>(getArity(), false);
		for (int i=0;i<getArity();i++){
			ret.setAttribute(i, getInputValue(i));
		}
		return ret;
	}

}
