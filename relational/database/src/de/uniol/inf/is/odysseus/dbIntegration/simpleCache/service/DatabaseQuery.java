package de.uniol.inf.is.odysseus.dbIntegration.simpleCache.service;


import java.util.LinkedList;

import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IDataAccess;

public class DatabaseQuery extends DBQuery{

	
	private LinkedList<Integer> params;
	private IDataAccess dal;
	
	public DatabaseQuery(DBQuery dbQuery, IDataAccess dal) {
		super(dbQuery.getDatabase(), dbQuery.getQuery(), dbQuery.isUpdate());
		this.dal = dal;
		params = new LinkedList<Integer>();
	}
	
	public LinkedList<Integer> getParams() {
		return params;
	}
	
	public void addParam(Integer key) {
		params.add(key);
	}
	
	public void removeParam(Integer key) {
		params.remove(key);
	}
	
	public IDataAccess getDataAccess() {
		return dal;
	}
	
	
//	
//	public List<RelationalTuple<?>> getData(RelationalTuple<?> param) {
//		param = dal.getRelevantParams(param);
//		if (queryParams.containsKey(param)) {
//			
//		}
//		return null;
//	}
	
}
