package de.uniol.inf.is.odysseus.broker.parser.cql;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.broker.metric.MetricMeasureAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerAsSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerQueue;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTComplexSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTMetric;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSelectStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource;
import de.uniol.inf.is.odysseus.parser.cql.parser.Node;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * The BrokerVisitor provides the implementation of the broker related parts
 * from the translation. This encapsulate the general translation from the
 * broker specific translation to avoid dependencies.
 * 
 * @author Dennis Geesen
 */
public class BrokerVisitor extends AbstractDefaultVisitor {

	private User user;

	public BrokerVisitor(User user) {
		this.user = user;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSource,
	 * java.lang.Object)
	 */
	@Override
	public Object visit(ASTBrokerSource node, Object data) {
		BrokerAO readFromBroker = null;
		if (node.jjtGetChild(0) instanceof ASTBrokerSimpleSource) {
			readFromBroker = (BrokerAO) visit((ASTBrokerSimpleSource) node.jjtGetChild(0), data);
		} else if (node.jjtGetChild(0) instanceof ASTBrokerAsSource) {
			readFromBroker = (BrokerAO) visit((ASTBrokerAsSource) node.jjtGetChild(0), data);
		}
		if (node.jjtGetNumChildren() > 1) {
			if (node.jjtGetChild(1) != null) {
				if (node.jjtGetChild(1) instanceof ASTBrokerQueue) {
					ASTComplexSelectStatement statement = (ASTComplexSelectStatement) node.jjtGetChild(1).jjtGetChild(0);
					CQLParser parser = new CQLParser();
					AbstractLogicalOperator topOfQueue = (AbstractLogicalOperator) parser.visit(statement, null);
					if (readFromBroker != null) {
						// queue - writing is always on port 1
						readFromBroker.subscribeToSource(topOfQueue, 1, 0, topOfQueue.getOutputSchema());
					}
				}
			}
		}
		return readFromBroker;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSimpleSource,
	 * java.lang.Object)
	 */
	@Override
	public Object visit(ASTBrokerSimpleSource node, Object data) {
		return getSimpleSource(node, data);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerAsSource,
	 * java.lang.Object)
	 */
	@Override
	public Object visit(ASTBrokerAsSource node, Object data) {
		ASTIdentifier ident = (ASTIdentifier) node.jjtGetChild(1);
		String name = ident.getName();

		// parse the nested substatement
		ASTComplexSelectStatement childNode = (ASTComplexSelectStatement) node.jjtGetChild(0);
		CQLParser v = new CQLParser();
		AbstractLogicalOperator result = (AbstractLogicalOperator) v.visit(childNode, null);

		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(name);
		broker.setSchema((SDFAttributeListExtended) result.getOutputSchema());
		if (!BrokerDictionary.getInstance().brokerExists(name)) {
			BrokerDictionary.getInstance().addBroker(name, broker.getOutputSchema(), broker.getQueueSchema());
		}

		// connect the source to broker
		broker.subscribeToSource(result, 0, 0, result.getOutputSchema());
		// make it accessible like a normal source
		DataDictionary.getInstance().addSourceType(name, "brokerStreaming");
		SDFEntity entity = new SDFEntity(name);
		entity.setAttributes(broker.getOutputSchema());
		DataDictionary.getInstance().addEntity(name, entity, user);
		return broker;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTSimpleSource,
	 * java.lang.Object)
	 */
	public Object visit(ASTSimpleSource node, Object data) {
		return getSimpleSource(node, data);

	}

	/**
	 * Gets a Broker as a simple source.
	 * 
	 * @param node
	 *            the node
	 * @param data
	 *            the data
	 * @return the simple source
	 */
	private Object getSimpleSource(Node node, Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
			BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
			return broker;
		} else {
			throw new RuntimeException("Broker " + brokerName + " not exists");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTBrokerSelectInto,
	 * java.lang.Object)
	 */
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
		for (int i = 2; i < numChilds; i++) {
			statement.jjtAddChild(node.jjtGetChild(i), number);
			number++;
		}
		// parse first nested statement
		CQLParser v = new CQLParser();
		AbstractLogicalOperator topOfSelectStatementOperator = (AbstractLogicalOperator) v.visit(statement, null);

		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		// add the schemas from existing one
		if (!BrokerDictionary.getInstance().brokerExists(brokerName)) {
			throw new RuntimeException("Broker with name \"" + brokerName + "\" not found. You have to create one first.");
		}
		broker.setSchema(BrokerDictionary.getInstance().getSchema(brokerName));
		broker.setQueueSchema(BrokerDictionary.getInstance().getQueueSchema(brokerName));

