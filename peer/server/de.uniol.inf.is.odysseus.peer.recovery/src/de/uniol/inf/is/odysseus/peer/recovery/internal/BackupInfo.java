package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.io.Serializable;

import de.uniol.inf.is.odysseus.peer.recovery.simplestrategy.SimpleRecoveryStrategy;

public class BackupInfo implements Serializable {
	private static final long serialVersionUID = 1881155918463866490L;

	public String pql;
	public String state;
	public String sharedQuery;
	public boolean master;
	public String strategy = SimpleRecoveryStrategy.getStrategyName();
}
