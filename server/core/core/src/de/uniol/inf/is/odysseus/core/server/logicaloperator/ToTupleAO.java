package de.uniol.inf.is.odysseus.core.server.logicaloperator;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Strings;

import de.uniol.inf.is.odysseus.core.collection.Tuple;
import de.uniol.inf.is.odysseus.core.logicaloperator.LogicalOperatorCategory;
import de.uniol.inf.is.odysseus.core.metadata.IMetaAttribute;
import de.uniol.inf.is.odysseus.core.metadata.IStreamObject;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFAttribute;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFMetaSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchema;
import de.uniol.inf.is.odysseus.core.sdf.schema.SDFSchemaFactory;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.LogicalOperator;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.annotations.Parameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.CreateAndRenameSDFAttributeParameter;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.RenameAttribute;
import de.uniol.inf.is.odysseus.core.server.logicaloperator.builder.StringParameter;

@LogicalOperator(maxInputPorts = 1, minInputPorts = 1, name = "ToTuple", doc = "Translates objects to a tuple", category = {
		LogicalOperatorCategory.TRANSFORM })
public class ToTupleAO extends UnaryLogicalOp {

	Logger LOG = LoggerFactory.getLogger(ToTupleAO.class);

	private static final long serialVersionUID = 4804826171047928513L;

	private String type = "";
	private List<RenameAttribute> attributes;
	private String dateFormat;

	private SDFSchema outputSchemaCached;

	public ToTupleAO() {
	}

	public ToTupleAO(ToTupleAO keyValueToTuple) {
		super(keyValueToTuple);
		this.attributes = keyValueToTuple.attributes;
		this.dateFormat = keyValueToTuple.dateFormat;
		this.outputSchemaCached = keyValueToTuple.outputSchemaCached;
		this.type = keyValueToTuple.type;
	}

	@Parameter(name = "Schema", type = CreateAndRenameSDFAttributeParameter.class, optional = false, isList = true)
	public void setAttributes(List<RenameAttribute> attributes) {
		this.attributes = attributes;
	}

	public List<RenameAttribute> getAttributes() {
		return this.attributes;
	}

	@Parameter(name = "type", type = StringParameter.class, optional = true, deprecated = false)
	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return this.type;
	}

	@Parameter(type = StringParameter.class, name = "dateFormat", isList = false, optional = true, doc = "If using a string for date information, use this format to parse the date (in Java syntax).")
	public void setDateFormat(String dateFormat) {
		if (!Strings.isNullOrEmpty(dateFormat)) {
			addParameterInfo("DATEFORMAT", "'" + dateFormat + "'");
		} else {
			removeParameterInfo("DATEFORMAT");
		}

		this.dateFormat = dateFormat;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	@Override
	protected SDFSchema getOutputSchemaIntern(int pos) {
		if (outputSchemaCached == null) {
			StringBuffer inputSourceName = new StringBuffer();
			for (String name : getInputSchema().getBaseSourceNames()) {
				inputSourceName.append(name);
			}
			List<SDFAttribute> attributeList = new ArrayList<SDFAttribute>();
			for (RenameAttribute att : attributes) {
				SDFAttribute sdfAtt = att.getAttribute();
				String name;
				if (!att.getNewName().equals("")) {
					name = att.getNewName();
				} else {
					name = att.getAttribute().getQualName();
				}
				name = SDFAttribute.replaceSpecialChars(name);

				String sourceName = getType();
				if (Strings.isNullOrEmpty(sourceName)) {
					sourceName = sdfAtt.getSourceName();
				}
				if (Strings.isNullOrEmpty(sourceName)) {
					sourceName = inputSourceName.toString();
				}
				attributeList.add(new SDFAttribute(sourceName, name, sdfAtt.getDatatype(), sdfAtt.getUnit(),
						sdfAtt.getDtConstraints()));
			}
			final List<SDFMetaSchema> metaSchema;
			metaSchema = getInputSchema().getMetaschema();
			@SuppressWarnings("unchecked")
			SDFSchema schema = SDFSchemaFactory.createNewSchema(Strings.isNullOrEmpty(getType())?inputSourceName.toString():getType(), (Class<? extends IStreamObject<?>>) Tuple.class,
					attributeList, getInputSchema());
			SDFSchema outputSchema = SDFSchemaFactory.createNewWithMetaSchema(schema, metaSchema);
			outputSchemaCached = outputSchema;
		}
		return outputSchemaCached;
	}

	@Override
	public AbstractLogicalOperator clone() {
		return new ToTupleAO(this);
	}

	public IMetaAttribute getLocalMetaAttribute() {
		return null;
	}

	public boolean readMetaData() {
		return false;
	}

}
