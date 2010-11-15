package de.uniol.inf.is.odysseus.objecttracking.parser;

import java.util.List;

import de.uniol.inf.is.odysseus.datadictionary.DataDictionary;
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
import de.uniol.inf.is.odysseus.parser.cql.parser.transformation.AbstractDefaultVisitor;
import de.uniol.inf.is.odysseus.scars.base.SensorAccessAO;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.description.SDFSource;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFDatatypeFactory;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.schema.SDFEntity;
import de.uniol.inf.is.odysseus.sourcedescription.sdf.vocabulary.SDFDatatypes;
import de.uniol.inf.is.odysseus.usermanagement.User;

public class CreateSensorVisitor extends AbstractDefaultVisitor {

	private String name;
	private String host;
	private Long port;

	private User user;

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Object visit(ASTCreateSensor node, Object data) {
		// System.out.println("Visit ASTCreateSensor(" + node + "," + data +
		// ")");
		this.name = ((ASTIdentifier) node.jjtGetChild(0)).getName();
		// System.out.println("Sensorname = " + name);

		data = null;
		SDFAttribute rootAttribute = (SDFAttribute) node.jjtGetChild(1)
				.jjtAccept(this, data);

		String objectListPath = ((ASTIdentifier) node.jjtGetChild(2)).getName();

		node.jjtGetChild(3).jjtAccept(this, data);

		SDFAttributeListExtended ex = new SDFAttributeListExtended(
				new SDFAttribute[] { rootAttribute });

		SDFEntity entity = new SDFEntity(name);
		entity.setAttributes(ex);
		DataDictionary.getInstance().addSourceType(name,
				"ObjectRelationalStreaming");
		DataDictionary.getInstance().addEntity(name, entity, user);

		// TODO: rekursiv ausgeben, was in der SDFAttributeListExtended ist
		// (extra Klasse oder so)
		// accessao bauen
		SensorAccessAO source = new SensorAccessAO(new SDFSource(name,
				"JDVEAccessMVPO"));
		source.setPort(port.intValue());
		source.setHost(host);
		source.setOutputSchema(ex);
		source.setObjectListPath(objectListPath);
		DataDictionary.getInstance().setView(name, source, user);
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
		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			SDFAttribute attr = (SDFAttribute) node.jjtGetChild(i).jjtAccept(
					this, data);
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

		for (int i = 1; i < node.jjtGetNumChildren(); i++) {
			SDFAttribute listedAttribute = (SDFAttribute) node.jjtGetChild(i)
					.jjtAccept(this, data);
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

		if (SDFDatatypes.isMeasurementValue(attribute.getDatatype())
				&& astAttrType.jjtGetNumChildren() > 0) {
			attribute
					.setCovariance((List<?>) astAttrType.jjtGetChild(0).jjtAccept(this, data));

		}

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
		this.host = (String) node.jjtGetChild(0).jjtAccept(this, data);
		this.port = ((ASTInteger) node.jjtGetChild(1)).getValue();
		return super.visit(node, data);
	}

}
