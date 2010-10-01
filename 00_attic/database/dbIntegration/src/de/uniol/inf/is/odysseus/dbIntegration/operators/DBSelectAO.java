package de.uniol.inf.is.odysseus.dbIntegration.operators;

import java.util.ArrayList;
import java.util.List;

import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.logicaloperator.UnaryLogicalOp;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

/**
 * Dieser logische Operator DBSelectAO wird zur Erstellung eines logischen
 * Anfrageplans mit integrierten Datenbankanfragen benoetigt.
 * 
 * @author crolfes
 *
 */
public class DBSelectAO extends UnaryLogicalOp {

	private static final long serialVersionUID = -9088151794536927866L;

	
	private DBQuery query; 
	private List<String> options;
	private Controller dbController;
	private String alias;
	
	
	public DBSelectAO() {
		super();
	}
	
	public DBSelectAO(DBSelectAO po) {
		super(po);
		query = po.getQuery();
		options = po.getOptions();
		dbController = po.getController();
		alias = po.getAlias();
	}
	
	/**
	 * Der endgueltige Operator, welcher bei der Transformation 
	 * zu einem physischen Operator umgewandelt wird.
	 * 
	 * @param query - die Datenbankanfrage und der Verweis auf die angesprochene Datenbank.
	 * @param options - eine Liste mit Optionen, welche Einstellungen bezueglich Controller, 
	 * Cache und Prefetchkomponente ermoeglicht.
	 * @param dbController - der Controller
	 * @param alias - der Alias, durch den uebergeordnete Operatoren auf die Attribute der 
	 * Datenbankanfrage zugreifen koennen.
	 */
	public DBSelectAO(DBQuery query, List<String> options, Controller dbController, String alias) {
		super();
		this.alias = alias;
		this.query = query;
		this.options = options;
		this.dbController = dbController;
		
	}
	
	
	/**
	 * Dieser Konstruktor wird waehrend der Anfrageverarbeitung in den Visitorklassen genutzt. 
	 * 
	 * @param query - die Datenbankanfrage und der Verweis auf die angesprochene Datenbank.
	 * @param dbController - der Controller
	 * @param alias - der Alias, durch den uebergeordnete Operatoren auf die Attribute der 
	 * Datenbankanfrage zugreifen koennen.
	 */
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
			outAttrs.add((SDFAttribute)new SDFAttribute(alias, string));
		}
		SDFAttributeList outputSchema;
		if (getInputSchema() != null) {
			outputSchema = new SDFAttributeList(getInputSchema());
			outputSchema.addAll(outAttrs);
		} else {
			outputSchema = new SDFAttributeList(outAttrs);
		}
		return outputSchema;
	}
	
	public String getAlias() {
		return alias;
	}
	
	
	
}
