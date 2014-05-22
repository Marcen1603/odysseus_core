package de.uniol.inf.is.odysseus.systemload;

import java.io.Serializable;
import java.util.List;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

public final class SystemLoadEntry implements Serializable {

	private static final long serialVersionUID = 7962567634409661546L;
	
	private final String name;
	private final List<SystemLoadSnapshot> loads = Lists.newArrayList();
	
	public SystemLoadEntry(String name) {
		Preconditions.checkArgument(!Strings.isNullOrEmpty(name), "Name for systemload entry must not be empty or null!");
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void addSystemLoad() {
		loads.add(new SystemLoadSnapshot());
	}
	
	void addExistingSystemLoadSnapshots(List<SystemLoadSnapshot> shots) {
		loads.addAll(shots);
	}
	
	public List<SystemLoadSnapshot> getSystemLoadSnapshots() {
		return loads;
	}
}
