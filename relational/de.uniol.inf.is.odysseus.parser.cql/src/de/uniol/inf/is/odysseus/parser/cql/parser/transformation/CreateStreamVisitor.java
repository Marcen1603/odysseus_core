package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import java.util.List;
import java.util.ListIterator;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.base.ILogicalOperator;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.logicaloperator.relational.FixedSetAccessAO;
import de.uniol.inf.is.odysseus.parser.cql.CQLParser;
import de.uniol.inf.is.odysseus.parser.cql.IVisitor;
import de.uniol.inf.is.odysseus.parser.cql.VisitorFactory;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeDefinitions;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTChannel;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTPriorizedStatement;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSocket;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTSilab;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTTimedTuples;
import de.uniol.inf.is.odysseus.parser.cql.parser.ParseException;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.CQLAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttributeList;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

/**
 * @author Jonas Jacobi
 */
public class CreateStreamVisitor extends AbstractDefaultVisitor {
	String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public SDFAttributeList getAttributes() {
		return attributes;
	}

	public void setAttributes(SDFAttributeList attributes) {
		this.attributes = attributes;
	}

	public ILogicalOperator getOperator() {
		return operator;
	}

	public void setOperator(ILogicalOperator operator) {
		this.operator = operator;
	}

	SDFAttributeList attributes;
	ILogicalOperator operator;

	@Override
	public Object visit(ASTCreateStatement node, Object data) {
		attributes = new SDFAttributeList();
		name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		node.jjtGetChild(1).jjtAccept(this, data);
		SDFEntity entity = new SDFEntity(name);
		entity.setAttributes(attributes);
		DataDictionary.getInstance().sourceTypeMap.put(name,
				"RelationalStreaming");
		DataDictionary.getInstance().entityMap.put(name, entity);
		for (SDFAttribute a : attributes) {
			DataDictionary.getInstance().attributeMap.put(name, a);
		}

		for (int i = 2; i < node.jjtGetNumChildren(); ++i) {
			node.jjtGetChild(i).jjtAccept(this, data);
		}

		return data;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTTimedTuples node, Object data) {
		FixedSetAccessAO newPO = new FixedSetAccessAO(DataDictionary
				.getInstance().getSource(name), node.getTuples(attributes));
		newPO.setOutputSchema(attributes);
		DataDictionary.getInstance().setView(name, newPO);
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Object visit(ASTPriorizedStatement node, Object data) {
		CQLParser parser = new CQLParser();
		operator = ((List<ILogicalOperator>) parser.visit(node, null)).get(0);
		SDFAttributeList otherAttributes = operator.getOutputSchema();
		if (otherAttributes.size() != this.attributes.size()) {
			throw new RuntimeException(
					"Query output does not match specified schema for: " + name);
		}
		ListIterator<SDFAttribute> li = otherAttributes.listIterator();
		for (SDFAttribute attr : this.attributes) {
			if (!((CQLAttribute) li.next()).getAttributeName().equals(
					((CQLAttribute) attr).getAttributeName())) {
				throw new RuntimeException(
						"Query output does not match specified schema for: "
								+ name);
			}
		}

		DataDictionary.getInstance().setView(name, operator);
		return null;
	}

	@Override
	public Object visit(ASTAttributeDefinitions node, Object data) {
		return node.childrenAccept(this, data);
	}

	@Override
	public Object visit(ASTAttributeDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		CQLAttribute attribute = new CQLAttribute(this.name, attrName);
		ASTAttributeType astAttrType = (ASTAttributeType) node.jjtGetChild(1);
		attribute.setDatatype(astAttrType.getType());
		if (SDFDatatypes.isDate(attribute.getDatatype())) {
			attribute.addDtConstraint("format", astAttrType.getDateFormat());
		}
		attribute.setCovariance(astAttrType.getRow());
		this.attributes.add(attribute);
		return data;
	}

	@Override
	public Object visit(ASTSocket node, Object data) {
		String host = ((ASTHost) node.jjtGetChild(0)).getValue();
		int port = ((ASTInteger) node.jjtGetChild(1)).getValue().intValue();
		AccessAO source = null;
		if (node.useTupleMode()) {
			source = new AccessAO(new SDFSource(name,
					"RelationalInputStreamAccessPO"));
			source.setPort(port);
			source.setHost(host);
		} else if (node.useMVMode()) {
			source = new AccessAO(new SDFSource(name,
					"RelationalAtomicDataInputStreamAccessMVPO"));
			source.setPort(port);
			source.setHost(host);
			source.setOutputSchema(this.attributes);
		} else {
			source = new AccessAO(new SDFSource(name,
					"RelationalAtomicDataInputStreamAccessPO"));
			source.setPort(port);
			source.setHost(host);
			source.setOutputSchema(this.attributes);
		}
		DataDictionary.getInstance().setView(name, source);
		return data;
	}

	@Override
	public Object visit(ASTChannel node, Object data) {
		String host = ((ASTHost) node.jjtGetChild(0)).getValue();
		int port = ((ASTInteger) node.jjtGetChild(1)).getValue().intValue();
		AccessAO source = new AccessAO(new SDFSource(name,
				"RelationalByteBufferAccessPO"));
		source.setPort(port);
		source.setHost(host);
		source.setOutputSchema(this.attributes);
		DataDictionary.getInstance().setView(name, source);
		return data;
	}
	
	@Override
	public Object visit(ASTSilab node, Object data){
		// TODO: Behandlung, wenn kein Visitor gefunden wird
		try {
			Class.forName("de.uniol.inf.is.odysseus.objecttracking.parser");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		IVisitor v = VisitorFactory.getInstance().getVisitor("Silab");
		return v.visit(node,data, this);

	}

	// @Override
	// public Object visit(ASTOSGI node, Object data) {
	// OSGIAccessAO source = new OSGIAccessAO(new SDFSource(name,
	// SourceType.OSGI));
	// source.setRegexp(node.getRegexp());
	// DataDictionary.getInstance().setView(name, source);
	// return data;
	// }
}
