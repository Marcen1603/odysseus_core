package de.uniol.inf.is.odysseus.p2p_new.physicaloperator;

public enum JxtaConnectionType {
	JXTA ("Jxta"),
	TCP ("TCP/IP"),
	UDP ("UDP/IP"),
	NONE ("None"),
	OTHER ("Other");
	
	private final String name;
	
	private JxtaConnectionType( String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
}
