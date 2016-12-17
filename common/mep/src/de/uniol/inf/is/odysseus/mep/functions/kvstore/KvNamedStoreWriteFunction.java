package de.uniol.inf.is.odysseus.mep.functions.kvstore;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvNamedStoreWriteFunction extends AbstractFunction<Boolean> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING},{SDFDatatype.STRING}, SDFDatatype.SIMPLE_TYPES};

	public KvNamedStoreWriteFunction(){
		super("kvwrite",3,accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Boolean getValue() {
		String storeKey = getInputValue(0);
		String valueKey = getInputValue(1);
		Object value = getInputValue(2);

		Map<String, Object> store = namedkeyValueStore.get(storeKey);

		if (store == null){
			store = new HashMap<>();
			namedkeyValueStore.put(storeKey, store);
		}

		store.put(valueKey, value);
		return true;
	}

}
