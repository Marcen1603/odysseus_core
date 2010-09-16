package de.uniol.inf.is.odysseus.transform.engine;

import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;

/**
 * Handles the global state: current loaded rules and current workflow
 * 
 * 
 * @author Dennis Geesen
 * 
 */
public class TransformationInventory extends AbstractInventory {

	private static TransformationInventory instance = null;

	private TransformationInventory(){	
		super();		
	}
	
	public TransformationInventory(AbstractInventory inventory) {
		super(inventory);
	}

	public static synchronized TransformationInventory getInstance() {
		if (instance == null) {
			instance = new TransformationInventory();
		}
		return instance;
	}
	
	public AbstractInventory getCurrentInstance(){
		return getInstance();
	}
	
	public String getInventoryName(){
		return "Transform";
	}	
}
