package de.offis.client;

import java.io.Serializable;

/**
 * Scai Data Model for OperatorGroup.
 *
 * @author Alexander Funk
 * 
 */
public class OperatorGroup extends AbstractScaiDTO implements Serializable {

	private static final long serialVersionUID = -279106109400492544L;
	private String name;
	private boolean running;

	public OperatorGroup(String name, boolean running) {
		super(ScaiType.OPERATOR_GROUP, Type.NONE);
		this.name = name;
		this.running = running;
	}
	
	protected OperatorGroup() {
		super(ScaiType.OPERATOR_GROUP, Type.NONE);
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isRunning() {
		return running;
	}
}
