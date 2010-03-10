package de.uniol.inf.is.odysseus.broker.parser.cql;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.transaction.GraphUtils;
import de.uniol.inf.is.odysseus.broker.transaction.TransactionType;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

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
		
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
		broker.setOutputSchema(result.getOutputSchema());
		if (!BrokerDictionary.getInstance().brokerExists(name)) {
			BrokerDictionary.getInstance().addBroker(name, broker.getOutputSchema());
		}

		// connect the source to broker
		int inPort = BrokerDictionary.getInstance().addNewWriteTransaction(name, TransactionType.Write.Continuous);
		broker.subscribeToSource(result, inPort, 0, result.getOutputSchema());		
		// make it accessible like a normal source
		DataDictionary.getInstance().sourceTypeMap.put(name, "brokerStreaming");
		SDFEntity entity = new SDFEntity(name);		
		entity.setAttributes(broker.getOutputSchema());		
		DataDictionary.getInstance().entityMap.put(name, entity);
		return broker;
	}

	public Object visit(ASTSimpleSource node,Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {					
			
			BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);			
			broker.setOutputSchema(BrokerDictionary.getInstance().getOutputSchema(brokerName));
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
		if(!BrokerDictionary.getInstance().brokerExists(brokerName)){
			throw new RuntimeException("Broker with name \""+brokerName+"\" not found. You have to create one first.");
		}
		broker.setOutputSchema(BrokerDictionary.getInstance().getOutputSchema(brokerName));
		// check, if broker and top of select have the same attributelist		
		if(schemaEquals(topOfSelectStatementOperator.getOutputSchema(),broker.getOutputSchema())){
			// determine transaction type			
			TransactionType.Write transType = TransactionType.Write.Continuous;
			if(GraphUtils.isCyclic(topOfSelectStatementOperator, broker.getIdentifier())){
				transType = TransactionType.Write.Cyclic;
			}
			int inPort = BrokerDictionary.getInstance().addNewWriteTransaction(brokerName, transType);			
			broker.subscribeToSource(topOfSelectStatementOperator, inPort, 0, topOfSelectStatementOperator.getOutputSchema());
		}else{			
			String message = "Schema to insert: "+topOfSelectStatementOperator.getOutputSchema().toString()+"\n";
			message = message + "Schema of Broker: "+broker.getOutputSchema().toString();
			throw new RuntimeException("Statement and broker do not have the same schema.\n"+message);
		}
		return broker;
		
	}
	
	public Object visit(ASTCreateBroker node, Object data) {
		String brokerName = ((ASTIdentifier)node.jjtGetChild(0)).getName();
		//check first if name already exists
		if(BrokerDictionary.getInstance().brokerExists(brokerName)){
			throw new RuntimeException("There is already a broker named \""+brokerName+"\".");
		}
		
		//parse attributes
		SDFAttributeList attributes = new SDFAttributeList();
		ASTAttributeDefinitions attributeDe = (ASTAttributeDefinitions) node.jjtGetChild(1);
		for(int i=0;i<attributeDe.jjtGetNumChildren();i++){
			ASTAttributeDefinition attrNode = (ASTAttributeDefinition)attributeDe.jjtGetChild(i);
			String attrName = ((ASTIdentifier) attrNode.jjtGetChild(0)).getName();
			SDFAttribute attribute = new SDFAttribute(brokerName, attrName);
			ASTAttributeType astAttrType = (ASTAttributeType) attrNode.jjtGetChild(1);
			attribute.setDatatype(astAttrType.getType());
			if (SDFDatatypes.isDate(attribute.getDatatype())) {
				attribute.addDtConstraint("format", astAttrType.getDateFormat());
			}
			attribute.setCovariance(astAttrType.getRow());
			attributes.add(attribute);
		}
		
		// add everything to DataDictionary
		for (SDFAttribute a : attributes) {
			DataDictionary.getInstance().attributeMap.put(brokerName, a);
		}
								
		// make it accessible like a normal source
		DataDictionary.getInstance().sourceTypeMap.put(brokerName, "brokerStreaming");
		SDFEntity entity = new SDFEntity(brokerName);		
		entity.setAttributes(attributes);		
		DataDictionary.getInstance().entityMap.put(brokerName, entity);
		//create the broker
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		broker.setOutputSchema(attributes);
		BrokerDictionary.getInstance().addBroker(brokerName, broker.getOutputSchema());
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
