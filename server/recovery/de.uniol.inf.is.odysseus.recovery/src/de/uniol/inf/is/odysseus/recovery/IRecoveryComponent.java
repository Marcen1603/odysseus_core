package de.uniol.inf.is.odysseus.recovery;

import java.util.List;

import com.google.common.collect.ImmutableCollection;

import de.uniol.inf.is.odysseus.recovery.systemlog.ISysLogEntry;

// TODO javaDoc
public interface IRecoveryComponent {
	
	public String getName();
	
	public ImmutableCollection<String> getDependencies();
	
	public void recover(List<ISysLogEntry> log) throws Exception;

}