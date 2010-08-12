package de.uniol.inf.is.odysseus.transform.engine;

import java.util.LinkedHashMap;
import java.util.PriorityQueue;

import de.uniol.inf.is.odysseus.ruleengine.rule.IRule;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.AbstractInventory;
import de.uniol.inf.is.odysseus.ruleengine.ruleflow.IRuleFlowGroup;

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
		// intentionally left blank
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
}
