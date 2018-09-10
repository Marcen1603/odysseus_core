package de.uniol.inf.is.odysseus.systemload;

import java.util.Collection;

import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;

public interface ISystemLoad extends IMetaAttribute {
	
	void addSystemLoad( String name );
	void removeSystemLoad( String name );
	Collection<String> getSystemLoadNames();
	int getCpuLoad(String name);
	int getMemLoad(String name);
	int getNetLoad(String name);
	SystemLoadEntry getSystemLoad(String name);
	void insert(ISystemLoad other);

//	@Override
//	public ISystemLoad clone();
	
}
