package de.uniol.inf.is.odysseus.planmanagement.plan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule;

/**
 * This class is a base object for creating reoptimization rules for global plans. It
 * provides methods to store global plans and to send reoptimize
 * request to these global plans if this rule is valid.
 * 
 * @author Wolf Bauer
 * 
 */
public abstract class AbstractPlanReoptimizeRule 
	implements IReoptimizeRule<IPlan> {

	/**
	 * List of global plans which are informed if this rule is valid.
	 */
	protected List<IPlan> reoptimizable = Collections.synchronizedList(new ArrayList<IPlan>());
	
	/**
	 * Informs all registered global plans that this rule is valid.
	 */
	protected void fireReoptimizeEvent() {
		for (IPlan reoptimizableType : this.reoptimizable) {
			reoptimizableType.reoptimize();
		}
	}

	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#addReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void addReoptimieRequester(IPlan reoptimizable) {
		this.reoptimizable.add(reoptimizable);
	}
	
	/* (non-Javadoc)
	 * @see de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRule#removeReoptimieRequester(de.uniol.inf.is.odysseus.planmanagement.IReoptimizeRequester)
	 */
	@Override
	public void removeReoptimieRequester(IPlan reoptimizable) {
		this.reoptimizable.remove(reoptimizable);
	}
	
	public abstract void deinitialize();
}
