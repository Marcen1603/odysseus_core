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
		this.brokers = new HashMap<SDFSource, BrokerAO>();
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
			
			// TODO ist das korrekt, oder muss man doch
			// einen eigenen AccessAO erzeugen?
			// access = (AccessAO)DataDictionary.getInstance().getView(sourceName);
			access = new AccessAO();
			access.setSource(new SDFSource(sourceName,""));
			access.setOutputSchema(DataDictionary.getInstance().getViewOutputSchema(sourceName));
//			access = new AccessAO(source);
//			SDFEntity entity = null;
//			try {
//				entity = DataDictionary.getInstance().getEntity(sourceName);
//			} catch (Exception e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			access.setOutputSchema(entity.getAttributes());

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
		DataDictionary inst = DataDictionary.getInstance();
		SDFSource source = DataDictionary.getInstance().getSource(sourceString);
		brokerStreamingSource(node, source, sourceString);

		return node.childrenAccept(this, data);
	}
	
	private void brokerStreamingSource(ASTBrokerOp node,
			SDFSource source, String sourceName) {
		// if the broker already exists and has no alias
		// then this method has no effect.
		
		BrokerAO broker = this.brokers.get(source);
		if (broker == null) {
			//broker = (BrokerAO)DataDictionary.getInstance().getView(sourceName);
			broker = new BrokerAO();
			this.brokers.put(source, broker);
			this.attributeResolver.addSource(sourceName, broker);
		}

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
