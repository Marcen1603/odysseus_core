package de.uniol.inf.is.odysseus.systemload;

import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface ISystemLoad extends IMetaAttribute {
	
	public void addSystemLoad( String name );
	public Collection<String> getSystemLoadNames();
	public List<SystemLoadSnapshot> getSystemLoadSnapshots(String name);
	
	@Override
	public ISystemLoad clone();

}
