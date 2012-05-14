package de.uniol.inf.is.odysseus.relational_interval;

import de.uniol.inf.is.odysseus.core.server.physicaloperator.ChangeDetectPO;
import de.uniol.inf.is.odysseus.core.collection.Tuple;

public class RelationalChangeDetectPO extends ChangeDetectPO<Tuple<?>> {

	private int[] comparePositions;

	public RelationalChangeDetectPO(int[] comparePositions) {
		super();
		this.comparePositions = comparePositions;
		StringBuffer tmp = new StringBuffer(" ");
		for (int i:comparePositions){
			tmp.append(i).append(",");
		}
		setName(getName()+tmp);
	}

	public RelationalChangeDetectPO(RelationalChangeDetectPO pipe) {
		super(pipe);
		this.comparePositions = pipe.comparePositions;
	}

	@Override
	protected boolean areDifferent(Tuple<?> object, Tuple<?> lastElement) {
		for (int i:comparePositions){
			Object a = object.getAttribute(i);
			Object b = lastElement.getAttribute(i);
			if (!a.equals(b)){
				return true;
			}
		}
		
		return false;
	}
	
	
}
