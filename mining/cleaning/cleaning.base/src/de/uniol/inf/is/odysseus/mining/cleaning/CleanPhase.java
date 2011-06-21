package de.uniol.inf.is.odysseus.mining.cleaning;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.uniol.inf.is.odysseus.logicaloperator.ILogicalOperator;
import de.uniol.inf.is.odysseus.mining.cleaning.model.Cleaning;
import de.uniol.inf.is.odysseus.mining.model.IPhase;

public class CleanPhase implements IPhase {
	
	private List<Cleaning> cleanings = new ArrayList<Cleaning>();
	
	public CleanPhase(){		
	}
	
	
	@Override
	public Collection<? extends ILogicalOperator> buildLogicalPlan() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void addCleaning(Cleaning cleaning){
		this.cleanings.add(cleaning);
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();		
		sb.append("Cleanings:\n");
		for(Cleaning c : this.cleanings){
			sb.append("  "+c.toString()+"\n");
		}
		return sb.toString();
	}

}
