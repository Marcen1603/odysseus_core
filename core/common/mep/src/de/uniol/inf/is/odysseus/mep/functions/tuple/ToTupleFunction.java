package de.uniol.inf.is.odysseus.mep.functions.tuple;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class ToTupleFunction extends AbstractFunction<Tuple<IMetaAttribute>> {

	private static final long serialVersionUID = -8696826583914890592L;

	public ToTupleFunction() {
		super("asTuple",1, null, SDFDatatype.TUPLE);	
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public Tuple<IMetaAttribute> getValue() {
		return (Tuple<IMetaAttribute>) getInputValue(0);
	}

}
