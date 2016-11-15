package de.uniol.inf.is.odysseus.core.metadata;

import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class GenericMetaAttributeMergeFunction implements IInlineMetadataMergeFunction<IGenericMetaAttribute> {

	@Override
	public void mergeInto(IGenericMetaAttribute result, IGenericMetaAttribute inLeft, IGenericMetaAttribute inRight) {		
		Tuple<?> left = inLeft.getContent();
		Tuple<?> right = inRight.getContent();
		
		Tuple<?> newTuple = (Tuple<?>) left.append(right);
		result.setContent(newTuple);
			
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
