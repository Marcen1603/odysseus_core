package de.uniol.inf.is.odysseus.pnapproach.base.physicaloperator.window.helper;

import de.uniol.inf.is.odysseus.base.IClone;
import de.uniol.inf.is.odysseus.metadata.base.IMetaAttribute;

public interface IDataFactory<M_In extends IClone, M_Out extends IClone, In extends IMetaAttribute<M_In>, Out extends IMetaAttribute<M_Out>> {

	public Out createData(In inElem);
}
