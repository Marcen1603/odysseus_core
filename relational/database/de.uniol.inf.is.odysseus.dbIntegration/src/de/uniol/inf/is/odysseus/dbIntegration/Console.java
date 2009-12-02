package de.uniol.inf.is.odysseus.dbIntegration;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.osgi.framework.console.CommandInterpreter;
import org.eclipse.osgi.framework.console.CommandProvider;
import org.osgi.service.prefs.BackingStoreException;

import de.uniol.inf.is.odysseus.base.IMetaAttribute;
import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.dataAccess.DataAccess;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBProperties;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.serviceInterfaces.IConnectionData;
import de.uniol.inf.is.odysseus.relational.base.RelationalTuple;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;

public class Console implements CommandProvider {

	
	IConnectionData connectionData;
	DataAccess dal;
	Controller controller;
	
	
	public void _setConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		String schema = "auction";
		String user = "root";
		String password = "";
		String url = "jdbc:mysql://localhost:3306/auction";
		String driver = "com.mysql.jdbc.Driver";
		DBProperties prop = new DBProperties(database, url, driver, user, password, schema);
		
		
		
		
		try {
			connectionData.addConnection(prop);
			commandInterpreter.println();
			_testConn(commandInterpreter);
		} catch (BackingStoreException e) {
			commandInterpreter.println("Error");
			e.printStackTrace();
		}
	}
	
	public void _testConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		try {
			DBProperties prop = connectionData.getConnection(database);
			if (prop != null) {
				commandInterpreter.println("database: " + prop.getDatabase());
				commandInterpreter.println("driver: " + prop.getDriverClass());
				commandInterpreter.println("password: " + prop.getPassword());
				commandInterpreter.println("schema: " + prop.getSchema());
				commandInterpreter.println("url: " + prop.getUrl());
				commandInterpreter.println("user: " + prop.getUser());
			} else {
				commandInterpreter.println("Connection " + database + " not exists!");
			}
			
			
		} catch (BackingStoreException e) {
			commandInterpreter.println("Error: Connection not possible!");
			e.printStackTrace();
		}
	}
	
	public void _deleteConn(CommandInterpreter commandInterpreter) {
		String database = "auction";
		
		try {
			connectionData.deleteConnection(database);
			commandInterpreter.println("Connection " + database + " deleted!");
		} catch (BackingStoreException e) {
			e.printStackTrace();
		}
	}
	
	public void _setInsertQuery(CommandInterpreter commandInterpreter) {
		
		
		boolean isUpdate = true;
		
		String database = "auction";
		String sql = "INSERT INTO auctionEnd " +
				"(buyer, price, itemId, time) " +
				"VALUES " +
				"($buyer.name, $bid.price, $item.id, now())";
		
		
		DBQuery dbQuery = new DBQuery(database, sql, isUpdate);
		
		//Testdaten liste
		List<SDFAttribute> attrList = new ArrayList<SDFAttribute>();
		attrList.add((SDFAttribute) new CQLAttribute("buyer", "name"));
		attrList.add((SDFAttribute) new CQLAttribute("buyer", "address"));
		attrList.add((SDFAttribute) new CQLAttribute("bid", "price"));
		attrList.add((SDFAttribute) new CQLAttribute("bid", "item"));
		attrList.add((SDFAttribute) new CQLAttribute("item", "id"));
		attrList.add((SDFAttribute) new CQLAttribute("item", "name"));
		attrList.add((SDFAttribute) new CQLAttribute("seller", "name"));
		SDFAttributeList sdfList = new SDFAttributeList(attrList);
		
		controller = new Controller(dbQuery, sdfList);
//		dal = new DataAccess(dbQuery, sdfList);
	}
	
	public void _testInsertExecute(CommandInterpreter commandInterpreter) {
		Object[] tupleObjs = new Object [7];
		
		tupleObjs[0] = "Hugo";
		tupleObjs[1] = "Oldenburg";
		tupleObjs[2] = 15;
		tupleObjs[3] = 2222;
		tupleObjs[4] = 2222;
		tupleObjs[5] = "Fernseher";
		tupleObjs[6] = "Egon";
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(tupleObjs);
		dal.executeBaseQuery(tuple);
		
	}
	
	
	public void _setSelectQuery(CommandInterpreter commandInterpreter) {
		String database = "auction";
		String sql = "SELECT itemId, time " +
				"FROM auctionEnd " +
				"WHERE itemId = $item.id";
		boolean isUpdate = false;
		
		DBQuery dbQuery = new DBQuery(database, sql, isUpdate);
		
		//Testdaten liste
		List<SDFAttribute> attrList = new ArrayList<SDFAttribute>();
		attrList.add((SDFAttribute) new CQLAttribute("buyer", "name"));
		attrList.add((SDFAttribute) new CQLAttribute("buyer", "address"));
		attrList.add((SDFAttribute) new CQLAttribute("bid", "price"));
		attrList.add((SDFAttribute) new CQLAttribute("bid", "item"));
		attrList.add((SDFAttribute) new CQLAttribute("item", "id"));
		attrList.add((SDFAttribute) new CQLAttribute("item", "name"));
		attrList.add((SDFAttribute) new CQLAttribute("seller", "name"));
		SDFAttributeList sdfList = new SDFAttributeList(attrList);
		
		
		dal = new DataAccess(dbQuery, sdfList);
	}

	public void _testSelectExecute(CommandInterpreter commandInterpreter) {
		Object[] tupleObjs = new Object [7];
		
		tupleObjs[0] = "Hugo";
		tupleObjs[1] = "Oldenburg";
		tupleObjs[2] = 15;
		tupleObjs[3] = 2222;
		tupleObjs[4] = 2222;
		tupleObjs[5] = "Fernseher";
		tupleObjs[6] = "Egon";
		
		RelationalTuple<IMetaAttribute> tuple = new RelationalTuple<IMetaAttribute>(tupleObjs);
		dal.executeBaseQuery(tuple);
		
	}
	
	
	
	
	@Override
	public String getHelp() {
		StringBuilder help = new StringBuilder();
		help.append("---Database Integration---").append(
		    "test - Test starten");
		return help.toString();
	}
	
	protected void setConnectionService(IConnectionData connectionData) {
		this.connectionData = connectionData;
	}
	
	protected void unsetConnectionService(IConnectionData connectionData) {
		this.connectionData = null;
	}
	
	
}
