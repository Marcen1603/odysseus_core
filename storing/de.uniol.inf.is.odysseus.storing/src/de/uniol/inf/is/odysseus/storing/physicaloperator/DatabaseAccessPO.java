package de.uniol.inf.is.odysseus.storing.physicaloperator;

import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.storing.DatabaseIterator;

public class DatabaseAccessPO extends AbstractSource<RelationalTuple<?>> {

	private String name;
	
	public DatabaseAccessPO(String name){
		this.name = name;
	}	
	
	public DatabaseAccessPO(DatabaseAccessPO original) {
		this.name = original.name;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		DatabaseIterator di = new DatabaseIterator(this.name, this.getOutputSchema());
		while(di.hasNext()){
			for(RelationalTuple<?> t : di.next()){
				transfer(t);			
			}
		}
		System.err.println("No more elements in database for table "+this.name);
		
	}

	@Override
	public DatabaseAccessPO clone() {	
		return new DatabaseAccessPO(this);
	}

}
