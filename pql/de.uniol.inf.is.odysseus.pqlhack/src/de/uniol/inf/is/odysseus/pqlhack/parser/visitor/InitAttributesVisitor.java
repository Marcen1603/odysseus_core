package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAOFactory;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTBrokerOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;




public class InitAttributesVisitor extends DefaultVisitor{

	private Map<SDFSource, AccessAO> sources;
	private Map<SDFSource, BrokerAO> brokers;
	private AttributeResolver attributeResolver;
	
	public InitAttributesVisitor(){
		super();
		this.attributeResolver = new AttributeResolver();
		this.sources = new HashMap<SDFSource, AccessAO>();
	}
	
	@Override
	public Object visit(ASTAccessOp node, Object data){
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();
		SDFSource source = DataDictionary.getInstance().getSource(sourceString);
		relationalStreamingSource(node, source, sourceString);

		return null;
	}
	
	private void relationalStreamingSource(ASTAccessOp node,
			SDFSource source, String sourceName) {
		// if the access operator already exists and has no
		// alias, then this method has no effect
		
		AccessAO access = this.sources.get(source);
		if (access == null) {
			access = new AccessAO(source);
			SDFEntity entity = null;
			try {
				entity = DataDictionary.getInstance().getEntity(sourceName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			access.setOutputSchema(entity.getAttributes());

			this.sources.put(source, access);
		}

		AbstractLogicalOperator inputOp = access;
		
		if (node.hasAlias()) {
			inputOp = new RenameAO();
			inputOp.subscribeToSource(access, 0, 0, access.getOutputSchema());
			((RenameAO)inputOp).setOutputSchema(createAliasSchema(node.getAlias(), access));
			sourceName = node.getAlias();
		}

		this.attributeResolver.addSource(sourceName, inputOp);
	}
	
	@Override
	public Object visit(ASTBrokerOp node, Object data){
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();		
		SDFSource source = DataDictionary.getInstance().getSource(sourceString);
		brokerStreamingSource(node, source, sourceString);

		return null;
	}
	
	private void brokerStreamingSource(ASTBrokerOp node,
			SDFSource source, String sourceName) {
		// if the broker already exists and has no alias
		// then this method has no effect.
		
		BrokerAO broker = this.brokers.get(source);
		if (broker == null) {
			broker = BrokerAOFactory.getFactory().createBrokerAO(sourceName);
			SDFAttributeList brokerSchema = null;
			SDFAttributeList brokerQueueSchema = null;
			try {
				brokerSchema = BrokerDictionary.getInstance().getSchema(sourceName);
				brokerQueueSchema = BrokerDictionary.getInstance().getQueueSchema(sourceName);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			broker.setSchema(brokerSchema);
			broker.setQueueSchema(brokerQueueSchema);

			this.brokers.put(source, broker);
		}

		AbstractLogicalOperator inputOp = broker;

		// The broker should not use an alias.
		// This might not work with the mapping
		// between queue in-port and data out-port
//		if (node.hasAlias()) {
//
//			throw new RuntimeException("Broker should not use an alias. Otherwise the mapping between queue in-port and data out-port is not correct.");
//			inputOp = new RenameAO();
//			inputOp.subscribeToSource(broker, 0, 0, broker.getOutputSchema());
//			((RenameAO)inputOp).setOutputSchema(createAliasSchema(node.getAlias(), broker));
//			sourceName = node.getAlias();
//		}

		this.attributeResolver.addSource(sourceName, inputOp);
	}
	
	private SDFAttributeList createAliasSchema(String alias, AbstractLogicalOperator access) {
		SDFAttributeList attributes = new SDFAttributeList();
		for (SDFAttribute attribute : access.getOutputSchema()) {
			SDFAttribute newAttribute = (SDFAttribute) attribute.clone();
			newAttribute.setSourceName(alias);
			attributes.add(newAttribute);
		}
		return attributes;
	}
	
	public AttributeResolver getAttributeResolver(){
		return this.attributeResolver;
	}

}
