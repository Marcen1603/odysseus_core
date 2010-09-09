package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttributeContainer;

public interface IDataFactory<M_In extends IMetaAttribute, M_Out extends IMetaAttribute, In extends IMetaAttributeContainer<M_In>, Out extends IMetaAttributeContainer<M_Out>> {

	public Out createData(In inElem);
}
