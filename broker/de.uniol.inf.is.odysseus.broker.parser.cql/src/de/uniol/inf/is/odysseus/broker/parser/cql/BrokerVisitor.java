package de.uniol.inf.is.odysseus.broker.parser.cql;

import java.util.List;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.LogicalSubscription;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.transaction.GraphUtils;
import de.uniol.inf.is.odysseus.broker.transaction.ReadTransaction;
import de.uniol.inf.is.odysseus.broker.transaction.WriteTransaction;
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
		// ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(0);
		// String name = ident.getName();
		//
		// // parse the nested substatement
		// ASTComplexSelectStatement childNode = (ASTComplexSelectStatement)
		// node.jjtGetChild(1);
		// CQLParser v = new CQLParser();
		// AbstractLogicalOperator result = (AbstractLogicalOperator)
		// v.visit(childNode, null);
		//
		// BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
		// broker.setOutputSchema(result.getOutputSchema());
		// if (!BrokerDictionary.getInstance().brokerExists(name)) {
		// BrokerDictionary.getInstance().addBroker(name,
		// broker.getOutputSchema(), broker.getMetaInputSchema());
		// }
		//
		// // connect the source to broker
		// int inPort = BrokerDictionary.getInstance().getNextWritePort(name);
		// broker.subscribeToSource(result, inPort, 0,
		// result.getOutputSchema());
		// // make it accessible like a normal source
		// DataDictionary.getInstance().sourceTypeMap.put(name,
		// "brokerStreaming");
		// SDFEntity entity = new SDFEntity(name);
		// entity.setAttributes(broker.getOutputSchema());
		// DataDictionary.getInstance().entityMap.put(name, entity);
		// return broker;
		return null;
	}

	public Object visit(ASTSimpleSource node, Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
			BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
			return broker;
		} else {
			throw new RuntimeException("Broker " + brokerName + " not exists");
		}

	}

	public Object visit(ASTBrokerSelectInto node, Object data) {
		ASTSelectStatement statement = new ASTSelectStatement(0);
		int number = 0;

		// add selectClause
		statement.jjtAddChild(node.jjtGetChild(0), number);
		number++;
		// get broker name
		String brokerName = ((ASTIdentifier) node.jjtGetChild(1)).getName();

		// add rest from select except of the last one if it's a complex
		// statement
		int numChilds = node.jjtGetNumChildren();
		if (node.jjtGetChild(node.jjtGetNumChildren() - 1) instanceof ASTComplexSelectStatement) {
			numChilds--;
		}
		for (int i = 2; i < numChilds; i++) {
			statement.jjtAddChild(node.jjtGetChild(i), number);
			number++;
		}
		// parse first nested statement
		CQLParser v = new CQLParser();
		AbstractLogicalOperator topOfSelectStatementOperator = (AbstractLogicalOperator) v.visit(statement, null);
		AbstractLogicalOperator topOfQueueSelectOperator = null;
		if (node.jjtGetChild(node.jjtGetNumChildren() - 1) instanceof ASTComplexSelectStatement) {
			// parse second nested statement

			ASTComplexSelectStatement metaStatement = (ASTComplexSelectStatement) node.jjtGetChild(node.jjtGetNumChildren() - 1);
			v = new CQLParser();
			topOfQueueSelectOperator = (AbstractLogicalOperator) v.visit(metaStatement, null);
		}

		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		// add the schemas from existing one
		if (!BrokerDictionary.getInstance().brokerExists(brokerName)) {
			throw new RuntimeException("Broker with name \"" + brokerName + "\" not found. You have to create one first.");
		}		
		broker.setSchema(BrokerDictionary.getInstance().getSchema(brokerName));
		broker.setQueueSchema(BrokerDictionary.getInstance().getQueueSchema(brokerName));

		// check incoming and outgoing subs
		//boolean readingFromAnyBroker = false;
		boolean readingFromOwnBroker = false;
		boolean isQueueWriting = false;

		List<BrokerAO> readingFromBrokers = GraphUtils.getReadingFromBrokers(topOfSelectStatementOperator);		
//		if (!readingFromBrokers.isEmpty()) {
//			readingFromAnyBroker = true;
//		}
		for (BrokerAO b : readingFromBrokers) {
			if (b.getIdentifier().equals(broker.getIdentifier())) {
				readingFromOwnBroker = true;
			}
		}

		if (topOfQueueSelectOperator != null) {
			isQueueWriting = true;
		}

		// the writing type for the queue is always Timestamp
		WriteTransaction queueWritingType = WriteTransaction.Timestamp;
		// a normal writing transaction is continuous
		WriteTransaction dataWritingType = WriteTransaction.Continuous;
		// if i read from my own operator, it is a cyclic transaction
		if (readingFromOwnBroker && isQueueWriting) {
			dataWritingType = WriteTransaction.Cyclic;
		}
		int mappedReadingPort = 0;
		//remap all reading ports so that everyone has its own one 
		for(BrokerAO readFromBroker : readingFromBrokers){					
			ReadTransaction dataReadTransaction = ReadTransaction.Continuous;
			//if i read from my own one -> it is cyclic
			if(readFromBroker.getIdentifier().equals(brokerName)){
				dataReadTransaction = ReadTransaction.Cyclic;				
			}
			// this is a SELECT INTO statement, so that there is automatically a writing transaction
			// if there would not be a writing transaction, then it is necessary to distinguish between
			// reading without and with queue (=> OneTime Read instead of cyclic read)
			int nextPort = BrokerDictionary.getInstance().addNewTransaction(readFromBroker.getIdentifier(), dataReadTransaction);
			for(LogicalSubscription sub : readFromBroker.getSubscriptions()){				
				broker.unsubscribeSink(sub.getTarget(), sub.getSinkInPort(), sub.getSourceOutPort(), sub.getSchema());			
				broker.subscribeSink(sub.getTarget(), sub.getSinkInPort(), nextPort, sub.getSchema());			
			}	
			// if it is cyclic reading, the reading port belongs to the writing port
			if(dataReadTransaction == ReadTransaction.Cyclic){
				mappedReadingPort = nextPort;
			}
		}

		// check schemas
		if (!schemaEquals(topOfSelectStatementOperator.getOutputSchema(), broker.getOutputSchema())) {
			String message = "Schema to insert: " + topOfSelectStatementOperator.getOutputSchema().toString() + "\n";
			message = message + "Schema of Broker: " + broker.getOutputSchema().toString();
			throw new RuntimeException("Statement and broker do not have the same schema.\n" + message);
		}
		// check, if it is an meta-channel nested select
		if (topOfQueueSelectOperator != null) {
			if (!schemaEquals(topOfQueueSelectOperator.getOutputSchema(), broker.getQueueSchema())) {
				String message = "Schema to insert: " + topOfSelectStatementOperator.getOutputSchema().toString() + "\n";
				message = message + "Queueschema of Broker: " + broker.getQueueSchema().toString();
				throw new RuntimeException("Queue statement and broker do not have the same schema.\n" + message);
			}
		}
		// connect the nested statement into the broker
		int dataPort = BrokerDictionary.getInstance().addNewTransaction(brokerName, dataWritingType);
		broker.subscribeToSource(topOfSelectStatementOperator, dataPort, 0, topOfSelectStatementOperator.getOutputSchema());

		// if there is a queue statement, connect it
		if (topOfQueueSelectOperator != null) {
			int queuePort = BrokerDictionary.getInstance().addNewTransaction(brokerName, queueWritingType);
			broker.subscribeToSource(topOfQueueSelectOperator, queuePort, 0, topOfQueueSelectOperator.getOutputSchema());
			// remember the connection between reading port and queuePort
			BrokerDictionary.getInstance().addNewTransactionMapping(brokerName, mappedReadingPort, queuePort);
		}

		return broker;

	}

	public Object visit(ASTCreateBroker node, Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// check first if name already exists
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
			throw new RuntimeException("There is already a broker named \"" + brokerName + "\".");
		}

		// parse attributes
		SDFAttributeList attributes = new SDFAttributeList();
		ASTAttributeDefinitions attributeDe = (ASTAttributeDefinitions) node.jjtGetChild(1);
		for (int i = 0; i < attributeDe.jjtGetNumChildren(); i++) {
			ASTAttributeDefinition attrNode = (ASTAttributeDefinition) attributeDe.jjtGetChild(i);
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

		// parse meta attributes
		SDFAttributeList metaAttributes = new SDFAttributeList();
		if (node.jjtGetChild(2) != null) {

			ASTAttributeDefinitions metaAttributeDe = (ASTAttributeDefinitions) node.jjtGetChild(2);
			for (int i = 0; i < metaAttributeDe.jjtGetNumChildren(); i++) {
				ASTAttributeDefinition attrNode = (ASTAttributeDefinition) metaAttributeDe.jjtGetChild(i);
				String attrName = ((ASTIdentifier) attrNode.jjtGetChild(0)).getName();
				SDFAttribute attribute = new SDFAttribute(brokerName, attrName);
				ASTAttributeType astAttrType = (ASTAttributeType) attrNode.jjtGetChild(1);
				attribute.setDatatype(astAttrType.getType());
				if (SDFDatatypes.isDate(attribute.getDatatype())) {
					attribute.addDtConstraint("format", astAttrType.getDateFormat());
				}
				attribute.setCovariance(astAttrType.getRow());
				metaAttributes.add(attribute);
			}
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
		// create the broker
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		broker.setSchema(attributes);
		broker.setQueueSchema(metaAttributes);
		BrokerDictionary.getInstance().addBroker(brokerName, broker.getOutputSchema(), broker.getQueueSchema());
		return broker;
	}

	private boolean schemaEquals(SDFAttributeList left, SDFAttributeList right) {
		if (left.size() != right.size()) {
			return false;
		} else {
			for (int i = 0; i < left.size(); i++) {
				if (!left.get(i).getAttributeName().equals(right.get(i).getAttributeName())) {
					return false;
				}
			}
			return true;
		}
	}
}
