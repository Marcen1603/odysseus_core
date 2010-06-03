package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateSensor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;

public class CreateSensorVisitor extends AbstractDefaultVisitor {

	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		System.out.println("Visit ASTCreateSensor(" + node + "," + data + ")");
		return null;
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) {
		// TODO Auto-generated method stub
		return null;
	}

}
