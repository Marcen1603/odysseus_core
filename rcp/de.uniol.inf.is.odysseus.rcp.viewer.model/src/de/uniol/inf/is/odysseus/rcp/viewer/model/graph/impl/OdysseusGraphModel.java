package de.uniol.inf.is.odysseus.rcp.viewer.model.graph.impl;

import de.uniol.inf.is.odysseus.base.IPhysicalOperator;
import de.uniol.inf.is.odysseus.base.planmanagement.query.IQuery;
import de.uniol.inf.is.odysseus.rcp.viewer.model.graph.IOdysseusGraphModel;

public class OdysseusGraphModel extends DefaultGraphModel<IPhysicalOperator> implements IOdysseusGraphModel {

	private final IQuery query;
	
	public OdysseusGraphModel() {
		this(null);
	}
	
	public OdysseusGraphModel( IQuery query ) {
		super();
		this.query = query;
	}
	
	@Override
	public IQuery getQuery() {
		return query;
	}
	
	@Override
	public String getName() {
		if( query != null ) 
			return "Query " + String.valueOf(query.getID());
		return super.getName();
	}

}
