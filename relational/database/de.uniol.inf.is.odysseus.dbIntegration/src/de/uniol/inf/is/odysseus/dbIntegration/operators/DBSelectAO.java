package de.uniol.inf.is.odysseus.dbIntegration.operators;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.logicaloperator.base.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class DBSelectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9088151794536927866L;

	
	private DBQuery query; 
	private List<String> options;
	private Controller dbController;
	private String alias;
//	private SDFAttributeList outputSchema;
	
	
	public DBSelectAO() {
		super();
	}
	
	
	
//	public DBSelectAO(String sql, String database, List<String> options, Controller controller) {
//		super();
//		this.options = options;
//		this.query = new DBQuery(database, sql, false);
//		this.dbController = controller;
//	}


	public DBSelectAO(DBSelectAO po) {
		super(po);
	}
	
	public DBSelectAO(DBQuery query, List<String> options, Controller dbController, String alias) {
		super();
		this.alias = alias;
		this.query = query;
		this.options = options;
		this.dbController = dbController;
		
	}
	
	public DBSelectAO(DBQuery query, Controller dbController, String alias) {
		super();
		this.alias = alias;
		this.query = query;
		this.dbController = dbController;
		
	}



	public DBSelectAO clone() {
		return new DBSelectAO(this);
	}



	public DBQuery getQuery() {
		return query;
	}



	public List<String> getOptions() {
		return options;
	}



	public Controller getController() {
		return dbController;
	}



	public void setController(Controller dbController) {
		this.dbController = dbController;
	}



	public void setQuery(DBQuery query) {
		this.query = query;
	}



	public void setOptions(List<String> options) {
		this.options = options;
	}



	@Override
	public SDFAttributeList getOutputSchema() {
		
		List<String> out = dbController.getOutAttributeSchema();
		
		List<SDFAttribute> outAttrs = new ArrayList<SDFAttribute>();
		
		for (String string : out) {
			outAttrs.add((SDFAttribute)new CQLAttribute(alias, string));
		}
		SDFAttributeList outputSchema;
		if (getInputSchema() != null) {
			outputSchema = new SDFAttributeList(getInputSchema());
			outputSchema.addAll(outAttrs);
		} else {
			outputSchema = new SDFAttributeList(outAttrs);
		}
		
//		SDFAttributeList outputSchema;
		
		
//		outputSchema = new SDFAttributeList(outAttrs);
//		if (getInputSchema() != null) {
//			SDFAttributeList input = getInputSchema();
//			outputSchema.addAll(getInputSchema());
//		}
		
		return outputSchema;
	}
	public String getAlias() {
		return alias;
	}
	
	
	
}
