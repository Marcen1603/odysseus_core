package de.uniol.inf.is.odysseus.mep.functions.kvstore;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvStoreRemoveFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING}};

	public KvStoreRemoveFunction(){
		super("kvremove",1,accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		String key = getInputValue(0);
		return keyValueStore.remove(key);
	}

}
