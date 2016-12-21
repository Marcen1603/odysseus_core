package de.uniol.inf.is.odysseus.mep.functions.kvstore;

import java.util.Map;

import de.uniol.inf.is.odysseus.core.sdf.schema.SDFDatatype;
import de.uniol.inf.is.odysseus.mep.AbstractFunction;

public class KvNamedStoreReadFunction extends AbstractFunction<Object> {

	private static final long serialVersionUID = -3263090969225261507L;

	public static SDFDatatype[][] accTypes = new SDFDatatype[][]{
		{SDFDatatype.STRING},{SDFDatatype.STRING}};

	public KvNamedStoreReadFunction(){
		super("kvread",2,accTypes, SDFDatatype.OBJECT, false);
	}

	@Override
	public Object getValue() {
		String storeKey = getInputValue(0);
		String valueKey = getInputValue(1);
		Map<String, Object> store = namedkeyValueStore.get(storeKey);

		if (store != null){
			return store.get(valueKey);
		}else{
			return null;
		}
	}

}
