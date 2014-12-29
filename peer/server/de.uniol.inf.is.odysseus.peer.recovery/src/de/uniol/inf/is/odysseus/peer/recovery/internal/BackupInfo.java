package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.io.Serializable;

public class BackupInfo implements Serializable {
	private static final long serialVersionUID = 1881155918463866490L;
	
	public String pql;
	public String state;
	public String sharedQuery;
	public boolean master;
	public String strategy = "simple";	// TODO implement simple strategy and take it's name as default
}
