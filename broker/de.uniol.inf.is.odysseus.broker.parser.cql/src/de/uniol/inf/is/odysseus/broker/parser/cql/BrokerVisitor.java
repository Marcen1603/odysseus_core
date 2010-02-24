package de.uniol.inf.is.odysseus.broker.parser.cql;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;

public class BrokerVisitor extends AbstractDefaultVisitor {

	@Override
	public Object visit(ASTBrokerSource node, Object data) {
		ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
		String name = ident.getName();

		// parse the nested substatement
		ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node
				.jjtGetChild(1);
		CQLParser v = new CQLParser();
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(
				childNode, null);

		// get existing broker or generate a new one
		//BrokerAO broker = new BrokerAO(name);
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
		broker.setOutputSchema(result.getOutputSchema());
		if (!BrokerDictionary.getInstance().brokerExists(name)) {
			BrokerDictionary.getInstance().addBroker(name, broker);
		} else {
			broker = BrokerDictionary.getInstance().getBroker(name);
		}

		// connect the source to broker
		broker.subscribeTo(result, result.getOutputSchema());
		// make it accessible like a normal source
		DataDictionary.getInstance().sourceTypeMap.put(name, "brokerStreaming");
		SDFEntity entity = new SDFEntity(name);
		//SDFAttributeList attributes = new SDFAttributeList();
		entity.setAttributes(broker.getOutputSchema());
		
		DataDictionary.getInstance().entityMap.put(name, entity);
		return broker;
	}

	public Object visit(ASTSimpleSource node,Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {					
			
			BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);			
			broker.setOutputSchema(BrokerDictionary.getInstance().getBroker(brokerName).getOutputSchema());
			return broker;
		}else{
			throw new RuntimeException("Broker "+brokerName+" not exists");
		}
		
	}
	
	public Object visit(ASTBrokerSelectInto node, Object data) {
		ASTSelectStatement statement = new ASTSelectStatement(0);
		int number = 0;
		
		// add selectClause
		statement.jjtAddChild(node.jjtGetChild(0), number);
		number++;
		//get broker name
		String brokerName = ((ASTIdentifier)node.jjtGetChild(1)).getName();
		
		//add rest from select
		for(int i=2;i<node.jjtGetNumChildren();i++){			
			statement.jjtAddChild(node.jjtGetChild(i), number);
			number++;			
		}
		CQLParser v = new CQLParser();
		AbstractLogicalOperator topOfSelectStatementOperator = (AbstractLogicalOperator) v.visit(
				statement, null);
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		// add the outputschema from existing one
		if(BrokerDictionary.getInstance().getBroker(brokerName)==null){
			throw new RuntimeException("Broker with name \""+brokerName+"\" not found. You have to create one first.");
		}
		broker.setOutputSchema(BrokerDictionary.getInstance().getBroker(brokerName).getOutputSchema());
		// check, if broker and top of select have the same attributelist		
		if(schemaEquals(topOfSelectStatementOperator.getOutputSchema(),broker.getOutputSchema())){
			broker.subscribeToSource(topOfSelectStatementOperator, 0, 0, topOfSelectStatementOperator.getOutputSchema());		
		}else{			
			String message = "Schema to insert: "+topOfSelectStatementOperator.getOutputSchema().toString()+"\n";
			message = message + "Schema of Broker: "+broker.getOutputSchema().toString();
			throw new RuntimeException("Statement and broker have not the same schema.\n"+message);
		}
		return broker;
		
	}
	
	private boolean schemaEquals(SDFAttributeList left, SDFAttributeList right){
		if(left.size()!=right.size()){
			return false;
		}else{
			for(int i=0;i<left.size();i++){
				if(!left.get(i).getAttributeName().equals(right.get(i).getAttributeName())){
					return false;
				}
			}
			return true;
		}
	}
}
