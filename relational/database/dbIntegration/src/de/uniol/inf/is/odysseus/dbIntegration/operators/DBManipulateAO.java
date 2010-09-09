package de.uniol.inf.is.odysseus.dbIntegration.operators;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DBManipulateAO extends UnaryLogicalOp {

	private static final long serialVersionUID = 8963355484143149376L;
	
	private DBQuery query; 
	
	
	public DBManipulateAO() {
		super();
	}
	
	public DBManipulateAO(String sql, String database) {
		super();
		
		this.query = new DBQuery(database, sql, true);
	}

	public DBManipulateAO(DBManipulateAO po) {
		super(po);
		query = po.getQuery();
	}
	
	public DBManipulateAO clone() {
		return new DBManipulateAO(this);
	}

	public DBQuery getQuery() {
		return query;
	}

	@Override
	public SDFAttributeList getOutputSchema() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
