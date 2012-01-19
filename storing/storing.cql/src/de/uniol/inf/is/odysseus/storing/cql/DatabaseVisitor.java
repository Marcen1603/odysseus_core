/** Copyright [2011] [The Odysseus Team]
  *
  * Licensed under the Apache License, Version 2.0 (the "License");
  * you may not use this file except in compliance with the License.
  * You may obtain a copy of the License at
  *
  *     http://www.apache.org/licenses/LICENSE-2.0
  *
  * Unless required by applicable law or agreed to in writing, software
  * distributed under the License is distributed on an "AS IS" BASIS,
  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  * See the License for the specific language governing permissions and
  * limitations under the License.
  */
package de.uniol.inf.is.odysseus.storing.cql;

import java.sql.Connection;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.uniol.inf.is.odysseus.datadictionary.IDataDictionary;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateFromDatabase;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.storing.DatabaseService;
import de.uniol.inf.is.odysseus.storing.logicaloperator.DatabaseAccessAO;
import de.uniol.inf.is.odysseus.usermanagement.ISession;

public class DatabaseVisitor{

	private ISession caller;
	private String name;
	private Connection connection;
	
	private IDataDictionary dd = null;
	
	protected volatile static Logger LOGGER = LoggerFactory.getLogger(DatabaseVisitor.class);

	public void setName(String name) {
		this.name = name;
	}
	
	public void setDataDictionary(IDataDictionary dd) {
		this.dd = dd;
	}

	public void setUser(ISession user) {
		this.caller = user;
	}

	public Object visit(ASTCreateFromDatabase node, Object data) {
//		DatabaseAccessAO access = null;
//		
//		if (node.jjtGetNumChildren() > 1) {
//			if (node.jjtGetChild(0) instanceof ASTJdbcIdentifier) {
//				String jdbc = ((ASTJdbcIdentifier) node.jjtGetChild(0)).getConnection();
//				String name = ((ASTIdentifier) node.jjtGetChild(1)).getName();
//				boolean sensitiv = false;
//				if (node.jjtGetNumChildren() == 3) {
//					sensitiv = true;
//				}
//				access = getAccessAOForJDBC(jdbc, name, sensitiv);
//			} else {
//				String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
//				access = getAccessAOForDefault(name, true);
//			}
//		} else {
//			String name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
//			access = getAccessAOForDefault(name, false);
//		}
//		dd.setStream(name, access, caller);
//		return access;
		return null;
	}

	private DatabaseAccessAO getAccessAOForDefault(String tableName, boolean isTimeSensitiv) {
		DatabaseAccessAO dba = null;
		try {
			dba = new DatabaseAccessAO(getSource(name), DatabaseService.getDefaultConnection(), tableName, isTimeSensitiv);
		} catch (SQLException e) {
			LOGGER.error("No Default Database Connection",e.getStackTrace());
		}
		return dba;
	}

	private DatabaseAccessAO getAccessAOForJDBC(String jdbcString, String tableName, boolean isTimeSensitiv) {
		try {
			connection = DatabaseService.getbyDefaultUser(jdbcString);
			DatabaseAccessAO databaseAccessAO = new DatabaseAccessAO(new SDFSource(tableName, "databaseReading"),connection,tableName,isTimeSensitiv);
			return databaseAccessAO;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	private SDFSource getSource(String sourceName) {
		return new SDFSource(sourceName, "databaseReading");
	}

//	public Object visit(ASTInsertIntoStatement node, Object data) throws QueryParseException {		
//		boolean create 			= false;
//		boolean truncate		= false;	
//		boolean ifnotexists		= false;
//		boolean savemetadata	= false;
//		
//		ASTComplexSelectStatement complexSelectStatement 	= null;
//		String jdbcString 									= null;
//		
//		ASTDatabaseTableOptions tableOps = (ASTDatabaseTableOptions) node.jjtGetChild(0);
//		
//		if(tableOps.jjtGetChild(0) instanceof ASTDatabaseCreateOption){
//			create = true;
//			name = ((ASTIdentifier)tableOps.jjtGetChild(0).jjtGetChild(0)).getName();
//			if(tableOps.jjtGetChild(0).jjtGetNumChildren()>1){
//				ifnotexists = true;
//			}	
//		}
//		else if(tableOps.jjtGetChild(0) instanceof ASTDatabaseTruncateOption){
//			truncate = true;
//			name = ((ASTIdentifier)tableOps.jjtGetChild(0).jjtGetChild(0)).getName();
//		}
//		else if(tableOps.jjtGetChild(0) instanceof ASTIdentifier){
//			name = ((ASTIdentifier)tableOps.jjtGetChild(0)).getName();
//		}
//		
//		if (node.jjtGetChild(1) instanceof ASTJdbcIdentifier) {
//			jdbcString = ((ASTJdbcIdentifier) node.jjtGetChild(1)).getConnection();
//		} 
//
//		if (node.jjtGetChild(2) instanceof ASTSaveMetaData) {
//			savemetadata = true;
//		}
//
//		try {
//			connection = DatabaseService.getbyDefaultUser(jdbcString);
//		} catch (SQLException e) {
//			LOGGER.error("SQLException: ",e.getStackTrace());
//		}
//		
//		int last = node.jjtGetNumChildren()-1;
//		complexSelectStatement = (ASTComplexSelectStatement)node.jjtGetChild(last);
//		
//		CQLParser cqlparser = (CQLParser) CQLParser.getInstance();
//
//		
//		//Soll das so bleiben??? s
//		//GET GLOBAL USER STATE FOR: User and Data Dictionary
//		cqlparser.setUser(caller);
//		cqlparser.setDataDictionary(GlobalState.getActiveDatadictionary());
//		
//		AbstractLogicalOperator result = (AbstractLogicalOperator)cqlparser.visit(complexSelectStatement, null);		
//		
//		DatabaseSinkAO databaseSink = new DatabaseSinkAO(connection, name, savemetadata, create, truncate, ifnotexists);
//		
//		if(savemetadata){
//			TimestampToPayloadAO timestampToPayloadAO = new TimestampToPayloadAO();
//			result.subscribeSink(timestampToPayloadAO, 0, 0, result.getOutputSchema());
//			timestampToPayloadAO.subscribeSink(databaseSink, 0, 0, timestampToPayloadAO.getOutputSchema());
//		}
//		else{
//			result.subscribeSink(databaseSink, 0, 0, result.getOutputSchema());
//		}
//	
//		return databaseSink;
//	}

}
