package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.usermanagement.User;
import de.uniol.inf.is.odysseus.broker.dictionary.BrokerDictionary;
import de.uniol.inf.is.odysseus.broker.logicaloperator.BrokerAO;
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




public class InitAttributesVisitor extends DefaultVisitor{

	private Map<SDFSource, AccessAO> sources;
	private Map<SDFSource, BrokerAO> brokers;
	private AttributeResolver attributeResolver;
	private User user;
	
	public InitAttributesVisitor(User user){
		super();
		this.attributeResolver = new AttributeResolver();
		this.sources = new HashMap<SDFSource, AccessAO>();
		this.brokers = new HashMap<SDFSource, BrokerAO>();
		this.user = user;
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
			
			access = (AccessAO)DataDictionary.getInstance().getView(sourceName, user);
//			access = new AccessAO();
//			access.setSource(new SDFSource(sourceName,""));
//			access.setOutputSchema(DataDictionary.getInstance().getViewOutputSchema(sourceName));
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
		
		// if there are multiple statements, there can be 
		// more than one access op with the same name.
		// In this case only add one of them and ignore the
		// other ones.
		if(this.attributeResolver.getSource(sourceName) == null){
			this.attributeResolver.addSource(sourceName, inputOp);
		}
	}
	
	@Override
	public Object visit(ASTBrokerOp node, Object data){
		Node childNode = node.jjtGetChild(0);
		String sourceString = ((ASTIdentifier) childNode).getName();		
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
			broker = BrokerDictionary.getInstance().getLogicalPlan(sourceName);
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
