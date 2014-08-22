package de.uniol.inf.is.odysseus.wrapper.shiproutes.iec.element;

import java.util.List;

import de.uniol.inf.is.odysseus.core.collection.KeyValueObject;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface IIecElement {
	
	public void fillMap(KeyValueObject<? extends IMetaAttribute> map, String prefix);
	
	public String toXML();
	
	public boolean hasSubelements();

	public boolean isValid();
	
	public void addExtension(IECExtension extension);
	
	public List<IECExtension> getExtensions();

}
