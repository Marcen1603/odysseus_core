package de.uniol.inf.is.odysseus.peer.recovery.internal;

import java.io.Serializable;

public class BackupInfo implements Serializable {
	private static final long serialVersionUID = 1881155918463866490L;
	
	public String pql;
	public String state;
}