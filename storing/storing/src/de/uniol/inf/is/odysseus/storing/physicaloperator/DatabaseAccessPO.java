package de.uniol.inf.is.odysseus.storing.physicaloperator;

import java.sql.Connection;
import java.util.List;

import de.uniol.inf.is.odysseus.metadata.ITimeInterval;
import de.uniol.inf.is.odysseus.physicaloperator.AbstractSource;
import de.uniol.inf.is.odysseus.physicaloperator.OpenFailedException;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.storing.DatabaseIterator;

public class DatabaseAccessPO extends AbstractSource<RelationalTuple<?>> {

	private String tableName;
	private Connection connection;
	private boolean timesenstiv;
	
	public DatabaseAccessPO(String tableName, Connection connection, boolean timeSensitiv){
		this.tableName = tableName;
		this.connection = connection;
		this.timesenstiv = timeSensitiv;
	}	
	
	public DatabaseAccessPO(DatabaseAccessPO original) {
		this.tableName = original.tableName;
		this.connection = original.connection;
	}

	@Override
	protected void process_open() throws OpenFailedException {
		DatabaseIterator di = new DatabaseIterator(this.tableName, this.getOutputSchema(), this.connection);
		if(this.timesenstiv){
			System.out.println("running in time sensitive mode");
			timesenstivTransfer(di);
		}else{
			System.out.println("running not in time sensitive mode");
			normalTransfer(di);
		}
		System.err.println("No more elements in database for table "+this.tableName);
		
	}

	@Override
	public DatabaseAccessPO clone() {	
		return new DatabaseAccessPO(this);
	}
	
	
	private void normalTransfer(DatabaseIterator di){
		while(di.hasNext()){
			for(RelationalTuple<?> t : di.next()){
				transfer(t);			
			}
		}
	}
	
	private void timesenstivTransfer(DatabaseIterator di){
		while(di.hasNext()){
			List<RelationalTuple<?>> list = di.next();
			for(int i=0;i<list.size();i++){
				RelationalTuple<?> current = list.get(i);
				transfer(list.get(i));
				if(i==(list.size()-1)){
					break;
				}
				RelationalTuple<?> next = list.get(i+1);				
				long currentStart = ((ITimeInterval)current.getMetadata()).getStart().getMainPoint();
				long nextStart = ((ITimeInterval)next.getMetadata()).getStart().getMainPoint();
				long timeout = nextStart - currentStart;
				try {
					wait(timeout);
				} catch (InterruptedException e) {					
					e.printStackTrace();
				}
			}						
			for(RelationalTuple<?> t : di.next()){
				transfer(t);			
			}
		}
	}

}
