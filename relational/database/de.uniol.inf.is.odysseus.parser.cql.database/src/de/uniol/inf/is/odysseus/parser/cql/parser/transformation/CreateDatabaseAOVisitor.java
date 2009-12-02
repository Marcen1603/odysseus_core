package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.ArrayList;

import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.dbIntegration.control.Controller;
import de.uniol.inf.is.odysseus.dbIntegration.model.DBQuery;
import de.uniol.inf.is.odysseus.dbIntegration.operators.DBManipulateAO;
import de.uniol.inf.is.odysseus.dbIntegration.operators.DBSelectAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAS;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBExecuteStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDBSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabase;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTDatabaseOptions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSQL;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;

public class CreateDatabaseAOVisitor extends AbstractDefaultVisitor {

	ILogicalOperator logicalOp;
	String sql;
	String database;
	SDFAttributeList attrList;
	ArrayList<String> dsAttributes;
	ArrayList<String> dbOptions = new ArrayList<String>();
	String alias;

	@Override
	public Object visit(ASTDBExecuteStatement node, Object data) {
		node.childrenAccept(this, data);
		logicalOp = new DBManipulateAO(sql, database);

		return logicalOp;
	}

	@Override
	public Object visit(ASTDBSelectStatement node, Object data) {
		node.childrenAccept(this, data);
		if (data != null) {
			SDFAttributeList inputSchema = ((ILogicalOperator) data)
					.getOutputSchema();
			DBQuery query = new DBQuery(database, sql, false);
			Controller dbController = new Controller(query, dbOptions,
					inputSchema);
			logicalOp = new DBSelectAO(query, dbOptions, dbController, alias);
			logicalOp.subscribeTo((ILogicalOperator) data, 0, 0, inputSchema);
		} else {
			DBQuery query = new DBQuery(database, sql, false);
			Controller dbController = new Controller(query);
			logicalOp = new DBSelectAO(query, dbController, alias);
		}

		return logicalOp;
	}

	@Override
	public Object visit(ASTDatabase node, Object data) {
		database = node.jjtGetChild(0).toString();
		return null;
	}

	@Override
	public Object visit(ASTSQL node, Object data) {
		// sql = node.jjtGetChild(0).toString();
		// sql = sql.replaceAll("\"", "");
		sql = node.getValue();
		sql = sql.replace("[", "");
		sql = sql.replace("]", "");
		return null;
	}

	@Override
	public Object visit(ASTDatabaseOptions node, Object data) {
		dbOptions.add(node.jjtGetChild(0).toString());
		return null;
	}

	private ArrayList<String> findDataStreamAttr(String sql) {
		String[] list = new String[0];
		list = sql.split("[^\\$\\w*\\.]");
		ArrayList<String> result = new ArrayList<String>();

		for (String string : list) {
			if (string.startsWith("$")) {
				result.add(string.replaceFirst("\\$", "").trim());
			}
		}
		// for (String string : result) {
		// System.out.println(string);
		// }

		return result;
	}

	public Object visit(ASTAS node, Object data) {
		alias = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		return null;
	}

}
