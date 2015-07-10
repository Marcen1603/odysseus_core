package de.uniol.inf.is.odysseus.recovery.gaprecovery;

import java.util.List;

import de.uniol.inf.is.odysseus.recovery.IRecoveryExecutor;

// TODO javaDoc
public class GapRecoveryExecutor implements IRecoveryExecutor {

	@Override
	public String getName() {
		return "GapRecovery";
	}

	@Override
	public void recover(List<Integer> queryIds) throws Exception {
		// TODO implement
	}

	@Override
	public void activateBackup(List<Integer> queryIds) {
		// TODO implement
	}

	@Override
	public void deactivateBackup(List<Integer> queryIds) {
		// TODO implement
	}
	
}