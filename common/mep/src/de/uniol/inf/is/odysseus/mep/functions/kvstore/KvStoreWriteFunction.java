package de.uniol.inf.is.odysseus.mep.functions.kvstore;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvStoreWriteFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING}, {SDFDatatype.OBJECT, SDFDatatype.TUPLE, SDFDatatype.LIST}};

	public KvStoreWriteFunction(){
		super("kvwrite",2,accTypes, SDFDatatype.BOOLEAN, false);
	}

	@Override
	public Boolean getValue() {
		String key = getInputValue(0);
		Object value = getInputValue(1);
		keyValueStore.put(key, value);
		return true;
	}

}
