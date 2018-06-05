package de.uniol.inf.is.odysseus.core.server.store.function;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.core.server.store.IStore;

public class KvNamedStoreRemoveFunction extends AbstractNamedStoreFunction<Object> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][] { { SDFDatatype.STRING }, { SDFDatatype.STRING } };

	public KvNamedStoreRemoveFunction() {
		super("kvremove", 2, accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		String storeKey = getInputValue(0);
		String valueKey = getInputValue(1);

		IStore<String, Object> store = getStore(storeKey);

		if (store != null) {
			Object val = store.remove(valueKey);
			return val;
		} else {
			return null;
		}
	}


}
