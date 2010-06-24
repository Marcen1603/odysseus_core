package de.uniol.inf.is.odysseus.parser.cql.parser.transformation;

import de.uniol.inf.is.odysseus.base.DataDictionary;
import de.uniol.inf.is.odysseus.logicaloperator.base.AccessAO;
import de.uniol.inf.is.odysseus.objecttracking.sdf.SDFAttributeListExtended;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttrDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTAttributeType;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTChannel;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTCreateSensor;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTHost;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTIdentifier;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTInteger;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTListDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTORSchemaDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordDefinition;
import de.uniol.inf.is.odysseus.parser.cql.parser.ASTRecordEntryDefinition;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;

public class CreateSensorVisitor extends AbstractDefaultVisitor {

	private String name;
	private String host;
	private Long port;
	
	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		System.out.println("Visit ASTCreateSensor(" + node + "," + data + ")");
		this.name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		System.out.println("Sensorname = " + name);
		
		SDFAttribute rootAttribute = (SDFAttribute)node.jjtGetChild(1).jjtAccept(this, data);
		node.jjtGetChild(2).jjtAccept(this, data);
		
		SDFAttributeListExtended ex = new SDFAttributeListExtended(new SDFAttribute[] { rootAttribute });
		
		SDFEntity entity = new SDFEntity(name);
		entity.setAttributes(ex);
		DataDictionary.getInstance().sourceTypeMap.put(name, "ObjectRelationalStreaming");
		DataDictionary.getInstance().entityMap.put(name, entity);
		
		// TODO: rekursiv ausgeben, was in der SDFAttributeListExtended ist (extra Klasse oder so)
		// accessao bauen
		AccessAO source = new AccessAO(new SDFSource(name, "JDVEAccessMVPO"));
		source.setPort(port.intValue());
		source.setHost(host);
		source.setOutputSchema(ex);
		DataDictionary.getInstance().setView(name, source);
		return null;
	}

	@Override
	public Object visit(ASTORSchemaDefinition node, Object data) {
		return node.jjtGetChild(0).jjtAccept(this, data);
	}

	@Override
	public Object visit(ASTRecordDefinition node, Object data) {
		String attrName = ((ASTIdentifier) node.jjtGetChild(0)).getName();

		SDFAttribute recordAttribute = new SDFAttribute(this.name, attrName);
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
		
		SDFAttribute attribute = new SDFAttribute(this.name, attrName);
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
		
		SDFAttribute attribute = new SDFAttribute(this.name, attrName);
		attribute.setDatatype(astAttrType.getType());
		attribute.setCovariance(astAttrType.getRow());
		if (SDFDatatypes.isDate(attribute.getDatatype())) 
			attribute.addDtConstraint("format", astAttrType.getDateFormat());
		
		return attribute;
	}
 
	@Override
	public Object visit(ASTHost node, Object data) {
		return node.getValue();
	}
	
	@Override
	public Object visit(ASTChannel node, Object data) {
		this.host = (String)node.jjtGetChild(0).jjtAccept(this, data);
		this.port = ((ASTInteger)node.jjtGetChild(1)).getValue();
		return super.visit(node, data);
	}
}
