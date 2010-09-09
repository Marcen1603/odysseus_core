package de.uniol.inf.is.odysseus.rewrite.engine;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;

public class RewriteInventory extends AbstractInventory {
	
	private static RewriteInventory instance = null;

	private RewriteInventory() {
		super();
	}

	public RewriteInventory(RewriteInventory currentinstance) {
		super(currentinstance);
	}

	public static synchronized RewriteInventory getInstance() {
		if (instance == null) {
			instance = new RewriteInventory();
		}
		return instance;
	}	

}
