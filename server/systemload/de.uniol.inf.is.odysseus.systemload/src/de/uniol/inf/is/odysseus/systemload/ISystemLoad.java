package de.uniol.inf.is.odysseus.systemload;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface ISystemLoad extends IMetaAttribute {
	
	public void addSystemLoad( String name );
	public Collection<String> getSystemLoadNames();
	public int getCpuLoad(String name);
	public int getMemLoad(String name);
	public int getNetLoad(String name);
	
	@Override
	public ISystemLoad clone();

}