		// check schemas
		if (!schemaEquals(topOfSelectStatementOperator.getOutputSchema(), broker.getOutputSchema())) {
			String message = "Schema to insert: " + topOfSelectStatementOperator.getOutputSchema().toString() + "\n";
			message = message + "Schema of Broker: " + broker.getOutputSchema().toString();
			throw new RuntimeException("Statement and broker do not have the same schema.\n" + message);
		}
		// connect the nested statement into the broker
		broker.subscribeToSource(topOfSelectStatementOperator, 0, 0, topOfSelectStatementOperator.getOutputSchema());
		return broker;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seede.uniol.inf.is.odysseus.parser.cql.parser.transformation.
	 * AbstractDefaultVisitor
	 * #visit(de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateBroker,
	 * java.lang.Object)
	 */
	public Object visit(ASTCreateBroker node, Object data) {
		String brokerName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// check first if name already exists
		if (BrokerDictionary.getInstance().brokerExists(brokerName)) {
			throw new RuntimeException("There is already a broker named \"" + brokerName + "\".");
		}

		// parse attributes
		SDFAttributeListExtended attributes = new SDFAttributeListExtended();

		if (node.jjtGetChild(1) instanceof ASTORSchemaDefinition) {
			SDFAttribute rootAttribute = (SDFAttribute)node.jjtGetChild(1).jjtAccept(this, brokerName);
			attributes.add(rootAttribute);
		} else {
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
		}
		
		// parse meta attributes
		SDFAttributeList metaAttributes = new SDFAttributeList();
		if (node.jjtGetNumChildren() > 2) {
			if (node.jjtGetChild(2) != null) {

				if( node.jjtGetChild(2) instanceof ASTAttributeDefinitions ) {
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
				} else if( node.jjtGetChild(2) instanceof ASTORSchemaDefinition) {
					SDFAttribute rootAttribute = (SDFAttribute)node.jjtGetChild(2).jjtAccept(this, brokerName);
					metaAttributes.add(rootAttribute);
				}
			}
		}

		// make it accessible like a normal source
		DataDictionary.getInstance().addSourceType(brokerName, "brokerStreaming");
		SDFEntity entity = new SDFEntity(brokerName);
		entity.setAttributes(attributes);
		DataDictionary.getInstance().addEntity(brokerName, entity, user);
		// create the broker
		BrokerAO broker = BrokerAOFactory.getFactory().createBrokerAO(brokerName);
		broker.setSchema(attributes);
		broker.setQueueSchema(metaAttributes);
		BrokerDictionary.getInstance().addBroker(brokerName, broker.getOutputSchema(), broker.getQueueSchema());

		// set the broker view in the data dictionary
		// used for procedural parser
		BrokerDictionary.getInstance().setLogicalPlan(brokerName, broker);

		// Is this necessary any more?
		DataDictionary.getInstance().setLogicalView(brokerName, broker, user);

		return broker;
	}

	@Override
	public Object visit(ASTMetric node, Object data) {
		String attribute = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		AbstractLogicalOperator topOp = (AbstractLogicalOperator) data;
		MetricMeasureAO metricOp = new MetricMeasureAO(attribute);
		metricOp.setOutputSchema(topOp.getOutputSchema());
		topOp.subscribeSink(metricOp, 0, 0, topOp.getOutputSchema());
		return metricOp;
	}

	/**
	 * Checks whether two schema are equal. This is only based on the attribute
	 * names and not on the assigned source names as well.
	 * 
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 * @return true, if successful
	 */
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
	
	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		
		SDFAttribute recordAttribute = new SDFAttribute(data.toString(), attrName);
		recordAttribute.setDatatype(SDFDatatypeFactory.getDatatype("Record"));
		for( int i = 1; i < node.jjtGetNumChildren(); i++ ) {
			SDFAttribute attr = (SDFAttribute)node.jjtGetChild(i).jjtAccept(this, data);
			recordAttribute.addSubattribute(attr);
		}
		
		return recordAttribute;
	}

	@Override
	public Object visit(ASTRecordEntryDefinition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTListDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		
		SDFAttribute attribute = new SDFAttribute(data.toString(), attrName);
		attribute.setDatatype(SDFDatatypeFactory.getDatatype("List"));
		
		for( int i = 1; i < node.jjtGetNumChildren(); i++ ) {
			SDFAttribute listedAttribute = (SDFAttribute)node.jjtGetChild(i).jjtAccept(this, data);
			attribute.addSubattribute(listedAttribute);
		}
		
		return attribute;
	}

	@Override
	public Object visit(ASTAttrDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);
		
		SDFAttribute attribute = new SDFAttribute(data.toString(), attrName);
		attribute.setDatatype(astAttrType.getType());
		attribute.setCovariance(astAttrType.getRow());
		if (SDFDatatypes.isDate(attribute.getDatatype())) 
			attribute.addDtConstraint("format", astAttrType.getDateFormat());
		
		return attribute;
	}
}
