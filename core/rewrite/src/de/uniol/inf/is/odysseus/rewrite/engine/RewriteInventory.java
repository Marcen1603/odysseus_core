package de.uniol.inf.is.odysseus.rewrite.engine;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;

public class RewriteInventory extends AbstractInventory {
	
	private static RewriteInventory instance = null;	

	public RewriteInventory() {
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
	
	@Override
	public AbstractInventory getCurrentInstance(){
		return getInstance();
	}
	
	@Override
	public String getInventoryName(){
		return "Rewrite";
	}		
}
