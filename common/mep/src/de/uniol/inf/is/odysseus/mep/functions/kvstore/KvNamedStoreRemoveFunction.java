package de.uniol.inf.is.odysseus.mep.functions.kvstore;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvNamedStoreRemoveFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING},{SDFDatatype.STRING}};

	public KvNamedStoreRemoveFunction(){
		super("kvremove",2,accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		String storeKey = getInputValue(0);
		String valueKey = getInputValue(1);
		Map<String, Object> store = namedkeyValueStore.get(storeKey);

		if (store != null){
			Object val = store.remove(valueKey);
			if (store.size() == 0){
				namedkeyValueStore.remove(storeKey);
			}
			return val;
		}else{
			return null;
		}
	}

}
