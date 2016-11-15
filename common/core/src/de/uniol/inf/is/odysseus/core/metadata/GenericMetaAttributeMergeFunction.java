package de.uniol.inf.is.odysseus.core.metadata;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;

public class GenericMetaAttributeMergeFunction implements IInlineMetadataMergeFunction<IGenericMetaAttribute> {

	@Override
	public void mergeInto(IGenericMetaAttribute result, IGenericMetaAttribute inLeft, IGenericMetaAttribute inRight) {
		List<SDFMetaSchema> all = new ArrayList<>();
		all.addAll(inLeft.getSchema());
		all.addAll(inRight.getSchema());
		
		Tuple<?> left = inLeft.getContent();
		Tuple<?> right = inRight.getContent();
		
		Tuple<?> newTuple = (Tuple<?>) left.append(right);
		result.setContent(newTuple, all);
			
	}

	@Override
	public IInlineMetadataMergeFunction<? super IGenericMetaAttribute> clone() {
		return new GenericMetaAttributeMergeFunction();
	}

	@Override
	public Class<? extends IMetaAttribute> getMetadataType() {
		return IGenericMetaAttribute.class;
	}

}
