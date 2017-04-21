package de.uniol.inf.is.odysseus.core.server.store.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.store.IStore;

public class KvNamedStoreWriteFunction extends AbstractNamedStoreFunction<Boolean> {

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

		IStore<String, Object> store = getStore(storeKey);

		store.put(valueKey, value);
		return true;
	}

}
