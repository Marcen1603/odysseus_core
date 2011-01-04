package de.uniol.inf.is.odysseus.datamining.classification;

import de.uniol.inf.is.odysseus.metadata.IMetaAttribute;

public interface IClassifier<T extends IMetaAttribute> {

	public Object getClassLabel(RelationalClassificationObject<T> tuple);
}
