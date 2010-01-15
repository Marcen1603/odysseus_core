package de.uniol.inf.is.odysseus.pqlhack.parser.visitor;

import java.util.HashMap;
import java.util.Map;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.base.AbstractLogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.base.RenameAO;
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AttributeResolver;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTAccessOp;
import de.uniol.inf.is.odysseus.pqlhack.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.pqlhack.parser.Node;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;




public class InitAttributesVisitor extends DefaultVisitor{

	private Map<SDFSource, AccessAO> sources;
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
