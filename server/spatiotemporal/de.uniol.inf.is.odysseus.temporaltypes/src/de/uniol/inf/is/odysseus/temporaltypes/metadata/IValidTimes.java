package de.uniol.inf.is.odysseus.temporaltypes.metadata;

import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IValidTimes extends IMetaAttribute {
	
	public List<IValidTime> getValidTimes();

}
