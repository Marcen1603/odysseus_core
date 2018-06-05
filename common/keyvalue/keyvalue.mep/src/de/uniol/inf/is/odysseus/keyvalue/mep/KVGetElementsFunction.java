package de.uniol.inf.is.odysseus.keyvalue.mep;

import de.uniol.inf.is.odysseus.keyvalue.datatype.KeyValueObject;
import de.uniol.inf.is.odysseus.keyvalue.datatype.SDFKeyValueDatatype;
import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KVGetElementsFunction extends AbstractFunction<Tuple<IMetaAttribute>> {

	private static final long serialVersionUID = -7563778697648088501L;
	private static final SDFDatatype[][] acceptedTypes = new SDFDatatype[][] {
		new SDFDatatype[] { SDFKeyValueDatatype.KEYVALUEOBJECT}, {SDFDatatype.STRING}};

	public KVGetElementsFunction(){
		super("getElements", 2, acceptedTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Tuple<IMetaAttribute> getValue() {
		KeyValueObject<?> kv = getInputValue(0);
		String keys = getInputValue(1);
		Tuple<IMetaAttribute> tuple = null;
		if (keys != null){
			String[] keyList = keys.split(", ");
			tuple = new Tuple<>(keyList.length, false);
			for(int i=0;i<keyList.length;i++){
				 tuple.setAttribute(i, kv.getAttribute(keyList[i]));
			}
		}
		return tuple;

	}



}
