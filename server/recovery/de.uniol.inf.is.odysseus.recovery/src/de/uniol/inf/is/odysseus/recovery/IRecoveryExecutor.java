package de.uniol.inf.is.odysseus.recovery;

import java.util.List;

// TODO javaDoc
public interface IRecoveryExecutor {
	
	public String getName();
	
	public void recover(List<Integer> queryIds) throws Exception;
	
	public void activateBackup(List<Integer> queryIds);
	
	public void deactivateBackup(List<Integer> queryIds);
	
}