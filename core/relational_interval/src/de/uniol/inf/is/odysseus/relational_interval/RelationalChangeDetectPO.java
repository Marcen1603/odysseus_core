package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;
import de.uniol.inf.is.odysseus.relational.base.Tuple;

public class RelationalChangeDetectPO extends ChangeDetectPO<Tuple<?>> {

	private int[] comparePositions;

	public RelationalChangeDetectPO(int[] comparePositions) {
		super();
		this.comparePositions = comparePositions;
	}

	public RelationalChangeDetectPO(RelationalChangeDetectPO pipe) {
		super(pipe);
	}

	@Override
	protected boolean compare(Tuple<?> object, Tuple<?> lastElement) {
		for (int i:comparePositions){
			if (!object.getAttribute(i).equals(lastElement.getAttribute(i))){
				return false;
			}
		}
		
		return true;
	}
	
	
}
