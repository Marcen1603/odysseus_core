package de.uniol.inf.is.odysseus.core.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

abstract public class AbstractBaseMetaAttribute extends AbstractMetaAttribute {
	
	private static final long serialVersionUID = -7497027906886410189L;

	@Override
	public void writeValues(List<Tuple<?>> values) {
		if (values.size() == 1){
			writeValue(values.get(0));
		}else{
			throw new IllegalArgumentException("Cannot write multiple values in single meta attribute");
		}
	}

	@Override
	public List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> getInlineMergeFunctions() {
		List<IInlineMetadataMergeFunction<? extends IMetaAttribute>> ret = new ArrayList<>();
		ret.add(getInlineMergeFunction());
		return ret;
	}
	
	abstract protected IInlineMetadataMergeFunction<? extends IMetaAttribute> getInlineMergeFunction();

	
}
